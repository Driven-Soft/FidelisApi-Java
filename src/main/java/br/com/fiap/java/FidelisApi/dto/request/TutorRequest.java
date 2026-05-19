package br.com.fiap.java.FidelisApi.dto.request;

import br.com.fiap.java.FidelisApi.validation.Cpf;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class TutorRequest {

    @NotBlank(message = "CPF é obrigatório")
    @Cpf
    private String cpf;

    @NotBlank(message = "Nome do tutor é obrigatório")
    @Size(max = 75)
    private String nome;

    @NotBlank(message = "Email é obrigatório")
    @Email(message = "Email inválido")
    private String email;

    @NotBlank(message = "Senha é obrigatória")
    @Size(min = 8, max = 100)
    private String senha;

    @NotBlank(message = "Telefone é obrigatório")
    @Size(max = 15)
    private String telefone;

    @NotBlank(message = "Endereço é obrigatório")
    @Size(max = 255)
    private String endereco;

    @NotNull(message = "Data de criação é obrigatória")
    private LocalDate dataCriacao;
}
