package br.com.fiap.java.FidelisApi.service;

import br.com.fiap.java.FidelisApi.entity.Comportamento;
import br.com.fiap.java.FidelisApi.entity.Pet;
import br.com.fiap.java.FidelisApi.exception.ResourceNotFoundException;
import br.com.fiap.java.FidelisApi.repository.ComportamentoRepository;
import br.com.fiap.java.FidelisApi.repository.PetRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ComportamentoService {

    private final ComportamentoRepository comportamentoRepository;
    private final PetRepository petRepository;

    @Cacheable(value = "comportamentos", key = "(#descricao != null ? #descricao : '') + '_' + #pageable.pageNumber + '_' + #pageable.pageSize + '_' + #pageable.sort")
    public Page<Comportamento> findAll(String descricao, Pageable pageable) {
        if (descricao != null && !descricao.isBlank()) {
            return comportamentoRepository.findByDescricaoContainingIgnoreCase(descricao, pageable);
        }
        return comportamentoRepository.findAll(pageable);
    }

    public Comportamento findById(Long id) {
        return comportamentoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Registro de comportamento não encontrado com id " + id));
    }

    @CacheEvict(value = "comportamentos", allEntries = true)
    public Comportamento create(Comportamento comportamento, Long petId) {
        Pet pet = petRepository.findById(petId)
                .orElseThrow(() -> new ResourceNotFoundException("Pet não encontrado com id " + petId));
        comportamento.setPet(pet);
        return comportamentoRepository.save(comportamento);
    }

    @CacheEvict(value = "comportamentos", allEntries = true)
    public Comportamento update(Long id, Comportamento update) {
        Comportamento existing = findById(id);
        existing.setData(update.getData());
        existing.setDescricao(update.getDescricao());
        return comportamentoRepository.save(existing);
    }

    @CacheEvict(value = "comportamentos", allEntries = true)
    public void delete(Long id) {
        Comportamento existing = findById(id);
        comportamentoRepository.delete(existing);
    }
}
