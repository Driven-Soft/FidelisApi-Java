package br.com.fiap.java.FidelisApi.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import io.swagger.v3.oas.annotations.media.Schema;

@Getter
@Setter
@Builder
@Schema(description = "Resposta com dados do medicamento")
public class MedicamentoResponse {
    private Long id;
    private String nome;
    private String descricao;
    private Long prescricaoId;
}
