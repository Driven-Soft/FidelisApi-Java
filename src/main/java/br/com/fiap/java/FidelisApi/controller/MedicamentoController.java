package br.com.fiap.java.FidelisApi.controller;

import br.com.fiap.java.FidelisApi.dto.request.MedicamentoRequest;
import br.com.fiap.java.FidelisApi.dto.response.MedicamentoResponse;
import br.com.fiap.java.FidelisApi.entity.Medicamento;
import br.com.fiap.java.FidelisApi.service.MedicamentoService;
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
@RequestMapping("/api/medicamentos")
@RequiredArgsConstructor
public class MedicamentoController {

    private final MedicamentoService medicamentoService;

    @GetMapping
    public ResponseEntity<Page<MedicamentoResponse>> listar(
            @RequestParam(required = false) String nome,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sort,
            @RequestParam(defaultValue = "ASC") String direction
    ) {
        Pageable pageable = PageRequest.of(page, size, Sort.Direction.fromString(direction), sort);
        Page<MedicamentoResponse> response = medicamentoService.findAll(nome, pageable)
                .map(this::toResponse);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<MedicamentoResponse> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(toResponse(medicamentoService.findById(id)));
    }

    @PostMapping
    public ResponseEntity<MedicamentoResponse> criar(@Validated @RequestBody MedicamentoRequest request) {
        Medicamento saved = medicamentoService.create(toEntity(request), request.getPrescricaoId());
        return ResponseEntity.created(URI.create("/api/medicamentos/" + saved.getId())).body(toResponse(saved));
    }

    @PutMapping("/{id}")
    public ResponseEntity<MedicamentoResponse> atualizar(@PathVariable Long id,
                                                         @Validated @RequestBody MedicamentoRequest request) {
        Medicamento updated = medicamentoService.update(id, toEntity(request));
        return ResponseEntity.ok(toResponse(updated));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        medicamentoService.delete(id);
        return ResponseEntity.noContent().build();
    }

    private Medicamento toEntity(MedicamentoRequest request) {
        return Medicamento.builder()
                .nome(request.getNome())
                .descricao(request.getDescricao())
                .build();
    }

    private MedicamentoResponse toResponse(Medicamento entity) {
        return MedicamentoResponse.builder()
                .id(entity.getId())
                .nome(entity.getNome())
                .descricao(entity.getDescricao())
                .prescricaoId(entity.getPrescricao().getId())
                .build();
    }
}
