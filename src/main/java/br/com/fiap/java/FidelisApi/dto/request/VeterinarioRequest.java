package br.com.fiap.java.FidelisApi.dto.request;

import jakarta.validation.constraints.Email;
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
@Schema(description = "Request para criar/atualizar veterinário")
public class VeterinarioRequest {

    @NotBlank(message = "CMVV é obrigatório")
    @Size(min = 5, max = 13, message = "CMVV deve ter entre 5 e 13 caracteres")
    @Pattern(regexp = "^[\\p{L}0-9][\\p{L}0-9 ./-]*$", message = "CMVV contém caracteres inválidos")
    private String cmvv;

    @NotBlank(message = "Nome do veterinário é obrigatório")
    @Size(min = 3, max = 75, message = "Nome deve ter entre 3 e 75 caracteres")
    @Pattern(regexp = "^[\\p{L}][\\p{L} .'-]*$", message = "Nome contém caracteres inválidos")
    private String nome;

    @NotBlank(message = "Email é obrigatório")
    @Email(message = "Email inválido")
    @Size(max = 75, message = "Email deve ter no máximo 75 caracteres")
    private String email;

    @NotBlank(message = "Senha é obrigatória")
    @Size(min = 8, max = 100)
    private String senha;

    @NotBlank(message = "Especialidade é obrigatória")
    @Size(min = 3, max = 50, message = "Especialidade deve ter entre 3 e 50 caracteres")
    @Pattern(regexp = "^[\\p{L}][\\p{L} .'/()\\-]*$", message = "Especialidade contém caracteres inválidos")
    private String especialidade;

    @NotNull(message = "Data de criação é obrigatória")
    @PastOrPresent(message = "Data de criação não pode ser futura")
    private LocalDate dataCriacao;

    @NotNull(message = "ID da clínica é obrigatório")
    private Long clinicaId;
}
