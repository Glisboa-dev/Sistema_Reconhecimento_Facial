package org.glisboa.backend.domain.repositories.record;

import org.glisboa.backend.domain.models.record.Record;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface RecordRepository extends JpaRepository<Record, Integer> {

}

