package br.com.fiap.java.FidelisApi.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;

@Getter
@Setter
@Builder
@Schema(description = "Resposta com dados da vermifugação")
public class VermifugacaoResponse {
    private Long id;
    private String produto;
    private LocalDate dataAplicacao;
    private LocalDate dataProxima;
    private Long petId;
    private Long veterinarioId;
}
