package org.glisboa.backend.domain.models;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;

@MappedSuperclass
@Getter
public abstract class ModeloEntidade {

    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
}
