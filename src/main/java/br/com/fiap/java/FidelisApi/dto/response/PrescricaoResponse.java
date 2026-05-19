package br.com.fiap.java.FidelisApi.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class PrescricaoResponse {
    private Long id;
    private String dosagem;
    private String frequencia;
    private Integer duracaoDias;
    private String observacao;
    private Long consultaId;
}
