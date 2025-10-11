package org.glisboa.backend.dto.response.student;

import org.glisboa.backend.domain.models.record.status.Status;
import org.glisboa.backend.domain.models.student.grade.Grade;

public record StudentResponse(Integer id, String name, String studentId, Grade grade, Status status) {
}
