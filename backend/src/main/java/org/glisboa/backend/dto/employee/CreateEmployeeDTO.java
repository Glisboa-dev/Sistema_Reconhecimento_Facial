package org.glisboa.backend.dto.employee;

import org.glisboa.backend.domain.models.employee.post.Post;

public record CreateEmployeeDTO(String cpf, Post post, String description) {
}
