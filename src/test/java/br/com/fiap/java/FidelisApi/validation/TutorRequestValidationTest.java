package br.com.fiap.java.FidelisApi.validation;

import br.com.fiap.java.FidelisApi.dto.request.TutorRequest;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

class TutorRequestValidationTest {

    private static Validator validator;

    @BeforeAll
    static void setUpValidator() {
        validator = Validation.buildDefaultValidatorFactory().getValidator();
    }

    @Test
    void deveAceitarTutorValido() {
        assertThat(validator.validate(requestBase())).isEmpty();
    }

    @Test
    void deveRejeitarNomeMuitoCurto() {
        TutorRequest request = requestBase();
        request.setNome("AB");

        assertThat(validator.validate(request))
                .extracting("message")
                .contains("Nome deve ter entre 3 e 75 caracteres");
    }

    @Test
    void deveRejeitarTelefoneMuitoCurto() {
        TutorRequest request = requestBase();
        request.setTelefone("123456789");

        assertThat(validator.validate(request))
                .extracting("message")
                .contains("Telefone deve ter entre 10 e 15 caracteres");
    }

    @Test
    void deveAceitarCpfSemFormatacao() {
        TutorRequest request = requestBase();
        request.setCpf("12345678909");

        assertThat(validator.validate(request)).isEmpty();
    }

    @Test
    void deveAceitarTelefoneSomenteComDigitos() {
        TutorRequest request = requestBase();
        request.setTelefone("11987654321");

        assertThat(validator.validate(request)).isEmpty();
    }

    @Test
    void deveRejeitarEnderecoSemQuatroLetrasOuNumero() {
        TutorRequest request = requestBase();
        request.setEndereco("Rua");

        assertThat(validator.validate(request))
                .extracting("message")
                .contains("Endereço deve conter no mínimo 4 letras e 1 número");
    }

    @Test
    void deveRejeitarNomeSemTresLetras() {
        TutorRequest request = requestBase();
        request.setNome("A1");

        assertThat(validator.validate(request))
                .extracting("message")
                .contains("Nome deve conter no mínimo 3 letras e apenas caracteres válidos");
    }

    @Test
    void deveRejeitarDataDeCriacaoFutura() {
        TutorRequest request = requestBase();
        request.setDataCriacao(LocalDate.now().plusDays(1));

        assertThat(validator.validate(request))
                .extracting("message")
                .contains("Data de criação não pode ser futura");
    }

    private TutorRequest requestBase() {
        TutorRequest request = new TutorRequest();
        request.setCpf("123.456.789-09");
        request.setNome("Henrique Torres");
        request.setEmail("henrique@example.com");
        request.setSenha("Senha123");
        request.setTelefone("(11) 98888-7777");
        request.setEndereco("Rua das Flores, 100");
        request.setDataCriacao(LocalDate.now());
        return request;
    }
}
