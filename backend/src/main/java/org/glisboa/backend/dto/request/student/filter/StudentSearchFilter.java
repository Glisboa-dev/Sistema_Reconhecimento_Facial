package org.glisboa.backend.dto.request.student.filter;

import org.glisboa.backend.domain.models.record.status.Status;
import org.glisboa.backend.domain.models.student.grade.Grade;

public record StudentSearchFilter(Grade grade, String name, String studentId, Status status) {
}
