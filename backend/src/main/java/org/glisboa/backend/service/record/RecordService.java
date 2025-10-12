package org.glisboa.backend.service.record;

import org.glisboa.backend.domain.models.record.Record;
import org.glisboa.backend.dto.request.employee.AddEmployeeRequest;
import org.glisboa.backend.dto.request.student.AddStudentRequest;
import org.springframework.web.multipart.MultipartFile;

public interface RecordService {
    void addNewStudentRecord(String name, MultipartFile file, AddStudentRequest addStudentRequest);
    void addNewEmployeeRecord(String name, MultipartFile file, AddEmployeeRequest addEmployeeRequest);
    void deleteRecordById(Integer id);
    void updateRecordName(Integer id, String newName);
    void updateRecordPhoto(Integer id, MultipartFile file);
    void deactivateRecord(Integer id);
    void activateRecord(Integer id);

    Record getRecordById(Integer registerId);
    void saveRecord(Record record);
    Record getRecordByEmployeeId(Integer employeeId);
}
