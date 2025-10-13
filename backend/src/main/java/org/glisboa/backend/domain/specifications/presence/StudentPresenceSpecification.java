package org.glisboa.backend.domain.specifications.presence;

import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import org.glisboa.backend.domain.models.presence.Presence;
import org.glisboa.backend.domain.models.record.Record;
import org.glisboa.backend.domain.models.record.type.Type;
import org.glisboa.backend.domain.models.student.Student;
import org.glisboa.backend.dto.request.presence.filter.SearchPresenceFilter;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class StudentPresenceSpecification {

    public static Specification<Presence> fromSearchPresencesForStudents(SearchPresenceFilter filter) {
        return (root, query, cb) -> {
            if (filter == null) {
                return cb.conjunction();
            }

            query.distinct(true);

            Join<Presence, Record> recordJoin = root.join("record", JoinType.INNER);
            // INNER join garante existir Student para essa Record
            Join<Record, Student> studentJoin = recordJoin.join("student", JoinType.INNER);

            String name = (filter.name() != null && !filter.name().isBlank())
                    ? filter.name().trim().toLowerCase()
                    : null;

            boolean hasStudentFilters =
                    filter.from() != null || filter.to() != null || filter.grade() != null || name != null;

            if (!hasStudentFilters) {
                return cb.conjunction();
            }

            List<Predicate> preds = new ArrayList<>();
            // garante que o record é do tipo ALUNO
            preds.add(cb.equal(recordJoin.get("type"), Type.ALUNO));

            if (filter.from() != null) {
                preds.add(cb.greaterThanOrEqualTo(root.get("createdAt"), filter.from()));
            }
            if (filter.to() != null) {
                preds.add(cb.lessThanOrEqualTo(root.get("createdAt"), filter.to()));
            }
            if (filter.grade() != null) {
                preds.add(cb.equal(studentJoin.get("grade"), filter.grade()));
            }
            if (name != null) {
                preds.add(cb.like(cb.lower(recordJoin.get("name")), "%" + name + "%"));
            }

            return cb.and(preds.toArray(new Predicate[0]));
        };
    }
}
