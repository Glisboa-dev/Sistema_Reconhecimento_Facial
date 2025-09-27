package org.glisboa.backend.domain.models.student.grade;

public enum Grade {
    PRIMEIRO_ANO("1 ANO"),
    SEGUNDO_ANO("2 ANO"),
    TERCEIRO_ANO("3 ANO"),
    QUARTO_ANO("4 ANO"),
    QUINTO_ANO("5 ANO"),
    SEXTO_ANO("6 ANO"),
    SETIMO_ANO("7 ANO"),
    OITAVO_ANO("8 ANO"),
    NONO_ANO("9 ANO"),
    PRIMEIRO_EM("1 EM"),
    SEGUNDO_EM("2 EM"),
    TERCEIRO_EM("3 EM");

    private final String label;

    Grade(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}
