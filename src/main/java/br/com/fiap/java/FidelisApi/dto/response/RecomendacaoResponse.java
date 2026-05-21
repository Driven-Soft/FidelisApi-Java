package br.com.fiap.java.FidelisApi.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;

@Getter
@Setter
@Builder
@Schema(description = "Resposta com dados da recomendação")
public class RecomendacaoResponse {
    private Long id;
    private String tipo;
    private String descricao;
    private LocalDate dataRecomendacao;
    private Long petId;
}
