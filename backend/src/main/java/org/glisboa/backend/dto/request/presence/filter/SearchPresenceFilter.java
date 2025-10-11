package org.glisboa.backend.dto.request.presence.filter;

import org.glisboa.backend.domain.models.employee.post.Post;
import org.glisboa.backend.domain.models.student.grade.Grade;

import java.time.LocalDateTime;

public record SearchPresenceFilter(LocalDateTime from, LocalDateTime to, Grade grade, String name, Post post, boolean searchStudents) {
}
