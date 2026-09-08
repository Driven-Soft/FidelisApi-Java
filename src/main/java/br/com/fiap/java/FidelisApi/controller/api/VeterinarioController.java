package br.com.fiap.java.FidelisApi.controller.api;

import br.com.fiap.java.FidelisApi.dto.request.VeterinarioRequest;
import br.com.fiap.java.FidelisApi.dto.response.VeterinarioResponse;
import br.com.fiap.java.FidelisApi.entity.Veterinario;
import br.com.fiap.java.FidelisApi.mapper.VeterinarioMapper;
import br.com.fiap.java.FidelisApi.service.VeterinarioService;
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

@Tag(name = "Veterinario")
@RestController
@RequestMapping("/api/v1/veterinarios")
@RequiredArgsConstructor
public class VeterinarioController {

    private final VeterinarioService veterinarioService;

    @GetMapping
    @Operation(summary = "Listar veterinários", description = "Retorna uma página de veterinários")
    public ResponseEntity<Page<VeterinarioResponse>> listar(
            @RequestParam(required = false) String nome,
            @RequestParam(required = false) String especialidade,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sort,
            @RequestParam(defaultValue = "ASC") String direction
    ) {
        Pageable pageable = PageableUtils.build(page, size, sort, direction);
        Page<VeterinarioResponse> response = veterinarioService.findAll(nome, especialidade, pageable)
                .map(VeterinarioMapper::toResponse);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar veterinário por ID", description = "Retorna os dados de um veterinário pelo seu ID")
    public ResponseEntity<VeterinarioResponse> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(VeterinarioMapper.toResponse(veterinarioService.findById(id)));
    }

    @PostMapping
    @Operation(summary = "Criar veterinário", description = "Cria um novo veterinário")
    public ResponseEntity<VeterinarioResponse> criar(@Validated @RequestBody VeterinarioRequest request) {
        Veterinario saved = veterinarioService.create(VeterinarioMapper.toEntity(request), request.getClinicaId());
        return ResponseEntity.created(URI.create("/api/v1/veterinarios/" + saved.getId())).body(VeterinarioMapper.toResponse(saved));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar veterinário", description = "Atualiza os dados de um veterinário")
    public ResponseEntity<VeterinarioResponse> atualizar(@PathVariable Long id,
                                                         @Validated @RequestBody VeterinarioRequest request) {
        Veterinario updated = veterinarioService.update(id, VeterinarioMapper.toEntity(request));
        return ResponseEntity.ok(VeterinarioMapper.toResponse(updated));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Deletar veterinário", description = "Remove um veterinário pelo ID")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        veterinarioService.delete(id);
        return ResponseEntity.noContent().build();
    }

}

