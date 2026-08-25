package br.com.fiap.java.FidelisApi.controller.api;

import br.com.fiap.java.FidelisApi.dto.request.PetRequest;
import br.com.fiap.java.FidelisApi.dto.response.PetResponse;
import br.com.fiap.java.FidelisApi.entity.Pet;
import br.com.fiap.java.FidelisApi.mapper.PetMapper;
import br.com.fiap.java.FidelisApi.service.PetService;
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

@Tag(name = "Pet")
@RestController
@RequestMapping("/api/v1/pets")
@RequiredArgsConstructor
public class PetController {

    private final PetService petService;

    @GetMapping
    @Operation(summary = "Listar pets", description = "Retorna uma página de pets com filtros opcionais")
    public ResponseEntity<Page<PetResponse>> listar(
            @RequestParam(required = false) String nome,
            @RequestParam(required = false) String especie,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sort,
            @RequestParam(defaultValue = "ASC") String direction
    ) {
        Pageable pageable = PageableUtils.build(page, size, sort, direction);
        Page<PetResponse> response = petService.findAll(nome, especie, pageable)
                .map(PetMapper::toResponse);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar pet por ID", description = "Retorna os dados de um pet pelo seu ID")
    public ResponseEntity<PetResponse> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(PetMapper.toResponse(petService.findById(id)));
    }

    @PostMapping
    @Operation(summary = "Criar pet", description = "Cria um novo pet")
    public ResponseEntity<PetResponse> criar(@Validated @RequestBody PetRequest request) {
        Pet saved = petService.create(PetMapper.toEntity(request), request.getTutorId(), request.getClinicaId());
        return ResponseEntity.created(URI.create("/api/v1/pets/" + saved.getId())).body(PetMapper.toResponse(saved));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar pet", description = "Atualiza os dados de um pet")
    public ResponseEntity<PetResponse> atualizar(@PathVariable Long id,
                                                 @Validated @RequestBody PetRequest request) {
        Pet updated = petService.update(id, PetMapper.toEntity(request));
        return ResponseEntity.ok(PetMapper.toResponse(updated));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Deletar pet", description = "Remove um pet pelo ID")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        petService.delete(id);
        return ResponseEntity.noContent().build();
    }

}

