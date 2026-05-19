package br.com.fiap.java.FidelisApi.controller;

import br.com.fiap.java.FidelisApi.dto.request.VeterinarioRequest;
import br.com.fiap.java.FidelisApi.dto.response.VeterinarioResponse;
import br.com.fiap.java.FidelisApi.entity.Veterinario;
import br.com.fiap.java.FidelisApi.service.VeterinarioService;
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
@RequestMapping("/api/veterinarios")
@RequiredArgsConstructor
public class VeterinarioController {

    private final VeterinarioService veterinarioService;

    @GetMapping
    public ResponseEntity<Page<VeterinarioResponse>> listar(
            @RequestParam(required = false) String nome,
            @RequestParam(required = false) String especialidade,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sort,
            @RequestParam(defaultValue = "ASC") String direction
    ) {
        Pageable pageable = PageRequest.of(page, size, Sort.Direction.fromString(direction), sort);
        Page<VeterinarioResponse> response = veterinarioService.findAll(nome, especialidade, pageable)
                .map(this::toResponse);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<VeterinarioResponse> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(toResponse(veterinarioService.findById(id)));
    }

    @PostMapping
    public ResponseEntity<VeterinarioResponse> criar(@Validated @RequestBody VeterinarioRequest request) {
        Veterinario saved = veterinarioService.create(toEntity(request), request.getClinicaId());
        return ResponseEntity.created(URI.create("/api/veterinarios/" + saved.getId())).body(toResponse(saved));
    }

    @PutMapping("/{id}")
    public ResponseEntity<VeterinarioResponse> atualizar(@PathVariable Long id,
                                                         @Validated @RequestBody VeterinarioRequest request) {
        Veterinario updated = veterinarioService.update(id, toEntity(request));
        return ResponseEntity.ok(toResponse(updated));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        veterinarioService.delete(id);
        return ResponseEntity.noContent().build();
    }

    private Veterinario toEntity(VeterinarioRequest request) {
        return Veterinario.builder()
                .cmvv(request.getCmvv())
                .nome(request.getNome())
                .email(request.getEmail())
                .senha(request.getSenha())
                .especialidade(request.getEspecialidade())
                .dataCriacao(request.getDataCriacao())
                .build();
    }

    private VeterinarioResponse toResponse(Veterinario entity) {
        return VeterinarioResponse.builder()
                .id(entity.getId())
                .cmvv(entity.getCmvv())
                .nome(entity.getNome())
                .email(entity.getEmail())
                .especialidade(entity.getEspecialidade())
                .dataCriacao(entity.getDataCriacao())
                .clinicaId(entity.getClinica().getId())
                .clinicaNome(entity.getClinica().getNome())
                .build();
    }
}
