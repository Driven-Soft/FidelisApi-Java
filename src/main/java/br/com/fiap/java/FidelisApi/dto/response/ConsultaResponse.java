package br.com.fiap.java.FidelisApi.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class ConsultaResponse {
    private Long id;
    private LocalDateTime dataHora;
    private String tipo;
    private String diagnostico;
    private String observacoes;
    private LocalDate dataRetorno;
    private Long veterinarioId;
    private String veterinarioNome;
    private Long petId;
    private String petNome;
}
