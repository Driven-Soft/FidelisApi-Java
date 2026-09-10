package br.com.fiap.java.FidelisApi.dto.request;

import br.com.fiap.java.FidelisApi.validation.DatasConsultaValidas;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@DatasConsultaValidas
@Schema(description = "Request para criar/atualizar consulta")
public class ConsultaRequest {

    @NotNull(message = "Data e hora da consulta são obrigatórias")
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private LocalDateTime dataHora;

    @NotBlank(message = "Tipo de consulta é obrigatório")
    @Size(min = 3, max = 50, message = "Tipo deve ter entre 3 e 50 caracteres")
    @Pattern(regexp = "^[\\p{L}0-9][\\p{L}0-9 .'/()\\-]*$", message = "Tipo de consulta contém caracteres inválidos")
    private String tipo;

    @Size(max = 500, message = "Diagnóstico deve ter no máximo 500 caracteres")
    private String diagnostico;

    @Size(max = 500, message = "Observações devem ter no máximo 500 caracteres")
    private String observacoes;

    @FutureOrPresent(message = "Data de retorno deve ser hoje ou futura")
    private LocalDate dataRetorno;

    @NotNull(message = "ID do veterinário é obrigatório")
    private Long veterinarioId;

    @NotNull(message = "ID do pet é obrigatório")
    private Long petId;
}