package org.glisboa.backend.domain.specifications.presence;

import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import org.glisboa.backend.domain.models.presence.Presence;
import org.glisboa.backend.domain.models.record.Record;
import org.glisboa.backend.domain.models.record.type.Type;
import org.glisboa.backend.domain.models.employee.Employee;
import org.glisboa.backend.dto.request.presence.filter.SearchPresenceFilter;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class EmployeePresenceSpecification {

    public static Specification<Presence> fromSearchPresencesForEmployees(SearchPresenceFilter filter) {
        return (root, query, cb) -> {
            if (filter == null) {
                return cb.conjunction();
            }

            query.distinct(true);

            Join<Presence, Record> recordJoin = root.join("record", JoinType.INNER);
            // INNER join garante existir Employee para essa Record
            Join<Record, Employee> employeeJoin = recordJoin.join("employee", JoinType.INNER);

            String name = (filter.name() != null && !filter.name().isBlank())
                    ? filter.name().trim().toLowerCase()
                    : null;

            boolean hasEmployeeFilters = filter.post() != null || name != null;

            if (!hasEmployeeFilters) {
                return cb.conjunction();
            }

            List<Predicate> preds = new ArrayList<>();
            preds.add(cb.equal(recordJoin.get("type"), Type.FUNCIONARIO));

            if (filter.post() != null) {
                preds.add(cb.equal(employeeJoin.get("post"), filter.post()));
            }
            if (name != null) {
                preds.add(cb.like(cb.lower(recordJoin.get("name")), "%" + name + "%"));
            }

            return cb.and(preds.toArray(new Predicate[0]));
        };
    }
}
