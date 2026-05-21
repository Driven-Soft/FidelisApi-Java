package br.com.fiap.java.FidelisApi.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;

@Getter
@Setter
@Schema(description = "Request para criar/atualizar exame")
public class ExameRequest {

    @NotBlank(message = "Tipo de exame é obrigatório")
    @Size(max = 50)
    private String tipo;

    private String descricao;

    private String resultado;

    @NotNull(message = "Data do exame é obrigatória")
    private LocalDate data;

    @NotNull(message = "ID da consulta é obrigatório")
    private Long consultaId;
}
