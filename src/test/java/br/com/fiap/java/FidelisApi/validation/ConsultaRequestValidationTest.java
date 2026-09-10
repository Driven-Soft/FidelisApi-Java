package br.com.fiap.java.FidelisApi.validation;

import br.com.fiap.java.FidelisApi.dto.request.ConsultaRequest;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

class ConsultaRequestValidationTest {

    private static Validator validator;

    @BeforeAll
    static void setUpValidator() {
        validator = Validation.buildDefaultValidatorFactory().getValidator();
    }

    @Test
    void deveAceitarConsultaValida() {
        assertThat(validator.validate(requestBase())).isEmpty();
    }

    @Test
    void devePermitirConsultaFutura() {
        ConsultaRequest request = requestBase();
        request.setDataHora(LocalDateTime.now().plusDays(2));

        assertThat(validator.validate(request)).isEmpty();
    }

    @Test
    void deveRejeitarTipoMuitoCurto() {
        ConsultaRequest request = requestBase();
        request.setTipo("RX");

        assertThat(validator.validate(request))
                .extracting("message")
                .contains("Tipo deve ter entre 3 e 50 caracteres");
    }

    @Test
    void deveRejeitarDataDeRetornoAnteriorAConsulta() {
        ConsultaRequest request = requestBase();
        request.setDataRetorno(LocalDate.of(2026, 9, 9));

        assertThat(validator.validate(request))
                .extracting("message")
                .contains("A data de retorno não pode ser anterior à data da consulta");
    }

    @Test
    void deveRejeitarDataDeRetornoNoPassado() {
        ConsultaRequest request = requestBase();
        request.setDataHora(LocalDateTime.now().minusDays(2));
        request.setDataRetorno(LocalDate.now().minusDays(1));

        assertThat(validator.validate(request))
                .extracting("message")
                .contains("Data de retorno deve ser hoje ou futura");
    }

    private ConsultaRequest requestBase() {
        ConsultaRequest request = new ConsultaRequest();
        request.setDataHora(LocalDateTime.of(2026, 9, 10, 10, 0));
        request.setTipo("Checkup");
        request.setDiagnostico("Animal saudável");
        request.setObservacoes("Retorno anual");
        request.setDataRetorno(LocalDate.of(2026, 9, 20));
        request.setPetId(1L);
        request.setVeterinarioId(1L);
        return request;
    }
}
