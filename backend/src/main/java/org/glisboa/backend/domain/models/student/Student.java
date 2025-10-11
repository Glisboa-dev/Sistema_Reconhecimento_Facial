package org.glisboa.backend.domain.models.student;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.glisboa.backend.domain.models.EntityModel;
import org.glisboa.backend.domain.models.record.Record;
import org.glisboa.backend.domain.models.student.grade.Grade;

@Entity
@Table(name = "ALUNO")
@AllArgsConstructor
@NoArgsConstructor
@Getter @Setter
public class Student extends EntityModel {

    @Column(name = "matricula", nullable = false, unique = true)
    private String studentId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, name = "serie")
    private Grade grade;

    @OneToOne
    @JoinColumn(name = "id_cadastro", nullable = false, unique = true)
    private Record record;

    public Student(String studentId, Grade grade) {
        super();
        this.studentId = studentId;
        this.grade = grade;
    }

}
