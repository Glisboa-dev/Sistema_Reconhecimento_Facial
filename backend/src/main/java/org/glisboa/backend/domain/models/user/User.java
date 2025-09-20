package org.glisboa.backend.domain.models.user;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.glisboa.backend.domain.models.EntityModel;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@Table(name = "usuario")
@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
@DiscriminatorColumn(name = "papel")
public abstract class User extends EntityModel {

    @Column(unique = true, nullable = false, name = "username")
    private String username;

    @Column(nullable = false, name = "senha")
    private String password;
}
