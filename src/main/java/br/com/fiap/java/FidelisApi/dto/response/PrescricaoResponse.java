package br.com.fiap.java.FidelisApi.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import io.swagger.v3.oas.annotations.media.Schema;

@Getter
@Setter
@Builder
@Schema(description = "Resposta com dados da prescrição")
public class PrescricaoResponse {
    private Long id;
    private String dosagem;
    private String frequencia;
    private Integer duracaoDias;
    private String observacao;
    private Long consultaId;
}
