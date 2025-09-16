package org.glisboa.backend.domain.models.funcionario;

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
public class Funcionario extends ModeloEntidade {

    @Column(length = 11, unique = true, nullable = false)
    private String cpf;

    private Boolean cargo; //0 - Administrativo, 1 - Docente


    private String descricao; // opcional

    @OneToOne(optional = false)
    @JoinColumn(name = "cadastro_id")
    private Cadastro cadastro;
}
