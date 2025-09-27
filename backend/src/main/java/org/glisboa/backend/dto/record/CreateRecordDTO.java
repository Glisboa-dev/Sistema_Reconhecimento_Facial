package org.glisboa.backend.dto.record;

import org.glisboa.backend.domain.models.record.type.Type;

public record CreateRecordDTO(String name, Type type) {
}
