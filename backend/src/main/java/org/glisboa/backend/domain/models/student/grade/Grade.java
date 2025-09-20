package org.glisboa.backend.domain.models.student.grade;

public enum Grade {
    PRIMEIRO_ANO("1º Ano"),
    SEGUNDO_ANO("2º Ano"),
    TERCEIRO_ANO("3º Ano"),
    QUARTO_ANO("4º Ano"),
    QUINTO_ANO("5º Ano"),
    SEXTO_ANO("6º Ano"),
    SETIMO_ANO("7º Ano"),
    OITAVO_ANO("8º Ano"),
    NONO_ANO("9º Ano"),
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
