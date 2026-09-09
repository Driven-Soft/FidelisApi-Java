package br.com.fiap.java.FidelisApi.service;

import br.com.fiap.java.FidelisApi.entity.Clinica;
import br.com.fiap.java.FidelisApi.entity.Pet;
import br.com.fiap.java.FidelisApi.entity.Tutor;
import br.com.fiap.java.FidelisApi.exception.ResourceNotFoundException;
import br.com.fiap.java.FidelisApi.repository.ClinicaRepository;
import br.com.fiap.java.FidelisApi.repository.PetRepository;
import br.com.fiap.java.FidelisApi.repository.TutorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PetService {

    private final PetRepository petRepository;
    private final TutorRepository tutorRepository;
    private final ClinicaRepository clinicaRepository;

    @Cacheable(value = "pets", key = "(#nome != null ? #nome : '') + '_' + (#especie != null ? #especie : '') + '_' + #pageable.pageNumber + '_' + #pageable.pageSize + '_' + #pageable.sort")
    @Transactional(readOnly = true)
    public Page<Pet> findAll(String nome, String especie, Pageable pageable) {
        if (nome != null && !nome.isBlank() && especie != null && !especie.isBlank()) {
            return petRepository.findByNomeContainingIgnoreCaseAndEspecieContainingIgnoreCase(nome, especie, pageable);
        }
        if (especie != null && !especie.isBlank()) {
            return petRepository.findByEspecieContainingIgnoreCase(especie, pageable);
        }
        if (nome != null && !nome.isBlank()) {
            return petRepository.findByNomeContainingIgnoreCase(nome, pageable);
        }
        return petRepository.findAll(pageable);
    }

    public Pet findById(Long id) {
        return petRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Pet não encontrado com id " + id));
    }

    @CacheEvict(value = "pets", allEntries = true)
    @Transactional
    public Pet create(Pet pet, Long tutorId, Long clinicaId) {
        Tutor tutor = tutorRepository.findById(tutorId)
                .orElseThrow(() -> new ResourceNotFoundException("Tutor não encontrado com id " + tutorId));
        Clinica clinica = clinicaRepository.findById(clinicaId)
                .orElseThrow(() -> new ResourceNotFoundException("Clínica não encontrada com id " + clinicaId));
        pet.setTutor(tutor);
        pet.setClinica(clinica);
        return petRepository.save(pet);
    }

    @CacheEvict(value = "pets", allEntries = true)
    @Transactional
    public Pet update(Long id, Pet update) {
        Pet existing = findById(id);
        existing.setNome(update.getNome());
        existing.setEspecie(update.getEspecie());
        existing.setRaca(update.getRaca());
        existing.setSexo(update.getSexo());
        existing.setDataNascimento(update.getDataNascimento());
        existing.setStatus(update.getStatus());
        existing.setFotoUrl(update.getFotoUrl());
        return petRepository.save(existing);
    }

    @CacheEvict(value = "pets", allEntries = true)
    @Transactional
    public void delete(Long id) {
        Pet existing = findById(id);
        petRepository.delete(existing);
    }

    @Transactional(readOnly = true)
    public Page<Pet> findAllByClinica(Long clinicaId, Pageable pageable) {
        return petRepository.findByClinicaIdComTutor(clinicaId, pageable);
    }
}
