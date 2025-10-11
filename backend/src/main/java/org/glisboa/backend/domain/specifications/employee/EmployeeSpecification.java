package org.glisboa.backend.domain.specifications.employee;

import jakarta.persistence.criteria.*;
import org.glisboa.backend.domain.models.employee.Employee;
import org.glisboa.backend.domain.models.employee.post.Post;
import org.glisboa.backend.domain.models.record.Record;
import org.glisboa.backend.domain.models.record.status.Status;
import org.glisboa.backend.utils.domain.specification.SpecificationUtils;
import org.springframework.data.jpa.domain.Specification;

public class EmployeeSpecification {

    public static Specification<Employee> filterBy(Post post, String name, String cpf, Status status) {
        return (Root<Employee> root, CriteriaQuery<?> query, CriteriaBuilder cb) -> {

            Join<Employee, Record> recordJoin = root.join("record");

            Predicate predicate = cb.conjunction();

            if (post != null) {
                predicate = cb.and(predicate, cb.equal(root.get("post"), post));
            }

            if (cpf != null && !cpf.isBlank()) {
                predicate = cb.and(predicate,
                        cb.like(cb.lower(root.get("cpf")), "%" + cpf.toLowerCase() + "%"));
            }

            predicate = SpecificationUtils.applyNameAndStatusFilters(cb, predicate, recordJoin, name, status);

            return predicate;
        };
    }
}
