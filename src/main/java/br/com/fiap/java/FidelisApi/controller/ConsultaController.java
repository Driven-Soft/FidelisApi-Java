package br.com.fiap.java.FidelisApi.controller;

import br.com.fiap.java.FidelisApi.dto.request.ConsultaRequest;
import br.com.fiap.java.FidelisApi.dto.response.ConsultaResponse;
import br.com.fiap.java.FidelisApi.entity.Consulta;
import br.com.fiap.java.FidelisApi.service.ConsultaService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/consultas")
@RequiredArgsConstructor
public class ConsultaController {

    private final ConsultaService consultaService;

    @GetMapping
    public ResponseEntity<Page<ConsultaResponse>> listar(
            @RequestParam(required = false) String tipo,
            @RequestParam(required = false) LocalDateTime inicio,
            @RequestParam(required = false) LocalDateTime fim,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sort,
            @RequestParam(defaultValue = "ASC") String direction
    ) {
        Pageable pageable = PageRequest.of(page, size, Sort.Direction.fromString(direction), sort);
        Page<ConsultaResponse> response = consultaService.findAll(tipo, inicio, fim, pageable)
                .map(this::toResponse);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ConsultaResponse> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(toResponse(consultaService.findById(id)));
    }

    @PostMapping
    public ResponseEntity<ConsultaResponse> criar(@Validated @RequestBody ConsultaRequest request) {
        Consulta saved = consultaService.create(toEntity(request), request.getVeterinarioId(), request.getPetId());
        return ResponseEntity.created(URI.create("/api/consultas/" + saved.getId())).body(toResponse(saved));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ConsultaResponse> atualizar(@PathVariable Long id,
                                                     @Validated @RequestBody ConsultaRequest request) {
        Consulta updated = consultaService.update(id, toEntity(request));
        return ResponseEntity.ok(toResponse(updated));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        consultaService.delete(id);
        return ResponseEntity.noContent().build();
    }

    private Consulta toEntity(ConsultaRequest request) {
        return Consulta.builder()
                .dataHora(request.getDataHora())
                .tipo(request.getTipo())
                .diagnostico(request.getDiagnostico())
                .observacoes(request.getObservacoes())
                .dataRetorno(request.getDataRetorno())
                .build();
    }

    private ConsultaResponse toResponse(Consulta entity) {
        return ConsultaResponse.builder()
                .id(entity.getId())
                .dataHora(entity.getDataHora())
                .tipo(entity.getTipo())
                .diagnostico(entity.getDiagnostico())
                .observacoes(entity.getObservacoes())
                .dataRetorno(entity.getDataRetorno())
                .veterinarioId(entity.getVeterinario().getId())
                .veterinarioNome(entity.getVeterinario().getNome())
                .petId(entity.getPet().getId())
                .petNome(entity.getPet().getNome())
                .build();
    }
}
