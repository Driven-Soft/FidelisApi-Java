package br.com.fiap.java.FidelisApi.entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import java.util.Arrays;
import java.util.Locale;

public enum SexoPet {
    M("Macho"),
    F("Fêmea");

    private final String rotulo;

    SexoPet(String rotulo) {
        this.rotulo = rotulo;
    }

    @JsonValue
    public String getRotulo() {
        return rotulo;
    }

    @JsonCreator
    public static SexoPet fromTexto(String texto) {
        if (texto == null) {
            return null;
        }
        String chave = texto.trim().toLowerCase(Locale.ROOT);
        return Arrays.stream(values())
                .filter(sexo -> sexo.name().toLowerCase(Locale.ROOT).equals(chave)
                        || sexo.rotulo.toLowerCase(Locale.ROOT).equals(chave))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Sexo inválido: " + texto));
    }

    @Override
    public String toString() {
        return rotulo;
    }
}
