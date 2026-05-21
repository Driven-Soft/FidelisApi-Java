package br.com.fiap.java.FidelisApi.controller;

import br.com.fiap.java.FidelisApi.dto.request.HistoricoPesoRequest;
import br.com.fiap.java.FidelisApi.dto.response.HistoricoPesoResponse;
import br.com.fiap.java.FidelisApi.entity.HistoricoPeso;
import br.com.fiap.java.FidelisApi.mapper.HistoricoPesoMapper;
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
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;

@Tag(name = "HistoricoPeso")
@RestController
@RequestMapping("/api/v1/historico-peso")
@RequiredArgsConstructor
public class HistoricoPesoController {

    private final HistoricoPesoService historicoPesoService;

    @GetMapping
    @Operation(summary = "Listar histórico de peso", description = "Retorna uma página do histórico de peso")
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
                .map(HistoricoPesoMapper::toResponse);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar histórico por ID", description = "Retorna um registro do histórico de peso pelo ID")
    public ResponseEntity<HistoricoPesoResponse> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(HistoricoPesoMapper.toResponse(historicoPesoService.findById(id)));
    }

    @PostMapping
    @Operation(summary = "Criar histórico de peso", description = "Cria um novo registro de histórico de peso")
    public ResponseEntity<HistoricoPesoResponse> criar(@Validated @RequestBody HistoricoPesoRequest request) {
        HistoricoPeso saved = historicoPesoService.create(HistoricoPesoMapper.toEntity(request), request.getPetId());
        return ResponseEntity.created(URI.create("/api/v1/historico-peso/" + saved.getId())).body(HistoricoPesoMapper.toResponse(saved));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar histórico de peso", description = "Atualiza um registro de histórico de peso")
    public ResponseEntity<HistoricoPesoResponse> atualizar(@PathVariable Long id,
                                                           @Validated @RequestBody HistoricoPesoRequest request) {
        HistoricoPeso updated = historicoPesoService.update(id, HistoricoPesoMapper.toEntity(request));
        return ResponseEntity.ok(HistoricoPesoMapper.toResponse(updated));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Deletar histórico de peso", description = "Remove um registro do histórico de peso pelo ID")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        historicoPesoService.delete(id);
        return ResponseEntity.noContent().build();
    }
}

