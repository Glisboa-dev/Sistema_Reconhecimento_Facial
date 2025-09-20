package org.glisboa.backend.domain.models.record;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.glisboa.backend.domain.models.EntityModel;
import org.glisboa.backend.domain.models.log.Log;
import org.glisboa.backend.domain.models.presence.Presence;
import org.glisboa.backend.domain.models.record.status.Status;
import org.glisboa.backend.domain.models.student.Student;
import org.glisboa.backend.domain.models.user.employee.Employee;

import java.util.List;

@Entity
@Table(name = "cadastro")
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

    @OneToMany(mappedBy = "record", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Student> students;

    @OneToMany(mappedBy = "record", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Employee> employees;

    @OneToMany(mappedBy = "record", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Presence> presences;

    @OneToMany(mappedBy = "record", cascade = CascadeType.ALL)
    private List<Log> logs;
}
