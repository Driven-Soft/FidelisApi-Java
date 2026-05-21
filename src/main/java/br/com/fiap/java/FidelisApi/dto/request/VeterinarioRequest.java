package br.com.fiap.java.FidelisApi.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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
    @Size(max = 13)
    private String cmvv;

    @NotBlank(message = "Nome do veterinário é obrigatório")
    @Size(max = 75)
    private String nome;

    @NotBlank(message = "Email é obrigatório")
    @Email(message = "Email inválido")
    private String email;

    @NotBlank(message = "Senha é obrigatória")
    @Size(min = 8, max = 100)
    private String senha;

    @NotBlank(message = "Especialidade é obrigatória")
    @Size(max = 50)
    private String especialidade;

    @NotNull(message = "Data de criação é obrigatória")
    private LocalDate dataCriacao;

    @NotNull(message = "ID da clínica é obrigatório")
    private Long clinicaId;
}
