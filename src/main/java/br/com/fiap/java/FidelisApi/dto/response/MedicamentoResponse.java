package br.com.fiap.java.FidelisApi.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class MedicamentoResponse {
    private Long id;
    private String nome;
    private String descricao;
    private Long prescricaoId;
}
