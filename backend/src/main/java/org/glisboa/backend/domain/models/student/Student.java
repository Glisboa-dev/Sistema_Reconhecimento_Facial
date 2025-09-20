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

    @ManyToOne
    @JoinColumn(name = "id_cadastro", nullable = false)
    private Record record;

}
