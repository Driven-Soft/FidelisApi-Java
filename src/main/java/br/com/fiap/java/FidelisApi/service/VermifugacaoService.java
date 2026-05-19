package br.com.fiap.java.FidelisApi.service;

import br.com.fiap.java.FidelisApi.entity.Pet;
import br.com.fiap.java.FidelisApi.entity.Vermifugacao;
import br.com.fiap.java.FidelisApi.entity.Veterinario;
import br.com.fiap.java.FidelisApi.exception.ResourceNotFoundException;
import br.com.fiap.java.FidelisApi.repository.PetRepository;
import br.com.fiap.java.FidelisApi.repository.VermifugacaoRepository;
import br.com.fiap.java.FidelisApi.repository.VeterinarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class VermifugacaoService {

    private final VermifugacaoRepository vermifugacaoRepository;
    private final PetRepository petRepository;
    private final VeterinarioRepository veterinarioRepository;

    @Cacheable("vermifugacoes")
    public Page<Vermifugacao> findAll(String produto, Pageable pageable) {
        if (produto != null && !produto.isBlank()) {
            return vermifugacaoRepository.findByProdutoContainingIgnoreCase(produto, pageable);
        }
        return vermifugacaoRepository.findAll(pageable);
    }

    public Vermifugacao findById(Long id) {
        return vermifugacaoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Vermifugação não encontrada com id " + id));
    }

    @CacheEvict(value = "vermifugacoes", allEntries = true)
    public Vermifugacao create(Vermifugacao vermifugacao, Long petId, Long veterinarioId) {
        Pet pet = petRepository.findById(petId)
                .orElseThrow(() -> new ResourceNotFoundException("Pet não encontrado com id " + petId));
        Veterinario veterinario = veterinarioRepository.findById(veterinarioId)
                .orElseThrow(() -> new ResourceNotFoundException("Veterinário não encontrado com id " + veterinarioId));
        vermifugacao.setPet(pet);
        vermifugacao.setVeterinario(veterinario);
        return vermifugacaoRepository.save(vermifugacao);
    }

    @CacheEvict(value = "vermifugacoes", allEntries = true)
    public Vermifugacao update(Long id, Vermifugacao update) {
        Vermifugacao existing = findById(id);
        existing.setProduto(update.getProduto());
        existing.setDataAplicacao(update.getDataAplicacao());
        existing.setDataProxima(update.getDataProxima());
        return vermifugacaoRepository.save(existing);
    }

    @CacheEvict(value = "vermifugacoes", allEntries = true)
    public void delete(Long id) {
        Vermifugacao existing = findById(id);
        vermifugacaoRepository.delete(existing);
    }
}
