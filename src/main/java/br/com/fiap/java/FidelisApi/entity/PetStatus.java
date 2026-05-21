package br.com.fiap.java.FidelisApi.entity;

public enum PetStatus {

    ATIVO('A'),
    INATIVO('I');

    private final char code;

    PetStatus(char code) {
        this.code = code;
    }

    public char getCode() {
        return code;
    }

    public static PetStatus fromCode(char code) {
        for (PetStatus status : values()) {
            if (status.code == code) {
                return status;
            }
        }
        throw new IllegalArgumentException("Código de status de pet inválido: " + code);
    }
}
