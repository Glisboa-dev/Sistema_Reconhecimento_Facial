package org.glisboa.backend.domain.specifications.student;

import jakarta.persistence.criteria.*;
import org.glisboa.backend.domain.models.record.Record;
import org.glisboa.backend.domain.models.record.status.Status;
import org.glisboa.backend.domain.models.student.Student;
import org.glisboa.backend.domain.models.student.grade.Grade;
import org.glisboa.backend.utils.domain.specification.SpecificationUtils;
import org.springframework.data.jpa.domain.Specification;

public class StudentSpecification {
    public static Specification<Student> filterBy(Grade grade, String name, String studentId, Status status) {
        return (Root<Student> root, CriteriaQuery<?> query, CriteriaBuilder cb) -> {

            Join<Student, Record> recordJoin = root.join("record");

            Predicate predicate = cb.conjunction();

            if (grade != null)
                predicate = cb.and(predicate, cb.equal(root.get("grade"), grade)); // enums use equal

            if (studentId != null && !studentId.isBlank())
                predicate = cb.and(predicate,
                        cb.like(cb.lower(root.get("studentId")), "%" + studentId.toLowerCase() + "%"));

            predicate = SpecificationUtils.applyNameAndStatusFilters(cb, predicate, recordJoin, name, status);

            return predicate;
        };
    }
}
