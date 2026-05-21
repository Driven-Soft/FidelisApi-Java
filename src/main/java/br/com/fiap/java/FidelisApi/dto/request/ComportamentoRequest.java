package br.com.fiap.java.FidelisApi.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;

@Getter
@Setter
@Schema(description = "Request para criar/atualizar comportamento")
public class ComportamentoRequest {

    @NotNull(message = "Data do registro é obrigatória")
    private LocalDate data;

    @NotBlank(message = "Descrição do comportamento é obrigatória")
    private String descricao;

    @NotNull(message = "ID do pet é obrigatório")
    private Long petId;
}
