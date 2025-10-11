package org.glisboa.backend.domain.repositories.user;

import org.glisboa.backend.domain.models.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;

public interface UserRepository extends JpaRepository<User, Integer> {
    boolean existsByUsername(String username);
    UserDetails findByUsername(String username);
}