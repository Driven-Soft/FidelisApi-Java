package br.com.fiap.java.FidelisApi.dto.request;

import br.com.fiap.java.FidelisApi.entity.LembreteStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;

@Getter
@Setter
@Schema(description = "Request para criar/atualizar lembrete")
public class LembreteRequest {

    @NotBlank(message = "Tipo de lembrete é obrigatório")
    @Size(max = 50)
    private String tipo;

    @NotBlank(message = "Descrição do lembrete é obrigatória")
    private String descricao;

    @NotNull(message = "Data prevista é obrigatória")
    private LocalDate dataPrevista;

    @NotNull(message = "Status é obrigatório")
    private LembreteStatus status;

    @NotNull(message = "ID do tutor é obrigatório")
    private Long tutorId;

    @NotNull(message = "ID do pet é obrigatório")
    private Long petId;
}
