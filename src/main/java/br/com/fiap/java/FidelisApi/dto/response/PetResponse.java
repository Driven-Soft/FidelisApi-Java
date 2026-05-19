package br.com.fiap.java.FidelisApi.dto.response;

import br.com.fiap.java.FidelisApi.entity.SexoPet;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@Builder
public class PetResponse {
    private Long id;
    private String nome;
    private String especie;
    private String raca;
    private SexoPet sexo;
    private LocalDate dataNascimento;
    private String status;
    private String fotoUrl;
    private Long tutorId;
    private String tutorNome;
    private Long clinicaId;
    private String clinicaNome;
}
