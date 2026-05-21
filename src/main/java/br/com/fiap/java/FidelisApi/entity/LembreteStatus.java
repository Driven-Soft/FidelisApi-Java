package br.com.fiap.java.FidelisApi.entity;

public enum LembreteStatus {
    PENDENTE('P'),
    CONCLUIDO('C');

    private final char code;

    LembreteStatus(char code) {
        this.code = code;
    }

    public char getCode() {
        return code;
    }

    public static LembreteStatus fromCode(char code) {
        for (LembreteStatus status : values()) {
            if (status.code == code) {
                return status;
            }
        }
        throw new IllegalArgumentException("Código de status de lembrete inválido: " + code);
    }
}
