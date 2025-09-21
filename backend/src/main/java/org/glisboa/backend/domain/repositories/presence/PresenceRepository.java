package org.glisboa.backend.domain.repositories.presence;

import org.glisboa.backend.domain.models.presence.Presence;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PresenceRepository extends JpaRepository<Presence, Integer> {}

