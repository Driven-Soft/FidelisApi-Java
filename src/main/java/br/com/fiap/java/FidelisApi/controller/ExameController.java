package br.com.fiap.java.FidelisApi.controller;

import br.com.fiap.java.FidelisApi.dto.request.ExameRequest;
import br.com.fiap.java.FidelisApi.dto.response.ExameResponse;
import br.com.fiap.java.FidelisApi.entity.Exame;
import br.com.fiap.java.FidelisApi.mapper.ExameMapper;
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
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;

@Tag(name = "Exame")
@RestController
@RequestMapping("/api/v1/exames")
@RequiredArgsConstructor
public class ExameController {

    private final ExameService exameService;

    @GetMapping
    @Operation(summary = "Listar exames", description = "Retorna uma página de exames")
    public ResponseEntity<Page<ExameResponse>> listar(
            @RequestParam(required = false) String tipo,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sort,
            @RequestParam(defaultValue = "ASC") String direction
    ) {
        Pageable pageable = PageRequest.of(page, size, Sort.Direction.fromString(direction), sort);
        Page<ExameResponse> response = exameService.findAll(tipo, pageable)
                .map(ExameMapper::toResponse);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar exame por ID", description = "Retorna os dados de um exame pelo seu ID")
    public ResponseEntity<ExameResponse> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(ExameMapper.toResponse(exameService.findById(id)));
    }

    @PostMapping
    @Operation(summary = "Criar exame", description = "Cria um novo exame")
    public ResponseEntity<ExameResponse> criar(@Validated @RequestBody ExameRequest request) {
        Exame saved = exameService.create(ExameMapper.toEntity(request), request.getConsultaId());
        return ResponseEntity.created(URI.create("/api/v1/exames/" + saved.getId())).body(ExameMapper.toResponse(saved));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar exame", description = "Atualiza os dados de um exame")
    public ResponseEntity<ExameResponse> atualizar(@PathVariable Long id,
                                                   @Validated @RequestBody ExameRequest request) {
        Exame updated = exameService.update(id, ExameMapper.toEntity(request));
        return ResponseEntity.ok(ExameMapper.toResponse(updated));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Deletar exame", description = "Remove um exame pelo ID")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        exameService.delete(id);
        return ResponseEntity.noContent().build();
    }
}

