package org.glisboa.backend.service.record;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.glisboa.backend.domain.models.record.Record;
import org.glisboa.backend.domain.models.record.status.Status;
import org.glisboa.backend.domain.models.record.type.Type;
import org.glisboa.backend.domain.repositories.record.RecordRepository;
import org.glisboa.backend.dto.request.employee.AddEmployeeRequest;
import org.glisboa.backend.dto.request.student.AddStudentRequest;
import org.glisboa.backend.service.employee.EmployeeService;
import org.glisboa.backend.service.minIO.MinIOService;
import org.glisboa.backend.service.student.StudentService;
import org.glisboa.backend.utils.domain.repo.RepositoryUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class RecordServiceImpl implements RecordService{
    private final RecordRepository recordRepo;
    private final StudentService studentService;
    private final EmployeeService employeeService;
    private final MinIOService minIOService;

    @Transactional
    @Override
    public void addNewStudentRecord(String name, MultipartFile file, AddStudentRequest addStudentRequest) {
        var record = new Record(name, Type.ALUNO);
        var student = studentService.createStudent(addStudentRequest);
        var pictureUrl = minIOService.uploadFile(file);

        record.setPictureUrl(pictureUrl);
        record.setStudent(student);
        student.setRecord(record);

        saveRecord(record);
        sendAddEmbenddingRequest(record.getId(), pictureUrl);
    }

    @Override
    public void addNewEmployeeRecord(String name, MultipartFile file, AddEmployeeRequest addEmployeeRequest) {
        var record = new Record(name, Type.FUNCIONARIO);
        var employee = employeeService.createEmployee(addEmployeeRequest);
        var pictureUrl = minIOService.uploadFile(file);

        record.setPictureUrl(pictureUrl);
        record.setEmployee(employee);
        employee.setRecord(record);

        saveRecord(record);
        sendAddEmbenddingRequest(record.getId(), pictureUrl);
    }

    private void sendAddEmbenddingRequest(Integer id, String objectName) {
        try {
            var url = "http://localhost:5001/add_embedding"; // or the container name if using Docker network
            var restTemplate = new RestTemplate();

            Map<String, Object> request = new HashMap<>();
            request.put("record_id", id);
            request.put("object_name", objectName);

            restTemplate.postForEntity(url, request, String.class);
        } catch (Exception e) {
            System.err.println("Failed to send embedding request: " + e.getMessage());
        }
    }

    @Transactional
    @Override
    public void deleteRecordById(Integer id) {
        var record = getRecordById(id);
        String pictureUrl = record.getPictureUrl();
        RepositoryUtils.deleteEntity(recordRepo, record);
        minIOService.deleteFile(pictureUrl);
    }

    @Override
    public void updateRecordName(Integer id, String newName) {
        var record = getRecordById(id);
        record.setName(newName);
        saveRecord(record);
    }

    @Transactional
    @Override
    public void updateRecordPhoto(Integer id, MultipartFile file) {
        var record = getRecordById(id);
        String oldPictureUrl = record.getPictureUrl();
        String newPictureUrl = minIOService.uploadFile(file);
        record.setPictureUrl(newPictureUrl);
        minIOService.deleteFile(oldPictureUrl);
        saveRecord(record);
    }

    @Transactional
    @Override
    public void deactivateRecord(Integer id) {
        var record = getRecordById(id);
        record.setStatus(Status.INATIVO);
        saveRecord(record);
    }

    @Override
    public Record getRecordById(Integer id) {
        return RepositoryUtils.findEntityByIdOrThrow(recordRepo, id);
    }

    @Override
    public void saveRecord(Record record) {
        RepositoryUtils.saveEntity(recordRepo, record);
    }
}
