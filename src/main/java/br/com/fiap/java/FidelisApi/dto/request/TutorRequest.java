package br.com.fiap.java.FidelisApi.dto.request;

import br.com.fiap.java.FidelisApi.validation.Cpf;
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
@Schema(description = "Request para criar/atualizar tutor")
public class TutorRequest {

    @NotBlank(message = "CPF é obrigatório")
    @Cpf
    private String cpf;

    @NotBlank(message = "Nome do tutor é obrigatório")
    @Size(min = 3, max = 75, message = "Nome deve ter entre 3 e 75 caracteres")
    @Pattern(regexp = "^(?=(?:.*\\p{L}){3,})[\\p{L} .'-]+$",
            message = "Nome deve conter no mínimo 3 letras e apenas caracteres válidos")
    private String nome;

    @NotBlank(message = "Email é obrigatório")
    @Email(message = "Email inválido")
    @Size(max = 75, message = "Email deve ter no máximo 75 caracteres")
    private String email;

    @NotBlank(message = "Senha é obrigatória")
    @Size(min = 8, max = 100, message = "Senha deve ter entre 8 e 100 caracteres")
    private String senha;

    @NotBlank(message = "Telefone é obrigatório")
    @Size(min = 10, max = 15, message = "Telefone deve ter entre 10 e 15 caracteres")
    @Pattern(regexp = "^(?:\\d{10,11}|\\(?\\d{2}\\)?[ .-]?\\d{4,5}[ .-]?\\d{4})$",
            message = "Informe um telefone com DDD e 10 ou 11 dígitos")
    private String telefone;

    @NotBlank(message = "Endereço é obrigatório")
    @Size(min = 5, max = 255, message = "Endereço deve ter entre 5 e 255 caracteres")
    @Pattern(regexp = "^(?=(?:.*\\p{L}){4,})(?=.*\\d)[\\p{L}\\p{N} .,'/#-]+$",
            message = "Endereço deve conter no mínimo 4 letras e 1 número")
    private String endereco;

    @NotNull(message = "Data de criação é obrigatória")
    @PastOrPresent(message = "Data de criação não pode ser futura")
    private LocalDate dataCriacao;
}
