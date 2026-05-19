package br.com.fiap.java.FidelisApi.service;

import br.com.fiap.java.FidelisApi.entity.Pet;
import br.com.fiap.java.FidelisApi.entity.Vacinacao;
import br.com.fiap.java.FidelisApi.entity.Veterinario;
import br.com.fiap.java.FidelisApi.exception.ResourceNotFoundException;
import br.com.fiap.java.FidelisApi.repository.PetRepository;
import br.com.fiap.java.FidelisApi.repository.VacinacaoRepository;
import br.com.fiap.java.FidelisApi.repository.VeterinarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class VacinacaoService {

    private final VacinacaoRepository vacinacaoRepository;
    private final PetRepository petRepository;
    private final VeterinarioRepository veterinarioRepository;

    @Cacheable("vacinacoes")
    public Page<Vacinacao> findAll(String vacinaAplicada, Pageable pageable) {
        if (vacinaAplicada != null && !vacinaAplicada.isBlank()) {
            return vacinacaoRepository.findByVacinaAplicadaContainingIgnoreCase(vacinaAplicada, pageable);
        }
        return vacinacaoRepository.findAll(pageable);
    }

    public Vacinacao findById(Long id) {
        return vacinacaoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Vacinação não encontrada com id " + id));
    }

    @CacheEvict(value = "vacinacoes", allEntries = true)
    public Vacinacao create(Vacinacao vacinacao, Long petId, Long veterinarioId) {
        Pet pet = petRepository.findById(petId)
                .orElseThrow(() -> new ResourceNotFoundException("Pet não encontrado com id " + petId));
        Veterinario veterinario = veterinarioRepository.findById(veterinarioId)
                .orElseThrow(() -> new ResourceNotFoundException("Veterinário não encontrado com id " + veterinarioId));
        vacinacao.setPet(pet);
        vacinacao.setVeterinario(veterinario);
        return vacinacaoRepository.save(vacinacao);
    }

    @CacheEvict(value = "vacinacoes", allEntries = true)
    public Vacinacao update(Long id, Vacinacao update) {
        Vacinacao existing = findById(id);
        existing.setDataAplicacao(update.getDataAplicacao());
        existing.setDataProxima(update.getDataProxima());
        existing.setVacinaAplicada(update.getVacinaAplicada());
        existing.setObservacao(update.getObservacao());
        return vacinacaoRepository.save(existing);
    }

    @CacheEvict(value = "vacinacoes", allEntries = true)
    public void delete(Long id) {
        Vacinacao existing = findById(id);
        vacinacaoRepository.delete(existing);
    }
}
