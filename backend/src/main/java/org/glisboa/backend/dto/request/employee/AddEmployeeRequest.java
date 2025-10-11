package org.glisboa.backend.dto.request.employee;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.glisboa.backend.domain.models.employee.post.Post;
import org.hibernate.validator.constraints.Length;

public record AddEmployeeRequest(@NotBlank(message = "O cpf é obrigatório") @Length(max = 11, message = "CPF inválido") String cpf,
                                 @NotNull(message = "Cargo é obrigatório") Post post, String description) {
}
