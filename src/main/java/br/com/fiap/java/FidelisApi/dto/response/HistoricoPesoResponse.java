package br.com.fiap.java.FidelisApi.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@Builder
public class HistoricoPesoResponse {
    private Long id;
    private BigDecimal pesoKg;
    private LocalDate dataMedicao;
    private String observacao;
    private Long petId;
}
