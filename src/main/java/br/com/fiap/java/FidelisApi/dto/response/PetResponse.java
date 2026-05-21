package br.com.fiap.java.FidelisApi.dto.response;

import br.com.fiap.java.FidelisApi.entity.PetStatus;
import br.com.fiap.java.FidelisApi.entity.SexoPet;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

    import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDate;

@Getter
@Setter
    @Schema(description = "Resposta com dados do pet")
@Builder
public class PetResponse {
    private Long id;
    private String nome;
    private String especie;
    private String raca;
    private SexoPet sexo;
    private LocalDate dataNascimento;
    private PetStatus status;
    private String fotoUrl;
    private Long tutorId;
    private String tutorNome;
    private Long clinicaId;
    private String clinicaNome;
}
