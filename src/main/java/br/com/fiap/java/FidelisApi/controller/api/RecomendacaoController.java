package br.com.fiap.java.FidelisApi.controller.api;

import br.com.fiap.java.FidelisApi.dto.request.RecomendacaoRequest;
import br.com.fiap.java.FidelisApi.dto.response.RecomendacaoResponse;
import br.com.fiap.java.FidelisApi.entity.Recomendacao;
import br.com.fiap.java.FidelisApi.mapper.RecomendacaoMapper;
import br.com.fiap.java.FidelisApi.service.RecomendacaoService;
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

@Tag(name = "Recomendacao")
@RestController
@RequestMapping("/api/v1/recomendacoes")
@RequiredArgsConstructor
public class RecomendacaoController {

    private final RecomendacaoService recomendacaoService;

    @GetMapping
    @Operation(summary = "Listar recomendações", description = "Retorna uma página de recomendações")
    public ResponseEntity<Page<RecomendacaoResponse>> listar(
            @RequestParam(required = false) String tipo,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sort,
            @RequestParam(defaultValue = "ASC") String direction
    ) {
        Pageable pageable = PageableUtils.build(page, size, sort, direction);
        Page<RecomendacaoResponse> response = recomendacaoService.findAll(tipo, pageable)
                .map(RecomendacaoMapper::toResponse);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar recomendação por ID", description = "Retorna os dados de uma recomendação pelo seu ID")
    public ResponseEntity<RecomendacaoResponse> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(RecomendacaoMapper.toResponse(recomendacaoService.findById(id)));
    }

    @PostMapping
    @Operation(summary = "Criar recomendação", description = "Cria uma nova recomendação")
    public ResponseEntity<RecomendacaoResponse> criar(@Validated @RequestBody RecomendacaoRequest request) {
        Recomendacao saved = recomendacaoService.create(RecomendacaoMapper.toEntity(request), request.getPetId());
        return ResponseEntity.created(URI.create("/api/v1/recomendacoes/" + saved.getId())).body(RecomendacaoMapper.toResponse(saved));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar recomendação", description = "Atualiza os dados de uma recomendação")
    public ResponseEntity<RecomendacaoResponse> atualizar(@PathVariable Long id,
                                                          @Validated @RequestBody RecomendacaoRequest request) {
        Recomendacao updated = recomendacaoService.update(id, RecomendacaoMapper.toEntity(request));
        return ResponseEntity.ok(RecomendacaoMapper.toResponse(updated));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Deletar recomendação", description = "Remove uma recomendação pelo ID")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        recomendacaoService.delete(id);
        return ResponseEntity.noContent().build();
    }
}

