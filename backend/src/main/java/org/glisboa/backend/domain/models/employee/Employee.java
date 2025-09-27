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
@Table(name = "funcionario")
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

    @ManyToOne
    @JoinColumn(name = "id_cadastro", nullable = false)
    private Record record;
}
