package br.com.fiap.java.FidelisApi.service;

import br.com.fiap.java.FidelisApi.entity.Consulta;
import br.com.fiap.java.FidelisApi.entity.Pet;
import br.com.fiap.java.FidelisApi.entity.Veterinario;
import br.com.fiap.java.FidelisApi.exception.ResourceNotFoundException;
import br.com.fiap.java.FidelisApi.repository.ConsultaRepository;
import br.com.fiap.java.FidelisApi.repository.PetRepository;
import br.com.fiap.java.FidelisApi.repository.VeterinarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class ConsultaService {

    private final ConsultaRepository consultaRepository;
    private final VeterinarioRepository veterinarioRepository;
    private final PetRepository petRepository;

    @Cacheable(value = "consultas", key = "(#tipo != null ? #tipo : '') + '_' + (#inicio != null ? #inicio.toString() : '') + '_' + (#fim != null ? #fim.toString() : '') + '_' + #pageable.pageNumber + '_' + #pageable.pageSize + '_' + #pageable.sort")
    public Page<Consulta> findAll(String tipo, LocalDateTime inicio, LocalDateTime fim, Pageable pageable) {
        if (tipo != null && !tipo.isBlank()) {
            return consultaRepository.findByTipoContainingIgnoreCase(tipo, pageable);
        }
        return consultaRepository.findByDataHoraBetween(inicio, fim, pageable);
    }

    public Consulta findById(Long id) {
        return consultaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Consulta não encontrada com id " + id));
    }

    @CacheEvict(value = "consultas", allEntries = true)
    public Consulta create(Consulta consulta, Long veterinarioId, Long petId) {
        Veterinario veterinario = veterinarioRepository.findById(veterinarioId)
                .orElseThrow(() -> new ResourceNotFoundException("Veterinário não encontrado com id " + veterinarioId));
        Pet pet = petRepository.findById(petId)
                .orElseThrow(() -> new ResourceNotFoundException("Pet não encontrado com id " + petId));
        consulta.setVeterinario(veterinario);
        consulta.setPet(pet);
        return consultaRepository.save(consulta);
    }

    @CacheEvict(value = "consultas", allEntries = true)
    public Consulta update(Long id, Consulta update) {
        Consulta existing = findById(id);
        existing.setDataHora(update.getDataHora());
        existing.setTipo(update.getTipo());
        existing.setDiagnostico(update.getDiagnostico());
        existing.setObservacoes(update.getObservacoes());
        existing.setDataRetorno(update.getDataRetorno());
        return consultaRepository.save(existing);
    }

    @CacheEvict(value = "consultas", allEntries = true)
    public void delete(Long id) {
        Consulta existing = findById(id);
        consultaRepository.delete(existing);
    }
}
