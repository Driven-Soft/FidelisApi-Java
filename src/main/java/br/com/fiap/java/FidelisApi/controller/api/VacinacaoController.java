package br.com.fiap.java.FidelisApi.controller.api;

import br.com.fiap.java.FidelisApi.dto.request.VacinacaoRequest;
import br.com.fiap.java.FidelisApi.dto.response.VacinacaoResponse;
import br.com.fiap.java.FidelisApi.entity.Vacinacao;
import br.com.fiap.java.FidelisApi.mapper.VacinacaoMapper;
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
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;

@Tag(name = "Vacinacao")
@RestController
@RequestMapping("/api/v1/vacinacoes")
@RequiredArgsConstructor
public class VacinacaoController {

    private final VacinacaoService vacinacaoService;

    @GetMapping
    @Operation(summary = "Listar vacinações", description = "Retorna uma página de vacinações")
    public ResponseEntity<Page<VacinacaoResponse>> listar(
            @RequestParam(required = false) String vacinaAplicada,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sort,
            @RequestParam(defaultValue = "ASC") String direction
    ) {
        Pageable pageable = PageRequest.of(page, size, Sort.Direction.fromString(direction), sort);
        Page<VacinacaoResponse> response = vacinacaoService.findAll(vacinaAplicada, pageable)
                .map(VacinacaoMapper::toResponse);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar vacinação por ID", description = "Retorna os dados de uma vacinação pelo seu ID")
    public ResponseEntity<VacinacaoResponse> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(VacinacaoMapper.toResponse(vacinacaoService.findById(id)));
    }

    @PostMapping
    @Operation(summary = "Criar vacinação", description = "Cria um novo registro de vacinação")
    public ResponseEntity<VacinacaoResponse> criar(@Validated @RequestBody VacinacaoRequest request) {
        Vacinacao saved = vacinacaoService.create(VacinacaoMapper.toEntity(request), request.getPetId(), request.getVeterinarioId());
        return ResponseEntity.created(URI.create("/api/v1/vacinacoes/" + saved.getId())).body(VacinacaoMapper.toResponse(saved));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar vacinação", description = "Atualiza os dados de uma vacinação")
    public ResponseEntity<VacinacaoResponse> atualizar(@PathVariable Long id,
                                                       @Validated @RequestBody VacinacaoRequest request) {
        Vacinacao updated = vacinacaoService.update(id, VacinacaoMapper.toEntity(request));
        return ResponseEntity.ok(VacinacaoMapper.toResponse(updated));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Deletar vacinação", description = "Remove uma vacinação pelo ID")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        vacinacaoService.delete(id);
        return ResponseEntity.noContent().build();
    }
}

