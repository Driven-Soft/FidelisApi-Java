package br.com.fiap.java.FidelisApi.dto.request;

import br.com.fiap.java.FidelisApi.entity.PetStatus;
import br.com.fiap.java.FidelisApi.entity.SexoPet;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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
    @Size(max = 30)
    private String nome;

    @NotBlank(message = "Espécie é obrigatória")
    @Size(max = 20)
    private String especie;

    @NotBlank(message = "Raça é obrigatória")
    @Size(max = 20)
    private String raca;

    @NotNull(message = "Sexo é obrigatório")
    private SexoPet sexo;

    @NotNull(message = "Data de nascimento é obrigatória")
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
