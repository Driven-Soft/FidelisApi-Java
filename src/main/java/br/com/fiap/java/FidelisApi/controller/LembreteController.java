package br.com.fiap.java.FidelisApi.controller;

import br.com.fiap.java.FidelisApi.dto.request.LembreteRequest;
import br.com.fiap.java.FidelisApi.dto.response.LembreteResponse;
import br.com.fiap.java.FidelisApi.entity.Lembrete;
import br.com.fiap.java.FidelisApi.service.LembreteService;
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
@RequestMapping("/api/lembretes")
@RequiredArgsConstructor
public class LembreteController {

    private final LembreteService lembreteService;

    @GetMapping
    public ResponseEntity<Page<LembreteResponse>> listar(
            @RequestParam(required = false) String tipo,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sort,
            @RequestParam(defaultValue = "ASC") String direction
    ) {
        Pageable pageable = PageRequest.of(page, size, Sort.Direction.fromString(direction), sort);
        Page<LembreteResponse> response = lembreteService.findAll(tipo, pageable)
                .map(this::toResponse);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<LembreteResponse> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(toResponse(lembreteService.findById(id)));
    }

    @PostMapping
    public ResponseEntity<LembreteResponse> criar(@Validated @RequestBody LembreteRequest request) {
        Lembrete saved = lembreteService.create(toEntity(request), request.getTutorId(), request.getPetId());
        return ResponseEntity.created(URI.create("/api/lembretes/" + saved.getId())).body(toResponse(saved));
    }

    @PutMapping("/{id}")
    public ResponseEntity<LembreteResponse> atualizar(@PathVariable Long id,
                                                      @Validated @RequestBody LembreteRequest request) {
        Lembrete updated = lembreteService.update(id, toEntity(request));
        return ResponseEntity.ok(toResponse(updated));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        lembreteService.delete(id);
        return ResponseEntity.noContent().build();
    }

    private Lembrete toEntity(LembreteRequest request) {
        return Lembrete.builder()
                .tipo(request.getTipo())
                .descricao(request.getDescricao())
                .dataPrevista(request.getDataPrevista())
                .status(request.getStatus())
                .build();
    }

    private LembreteResponse toResponse(Lembrete entity) {
        return LembreteResponse.builder()
                .id(entity.getId())
                .tipo(entity.getTipo())
                .descricao(entity.getDescricao())
                .dataPrevista(entity.getDataPrevista())
                .status(entity.getStatus())
                .tutorId(entity.getTutor().getId())
                .petId(entity.getPet().getId())
                .build();
    }
}
