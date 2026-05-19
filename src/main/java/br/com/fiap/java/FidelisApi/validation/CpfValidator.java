package br.com.fiap.java.FidelisApi.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class CpfValidator implements ConstraintValidator<Cpf, String> {

    @Override
    public boolean isValid(String cpf, ConstraintValidatorContext context) {
        if (cpf == null) {
            return true;
        }
        String digits = cpf.replaceAll("[^0-9]", "");
        if (digits.length() != 11 || digits.chars().distinct().count() == 1) {
            return false;
        }
        try {
            int[] numbers = digits.chars().map(c -> c - '0').toArray();
            int firstVerifier = calculateVerifier(numbers, 9);
            int secondVerifier = calculateVerifier(numbers, 10);
            return firstVerifier == numbers[9] && secondVerifier == numbers[10];
        } catch (Exception e) {
            return false;
        }
    }

    private int calculateVerifier(int[] numbers, int length) {
        int sum = 0;
        for (int i = 0; i < length; i++) {
            sum += numbers[i] * (length + 1 - i);
        }
        int remainder = sum % 11;
        return remainder < 2 ? 0 : 11 - remainder;
    }
}
