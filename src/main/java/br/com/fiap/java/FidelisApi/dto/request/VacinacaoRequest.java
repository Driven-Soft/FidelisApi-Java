package br.com.fiap.java.FidelisApi.dto.request;

import br.com.fiap.java.FidelisApi.validation.DatasVacinacaoValidas;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;

@Getter
@Setter
@DatasVacinacaoValidas
@Schema(description = "Request para criar/atualizar vacinação")
public class VacinacaoRequest {

    @NotNull(message = "Data de aplicação é obrigatória")
    @PastOrPresent(message = "Data de aplicação não pode ser futura")
    private LocalDate dataAplicacao;

    private LocalDate dataProxima;

    @NotBlank(message = "Vacina aplicada é obrigatória")
    @Size(min = 3, max = 50, message = "Nome da vacina deve ter entre 3 e 50 caracteres")
    @Pattern(regexp = "^[\\p{L}0-9][\\p{L}0-9 .'/()\\-]*$", message = "Nome da vacina contém caracteres inválidos")
    private String vacinaAplicada;

    @Size(max = 500, message = "Observação deve ter no máximo 500 caracteres")
    private String observacao;

    @NotNull(message = "ID do pet é obrigatório")
    private Long petId;

    @NotNull(message = "ID do veterinário é obrigatório")
    private Long veterinarioId;
}
