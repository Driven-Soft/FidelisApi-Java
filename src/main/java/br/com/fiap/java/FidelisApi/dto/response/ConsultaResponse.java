package br.com.fiap.java.FidelisApi.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@Schema(description = "Resposta com dados da consulta")
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
