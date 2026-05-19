package br.com.fiap.java.FidelisApi.controller;

import br.com.fiap.java.FidelisApi.dto.request.ComportamentoRequest;
import br.com.fiap.java.FidelisApi.dto.response.ComportamentoResponse;
import br.com.fiap.java.FidelisApi.entity.Comportamento;
import br.com.fiap.java.FidelisApi.service.ComportamentoService;
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
@RequestMapping("/api/comportamentos")
@RequiredArgsConstructor
public class ComportamentoController {

    private final ComportamentoService comportamentoService;

    @GetMapping
    public ResponseEntity<Page<ComportamentoResponse>> listar(
            @RequestParam(required = false) String descricao,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sort,
            @RequestParam(defaultValue = "ASC") String direction
    ) {
        Pageable pageable = PageRequest.of(page, size, Sort.Direction.fromString(direction), sort);
        Page<ComportamentoResponse> response = comportamentoService.findAll(descricao, pageable)
                .map(this::toResponse);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ComportamentoResponse> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(toResponse(comportamentoService.findById(id)));
    }

    @PostMapping
    public ResponseEntity<ComportamentoResponse> criar(@Validated @RequestBody ComportamentoRequest request) {
        Comportamento saved = comportamentoService.create(toEntity(request), request.getPetId());
        return ResponseEntity.created(URI.create("/api/comportamentos/" + saved.getId())).body(toResponse(saved));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ComportamentoResponse> atualizar(@PathVariable Long id,
                                                           @Validated @RequestBody ComportamentoRequest request) {
        Comportamento updated = comportamentoService.update(id, toEntity(request));
        return ResponseEntity.ok(toResponse(updated));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        comportamentoService.delete(id);
        return ResponseEntity.noContent().build();
    }

    private Comportamento toEntity(ComportamentoRequest request) {
        return Comportamento.builder()
                .data(request.getData())
                .descricao(request.getDescricao())
                .build();
    }

    private ComportamentoResponse toResponse(Comportamento entity) {
        return ComportamentoResponse.builder()
                .id(entity.getId())
                .data(entity.getData())
                .descricao(entity.getDescricao())
                .petId(entity.getPet().getId())
                .build();
    }
}
