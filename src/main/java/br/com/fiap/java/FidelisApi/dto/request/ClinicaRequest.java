package br.com.fiap.java.FidelisApi.dto.request;

import br.com.fiap.java.FidelisApi.validation.Cnpj;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import io.swagger.v3.oas.annotations.media.Schema;

@Getter
@Setter
public class ClinicaRequest {

    @NotBlank(message = "Nome da clínica é obrigatório")
    @Size(min = 3, max = 100, message = "Nome da clínica deve ter entre 3 e 100 caracteres")
    @Pattern(regexp = "^[\\p{L}][\\p{L} .'-]*$", message = "Nome da clínica contém caracteres inválidos")
    private String nome;

    @NotBlank(message = "CNPJ é obrigatório")
    @Cnpj
    private String cnpj;

    @NotBlank(message = "Telefone da clínica é obrigatório")
    @Size(min = 10, max = 15, message = "Telefone deve ter entre 10 e 15 caracteres")
    @Pattern(regexp = "^[0-9()+ .-]+$", message = "Telefone contém caracteres inválidos")
    private String telefone;

    @NotBlank(message = "Email da clínica é obrigatório")
    @Email(message = "Email inválido")
    @Schema(description = "Email da clínica")
    private String email;

    @NotBlank(message = "Endereço da clínica é obrigatório")
    @Size(min = 5, max = 255, message = "Endereço deve ter entre 5 e 255 caracteres")
    private String endereco;
}
