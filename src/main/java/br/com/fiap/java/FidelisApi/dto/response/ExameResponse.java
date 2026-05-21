package br.com.fiap.java.FidelisApi.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;

@Getter
@Setter
@Builder
@Schema(description = "Resposta com dados do exame")
public class ExameResponse {
    private Long id;
    private String tipo;
    private String descricao;
    private String resultado;
    private LocalDate data;
    private Long consultaId;
}
