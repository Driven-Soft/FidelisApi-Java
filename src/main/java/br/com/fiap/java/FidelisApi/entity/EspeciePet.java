package br.com.fiap.java.FidelisApi.entity;

import java.util.Arrays;
import java.util.Locale;
import java.util.Optional;

public enum EspeciePet {

    CACHORRO("Cachorro", 31),
    GATO("Gato", 39);

    private final String rotulo;
    private final int idadeMaximaAnos;

    EspeciePet(String rotulo, int idadeMaximaAnos) {
        this.rotulo = rotulo;
        this.idadeMaximaAnos = idadeMaximaAnos;
    }

    public String getRotulo() {
        return rotulo;
    }

    public int getIdadeMaximaAnos() {
        return idadeMaximaAnos;
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