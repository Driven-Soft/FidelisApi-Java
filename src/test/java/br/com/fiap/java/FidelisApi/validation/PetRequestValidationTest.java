package br.com.fiap.java.FidelisApi.validation;

import br.com.fiap.java.FidelisApi.dto.request.PetRequest;
import br.com.fiap.java.FidelisApi.entity.PetStatus;
import br.com.fiap.java.FidelisApi.entity.SexoPet;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

class PetRequestValidationTest {

    private static Validator validator;

    @BeforeAll
    static void setUpValidator() {
        validator = Validation.buildDefaultValidatorFactory().getValidator();
    }

    @Test
    void deveRejeitarDataDeNascimentoFutura() {
        PetRequest request = requestBase();
        request.setDataNascimento(LocalDate.now().plusDays(1));

        assertThat(validator.validate(request))
                .extracting("message")
                .contains("Data de nascimento não pode ser futura");
    }

    @Test
    void deveAceitarDataDeNascimentoAtual() {
        PetRequest request = requestBase();
        request.setDataNascimento(LocalDate.now());

        assertThat(validator.validate(request)).isEmpty();
    }

    @Test
    void deveRejeitarDataDeNascimentoComMaisDeCemAnos() {
        PetRequest request = requestBase();
        request.setDataNascimento(LocalDate.now().minusYears(100).minusDays(1));

        assertThat(validator.validate(request))
                .extracting("message")
                .contains("Data de nascimento não pode indicar idade superior a 100 anos");
    }

    @Test
    void deveRejeitarNomeMuitoCurto() {
        PetRequest request = requestBase();
        request.setNome("R");

        assertThat(validator.validate(request))
                .extracting("message")
                .contains("Nome deve ter entre 3 e 30 caracteres");
    }

    @Test
    void deveRejeitarNomeComCaracteresInvalidos() {
        PetRequest request = requestBase();
        request.setNome("Rex123");

        assertThat(validator.validate(request))
                .extracting("message")
                .contains("Nome deve conter apenas letras, espaços, ponto ou hífen");
    }

    @Test
    void deveAceitarPetComCamposTextuaisValidos() {
        assertThat(validator.validate(requestBase())).isEmpty();
    }

    private PetRequest requestBase() {
        PetRequest request = new PetRequest();
        request.setNome("Rex");
        request.setEspecie("Cachorro");
        request.setRaca("Vira-lata");
        request.setSexo(SexoPet.M);
        request.setDataNascimento(LocalDate.of(2020, 1, 1));
        request.setStatus(PetStatus.ATIVO);
        request.setTutorId(1L);
        request.setClinicaId(1L);
        return request;
    }
}
