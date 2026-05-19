package br.com.fiap.java.FidelisApi.controller;

import br.com.fiap.java.FidelisApi.dto.request.PrescricaoRequest;
import br.com.fiap.java.FidelisApi.dto.response.PrescricaoResponse;
import br.com.fiap.java.FidelisApi.entity.Prescricao;
import br.com.fiap.java.FidelisApi.service.PrescricaoService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/api/prescricoes")
@RequiredArgsConstructor
public class PrescricaoController {

    private final PrescricaoService prescricaoService;

    @GetMapping
    public ResponseEntity<Page<PrescricaoResponse>> listar(
            @RequestParam(required = false) String dosagem,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sort,
            @RequestParam(defaultValue = "ASC") String direction
    ) {
        Pageable pageable = PageRequest.of(page, size, Sort.Direction.fromString(direction), sort);
        Page<PrescricaoResponse> response = prescricaoService.findAll(dosagem, pageable)
                .map(this::toResponse);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PrescricaoResponse> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(toResponse(prescricaoService.findById(id)));
    }

    @PostMapping
    public ResponseEntity<PrescricaoResponse> criar(@Validated @RequestBody PrescricaoRequest request) {
        Prescricao saved = prescricaoService.create(toEntity(request), request.getConsultaId());
        return ResponseEntity.created(URI.create("/api/prescricoes/" + saved.getId())).body(toResponse(saved));
    }

    @PutMapping("/{id}")
    public ResponseEntity<PrescricaoResponse> atualizar(@PathVariable Long id,
                                                        @Validated @RequestBody PrescricaoRequest request) {
        Prescricao updated = prescricaoService.update(id, toEntity(request));
        return ResponseEntity.ok(toResponse(updated));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        prescricaoService.delete(id);
        return ResponseEntity.noContent().build();
    }

    private Prescricao toEntity(PrescricaoRequest request) {
        return Prescricao.builder()
                .dosagem(request.getDosagem())
                .frequencia(request.getFrequencia())
                .duracaoDias(request.getDuracaoDias())
                .observacao(request.getObservacao())
                .build();
    }

    private PrescricaoResponse toResponse(Prescricao entity) {
        return PrescricaoResponse.builder()
                .id(entity.getId())
                .dosagem(entity.getDosagem())
                .frequencia(entity.getFrequencia())
                .duracaoDias(entity.getDuracaoDias())
                .observacao(entity.getObservacao())
                .consultaId(entity.getConsulta().getId())
                .build();
    }
}
