package br.com.fiap.java.FidelisApi.dto.request;

import br.com.fiap.java.FidelisApi.entity.PetStatus;
import br.com.fiap.java.FidelisApi.entity.SexoPet;
import br.com.fiap.java.FidelisApi.validation.DataNascimentoPlausivel;
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
@Schema(description = "Request para criar/atualizar pet")
public class PetRequest {

    @NotBlank(message = "Nome do pet é obrigatório")
    @Size(min = 3, max = 30, message = "Nome deve ter entre 3 e 30 caracteres")
    @Pattern(regexp = "^[\\p{L}][\\p{L} .'-]*$", message = "Nome deve conter apenas letras, espaços, ponto ou hífen")
    private String nome;

    @NotBlank(message = "Espécie é obrigatória")
    @Size(min = 3, max = 20, message = "Espécie deve ter entre 3 e 20 caracteres")
    @Pattern(regexp = "^[\\p{L}][\\p{L} .'-]*$", message = "Espécie deve conter apenas letras, espaços, ponto ou hífen")
    private String especie;

    @NotBlank(message = "Raça é obrigatória")
    @Size(min = 2, max = 20, message = "Raça deve ter entre 2 e 20 caracteres")
    @Pattern(regexp = "^[\\p{L}0-9][\\p{L}0-9 .'/()-]*$", message = "Raça contém caracteres inválidos")
    private String raca;

    @NotNull(message = "Sexo é obrigatório")
    private SexoPet sexo;

    @NotNull(message = "Data de nascimento é obrigatória")
    @PastOrPresent(message = "Data de nascimento não pode ser futura")
    @DataNascimentoPlausivel
    private LocalDate dataNascimento;

    @NotNull(message = "Status do pet é obrigatório")
    private PetStatus status;

    @Size(max = 255)
    private String fotoUrl;

    @NotNull(message = "ID do tutor é obrigatório")
    private Long tutorId;

    @NotNull(message = "ID da clínica é obrigatório")
    private Long clinicaId;
}
