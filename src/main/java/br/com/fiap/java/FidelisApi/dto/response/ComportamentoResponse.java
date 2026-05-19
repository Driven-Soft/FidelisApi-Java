package br.com.fiap.java.FidelisApi.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@Builder
public class ComportamentoResponse {
    private Long id;
    private LocalDate data;
    private String descricao;
    private Long petId;
}
