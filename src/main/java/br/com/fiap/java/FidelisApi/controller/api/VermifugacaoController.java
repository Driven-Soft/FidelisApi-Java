package br.com.fiap.java.FidelisApi.controller.api;

import br.com.fiap.java.FidelisApi.dto.request.VermifugacaoRequest;
import br.com.fiap.java.FidelisApi.dto.response.VermifugacaoResponse;
import br.com.fiap.java.FidelisApi.entity.Vermifugacao;
import br.com.fiap.java.FidelisApi.mapper.VermifugacaoMapper;
import br.com.fiap.java.FidelisApi.service.VermifugacaoService;
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

@Tag(name = "Vermifugacao")
@RestController
@RequestMapping("/api/v1/vermifugacoes")
@RequiredArgsConstructor
public class VermifugacaoController {

    private final VermifugacaoService vermifugacaoService;

    @GetMapping
    @Operation(summary = "Listar vermifugações", description = "Retorna uma página de vermifugações")
    public ResponseEntity<Page<VermifugacaoResponse>> listar(
            @RequestParam(required = false) String produto,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sort,
            @RequestParam(defaultValue = "ASC") String direction
    ) {
        Pageable pageable = PageableUtils.build(page, size, sort, direction);
        Page<VermifugacaoResponse> response = vermifugacaoService.findAll(produto, pageable)
                .map(VermifugacaoMapper::toResponse);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar vermifugação por ID", description = "Retorna os dados de uma vermifugação pelo seu ID")
    public ResponseEntity<VermifugacaoResponse> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(VermifugacaoMapper.toResponse(vermifugacaoService.findById(id)));
    }

    @PostMapping
    @Operation(summary = "Criar vermifugação", description = "Cria um novo registro de vermifugação")
    public ResponseEntity<VermifugacaoResponse> criar(@Validated @RequestBody VermifugacaoRequest request) {
        Vermifugacao saved = vermifugacaoService.create(VermifugacaoMapper.toEntity(request), request.getPetId(), request.getVeterinarioId());
        return ResponseEntity.created(URI.create("/api/v1/vermifugacoes/" + saved.getId())).body(VermifugacaoMapper.toResponse(saved));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar vermifugação", description = "Atualiza os dados de uma vermifugação")
    public ResponseEntity<VermifugacaoResponse> atualizar(@PathVariable Long id,
                                                           @Validated @RequestBody VermifugacaoRequest request) {
        Vermifugacao updated = vermifugacaoService.update(id, VermifugacaoMapper.toEntity(request));
        return ResponseEntity.ok(VermifugacaoMapper.toResponse(updated));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Deletar vermifugação", description = "Remove uma vermifugação pelo ID")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        vermifugacaoService.delete(id);
        return ResponseEntity.noContent().build();
    }
}

