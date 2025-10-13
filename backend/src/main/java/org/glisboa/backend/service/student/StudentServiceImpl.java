package org.glisboa.backend.service.student;

import jakarta.persistence.EntityExistsException;
import lombok.RequiredArgsConstructor;
import org.glisboa.backend.domain.models.student.Student;
import org.glisboa.backend.domain.repositories.student.StudentRepository;
import org.glisboa.backend.domain.specifications.student.StudentSpecification;
import org.glisboa.backend.dto.request.student.AddStudentRequest;
import org.glisboa.backend.dto.request.student.filter.StudentSearchFilter;
import org.glisboa.backend.dto.response.student.StudentResponse;
import org.glisboa.backend.utils.domain.repo.RepositoryUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.PagedModel;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@RequiredArgsConstructor
public class StudentServiceImpl implements StudentService{
    private final StudentRepository studentRepo;

    @Override
    public Student createStudent(AddStudentRequest request) {
        String studentId = request.studentId();
        if(isStudentIdValid(studentId)){
            throw new EntityExistsException("Esta matrícula já está em uso");
        }

        return new Student(studentId, request.grade());
    }

    @Override
    public PagedModel<StudentResponse> searchStudents(StudentSearchFilter filter, Pageable pageable) {
        Page<Student> students = studentRepo.findAll(
                StudentSpecification.filterBy(
                        filter.grade(),
                        filter.name(),
                        filter.studentId(),
                        filter.status()
                ),
                pageable
        );

        List<StudentResponse> dtoList = students.stream()
                .map(s -> new StudentResponse(
                        s.getRecord().getId(),
                        s.getRecord().getName(),
                        s.getStudentId(),
                        s.getGrade(),
                        s.getRecord().getStatus()
                ))
                .toList();


        PagedModel.PageMetadata metadata = new PagedModel.PageMetadata(
                pageable.getPageSize(),
                students.getNumber(),
                students.getTotalElements(),
                students.getTotalPages()
        );

        return PagedModel.of(dtoList, metadata);
    }

    private void saveStudent(Student student){
        RepositoryUtils.saveEntity(studentRepo, student);
    }

    private boolean isStudentIdValid(String studentId){
        return studentRepo.existsByStudentId(studentId);
    }
}
