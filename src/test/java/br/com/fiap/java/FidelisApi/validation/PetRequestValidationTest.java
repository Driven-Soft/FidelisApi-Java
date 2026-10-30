package br.com.fiap.java.FidelisApi.validation;

import br.com.fiap.java.FidelisApi.dto.request.PetRequest;
import br.com.fiap.java.FidelisApi.entity.PetStatus;
import br.com.fiap.java.FidelisApi.entity.SexoPet;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.math.BigDecimal;

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
    void deveRejeitarDataDeNascimentoDeCachorroComMaisDeTrintaEUmAnos() {
        PetRequest request = requestBase();
        request.setDataNascimento(LocalDate.now().minusYears(31).minusDays(1));

        assertThat(validator.validate(request))
                .extracting("message")
                .contains("Data de nascimento excede a idade máxima para a espécie informada");
    }

    @Test
    void deveAceitarDataDeNascimentoNoLimiteDeIdadeDeCachorro() {
        PetRequest request = requestBase();
        request.setDataNascimento(LocalDate.now().minusYears(31));

        assertThat(validator.validate(request)).isEmpty();
    }

    @Test
    void deveAceitarDataDeNascimentoNoLimiteDeIdadeDeGato() {
        PetRequest request = requestBase();
        request.setEspecie("Gato");
        request.setDataNascimento(LocalDate.now().minusYears(39));

        assertThat(validator.validate(request)).isEmpty();
    }

    @Test
    void deveRejeitarDataDeNascimentoDeGatoComMaisDeTrintaENoveAnos() {
        PetRequest request = requestBase();
        request.setEspecie("Gato");
        request.setDataNascimento(LocalDate.now().minusYears(39).minusDays(1));

        assertThat(validator.validate(request))
                .extracting("message")
                .contains("Data de nascimento excede a idade máxima para a espécie informada");
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
    void deveAceitarEspecieIgnorandoMaiusculas() {
        PetRequest request = requestBase();
        request.setEspecie("gato");

        assertThat(validator.validate(request)).isEmpty();
    }

    @Test
    void deveRejeitarEspecieNaoSuportada() {
        PetRequest request = requestBase();
        request.setEspecie("Coelho");

        assertThat(validator.validate(request))
                .extracting("message")
                .contains("Espécie deve ser Cachorro ou Gato");
    }

    @Test
    void deveRejeitarEspecieEmBranco() {
        PetRequest request = requestBase();
        request.setEspecie(" ");

        assertThat(validator.validate(request))
                .extracting("message")
                .contains("Espécie é obrigatória");
    }

    @Test
    void deveAceitarRacaComAte50Caracteres() {
        PetRequest request = requestBase();
        request.setRaca("A".repeat(50));

        assertThat(validator.validate(request)).isEmpty();
    }

    @Test
    void deveRejeitarRacaComMaisDe50Caracteres() {
        PetRequest request = requestBase();
        request.setRaca("A".repeat(51));

        assertThat(validator.validate(request))
                .extracting("message")
                .contains("Raça deve ter entre 2 e 50 caracteres");
    }

    @Test
    void deveAceitarPetComCamposTextuaisValidos() {
        assertThat(validator.validate(requestBase())).isEmpty();
    }

    @Test
    void devePermitirPesoInicialOpcional() {
        PetRequest request = requestBase();
        request.setPesoKg(null);

        assertThat(validator.validate(request)).isEmpty();
    }

    @Test
    void deveRejeitarPesoInicialIgualAZero() {
        PetRequest request = requestBase();
        request.setPesoKg(BigDecimal.ZERO);

        assertThat(validator.validate(request))
                .extracting("message")
                .contains("Peso deve ser maior que zero");
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