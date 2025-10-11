package org.glisboa.backend.service.token;

import org.glisboa.backend.domain.models.user.User;

public interface TokenService {
    String genToken(User user);
    String validateToken(String token);
}
