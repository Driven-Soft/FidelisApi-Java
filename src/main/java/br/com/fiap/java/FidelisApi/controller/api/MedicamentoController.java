package br.com.fiap.java.FidelisApi.controller.api;

import br.com.fiap.java.FidelisApi.dto.request.MedicamentoRequest;
import br.com.fiap.java.FidelisApi.dto.response.MedicamentoResponse;
import br.com.fiap.java.FidelisApi.entity.Medicamento;
import br.com.fiap.java.FidelisApi.mapper.MedicamentoMapper;
import br.com.fiap.java.FidelisApi.service.MedicamentoService;
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

@Tag(name = "Medicamento")
@RestController
@RequestMapping("/api/v1/medicamentos")
@RequiredArgsConstructor
public class MedicamentoController {

    private final MedicamentoService medicamentoService;

    @GetMapping
    @Operation(summary = "Listar medicamentos", description = "Retorna uma página de medicamentos")
    public ResponseEntity<Page<MedicamentoResponse>> listar(
            @RequestParam(required = false) String nome,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sort,
            @RequestParam(defaultValue = "ASC") String direction
    ) {
            Pageable pageable = PageableUtils.build(page, size, sort, direction);
        Page<MedicamentoResponse> response = medicamentoService.findAll(nome, pageable)
                .map(MedicamentoMapper::toResponse);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar medicamento por ID", description = "Retorna os dados de um medicamento pelo seu ID")
    public ResponseEntity<MedicamentoResponse> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(MedicamentoMapper.toResponse(medicamentoService.findById(id)));
    }

    @PostMapping
    @Operation(summary = "Criar medicamento", description = "Cria um novo medicamento")
    public ResponseEntity<MedicamentoResponse> criar(@Validated @RequestBody MedicamentoRequest request) {
        Medicamento saved = medicamentoService.create(MedicamentoMapper.toEntity(request), request.getPrescricaoId());
        return ResponseEntity.created(URI.create("/api/v1/medicamentos/" + saved.getId())).body(MedicamentoMapper.toResponse(saved));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar medicamento", description = "Atualiza os dados de um medicamento")
    public ResponseEntity<MedicamentoResponse> atualizar(@PathVariable Long id,
                                                         @Validated @RequestBody MedicamentoRequest request) {
        Medicamento updated = medicamentoService.update(id, MedicamentoMapper.toEntity(request));
        return ResponseEntity.ok(MedicamentoMapper.toResponse(updated));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Deletar medicamento", description = "Remove um medicamento pelo ID")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        medicamentoService.delete(id);
        return ResponseEntity.noContent().build();
    }
}

