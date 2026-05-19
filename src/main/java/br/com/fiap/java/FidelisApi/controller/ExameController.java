package br.com.fiap.java.FidelisApi.controller;

import br.com.fiap.java.FidelisApi.dto.request.ExameRequest;
import br.com.fiap.java.FidelisApi.dto.response.ExameResponse;
import br.com.fiap.java.FidelisApi.entity.Exame;
import br.com.fiap.java.FidelisApi.service.ExameService;
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
@RequestMapping("/api/exames")
@RequiredArgsConstructor
public class ExameController {

    private final ExameService exameService;

    @GetMapping
    public ResponseEntity<Page<ExameResponse>> listar(
            @RequestParam(required = false) String tipo,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sort,
            @RequestParam(defaultValue = "ASC") String direction
    ) {
        Pageable pageable = PageRequest.of(page, size, Sort.Direction.fromString(direction), sort);
        Page<ExameResponse> response = exameService.findAll(tipo, pageable)
                .map(this::toResponse);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ExameResponse> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(toResponse(exameService.findById(id)));
    }

    @PostMapping
    public ResponseEntity<ExameResponse> criar(@Validated @RequestBody ExameRequest request) {
        Exame saved = exameService.create(toEntity(request), request.getConsultaId());
        return ResponseEntity.created(URI.create("/api/exames/" + saved.getId())).body(toResponse(saved));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ExameResponse> atualizar(@PathVariable Long id,
                                                   @Validated @RequestBody ExameRequest request) {
        Exame updated = exameService.update(id, toEntity(request));
        return ResponseEntity.ok(toResponse(updated));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        exameService.delete(id);
        return ResponseEntity.noContent().build();
    }

    private Exame toEntity(ExameRequest request) {
        return Exame.builder()
                .tipo(request.getTipo())
                .descricao(request.getDescricao())
                .resultado(request.getResultado())
                .data(request.getData())
                .build();
    }

    private ExameResponse toResponse(Exame entity) {
        return ExameResponse.builder()
                .id(entity.getId())
                .tipo(entity.getTipo())
                .descricao(entity.getDescricao())
                .resultado(entity.getResultado())
                .data(entity.getData())
                .consultaId(entity.getConsulta().getId())
                .build();
    }
}
