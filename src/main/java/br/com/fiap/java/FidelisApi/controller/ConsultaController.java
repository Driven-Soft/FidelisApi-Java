package br.com.fiap.java.FidelisApi.controller;

import br.com.fiap.java.FidelisApi.dto.request.ConsultaRequest;
import br.com.fiap.java.FidelisApi.dto.response.ConsultaResponse;
import br.com.fiap.java.FidelisApi.entity.Consulta;
import br.com.fiap.java.FidelisApi.mapper.ConsultaMapper;
import br.com.fiap.java.FidelisApi.service.ConsultaService;
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
import java.time.LocalDateTime;

@Tag(name = "Consulta")
@RestController
@RequestMapping("/api/v1/consultas")
@RequiredArgsConstructor
public class ConsultaController {

    private final ConsultaService consultaService;

    @GetMapping
    @Operation(summary = "Listar consultas", description = "Retorna uma página de consultas com filtros opcionais")
    public ResponseEntity<Page<ConsultaResponse>> listar(
            @RequestParam(required = false) String tipo,
            @RequestParam(required = false) LocalDateTime inicio,
            @RequestParam(required = false) LocalDateTime fim,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sort,
            @RequestParam(defaultValue = "ASC") String direction
    ) {
        Pageable pageable = PageRequest.of(page, size, Sort.Direction.fromString(direction), sort);
        Page<ConsultaResponse> response = consultaService.findAll(tipo, inicio, fim, pageable)
                .map(ConsultaMapper::toResponse);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar consulta por ID", description = "Retorna os dados de uma consulta pelo seu ID")
    public ResponseEntity<ConsultaResponse> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(ConsultaMapper.toResponse(consultaService.findById(id)));
    }

    @PostMapping
    @Operation(summary = "Criar consulta", description = "Cria uma nova consulta")
    public ResponseEntity<ConsultaResponse> criar(@Validated @RequestBody ConsultaRequest request) {
        Consulta saved = consultaService.create(ConsultaMapper.toEntity(request), request.getVeterinarioId(), request.getPetId());
        return ResponseEntity.created(URI.create("/api/v1/consultas/" + saved.getId())).body(ConsultaMapper.toResponse(saved));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar consulta", description = "Atualiza os dados de uma consulta")
    public ResponseEntity<ConsultaResponse> atualizar(@PathVariable Long id,
                                                     @Validated @RequestBody ConsultaRequest request) {
        Consulta updated = consultaService.update(id, ConsultaMapper.toEntity(request));
        return ResponseEntity.ok(ConsultaMapper.toResponse(updated));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Deletar consulta", description = "Remove uma consulta pelo ID")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        consultaService.delete(id);
        return ResponseEntity.noContent().build();
    }

}

