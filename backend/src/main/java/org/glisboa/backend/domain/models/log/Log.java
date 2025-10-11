package org.glisboa.backend.domain.models.log;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.glisboa.backend.domain.models.EntityModel;
import org.glisboa.backend.domain.models.log.operationType.OperationType;
import org.glisboa.backend.domain.models.presence.Presence;

import java.time.LocalDateTime;

@Entity
@Table(name = "AUDITORIA")
@AllArgsConstructor
@NoArgsConstructor
@Getter @Setter
public class Log extends EntityModel {

    @Column(name = "tabela_afetada", nullable = false, length = 50)
    private String affectedTable;

    @Column(name = "tipo_operacao", nullable = false)
    @Enumerated(EnumType.STRING)
    private OperationType operationType;

    @Column(name = "registro_anterior", columnDefinition = "TEXT")
    private String pastValue;

    @Column(name = "registro_novo", columnDefinition = "TEXT")
    private String newValue;

    @Column(name = "data_hora", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "id_cadastro", nullable = false)
    private Integer id_record;

    @ManyToOne
    @JoinColumn(name = "id_presenca")
    private Presence presence;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }
}
