package br.com.fiap.java.FidelisApi.validation;

import br.com.fiap.java.FidelisApi.dto.request.ClinicaRequest;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ClinicaRequestValidationTest {

    private static Validator validator;

    @BeforeAll
    static void setUpValidator() {
        validator = Validation.buildDefaultValidatorFactory().getValidator();
    }

    @Test
    void deveAceitarClinicaValida() {
        assertThat(validator.validate(requestBase())).isEmpty();
    }

    @Test
    void deveRejeitarNomeMuitoCurto() {
        ClinicaRequest request = requestBase();
        request.setNome("AB");

        assertThat(validator.validate(request))
                .extracting("message")
                .contains("Nome da clínica deve ter entre 3 e 100 caracteres");
    }

    @Test
    void deveRejeitarTelefoneMuitoCurto() {
        ClinicaRequest request = requestBase();
        request.setTelefone("123456789");

        assertThat(validator.validate(request))
                .extracting("message")
                .contains("Telefone deve ter entre 10 e 15 caracteres");
    }

    @Test
    void deveRejeitarEmailInvalido() {
        ClinicaRequest request = requestBase();
        request.setEmail("email-invalido");

        assertThat(validator.validate(request))
                .extracting("message")
                .contains("Email inválido");
    }

    private ClinicaRequest requestBase() {
        ClinicaRequest request = new ClinicaRequest();
        request.setNome("Clínica Fidelis");
        request.setCnpj("12.345.678/0001-95");
        request.setTelefone("(11) 3333-4444");
        request.setEmail("contato@fidelis.com");
        request.setEndereco("Rua das Flores, 100");
        return request;
    }
}
