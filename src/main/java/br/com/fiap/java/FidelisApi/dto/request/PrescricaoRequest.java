package br.com.fiap.java.FidelisApi.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PrescricaoRequest {

    @NotBlank(message = "Dosagem é obrigatória")
    @Size(max = 10)
    private String dosagem;

    @NotBlank(message = "Frequência é obrigatória")
    @Size(max = 50)
    private String frequencia;

    @NotNull(message = "Duração em dias é obrigatória")
    private Integer duracaoDias;

    private String observacao;

    @NotNull(message = "ID da consulta é obrigatório")
    private Long consultaId;
}
