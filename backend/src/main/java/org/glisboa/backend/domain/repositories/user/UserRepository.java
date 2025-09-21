package org.glisboa.backend.domain.repositories.user;

import org.glisboa.backend.domain.models.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Integer> {}