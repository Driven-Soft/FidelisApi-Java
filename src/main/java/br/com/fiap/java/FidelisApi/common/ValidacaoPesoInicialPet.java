package br.com.fiap.java.FidelisApi.common;

import br.com.fiap.java.FidelisApi.entity.EspeciePet;
import br.com.fiap.java.FidelisApi.entity.Pet;
import br.com.fiap.java.FidelisApi.exception.BusinessException;

import java.math.BigDecimal;

public final class ValidacaoPesoInicialPet {

    private static final BigDecimal PESO_MINIMO_GATO_KG = new BigDecimal("0.5");
    private static final BigDecimal PESO_MAXIMO_GATO_KG = new BigDecimal("22");
    private static final BigDecimal PESO_MINIMO_CACHORRO_KG = new BigDecimal("0.1");
    private static final BigDecimal PESO_MAXIMO_CACHORRO_KG = new BigDecimal("157");

    private ValidacaoPesoInicialPet() {
    }

    public static void validar(Pet pet) {
        BigDecimal pesoInicial = pet.getHistoricoPeso().stream()
                .map(historico -> historico.getPesoKg())
                .findFirst()
                .orElse(null);
        if (pesoInicial == null) {
            return;
        }

        BigDecimal pesoMinimo = pesoMinimo(pet.getEspecie());
        BigDecimal pesoMaximo = pesoMaximo(pet.getEspecie());
        if (pesoInicial.compareTo(pesoMinimo) < 0 || pesoInicial.compareTo(pesoMaximo) > 0) {
            throw new BusinessException(String.format(
                    "Peso para %s no cadastro deve estar entre %s kg e %s kg",
                    pet.getEspecie().getRotulo(),
                    pesoMinimo.toPlainString(),
                    pesoMaximo.toPlainString()));
        }
    }

    public static BigDecimal pesoMinimo(EspeciePet especie) {
        return switch (especie) {
            case GATO -> PESO_MINIMO_GATO_KG;
            case CACHORRO -> PESO_MINIMO_CACHORRO_KG;
        };
    }

    public static BigDecimal pesoMaximo(EspeciePet especie) {
        return switch (especie) {
            case GATO -> PESO_MAXIMO_GATO_KG;
            case CACHORRO -> PESO_MAXIMO_CACHORRO_KG;
        };
    }
}
