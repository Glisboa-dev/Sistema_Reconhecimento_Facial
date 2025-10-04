package org.glisboa.backend.services.student;

import lombok.AllArgsConstructor;
import org.glisboa.backend.domain.models.record.Record;
import org.glisboa.backend.domain.models.student.Student;
import org.glisboa.backend.domain.repositories.student.StudentRepository;
import org.glisboa.backend.dto.student.CreateStudentDTO;
import org.glisboa.backend.exception.EntityAlreadyExistsException;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class StudentServiceImpl implements  StudentService {
    private final StudentRepository studentRepository;


    @Override
    public void createStudent(CreateStudentDTO createStudentDTO, Record record) {
        String studentId = createStudentDTO.studentId();
        validateStudentId(studentId);
        saveStudent(new Student(studentId, createStudentDTO.grade(), record));
    }

    @Override
    public void deleteStudentByRecordId(Integer recordId) {
        studentRepository.deleteByRecord_Id(recordId);
    }

    private void validateStudentId(String studentId) {
        if(studentRepository.existsByStudentId(studentId)) {
            throw new EntityAlreadyExistsException("Matrícula já existe");
        }
    }

    private void saveStudent(Student student) {
        studentRepository.save(student);
    }
}
