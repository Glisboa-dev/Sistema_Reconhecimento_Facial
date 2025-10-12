package org.glisboa.backend.service.token;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import lombok.RequiredArgsConstructor;
import org.glisboa.backend.domain.models.user.User;
import org.glisboa.backend.exception.auth.AuthException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Date;

@Service
@RequiredArgsConstructor
public class TokenServiceImpl implements TokenService {

    @Value("${jwt.secret}")
    private String secretKey;

    @Override
    public String genToken(User user) {
        try {
            var algorithm = Algorithm.HMAC256(secretKey);
            return JWT.create()
                    .withIssuer("auth-api")
                    .withSubject(user.getUsername())
                    .withClaim("role", user.getRole().name())
                    .withExpiresAt(Date.from(genExpDate()))
                    .sign(algorithm);
        } catch (JWTCreationException | UnsupportedEncodingException e) {
            throw new AuthException("Erro ao gerar token");
        }
    }

    @Override
    public String validateToken(String token) {
        try {
            var algorithm = Algorithm.HMAC256(secretKey);
            return JWT.require(algorithm)
                    .withIssuer("auth-api")
                    .build()
                    .verify(token)
                    .getSubject();
        } catch (Exception e) {
            throw new AuthException("Token inválido ou expirado");
        }
    }

    @Override
    public String getTokenClaim(String token) {
        try {
            var algorithm = Algorithm.HMAC256(secretKey);
            return JWT.require(algorithm)
                    .withIssuer("auth-api")
                    .build()
                    .verify(token)
                    .getClaim("role").asString();
        } catch (Exception e) {
            throw new AuthException("Não foi possível obter o subject do token");
        }
    }



    private Instant genExpDate() {
        return LocalDateTime.now().plusHours(3).toInstant(ZoneOffset.of("-03:00"));
    }
}
