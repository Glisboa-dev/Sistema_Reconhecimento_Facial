package org.glisboa.backend.dto.request.student;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.glisboa.backend.domain.models.student.grade.Grade;

public record AddStudentRequest(
        @NotBlank(message = "A matrícula é obrigatória") String studentId,
        @NotNull(message = "A série é obrigatória") Grade grade) {
}
