package br.com.fiap.java.FidelisApi.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;

@Getter
@Setter
@Builder
@Schema(description = "Resposta com dados do veterinário")
public class VeterinarioResponse {
    private Long id;
    private String cmvv;
    private String nome;
    private String email;
    private String especialidade;
    private LocalDate dataCriacao;
    private Long clinicaId;
    private String clinicaNome;
}
