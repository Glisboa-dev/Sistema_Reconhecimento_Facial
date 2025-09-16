package org.glisboa.backend.domain.models.cadastro;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.glisboa.backend.domain.models.ModeloEntidade;
import org.glisboa.backend.domain.models.presenca.Presenca;

import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter @Setter
public class Cadastro extends ModeloEntidade {

    @Column(length = 100, nullable = false)
    private String nome;

    @Column(length = 1, nullable = false)
    private Character status;

    private String foto; // opcional (Url)

    @OneToMany(mappedBy = "cadastro", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Presenca> presencas;
}
