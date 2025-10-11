package org.glisboa.backend.service.student;

import org.glisboa.backend.domain.models.student.Student;
import org.glisboa.backend.dto.request.student.AddStudentRequest;
import org.glisboa.backend.dto.request.student.filter.StudentSearchFilter;
import org.glisboa.backend.dto.response.student.StudentResponse;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.PagedModel;

public interface StudentService {
    Student createStudent(AddStudentRequest request);
    PagedModel<StudentResponse> searchStudents(StudentSearchFilter filter, Pageable pageable);
}
