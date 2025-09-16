package org.glisboa.backend.domain.models.presenca;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.glisboa.backend.domain.models.ModeloEntidade;
import org.glisboa.backend.domain.models.cadastro.Cadastro;

import java.time.LocalDateTime;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter @Setter
public class Presenca extends ModeloEntidade {

    @Column(nullable = false)
    private LocalDateTime dataHoraRegistro;

    @ManyToOne(optional = false)
    @JoinColumn(name = "cadastro_id", nullable = false)
    private Cadastro cadastro;
}
