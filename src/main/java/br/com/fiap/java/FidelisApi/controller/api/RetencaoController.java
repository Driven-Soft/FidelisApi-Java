package br.com.fiap.java.FidelisApi.controller.api;

import br.com.fiap.java.FidelisApi.dto.response.PetRiscoResponse;
import br.com.fiap.java.FidelisApi.service.RetencaoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Retenção")
@RestController
@RequestMapping("/api/v1/clinicas/{clinicaId}/retencao")
@RequiredArgsConstructor
public class RetencaoController {

    private final RetencaoService retencaoService;

    @GetMapping
    @Operation(summary = "Listar pets em risco de retenção",
            description = "Pets sem consulta há mais de 90 dias, ou que nunca tiveram consulta")
    public ResponseEntity<List<PetRiscoResponse>> listar(@PathVariable Long clinicaId) {
        List<PetRiscoResponse> resposta = retencaoService.listarPetsEmRisco(clinicaId).stream()
                .map(item -> PetRiscoResponse.builder()
                        .petId(item.pet().getId())
                        .petNome(item.pet().getNome())
                        .tutorNome(item.pet().getTutor().getNome())
                        .ultimaConsulta(item.ultimaConsulta())
                        .diasSemConsulta(item.diasSemConsulta())
                        .build())
                .toList();
        return ResponseEntity.ok(resposta);
    }
}