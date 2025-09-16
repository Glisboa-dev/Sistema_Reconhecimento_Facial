package org.glisboa.backend.domain.models.aluno;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.glisboa.backend.domain.models.ModeloEntidade;
import org.glisboa.backend.domain.models.cadastro.Cadastro;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter @Setter
public class Aluno extends ModeloEntidade {

    @Column(unique = true, nullable = false)
    private Integer matricula;

    private Boolean serie; // 0 - Fundamental, 1 - Médio

    @OneToOne(optional = false)
    @JoinColumn(name = "cadastro_id")
    private Cadastro cadastro;
}
