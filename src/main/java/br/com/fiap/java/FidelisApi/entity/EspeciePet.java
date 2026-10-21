package br.com.fiap.java.FidelisApi.entity;

import java.util.Arrays;
import java.util.Locale;
import java.util.Optional;

public enum EspeciePet {

    CACHORRO("Cachorro"),
    GATO("Gato");

    private final String rotulo;

    EspeciePet(String rotulo) {
        this.rotulo = rotulo;
    }

    public String getRotulo() {
        return rotulo;
    }

    public static Optional<EspeciePet> fromTexto(String texto) {
        if (texto == null) {
            return Optional.empty();
        }
        String chave = texto.trim().toLowerCase(Locale.ROOT);
        return Arrays.stream(values())
                .filter(especie -> especie.rotulo.toLowerCase(Locale.ROOT).equals(chave)
                        || especie.name().toLowerCase(Locale.ROOT).equals(chave))
                .findFirst();
    }
}