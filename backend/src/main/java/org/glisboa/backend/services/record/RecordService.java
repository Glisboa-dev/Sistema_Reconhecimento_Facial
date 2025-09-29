package org.glisboa.backend.services.record;

import org.glisboa.backend.dto.employee.CreateEmployeeDTO;
import org.glisboa.backend.dto.student.CreateStudentDTO;
import org.springframework.web.multipart.MultipartFile;

public interface RecordService {
    void createStudentRecord(String name, CreateStudentDTO createStudentDTO, MultipartFile file);
    void createEmployeeRecord(String name, CreateEmployeeDTO createEmployeeDTO, MultipartFile file);
    void deleteRecordById(Integer id);
    void updateRecordName(Integer id, String newName);


}
