package org.glisboa.backend.domain.repositories.record;

import org.glisboa.backend.domain.models.record.Record;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RecordRepository extends JpaRepository<Record, Integer> {

  boolean existsByNameIgnoreCase(String name);
}

