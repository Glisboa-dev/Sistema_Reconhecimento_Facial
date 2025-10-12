package org.glisboa.backend.domain.models.presence;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.glisboa.backend.domain.models.EntityModel;
import org.glisboa.backend.domain.models.record.Record;

import java.time.LocalDateTime;


@Entity
@Table(name = "PRESENCA")
@AllArgsConstructor
@NoArgsConstructor
@Getter @Setter
public class Presence extends EntityModel {

    @ManyToOne
    @JoinColumn(name = "id_cadastro", nullable = false)
    private Record record;

    @Column(name = "data_hora", nullable = false)
    private LocalDateTime createdAt;

    public Presence(Record record) {
        super();
        this.record = record;
        this.createdAt = LocalDateTime.now();
    }

}
