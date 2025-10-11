package org.glisboa.backend.dto.response.presence;

import java.time.LocalDateTime;

public record PresenceResponse(Integer id, LocalDateTime createdAt, String name) {
}
