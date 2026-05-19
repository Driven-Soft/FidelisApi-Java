package br.com.fiap.java.FidelisApi.controller;

import br.com.fiap.java.FidelisApi.dto.request.HistoricoPesoRequest;
import br.com.fiap.java.FidelisApi.dto.response.HistoricoPesoResponse;
import br.com.fiap.java.FidelisApi.entity.HistoricoPeso;
import br.com.fiap.java.FidelisApi.service.HistoricoPesoService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.net.URI;

@RestController
@RequestMapping("/api/historico-peso")
@RequiredArgsConstructor
public class HistoricoPesoController {

    private final HistoricoPesoService historicoPesoService;

    @GetMapping
    public ResponseEntity<Page<HistoricoPesoResponse>> listar(
            @RequestParam(required = false) BigDecimal minPeso,
            @RequestParam(required = false) BigDecimal maxPeso,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sort,
            @RequestParam(defaultValue = "ASC") String direction
    ) {
        Pageable pageable = PageRequest.of(page, size, Sort.Direction.fromString(direction), sort);
        Page<HistoricoPesoResponse> response = historicoPesoService.findAll(minPeso, maxPeso, pageable)
                .map(this::toResponse);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<HistoricoPesoResponse> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(toResponse(historicoPesoService.findById(id)));
    }

    @PostMapping
    public ResponseEntity<HistoricoPesoResponse> criar(@Validated @RequestBody HistoricoPesoRequest request) {
        HistoricoPeso saved = historicoPesoService.create(toEntity(request), request.getPetId());
        return ResponseEntity.created(URI.create("/api/historico-peso/" + saved.getId())).body(toResponse(saved));
    }

    @PutMapping("/{id}")
    public ResponseEntity<HistoricoPesoResponse> atualizar(@PathVariable Long id,
                                                           @Validated @RequestBody HistoricoPesoRequest request) {
        HistoricoPeso updated = historicoPesoService.update(id, toEntity(request));
        return ResponseEntity.ok(toResponse(updated));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        historicoPesoService.delete(id);
        return ResponseEntity.noContent().build();
    }

    private HistoricoPeso toEntity(HistoricoPesoRequest request) {
        return HistoricoPeso.builder()
                .pesoKg(request.getPesoKg())
                .dataMedicao(request.getDataMedicao())
                .observacao(request.getObservacao())
                .build();
    }

    private HistoricoPesoResponse toResponse(HistoricoPeso entity) {
        return HistoricoPesoResponse.builder()
                .id(entity.getId())
                .pesoKg(entity.getPesoKg())
                .dataMedicao(entity.getDataMedicao())
                .observacao(entity.getObservacao())
                .petId(entity.getPet().getId())
                .build();
    }
}
