package br.com.fiap.java.FidelisApi.controller;

import br.com.fiap.java.FidelisApi.dto.request.VacinacaoRequest;
import br.com.fiap.java.FidelisApi.dto.response.VacinacaoResponse;
import br.com.fiap.java.FidelisApi.entity.Vacinacao;
import br.com.fiap.java.FidelisApi.service.VacinacaoService;
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
@RequestMapping("/api/vacinacoes")
@RequiredArgsConstructor
public class VacinacaoController {

    private final VacinacaoService vacinacaoService;

    @GetMapping
    public ResponseEntity<Page<VacinacaoResponse>> listar(
            @RequestParam(required = false) String vacinaAplicada,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sort,
            @RequestParam(defaultValue = "ASC") String direction
    ) {
        Pageable pageable = PageRequest.of(page, size, Sort.Direction.fromString(direction), sort);
        Page<VacinacaoResponse> response = vacinacaoService.findAll(vacinaAplicada, pageable)
                .map(this::toResponse);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<VacinacaoResponse> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(toResponse(vacinacaoService.findById(id)));
    }

    @PostMapping
    public ResponseEntity<VacinacaoResponse> criar(@Validated @RequestBody VacinacaoRequest request) {
        Vacinacao saved = vacinacaoService.create(toEntity(request), request.getPetId(), request.getVeterinarioId());
        return ResponseEntity.created(URI.create("/api/vacinacoes/" + saved.getId())).body(toResponse(saved));
    }

    @PutMapping("/{id}")
    public ResponseEntity<VacinacaoResponse> atualizar(@PathVariable Long id,
                                                       @Validated @RequestBody VacinacaoRequest request) {
        Vacinacao updated = vacinacaoService.update(id, toEntity(request));
        return ResponseEntity.ok(toResponse(updated));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        vacinacaoService.delete(id);
        return ResponseEntity.noContent().build();
    }

    private Vacinacao toEntity(VacinacaoRequest request) {
        return Vacinacao.builder()
                .dataAplicacao(request.getDataAplicacao())
                .dataProxima(request.getDataProxima())
                .vacinaAplicada(request.getVacinaAplicada())
                .observacao(request.getObservacao())
                .build();
    }

    private VacinacaoResponse toResponse(Vacinacao entity) {
        return VacinacaoResponse.builder()
                .id(entity.getId())
                .dataAplicacao(entity.getDataAplicacao())
                .dataProxima(entity.getDataProxima())
                .vacinaAplicada(entity.getVacinaAplicada())
                .observacao(entity.getObservacao())
                .petId(entity.getPet().getId())
                .veterinarioId(entity.getVeterinario().getId())
                .build();
    }
}
