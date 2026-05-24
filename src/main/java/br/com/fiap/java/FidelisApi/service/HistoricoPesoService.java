package br.com.fiap.java.FidelisApi.service;

import br.com.fiap.java.FidelisApi.entity.HistoricoPeso;
import br.com.fiap.java.FidelisApi.entity.Pet;
import br.com.fiap.java.FidelisApi.exception.ResourceNotFoundException;
import br.com.fiap.java.FidelisApi.repository.HistoricoPesoRepository;
import br.com.fiap.java.FidelisApi.repository.PetRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class HistoricoPesoService {

    private final HistoricoPesoRepository historicoPesoRepository;
    private final PetRepository petRepository;

    @Cacheable(value = "historicoPeso", key = "(#minPeso != null ? #minPeso.toString() : '') + '_' + (#maxPeso != null ? #maxPeso.toString() : '') + '_' + #pageable.pageNumber + '_' + #pageable.pageSize + '_' + #pageable.sort")
    public Page<HistoricoPeso> findAll(BigDecimal minPeso, BigDecimal maxPeso, Pageable pageable) {
        if (minPeso != null && maxPeso != null) {
            return historicoPesoRepository.findByPesoKgBetween(minPeso, maxPeso, pageable);
        }
        return historicoPesoRepository.findAll(pageable);
    }

    public HistoricoPeso findById(Long id) {
        return historicoPesoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Histórico de peso não encontrado com id " + id));
    }

    @CacheEvict(value = "historicoPeso", allEntries = true)
    public HistoricoPeso create(HistoricoPeso historicoPeso, Long petId) {
        Pet pet = petRepository.findById(petId)
                .orElseThrow(() -> new ResourceNotFoundException("Pet não encontrado com id " + petId));
        historicoPeso.setPet(pet);
        return historicoPesoRepository.save(historicoPeso);
    }

    @CacheEvict(value = "historicoPeso", allEntries = true)
    public HistoricoPeso update(Long id, HistoricoPeso update) {
        HistoricoPeso existing = findById(id);
        existing.setPesoKg(update.getPesoKg());
        existing.setDataMedicao(update.getDataMedicao());
        existing.setObservacao(update.getObservacao());
        return historicoPesoRepository.save(existing);
    }

    @CacheEvict(value = "historicoPeso", allEntries = true)
    public void delete(Long id) {
        HistoricoPeso existing = findById(id);
        historicoPesoRepository.delete(existing);
    }
}
