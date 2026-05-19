package br.com.fiap.java.FidelisApi.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@Builder
public class VermifugacaoResponse {
    private Long id;
    private String produto;
    private LocalDate dataAplicacao;
    private LocalDate dataProxima;
    private Long petId;
    private Long veterinarioId;
}
