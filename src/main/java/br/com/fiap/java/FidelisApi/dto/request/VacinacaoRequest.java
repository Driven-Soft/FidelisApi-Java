package br.com.fiap.java.FidelisApi.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;

@Getter
@Setter
@Schema(description = "Request para criar/atualizar vacinação")
public class VacinacaoRequest {

    @NotNull(message = "Data de aplicação é obrigatória")
    private LocalDate dataAplicacao;

    private LocalDate dataProxima;

    @NotBlank(message = "Vacina aplicada é obrigatória")
    @Size(max = 50)
    private String vacinaAplicada;

    private String observacao;

    @NotNull(message = "ID do pet é obrigatório")
    private Long petId;

    @NotNull(message = "ID do veterinário é obrigatório")
    private Long veterinarioId;
}
