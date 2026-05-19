package br.com.fiap.java.FidelisApi.controller;

import br.com.fiap.java.FidelisApi.dto.request.VermifugacaoRequest;
import br.com.fiap.java.FidelisApi.dto.response.VermifugacaoResponse;
import br.com.fiap.java.FidelisApi.entity.Vermifugacao;
import br.com.fiap.java.FidelisApi.service.VermifugacaoService;
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
@RequestMapping("/api/vermifugacoes")
@RequiredArgsConstructor
public class VermifugacaoController {

    private final VermifugacaoService vermifugacaoService;

    @GetMapping
    public ResponseEntity<Page<VermifugacaoResponse>> listar(
            @RequestParam(required = false) String produto,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sort,
            @RequestParam(defaultValue = "ASC") String direction
    ) {
        Pageable pageable = PageRequest.of(page, size, Sort.Direction.fromString(direction), sort);
        Page<VermifugacaoResponse> response = vermifugacaoService.findAll(produto, pageable)
                .map(this::toResponse);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<VermifugacaoResponse> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(toResponse(vermifugacaoService.findById(id)));
    }

    @PostMapping
    public ResponseEntity<VermifugacaoResponse> criar(@Validated @RequestBody VermifugacaoRequest request) {
        Vermifugacao saved = vermifugacaoService.create(toEntity(request), request.getPetId(), request.getVeterinarioId());
        return ResponseEntity.created(URI.create("/api/vermifugacoes/" + saved.getId())).body(toResponse(saved));
    }

    @PutMapping("/{id}")
    public ResponseEntity<VermifugacaoResponse> atualizar(@PathVariable Long id,
                                                           @Validated @RequestBody VermifugacaoRequest request) {
        Vermifugacao updated = vermifugacaoService.update(id, toEntity(request));
        return ResponseEntity.ok(toResponse(updated));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        vermifugacaoService.delete(id);
        return ResponseEntity.noContent().build();
    }

    private Vermifugacao toEntity(VermifugacaoRequest request) {
        return Vermifugacao.builder()
                .produto(request.getProduto())
                .dataAplicacao(request.getDataAplicacao())
                .dataProxima(request.getDataProxima())
                .build();
    }

    private VermifugacaoResponse toResponse(Vermifugacao entity) {
        return VermifugacaoResponse.builder()
                .id(entity.getId())
                .produto(entity.getProduto())
                .dataAplicacao(entity.getDataAplicacao())
                .dataProxima(entity.getDataProxima())
                .petId(entity.getPet().getId())
                .veterinarioId(entity.getVeterinario().getId())
                .build();
    }
}
