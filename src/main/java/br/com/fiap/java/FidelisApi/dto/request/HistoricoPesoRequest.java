package br.com.fiap.java.FidelisApi.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import lombok.Getter;
import lombok.Setter;
import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@Schema(description = "Request para criar/atualizar histórico de peso")
public class HistoricoPesoRequest {

    @NotNull(message = "Peso é obrigatório")
    @DecimalMin(value = "0.01", message = "Peso deve ser maior que zero")
    @Digits(integer = 5, fraction = 2, message = "Peso deve ter no máximo 5 dígitos inteiros e 2 decimais")
    private BigDecimal pesoKg;

    @NotNull(message = "Data de medição é obrigatória")
    private LocalDate dataMedicao;

    private String observacao;

    @NotNull(message = "ID do pet é obrigatório")
    private Long petId;
}
