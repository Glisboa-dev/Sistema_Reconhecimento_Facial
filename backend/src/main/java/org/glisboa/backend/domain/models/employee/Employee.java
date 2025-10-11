package org.glisboa.backend.domain.models.employee;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.glisboa.backend.domain.models.EntityModel;
import org.glisboa.backend.domain.models.employee.post.Post;
import org.glisboa.backend.domain.models.record.Record;

@Entity
@Table(name = "FUNCIONARIO")
@AllArgsConstructor
@NoArgsConstructor
@Getter @Setter
public class Employee extends EntityModel {
    @Column(length = 11, nullable = false, unique = true)
    private String cpf;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, name = "cargo")
    private Post post; // CARGO

    @Column(length = 20, name = "descricao")
    private String description;

    @OneToOne
    @JoinColumn(name = "id_cadastro", nullable = false, unique = true)
    private Record record;

    public Employee(String cpf, Post post, String description) {
        super();
        this.cpf = cpf;
        this.post = post;
        this.description = description;
    }
}
