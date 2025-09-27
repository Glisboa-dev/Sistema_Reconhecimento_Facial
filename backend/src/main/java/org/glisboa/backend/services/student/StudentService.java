package org.glisboa.backend.services.student;

import org.glisboa.backend.domain.models.record.Record;
import org.glisboa.backend.dto.student.CreateStudentDTO;

public interface StudentService {
    void createStudent(CreateStudentDTO createStudentDTO, Record record);
}
