package org.glisboa.backend.utils.domain.specification;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Predicate;
import org.glisboa.backend.domain.models.record.Record;
import org.glisboa.backend.domain.models.record.status.Status;

public class SpecificationUtils {
    public static Predicate applyNameAndStatusFilters(
            CriteriaBuilder cb,
            Predicate predicate,
            Join<?, Record> recordJoin,
            String name,
            Status status
    ) {
        if (name != null && !name.isBlank()) {
            String trimmedLower = name.trim().toLowerCase();
            predicate = cb.and(predicate, cb.like(cb.lower(recordJoin.get("name")), "%" + trimmedLower + "%"));
        }

        if (status != null) {
            predicate = cb.and(predicate, cb.equal(recordJoin.get("status"), status));
        }

        return predicate;
    }
}
