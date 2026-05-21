package br.com.fiap.java.FidelisApi.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;

@Getter
@Setter
@Builder
@Schema(description = "Resposta com dados de comportamento")
public class ComportamentoResponse {
    private Long id;
    private LocalDate data;
    private String descricao;
    private Long petId;
}
