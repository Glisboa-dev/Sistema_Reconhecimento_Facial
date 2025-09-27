package org.glisboa.backend.domain.models.presence;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.glisboa.backend.domain.models.EntityModel;
import org.glisboa.backend.domain.models.log.Log;
import org.glisboa.backend.domain.models.record.Record;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "presenca")
@AllArgsConstructor
@NoArgsConstructor
@Getter @Setter
public class Presence extends EntityModel {

    @ManyToOne
    @JoinColumn(name = "id_cadastro", nullable = false)
    private Record record;

    @Column(name = "data_hora", nullable = false)
    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "presence", cascade = CascadeType.ALL)
    private List<Log> logs;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }
}
