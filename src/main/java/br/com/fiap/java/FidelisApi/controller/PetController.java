package br.com.fiap.java.FidelisApi.controller;

import br.com.fiap.java.FidelisApi.dto.request.PetRequest;
import br.com.fiap.java.FidelisApi.dto.response.PetResponse;
import br.com.fiap.java.FidelisApi.entity.Pet;
import br.com.fiap.java.FidelisApi.service.PetService;
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
@RequestMapping("/api/pets")
@RequiredArgsConstructor
public class PetController {

    private final PetService petService;

    @GetMapping
    public ResponseEntity<Page<PetResponse>> listar(
            @RequestParam(required = false) String nome,
            @RequestParam(required = false) String especie,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sort,
            @RequestParam(defaultValue = "ASC") String direction
    ) {
        Pageable pageable = PageRequest.of(page, size, Sort.Direction.fromString(direction), sort);
        Page<PetResponse> response = petService.findAll(nome, especie, pageable)
                .map(this::toResponse);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PetResponse> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(toResponse(petService.findById(id)));
    }

    @PostMapping
    public ResponseEntity<PetResponse> criar(@Validated @RequestBody PetRequest request) {
        Pet saved = petService.create(toEntity(request), request.getTutorId(), request.getClinicaId());
        return ResponseEntity.created(URI.create("/api/pets/" + saved.getId())).body(toResponse(saved));
    }

    @PutMapping("/{id}")
    public ResponseEntity<PetResponse> atualizar(@PathVariable Long id,
                                                 @Validated @RequestBody PetRequest request) {
        Pet updated = petService.update(id, toEntity(request));
        return ResponseEntity.ok(toResponse(updated));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        petService.delete(id);
        return ResponseEntity.noContent().build();
    }

    private Pet toEntity(PetRequest request) {
        return Pet.builder()
                .nome(request.getNome())
                .especie(request.getEspecie())
                .raca(request.getRaca())
                .sexo(request.getSexo())
                .dataNascimento(request.getDataNascimento())
                .status(request.getStatus())
                .fotoUrl(request.getFotoUrl())
                .build();
    }

    private PetResponse toResponse(Pet entity) {
        return PetResponse.builder()
                .id(entity.getId())
                .nome(entity.getNome())
                .especie(entity.getEspecie())
                .raca(entity.getRaca())
                .sexo(entity.getSexo())
                .dataNascimento(entity.getDataNascimento())
                .status(entity.getStatus())
                .fotoUrl(entity.getFotoUrl())
                .tutorId(entity.getTutor().getId())
                .tutorNome(entity.getTutor().getNome())
                .clinicaId(entity.getClinica().getId())
                .clinicaNome(entity.getClinica().getNome())
                .build();
    }
}
