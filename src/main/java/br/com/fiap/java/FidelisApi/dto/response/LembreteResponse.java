package br.com.fiap.java.FidelisApi.dto.response;

import br.com.fiap.java.FidelisApi.entity.LembreteStatus;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;

@Getter
@Setter
@Builder
@Schema(description = "Resposta com dados do lembrete")
public class LembreteResponse {
    private Long id;
    private String tipo;
    private String descricao;
    private LocalDate dataPrevista;
    private LembreteStatus status;
    private Long tutorId;
    private Long petId;
}
