package br.com.fiap.java.FidelisApi.validation;

import br.com.fiap.java.FidelisApi.dto.request.ConsultaRequest;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class DatasConsultaValidasValidator implements ConstraintValidator<DatasConsultaValidas, ConsultaRequest> {

    @Override
    public boolean isValid(ConsultaRequest request, ConstraintValidatorContext context) {
        if (request == null || request.getDataHora() == null || request.getDataRetorno() == null) {
            return true;
        }
        if (!request.getDataRetorno().isBefore(request.getDataHora().toLocalDate())) {
            return true;
        }

        context.disableDefaultConstraintViolation();
        context.buildConstraintViolationWithTemplate(context.getDefaultConstraintMessageTemplate())
                .addPropertyNode("dataRetorno")
                .addConstraintViolation();
        return false;
    }
}
