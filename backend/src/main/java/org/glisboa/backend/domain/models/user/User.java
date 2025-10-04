package org.glisboa.backend.domain.models.user;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.glisboa.backend.domain.models.EntityModel;
import org.glisboa.backend.domain.models.record.Record;
import org.glisboa.backend.domain.models.user.role.Role;

@Entity
@Table(name = "usuario")
@AllArgsConstructor
@NoArgsConstructor
@Getter @Setter
public abstract class User extends EntityModel {

    @Column(unique = true, nullable = false, name = "username")
    private String username;

    @Column(nullable = false, name = "senha")
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, name = "papel")
    private Role role;

    @OneToOne
    @JoinColumn(name = "id_cadastro", nullable = false)
    private Record record;
}
