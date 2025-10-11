package org.glisboa.backend.domain.models.record;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.glisboa.backend.domain.models.EntityModel;
import org.glisboa.backend.domain.models.employee.Employee;
import org.glisboa.backend.domain.models.presence.Presence;
import org.glisboa.backend.domain.models.record.status.Status;
import org.glisboa.backend.domain.models.record.type.Type;
import org.glisboa.backend.domain.models.student.Student;
import org.glisboa.backend.domain.models.user.User;

import java.util.List;

@Entity
@Table(name = "CADASTRO")
@AllArgsConstructor
@NoArgsConstructor
@Getter @Setter
public class Record extends EntityModel {

    @Column(length = 20, nullable = false, name = "nome")
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Status status; // ATIVO, INATIVO

    @Column(name = "foto")
    private String pictureUrl;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Type type; // ALUNO, FUNCIONARIO

    @OneToMany(mappedBy = "record", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Presence> presences;

    @OneToOne(mappedBy = "record", cascade = CascadeType.ALL, orphanRemoval = true)
    private Student student;

    @OneToOne(mappedBy = "record", cascade = CascadeType.ALL, orphanRemoval = true)
    private Employee employee;


    @OneToOne(mappedBy = "record", cascade = CascadeType.ALL, orphanRemoval = true)
    private User user;

    public Record(String name, Type type) {
        super();
        this.name = name;
        this.type = type;
        this.status = Status.ATIVO;
    }

    public void addPresence(Presence presence) {
        this.presences.add(presence);
        presence.setRecord(this);
    }
}
