package br.com.fiap.java.FidelisApi.dto.request;

import br.com.fiap.java.FidelisApi.entity.SexoPet;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
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

    @Size(max = 20)
    private String status;

    @Size(max = 255)
    private String fotoUrl;

    @NotNull(message = "ID do tutor é obrigatório")
    private Long tutorId;

    @NotNull(message = "ID da clínica é obrigatório")
    private Long clinicaId;
}
