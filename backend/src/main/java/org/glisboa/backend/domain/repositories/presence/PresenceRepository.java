package org.glisboa.backend.domain.repositories.presence;

import org.glisboa.backend.domain.models.presence.Presence;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface PresenceRepository extends JpaRepository<Presence, Integer>, JpaSpecificationExecutor<Presence> {}

