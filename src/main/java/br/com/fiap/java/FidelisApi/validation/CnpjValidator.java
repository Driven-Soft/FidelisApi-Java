package br.com.fiap.java.FidelisApi.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class CnpjValidator implements ConstraintValidator<Cnpj, String> {

    @Override
    public boolean isValid(String cnpj, ConstraintValidatorContext context) {
        if (cnpj == null) {
            return true;
        }
        String digits = cnpj.replaceAll("[^0-9]", "");
        if (digits.length() != 14 || digits.chars().distinct().count() == 1) {
            return false;
        }
        try {
            int[] numbers = digits.chars().map(c -> c - '0').toArray();
            int firstVerifier = calculateVerifier(numbers, 12);
            int secondVerifier = calculateVerifier(numbers, 13);
            return firstVerifier == numbers[12] && secondVerifier == numbers[13];
        } catch (Exception e) {
            return false;
        }
    }

    private int calculateVerifier(int[] numbers, int length) {
        int[] weights = length == 12 ? new int[]{5, 4, 3, 2, 9, 8, 7, 6, 5, 4, 3, 2}
                : new int[]{6, 5, 4, 3, 2, 9, 8, 7, 6, 5, 4, 3, 2};
        int sum = 0;
        for (int i = 0; i < length; i++) {
            sum += numbers[i] * weights[i];
        }
        int remainder = sum % 11;
        return remainder < 2 ? 0 : 11 - remainder;
    }
}
