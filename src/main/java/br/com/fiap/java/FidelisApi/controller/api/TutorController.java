package br.com.fiap.java.FidelisApi.controller.api;

import br.com.fiap.java.FidelisApi.dto.request.TutorRequest;
import br.com.fiap.java.FidelisApi.dto.response.TutorResponse;
import br.com.fiap.java.FidelisApi.entity.Tutor;
import br.com.fiap.java.FidelisApi.mapper.TutorMapper;
import br.com.fiap.java.FidelisApi.service.TutorService;
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

@Tag(name = "Tutor")
@RestController
@RequestMapping("/api/v1/tutores")
@RequiredArgsConstructor
public class TutorController {

    private final TutorService tutorService;

    @GetMapping
    @Operation(summary = "Listar tutores", description = "Retorna uma página de tutores com filtros opcionais")
    public ResponseEntity<Page<TutorResponse>> listar(
            @RequestParam(required = false) String nome,
            @RequestParam(required = false) String email,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sort,
            @RequestParam(defaultValue = "ASC") String direction
    ) {
        Pageable pageable = PageableUtils.build(page, size, sort, direction);
        Page<TutorResponse> response = tutorService.findAll(nome, email, pageable)
                .map(TutorMapper::toResponse);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar tutor por ID", description = "Retorna os dados de um tutor pelo seu ID")
    public ResponseEntity<TutorResponse> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(TutorMapper.toResponse(tutorService.findById(id)));
    }

    @PostMapping
    @Operation(summary = "Criar tutor", description = "Cria um novo tutor")
    public ResponseEntity<TutorResponse> criar(@Validated @RequestBody TutorRequest request) {
        Tutor saved = tutorService.create(TutorMapper.toEntity(request));
        return ResponseEntity.created(URI.create("/api/v1/tutores/" + saved.getId())).body(TutorMapper.toResponse(saved));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar tutor", description = "Atualiza os dados de um tutor")
    public ResponseEntity<TutorResponse> atualizar(@PathVariable Long id,
                                                   @Validated @RequestBody TutorRequest request) {
        Tutor updated = tutorService.update(id, TutorMapper.toEntity(request));
        return ResponseEntity.ok(TutorMapper.toResponse(updated));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Deletar tutor", description = "Remove um tutor pelo ID")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        tutorService.delete(id);
        return ResponseEntity.noContent().build();
    }

}

