package org.glisboa.backend.domain.models.presenca;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.glisboa.backend.domain.models.ModeloEntidade;
import org.glisboa.backend.domain.models.cadastro.Cadastro;

import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter @Setter
public class Presenca extends ModeloEntidade {

    @Column(nullable = false)
    private LocalDate data;

    @Column(nullable = false)
    private LocalTime time;

    @ManyToOne(optional = false)
    @JoinColumn(name = "cadastro_id", nullable = false)
    private Cadastro cadastro;
}
