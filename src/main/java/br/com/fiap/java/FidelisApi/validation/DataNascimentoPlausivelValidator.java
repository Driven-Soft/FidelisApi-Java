package br.com.fiap.java.FidelisApi.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.time.LocalDate;

public class DataNascimentoPlausivelValidator implements ConstraintValidator<DataNascimentoPlausivel, LocalDate> {

    private static final long IDADE_MAXIMA_ANOS = 100;

    @Override
    public boolean isValid(LocalDate dataNascimento, ConstraintValidatorContext context) {
        if (dataNascimento == null) {
            return true;
        }
        return !dataNascimento.isBefore(LocalDate.now().minusYears(IDADE_MAXIMA_ANOS));
    }
}
