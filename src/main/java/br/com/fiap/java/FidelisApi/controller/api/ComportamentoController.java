package br.com.fiap.java.FidelisApi.controller.api;

import br.com.fiap.java.FidelisApi.dto.request.ComportamentoRequest;
import br.com.fiap.java.FidelisApi.dto.response.ComportamentoResponse;
import br.com.fiap.java.FidelisApi.entity.Comportamento;
import br.com.fiap.java.FidelisApi.mapper.ComportamentoMapper;
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
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;

@Tag(name = "Comportamento")
@RestController
@RequestMapping("/api/v1/comportamentos")
@RequiredArgsConstructor
public class ComportamentoController {

    private final ComportamentoService comportamentoService;

    @GetMapping
    @Operation(summary = "Listar comportamentos", description = "Retorna uma página de comportamentos")
    public ResponseEntity<Page<ComportamentoResponse>> listar(
            @RequestParam(required = false) String descricao,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sort,
            @RequestParam(defaultValue = "ASC") String direction
    ) {
        Pageable pageable = PageRequest.of(page, size, Sort.Direction.fromString(direction), sort);
        Page<ComportamentoResponse> response = comportamentoService.findAll(descricao, pageable)
                .map(ComportamentoMapper::toResponse);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar comportamento por ID", description = "Retorna os dados de um comportamento pelo seu ID")
    public ResponseEntity<ComportamentoResponse> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(ComportamentoMapper.toResponse(comportamentoService.findById(id)));
    }

    @PostMapping
    @Operation(summary = "Criar comportamento", description = "Cria um novo registro de comportamento")
    public ResponseEntity<ComportamentoResponse> criar(@Validated @RequestBody ComportamentoRequest request) {
        Comportamento saved = comportamentoService.create(ComportamentoMapper.toEntity(request), request.getPetId());
        return ResponseEntity.created(URI.create("/api/v1/comportamentos/" + saved.getId())).body(ComportamentoMapper.toResponse(saved));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar comportamento", description = "Atualiza os dados de um comportamento")
    public ResponseEntity<ComportamentoResponse> atualizar(@PathVariable Long id,
                                                           @Validated @RequestBody ComportamentoRequest request) {
        Comportamento updated = comportamentoService.update(id, ComportamentoMapper.toEntity(request));
        return ResponseEntity.ok(ComportamentoMapper.toResponse(updated));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Deletar comportamento", description = "Remove um comportamento pelo ID")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        comportamentoService.delete(id);
        return ResponseEntity.noContent().build();
    }
}

