package org.glisboa.backend.dto.response.employee;

import org.glisboa.backend.domain.models.employee.post.Post;
import org.glisboa.backend.domain.models.record.status.Status;

public record EmployeeResponse(Integer id, String name, Post post, String description, String cpf, Status status) {
}
