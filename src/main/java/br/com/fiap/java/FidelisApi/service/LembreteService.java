package br.com.fiap.java.FidelisApi.service;

import br.com.fiap.java.FidelisApi.entity.Lembrete;
import br.com.fiap.java.FidelisApi.entity.Pet;
import br.com.fiap.java.FidelisApi.entity.Tutor;
import br.com.fiap.java.FidelisApi.exception.ResourceNotFoundException;
import br.com.fiap.java.FidelisApi.repository.LembreteRepository;
import br.com.fiap.java.FidelisApi.repository.PetRepository;
import br.com.fiap.java.FidelisApi.repository.TutorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LembreteService {

    private final LembreteRepository lembreteRepository;
    private final TutorRepository tutorRepository;
    private final PetRepository petRepository;

    @Cacheable(value = "lembretes", key = "(#tipo != null ? #tipo : '') + '_' + #pageable.pageNumber + '_' + #pageable.pageSize + '_' + #pageable.sort")
    public Page<Lembrete> findAll(String tipo, Pageable pageable) {
        if (tipo != null && !tipo.isBlank()) {
            return lembreteRepository.findByTipoContainingIgnoreCase(tipo, pageable);
        }
        return lembreteRepository.findAll(pageable);
    }

    public Lembrete findById(Long id) {
        return lembreteRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Lembrete não encontrado com id " + id));
    }

    @CacheEvict(value = "lembretes", allEntries = true)
    public Lembrete create(Lembrete lembrete, Long tutorId, Long petId) {
        Tutor tutor = tutorRepository.findById(tutorId)
                .orElseThrow(() -> new ResourceNotFoundException("Tutor não encontrado com id " + tutorId));
        Pet pet = petRepository.findById(petId)
                .orElseThrow(() -> new ResourceNotFoundException("Pet não encontrado com id " + petId));
        lembrete.setTutor(tutor);
        lembrete.setPet(pet);
        return lembreteRepository.save(lembrete);
    }

    @CacheEvict(value = "lembretes", allEntries = true)
    public Lembrete update(Long id, Lembrete update) {
        Lembrete existing = findById(id);
        existing.setTipo(update.getTipo());
        existing.setDescricao(update.getDescricao());
        existing.setDataPrevista(update.getDataPrevista());
        existing.setStatus(update.getStatus());
        return lembreteRepository.save(existing);
    }

    @CacheEvict(value = "lembretes", allEntries = true)
    public void delete(Long id) {
        Lembrete existing = findById(id);
        lembreteRepository.delete(existing);
    }
}
