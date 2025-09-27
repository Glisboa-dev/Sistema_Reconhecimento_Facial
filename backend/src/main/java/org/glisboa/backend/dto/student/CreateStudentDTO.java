package org.glisboa.backend.dto.student;

import org.glisboa.backend.domain.models.student.grade.Grade;

public record CreateStudentDTO(Integer studentId, Grade grade) {
}
