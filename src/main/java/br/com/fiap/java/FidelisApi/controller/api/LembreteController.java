package br.com.fiap.java.FidelisApi.controller.api;

import br.com.fiap.java.FidelisApi.dto.request.LembreteRequest;
import br.com.fiap.java.FidelisApi.dto.response.LembreteResponse;
import br.com.fiap.java.FidelisApi.entity.Lembrete;
import br.com.fiap.java.FidelisApi.mapper.LembreteMapper;
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
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;

@Tag(name = "Lembrete")
@RestController
@RequestMapping("/api/v1/lembretes")
@RequiredArgsConstructor
public class LembreteController {

    private final LembreteService lembreteService;

    @GetMapping
    @Operation(summary = "Listar lembretes", description = "Retorna uma página de lembretes")
    public ResponseEntity<Page<LembreteResponse>> listar(
            @RequestParam(required = false) String tipo,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sort,
            @RequestParam(defaultValue = "ASC") String direction
    ) {
        Pageable pageable = PageRequest.of(page, size, Sort.Direction.fromString(direction), sort);
        Page<LembreteResponse> response = lembreteService.findAll(tipo, pageable)
                .map(LembreteMapper::toResponse);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar lembrete por ID", description = "Retorna os dados de um lembrete pelo seu ID")
    public ResponseEntity<LembreteResponse> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(LembreteMapper.toResponse(lembreteService.findById(id)));
    }

    @PostMapping
    @Operation(summary = "Criar lembrete", description = "Cria um novo lembrete")
    public ResponseEntity<LembreteResponse> criar(@Validated @RequestBody LembreteRequest request) {
        Lembrete saved = lembreteService.create(LembreteMapper.toEntity(request), request.getTutorId(), request.getPetId());
        return ResponseEntity.created(URI.create("/api/v1/lembretes/" + saved.getId())).body(LembreteMapper.toResponse(saved));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar lembrete", description = "Atualiza os dados de um lembrete")
    public ResponseEntity<LembreteResponse> atualizar(@PathVariable Long id,
                                                      @Validated @RequestBody LembreteRequest request) {
        Lembrete updated = lembreteService.update(id, LembreteMapper.toEntity(request));
        return ResponseEntity.ok(LembreteMapper.toResponse(updated));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Deletar lembrete", description = "Remove um lembrete pelo ID")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        lembreteService.delete(id);
        return ResponseEntity.noContent().build();
    }

}

