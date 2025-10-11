package org.glisboa.backend.domain.specifications.presence;

import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import org.glisboa.backend.domain.models.employee.Employee;
import org.glisboa.backend.domain.models.presence.Presence;
import org.glisboa.backend.domain.models.record.Record;
import org.glisboa.backend.domain.models.student.Student;
import org.glisboa.backend.dto.request.presence.filter.SearchPresenceFilter;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class PresenceSpecification {

    public static Specification<Presence> fromSearchPresences(SearchPresenceFilter filter) {
        return (root, query, cb) -> {

            if (filter == null) {
                return cb.conjunction();
            }

            assert query != null;
            query.distinct(true);

            Join<Presence, Record> recordJoin = root.join("record", JoinType.INNER);
            Join<Record, Student> studentJoin = recordJoin.join("student", JoinType.LEFT);
            Join<Record, Employee> employeeJoin = recordJoin.join("employee", JoinType.LEFT);

            String name = (filter.name() != null && !filter.name().isBlank())
                    ? filter.name().trim().toLowerCase()
                    : null;

            boolean hasStudentFilters =
                    filter.from() != null || filter.to() != null || filter.grade() != null || name != null;

            boolean hasEmployeeFilters =
                    filter.post() != null || name != null;


            if (!hasStudentFilters && !hasEmployeeFilters) {
                return cb.conjunction();
            }

            Predicate studentPredicate = cb.disjunction();
            if (filter.searchStudents() && hasStudentFilters) {
                List<Predicate> studentPreds = new ArrayList<>();

                studentPreds.add(cb.isNotNull(studentJoin.get("id")));

                if (filter.from() != null) {
                    studentPreds.add(cb.greaterThanOrEqualTo(root.get("createdAt"), filter.from()));
                }
                if (filter.to() != null) {
                    studentPreds.add(cb.lessThanOrEqualTo(root.get("createdAt"), filter.to()));
                }
                if (filter.grade() != null) {
                    studentPreds.add(cb.equal(studentJoin.get("grade"), filter.grade()));
                }
                if (name != null) {
                    studentPreds.add(cb.like(cb.lower(recordJoin.get("name")), "%" + name + "%"));
                }

                studentPredicate = cb.and(studentPreds.toArray(new Predicate[0]));
            }

            Predicate employeePredicate = cb.disjunction();
            if (!filter.searchStudents() && hasEmployeeFilters) {
                List<Predicate> employeePreds = new ArrayList<>();

                employeePreds.add(cb.isNotNull(employeeJoin.get("id")));

                if (filter.post() != null) {
                    employeePreds.add(cb.equal(employeeJoin.get("post"), filter.post()));
                }
                if (name != null) {
                    employeePreds.add(cb.like(cb.lower(recordJoin.get("name")), "%" + name + "%"));
                }

                employeePredicate = cb.and(employeePreds.toArray(new Predicate[0]));
            }

            return cb.or(studentPredicate, employeePredicate);
        };
    }
}
