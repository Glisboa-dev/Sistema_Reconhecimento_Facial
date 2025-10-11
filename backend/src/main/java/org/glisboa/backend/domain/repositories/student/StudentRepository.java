package org.glisboa.backend.domain.repositories.student;

import org.glisboa.backend.domain.models.student.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface StudentRepository extends JpaRepository<Student, Integer>, JpaSpecificationExecutor<Student> {
    boolean existsByStudentId(String studentId);


}