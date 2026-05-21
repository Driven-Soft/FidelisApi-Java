package br.com.fiap.java.FidelisApi.dto.request;

import br.com.fiap.java.FidelisApi.validation.Cnpj;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import io.swagger.v3.oas.annotations.media.Schema;

@Getter
@Setter
public class ClinicaRequest {

    @NotBlank(message = "Nome da clínica é obrigatório")
    @Size(max = 100)
    private String nome;

    @NotBlank(message = "CNPJ é obrigatório")
    @Cnpj
    private String cnpj;

    @NotBlank(message = "Telefone da clínica é obrigatório")
    @Size(max = 15)
    private String telefone;

        @NotBlank(message = "Email da clínica é obrigatório")
        @Email(message = "Email inválido")
        @Schema(description = "Email da clínica")
        private String email;

    @NotBlank(message = "Endereço da clínica é obrigatório")
    @Size(max = 255)
    private String endereco;
}
