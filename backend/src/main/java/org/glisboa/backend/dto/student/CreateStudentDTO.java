package org.glisboa.backend.dto.student;

import jakarta.validation.constraints.NotBlank;
import org.glisboa.backend.domain.models.student.grade.Grade;

public record CreateStudentDTO(@NotBlank(message = "A matrícula é obrigatória") String studentId, @NotBlank(message = "A série é obrigatória") Grade grade) {
}
