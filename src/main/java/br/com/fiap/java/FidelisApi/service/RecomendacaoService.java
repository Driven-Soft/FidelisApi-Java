package br.com.fiap.java.FidelisApi.service;

import br.com.fiap.java.FidelisApi.entity.Pet;
import br.com.fiap.java.FidelisApi.entity.Recomendacao;
import br.com.fiap.java.FidelisApi.exception.ResourceNotFoundException;
import br.com.fiap.java.FidelisApi.repository.PetRepository;
import br.com.fiap.java.FidelisApi.repository.RecomendacaoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RecomendacaoService {

    private final RecomendacaoRepository recomendacaoRepository;
    private final PetRepository petRepository;

    @Cacheable("recomendacoes")
    public Page<Recomendacao> findAll(String tipo, Pageable pageable) {
        if (tipo != null && !tipo.isBlank()) {
            return recomendacaoRepository.findByTipoContainingIgnoreCase(tipo, pageable);
        }
        return recomendacaoRepository.findAll(pageable);
    }

    public Recomendacao findById(Long id) {
        return recomendacaoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Recomendação não encontrada com id " + id));
    }

    @CacheEvict(value = "recomendacoes", allEntries = true)
    public Recomendacao create(Recomendacao recomendacao, Long petId) {
        Pet pet = petRepository.findById(petId)
                .orElseThrow(() -> new ResourceNotFoundException("Pet não encontrado com id " + petId));
        recomendacao.setPet(pet);
        return recomendacaoRepository.save(recomendacao);
    }

    @CacheEvict(value = "recomendacoes", allEntries = true)
    public Recomendacao update(Long id, Recomendacao update) {
        Recomendacao existing = findById(id);
        existing.setTipo(update.getTipo());
        existing.setDescricao(update.getDescricao());
        existing.setDataRecomendacao(update.getDataRecomendacao());
        return recomendacaoRepository.save(existing);
    }

    @CacheEvict(value = "recomendacoes", allEntries = true)
    public void delete(Long id) {
        Recomendacao existing = findById(id);
        recomendacaoRepository.delete(existing);
    }
}
