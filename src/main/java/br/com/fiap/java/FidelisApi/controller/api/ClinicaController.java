package br.com.fiap.java.FidelisApi.controller.api;

import br.com.fiap.java.FidelisApi.dto.request.ClinicaRequest;
import br.com.fiap.java.FidelisApi.dto.response.ClinicaResponse;
import br.com.fiap.java.FidelisApi.entity.Clinica;
import br.com.fiap.java.FidelisApi.mapper.ClinicaMapper;
import br.com.fiap.java.FidelisApi.service.ClinicaService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import br.com.fiap.java.FidelisApi.common.PageableUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;

@Tag(name = "Clinica")
@RestController
@RequestMapping("/api/v1/clinicas")
@RequiredArgsConstructor
public class ClinicaController {

    private final ClinicaService clinicaService;

    @GetMapping
    @Operation(summary = "Listar clínicas", description = "Retorna uma página de clínicas com filtros opcionais")
    public ResponseEntity<Page<ClinicaResponse>> listar(
            @RequestParam(required = false) String nome,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sort,
            @RequestParam(defaultValue = "ASC") String direction
    ) {
        Pageable pageable = PageableUtils.build(page, size, sort, direction);
        Page<ClinicaResponse> response = clinicaService.findAll(nome, pageable)
                .map(ClinicaMapper::toResponse);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar clínica por ID", description = "Retorna os dados de uma clínica pelo seu ID")
    public ResponseEntity<ClinicaResponse> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(ClinicaMapper.toResponse(clinicaService.findById(id)));
    }

    @PostMapping
    @Operation(summary = "Criar clínica", description = "Cria uma nova clínica com os dados fornecidos")
    public ResponseEntity<ClinicaResponse> criar(@Validated @RequestBody ClinicaRequest request) {
        Clinica saved = clinicaService.create(ClinicaMapper.toEntity(request));
        return ResponseEntity.created(URI.create("/api/v1/clinicas/" + saved.getId())).body(ClinicaMapper.toResponse(saved));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar clínica", description = "Atualiza os dados de uma clínica existente")
    public ResponseEntity<ClinicaResponse> atualizar(@PathVariable Long id,
                                                     @Validated @RequestBody ClinicaRequest request) {
        Clinica updated = clinicaService.update(id, ClinicaMapper.toEntity(request));
        return ResponseEntity.ok(ClinicaMapper.toResponse(updated));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Deletar clínica", description = "Remove uma clínica pelo ID")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        clinicaService.delete(id);
        return ResponseEntity.noContent().build();
    }

}

