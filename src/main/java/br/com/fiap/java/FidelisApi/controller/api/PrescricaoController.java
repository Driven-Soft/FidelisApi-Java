package br.com.fiap.java.FidelisApi.controller.api;

import br.com.fiap.java.FidelisApi.dto.request.PrescricaoRequest;
import br.com.fiap.java.FidelisApi.dto.response.PrescricaoResponse;
import br.com.fiap.java.FidelisApi.entity.Prescricao;
import br.com.fiap.java.FidelisApi.mapper.PrescricaoMapper;
import br.com.fiap.java.FidelisApi.service.PrescricaoService;
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

@Tag(name = "Prescricao")
@RestController
@RequestMapping("/api/v1/prescricoes")
@RequiredArgsConstructor
public class PrescricaoController {

    private final PrescricaoService prescricaoService;

    @GetMapping
    @Operation(summary = "Listar prescrições", description = "Retorna uma página de prescrições")
    public ResponseEntity<Page<PrescricaoResponse>> listar(
            @RequestParam(required = false) String dosagem,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sort,
            @RequestParam(defaultValue = "ASC") String direction
    ) {
        Pageable pageable = PageableUtils.build(page, size, sort, direction);
        Page<PrescricaoResponse> response = prescricaoService.findAll(dosagem, pageable)
                .map(PrescricaoMapper::toResponse);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar prescrição por ID", description = "Retorna os dados de uma prescrição pelo seu ID")
    public ResponseEntity<PrescricaoResponse> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(PrescricaoMapper.toResponse(prescricaoService.findById(id)));
    }

    @PostMapping
    @Operation(summary = "Criar prescrição", description = "Cria uma nova prescrição")
    public ResponseEntity<PrescricaoResponse> criar(@Validated @RequestBody PrescricaoRequest request) {
        Prescricao saved = prescricaoService.create(PrescricaoMapper.toEntity(request), request.getConsultaId());
        return ResponseEntity.created(URI.create("/api/v1/prescricoes/" + saved.getId())).body(PrescricaoMapper.toResponse(saved));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar prescrição", description = "Atualiza os dados de uma prescrição")
    public ResponseEntity<PrescricaoResponse> atualizar(@PathVariable Long id,
                                                        @Validated @RequestBody PrescricaoRequest request) {
        Prescricao updated = prescricaoService.update(id, PrescricaoMapper.toEntity(request));
        return ResponseEntity.ok(PrescricaoMapper.toResponse(updated));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Deletar prescrição", description = "Remove uma prescrição pelo ID")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        prescricaoService.delete(id);
        return ResponseEntity.noContent().build();
    }
}

