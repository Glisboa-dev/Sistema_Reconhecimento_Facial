package org.glisboa.backend.services.record;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.glisboa.backend.domain.models.record.Record;
import org.glisboa.backend.domain.models.record.type.Type;
import org.glisboa.backend.domain.repositories.record.RecordRepository;
import org.glisboa.backend.dto.employee.CreateEmployeeDTO;
import org.glisboa.backend.dto.student.CreateStudentDTO;
import org.glisboa.backend.services.employee.EmployeeService;
import org.glisboa.backend.services.minIO.MinIOService;
import org.glisboa.backend.services.student.StudentService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@AllArgsConstructor
public class RecordServiceImpl implements RecordService{
    private final RecordRepository recordRepository;
    private final StudentService studentService;
    private final EmployeeService employeeService;
    private final MinIOService minIOService;

    @Transactional
    @Override
    public void createStudentRecord(String name,
                                    CreateStudentDTO createStudentDTO,
                                    MultipartFile file) {
        var record = new Record(name, Type.ALUNO);
        saveRecord(record);

        studentService.createStudent(createStudentDTO, record);

        record.setPictureUrl(minIOService.uploadFile(file));
        saveRecord(record);
    }

    @Transactional
    @Override
    public void createEmployeeRecord(String name,
                                     CreateEmployeeDTO createEmployeeDTO,
                                     MultipartFile file) {
        var record = new Record(name, Type.FUNCIONARIO);
        saveRecord(record);

        employeeService.createEmployee(createEmployeeDTO);

        record.setPictureUrl(minIOService.uploadFile(file));
        saveRecord(record);
    }

    @Transactional
    @Override
    public void deleteRecordById(Integer id) {
        var record = getRecordById(id);
        minIOService.deleteFile(record.getPictureUrl());

        if(record.getType().equals(Type.ALUNO)){
            studentService.deleteStudentByRecordId(id);
        }
        else{
            employeeService.deleteEmployeeByRecordId(id);
        }
        recordRepository.delete(record);
    }

    @Transactional
    @Override
    public void updateRecordName(Integer id, String newName) {
        var record = getRecordById(id);
        record.setName(newName);
        saveRecord(record);
    }

    private Record getRecordById(Integer id){
        return recordRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Registro não encontrado"));
    }

    private void saveRecord(Record record){
        recordRepository.save(record);
    }
}
