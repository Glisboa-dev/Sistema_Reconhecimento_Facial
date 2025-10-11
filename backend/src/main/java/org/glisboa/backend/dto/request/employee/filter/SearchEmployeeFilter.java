package org.glisboa.backend.dto.request.employee.filter;

import org.glisboa.backend.domain.models.employee.post.Post;
import org.glisboa.backend.domain.models.record.status.Status;

public record SearchEmployeeFilter(Post post, String name, String cpf, Status status) {
}
