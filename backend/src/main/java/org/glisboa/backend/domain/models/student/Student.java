package org.glisboa.backend.domain.models.student;

import jakarta.persistence.*;
import org.glisboa.backend.domain.models.EntityModel;
import org.glisboa.backend.domain.models.record.Record;
import org.glisboa.backend.domain.models.student.grade.Grade;

@Entity
@Table(name = "aluno")
public class Student extends EntityModel {

    @Column(name = "matricula", nullable = false, unique = true)
    private Integer studentId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, name = "serie")
    private Grade grade;

    @OneToOne
    @JoinColumn(name = "id_cadastro", nullable = false, unique = true)
    private Record record;

    public Student(Integer studentId, Grade grade, Record record) {
        super();
        this.studentId = studentId;
        this.grade = grade;
        this.record = record;
    }
}
