package org.glisboa.backend.domain.models.user.employee;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.glisboa.backend.domain.models.record.Record;
import org.glisboa.backend.domain.models.user.User;
import org.glisboa.backend.domain.models.user.employee.role.Role;

import java.time.LocalDateTime;

@Entity
@DiscriminatorValue("FUNCIONARIO")
@AllArgsConstructor
@NoArgsConstructor
@Getter @Setter
public class Employee extends User {

    @Column(nullable = false, unique = true, length = 11)
    private String cpf;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    @Column(name = "descricao", length = 20)
    private String description;

    @Column(name = "data_hora", nullable = false)
    private LocalDateTime createdAt;

    @ManyToOne
    @JoinColumn(name = "id_cadastro", nullable = false)
    private Record record;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }
}
