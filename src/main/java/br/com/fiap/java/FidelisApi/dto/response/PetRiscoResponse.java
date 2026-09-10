package br.com.fiap.java.FidelisApi.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
@Schema(description = "Pet em risco de retenção/churn")
public class PetRiscoResponse {
    private Long petId;
    private String petNome;
    private String tutorNome;
    private LocalDateTime ultimaConsulta;
    private long diasSemConsulta;
}