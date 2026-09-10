package br.com.fiap.java.FidelisApi.validation;

import br.com.fiap.java.FidelisApi.dto.request.VacinacaoRequest;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

class VacinacaoRequestValidationTest {

    private static Validator validator;

    @BeforeAll
    static void setUpValidator() {
        validator = Validation.buildDefaultValidatorFactory().getValidator();
    }

    @Test
    void deveAceitarVacinacaoValida() {
        assertThat(validator.validate(requestBase())).isEmpty();
    }

    @Test
    void deveRejeitarDataDeAplicacaoFutura() {
        VacinacaoRequest request = requestBase();
        request.setDataAplicacao(LocalDate.now().plusDays(1));

        assertThat(validator.validate(request))
                .extracting("message")
                .contains("Data de aplicação não pode ser futura");
    }

    @Test
    void deveRejeitarProximaDoseAnteriorAAplicacao() {
        VacinacaoRequest request = requestBase();
        request.setDataProxima(request.getDataAplicacao().minusDays(1));

        assertThat(validator.validate(request))
                .extracting("message")
                .contains("A próxima dose não pode ser anterior à data de aplicação");
    }

    @Test
    void deveRejeitarNomeDeVacinaMuitoCurto() {
        VacinacaoRequest request = requestBase();
        request.setVacinaAplicada("Ab");

        assertThat(validator.validate(request))
                .extracting("message")
                .contains("Nome da vacina deve ter entre 3 e 50 caracteres");
    }

    private VacinacaoRequest requestBase() {
        VacinacaoRequest request = new VacinacaoRequest();
        request.setDataAplicacao(LocalDate.of(2026, 9, 10));
        request.setDataProxima(LocalDate.of(2027, 9, 10));
        request.setVacinaAplicada("Antirrábica");
        request.setObservacao("Aplicação anual");
        request.setPetId(1L);
        request.setVeterinarioId(1L);
        return request;
    }
}
