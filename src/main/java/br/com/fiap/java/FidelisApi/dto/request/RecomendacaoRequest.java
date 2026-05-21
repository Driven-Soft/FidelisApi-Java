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
@Schema(description = "Request para criar/atualizar recomendação")
public class RecomendacaoRequest {

    @NotBlank(message = "Tipo de recomendação é obrigatório")
    @Size(max = 50)
    private String tipo;

    @NotBlank(message = "Descrição da recomendação é obrigatória")
    private String descricao;

    @NotNull(message = "Data da recomendação é obrigatória")
    private LocalDate dataRecomendacao;

    @NotNull(message = "ID do pet é obrigatório")
    private Long petId;
}
