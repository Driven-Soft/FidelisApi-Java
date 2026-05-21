package br.com.fiap.java.FidelisApi.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@Schema(description = "Request para criar/atualizar consulta")
public class ConsultaRequest {

    @NotNull(message = "Data e hora da consulta são obrigatórias")
    private LocalDateTime dataHora;

    @NotBlank(message = "Tipo de consulta é obrigatório")
    @Size(max = 50)
    private String tipo;

    private String diagnostico;

    private String observacoes;

    private LocalDate dataRetorno;

    @NotNull(message = "ID do veterinário é obrigatório")
    private Long veterinarioId;

    @NotNull(message = "ID do pet é obrigatório")
    private Long petId;
}
