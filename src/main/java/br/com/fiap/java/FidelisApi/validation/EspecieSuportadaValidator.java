package br.com.fiap.java.FidelisApi.validation;

import br.com.fiap.java.FidelisApi.entity.EspeciePet;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class EspecieSuportadaValidator implements ConstraintValidator<EspecieSuportada, String> {

    @Override
    public boolean isValid(String especie, ConstraintValidatorContext context) {
        if (especie == null || especie.isBlank()) {
            return true;
        }
        return EspeciePet.fromTexto(especie).isPresent();
    }
}