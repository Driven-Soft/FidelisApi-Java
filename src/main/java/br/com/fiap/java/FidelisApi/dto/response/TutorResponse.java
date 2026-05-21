package br.com.fiap.java.FidelisApi.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;

@Getter
@Setter
@Builder
@Schema(description = "Resposta com dados do tutor")
public class TutorResponse {
    private Long id;
    private String cpf;
    private String nome;
    private String email;
    private String telefone;
    private String endereco;
    private LocalDate dataCriacao;
}
