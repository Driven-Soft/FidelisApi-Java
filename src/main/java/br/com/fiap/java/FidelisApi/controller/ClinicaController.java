package br.com.fiap.java.FidelisApi.controller;

import br.com.fiap.java.FidelisApi.dto.request.ClinicaRequest;
import br.com.fiap.java.FidelisApi.dto.response.ClinicaResponse;
import br.com.fiap.java.FidelisApi.entity.Clinica;
import br.com.fiap.java.FidelisApi.service.ClinicaService;
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
@RequestMapping("/api/clinicas")
@RequiredArgsConstructor
public class ClinicaController {

    private final ClinicaService clinicaService;

    @GetMapping
    public ResponseEntity<Page<ClinicaResponse>> listar(
            @RequestParam(required = false) String nome,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sort,
            @RequestParam(defaultValue = "ASC") String direction
    ) {
        Pageable pageable = PageRequest.of(page, size, Sort.Direction.fromString(direction), sort);
        Page<ClinicaResponse> response = clinicaService.findAll(nome, pageable)
                .map(this::toResponse);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ClinicaResponse> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(toResponse(clinicaService.findById(id)));
    }

    @PostMapping
    public ResponseEntity<ClinicaResponse> criar(@Validated @RequestBody ClinicaRequest request) {
        Clinica saved = clinicaService.create(toEntity(request));
        return ResponseEntity.created(URI.create("/api/clinicas/" + saved.getId())).body(toResponse(saved));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ClinicaResponse> atualizar(@PathVariable Long id,
                                                     @Validated @RequestBody ClinicaRequest request) {
        Clinica updated = clinicaService.update(id, toEntity(request));
        return ResponseEntity.ok(toResponse(updated));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        clinicaService.delete(id);
        return ResponseEntity.noContent().build();
    }

    private Clinica toEntity(ClinicaRequest request) {
        return Clinica.builder()
                .nome(request.getNome())
                .cnpj(request.getCnpj())
                .telefone(request.getTelefone())
                .email(request.getEmail())
                .endereco(request.getEndereco())
                .build();
    }

    private ClinicaResponse toResponse(Clinica entity) {
        return ClinicaResponse.builder()
                .id(entity.getId())
                .nome(entity.getNome())
                .cnpj(entity.getCnpj())
                .telefone(entity.getTelefone())
                .email(entity.getEmail())
                .endereco(entity.getEndereco())
                .build();
    }
}
