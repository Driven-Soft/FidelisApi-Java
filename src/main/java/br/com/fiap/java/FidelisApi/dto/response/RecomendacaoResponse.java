package br.com.fiap.java.FidelisApi.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@Builder
public class RecomendacaoResponse {
    private Long id;
    private String tipo;
    private String descricao;
    private LocalDate dataRecomendacao;
    private Long petId;
}
