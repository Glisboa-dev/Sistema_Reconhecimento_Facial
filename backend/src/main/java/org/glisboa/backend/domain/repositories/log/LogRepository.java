package org.glisboa.backend.domain.repositories.log;

import org.glisboa.backend.domain.models.log.Log;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LogRepository extends JpaRepository<Log, Integer> {}