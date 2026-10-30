package br.com.fiap.java.FidelisApi.validation;

import br.com.fiap.java.FidelisApi.dto.request.PetRequest;
import br.com.fiap.java.FidelisApi.entity.EspeciePet;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.time.LocalDate;

public class DataNascimentoPlausivelValidator implements ConstraintValidator<DataNascimentoPlausivel, PetRequest> {

    @Override
    public boolean isValid(PetRequest request, ConstraintValidatorContext context) {
        if (request == null || request.getDataNascimento() == null) {
            return true;
        }

        EspeciePet especie = EspeciePet.fromTexto(request.getEspecie()).orElse(null);
        if (especie == null) {
            return true;
        }

        boolean dataPlausivel = !request.getDataNascimento()
                .isBefore(LocalDate.now().minusYears(especie.getIdadeMaximaAnos()));
        if (!dataPlausivel) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(context.getDefaultConstraintMessageTemplate())
                    .addPropertyNode("dataNascimento")
                    .addConstraintViolation();
        }
        return dataPlausivel;
    }
}
