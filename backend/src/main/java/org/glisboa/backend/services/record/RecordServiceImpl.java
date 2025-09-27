package org.glisboa.backend.services.record;

import lombok.AllArgsConstructor;
import org.glisboa.backend.domain.models.record.Record;
import org.glisboa.backend.domain.models.record.type.Type;
import org.glisboa.backend.domain.repositories.record.RecordRepository;
import org.glisboa.backend.dto.employee.CreateEmployeeDTO;
import org.glisboa.backend.dto.record.CreateRecordDTO;
import org.glisboa.backend.dto.student.CreateStudentDTO;
import org.glisboa.backend.services.employee.EmployeeService;
import org.glisboa.backend.services.student.StudentService;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class RecordServiceImpl implements RecordService{
    private final RecordRepository recordRepository;
    private final StudentService studentService;
    private final EmployeeService employeeService;


    @Override
    public <T> void createRecord(CreateRecordDTO createRecordDTO, T createEntityDTO) {
        var record = new Record(createRecordDTO.name(), createRecordDTO.type());
        saveRecord(record);

        if (createRecordDTO.type().equals(Type.ALUNO)) {
            studentService.createStudent((CreateStudentDTO) createEntityDTO, record);
        } else {
            employeeService.createEmployee((CreateEmployeeDTO) createEntityDTO);
        }
    }

    private void saveRecord(Record record){
        recordRepository.save(record);
    }
}
