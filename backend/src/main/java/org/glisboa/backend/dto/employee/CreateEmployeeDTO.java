package org.glisboa.backend.dto.employee;

import jakarta.validation.constraints.NotBlank;
import org.glisboa.backend.domain.models.employee.post.Post;
import org.hibernate.validator.constraints.Length;

public record CreateEmployeeDTO(@NotBlank(message = "O cpf é obrigatório") @Length(max = 11, message = "CPF inválido") String cpf,
                                @NotBlank(message = "Cargo é obrigatório") Post post, String description) {
}
