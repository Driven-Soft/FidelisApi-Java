package br.com.fiap.java.FidelisApi.validation;

import br.com.fiap.java.FidelisApi.dto.request.VeterinarioRequest;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

class VeterinarioRequestValidationTest {

    private static Validator validator;

    @BeforeAll
    static void setUpValidator() {
        validator = Validation.buildDefaultValidatorFactory().getValidator();
    }

    @Test
    void deveAceitarVeterinarioValido() {
        assertThat(validator.validate(requestBase())).isEmpty();
    }

    @Test
    void deveRejeitarCmvvMuitoCurto() {
        VeterinarioRequest request = requestBase();
        request.setCmvv("1234");

        assertThat(validator.validate(request))
                .extracting("message")
                .contains("CMVV deve ter entre 5 e 13 caracteres");
    }

    @Test
    void deveRejeitarEspecialidadeMuitoCurta() {
        VeterinarioRequest request = requestBase();
        request.setEspecialidade("AB");

        assertThat(validator.validate(request))
                .extracting("message")
                .contains("Especialidade deve ter entre 3 e 50 caracteres");
    }

    @Test
    void deveRejeitarDataDeCriacaoFutura() {
        VeterinarioRequest request = requestBase();
        request.setDataCriacao(LocalDate.now().plusDays(1));

        assertThat(validator.validate(request))
                .extracting("message")
                .contains("Data de criação não pode ser futura");
    }

    private VeterinarioRequest requestBase() {
        VeterinarioRequest request = new VeterinarioRequest();
        request.setCmvv("CRMV-SP 12345");
        request.setNome("Ana Veterinária");
        request.setEmail("ana.vet@example.com");
        request.setSenha("Senha123");
        request.setEspecialidade("Clínica Geral");
        request.setDataCriacao(LocalDate.now());
        request.setClinicaId(1L);
        return request;
    }
}
