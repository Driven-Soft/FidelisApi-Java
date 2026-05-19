package br.com.fiap.java.FidelisApi.dto.response;

import br.com.fiap.java.FidelisApi.entity.LembreteStatus;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@Builder
public class LembreteResponse {
    private Long id;
    private String tipo;
    private String descricao;
    private LocalDate dataPrevista;
    private LembreteStatus status;
    private Long tutorId;
    private Long petId;
}
