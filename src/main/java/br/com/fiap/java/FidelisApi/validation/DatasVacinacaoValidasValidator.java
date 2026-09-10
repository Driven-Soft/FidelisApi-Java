package br.com.fiap.java.FidelisApi.validation;

import br.com.fiap.java.FidelisApi.dto.request.VacinacaoRequest;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class DatasVacinacaoValidasValidator implements ConstraintValidator<DatasVacinacaoValidas, VacinacaoRequest> {

    @Override
    public boolean isValid(VacinacaoRequest request, ConstraintValidatorContext context) {
        if (request == null || request.getDataAplicacao() == null || request.getDataProxima() == null) {
            return true;
        }
        if (!request.getDataProxima().isBefore(request.getDataAplicacao())) {
            return true;
        }

        context.disableDefaultConstraintViolation();
        context.buildConstraintViolationWithTemplate(context.getDefaultConstraintMessageTemplate())
                .addPropertyNode("dataProxima")
                .addConstraintViolation();
        return false;
    }
}
