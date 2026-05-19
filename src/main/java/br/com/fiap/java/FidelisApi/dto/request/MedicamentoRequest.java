package br.com.fiap.java.FidelisApi.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MedicamentoRequest {

    @NotBlank(message = "Nome do medicamento é obrigatório")
    @Size(max = 50)
    private String nome;

    private String descricao;

    @NotNull(message = "ID da prescrição é obrigatório")
    private Long prescricaoId;
}
