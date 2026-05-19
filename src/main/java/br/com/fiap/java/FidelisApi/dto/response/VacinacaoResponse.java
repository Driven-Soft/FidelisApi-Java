package br.com.fiap.java.FidelisApi.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@Builder
public class VacinacaoResponse {
    private Long id;
    private LocalDate dataAplicacao;
    private LocalDate dataProxima;
    private String vacinaAplicada;
    private String observacao;
    private Long petId;
    private Long veterinarioId;
}
