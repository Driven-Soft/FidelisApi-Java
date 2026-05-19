package br.com.fiap.java.FidelisApi.controller;

import br.com.fiap.java.FidelisApi.dto.request.RecomendacaoRequest;
import br.com.fiap.java.FidelisApi.dto.response.RecomendacaoResponse;
import br.com.fiap.java.FidelisApi.entity.Recomendacao;
import br.com.fiap.java.FidelisApi.service.RecomendacaoService;
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
@RequestMapping("/api/recomendacoes")
@RequiredArgsConstructor
public class RecomendacaoController {

    private final RecomendacaoService recomendacaoService;

    @GetMapping
    public ResponseEntity<Page<RecomendacaoResponse>> listar(
            @RequestParam(required = false) String tipo,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sort,
            @RequestParam(defaultValue = "ASC") String direction
    ) {
        Pageable pageable = PageRequest.of(page, size, Sort.Direction.fromString(direction), sort);
        Page<RecomendacaoResponse> response = recomendacaoService.findAll(tipo, pageable)
                .map(this::toResponse);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<RecomendacaoResponse> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(toResponse(recomendacaoService.findById(id)));
    }

    @PostMapping
    public ResponseEntity<RecomendacaoResponse> criar(@Validated @RequestBody RecomendacaoRequest request) {
        Recomendacao saved = recomendacaoService.create(toEntity(request), request.getPetId());
        return ResponseEntity.created(URI.create("/api/recomendacoes/" + saved.getId())).body(toResponse(saved));
    }

    @PutMapping("/{id}")
    public ResponseEntity<RecomendacaoResponse> atualizar(@PathVariable Long id,
                                                          @Validated @RequestBody RecomendacaoRequest request) {
        Recomendacao updated = recomendacaoService.update(id, toEntity(request));
        return ResponseEntity.ok(toResponse(updated));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        recomendacaoService.delete(id);
        return ResponseEntity.noContent().build();
    }

    private Recomendacao toEntity(RecomendacaoRequest request) {
        return Recomendacao.builder()
                .tipo(request.getTipo())
                .descricao(request.getDescricao())
                .dataRecomendacao(request.getDataRecomendacao())
                .build();
    }

    private RecomendacaoResponse toResponse(Recomendacao entity) {
        return RecomendacaoResponse.builder()
                .id(entity.getId())
                .tipo(entity.getTipo())
                .descricao(entity.getDescricao())
                .dataRecomendacao(entity.getDataRecomendacao())
                .petId(entity.getPet().getId())
                .build();
    }
}
