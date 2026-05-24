package br.com.fiap.java.FidelisApi.service;

import br.com.fiap.java.FidelisApi.entity.Medicamento;
import br.com.fiap.java.FidelisApi.entity.Prescricao;
import br.com.fiap.java.FidelisApi.exception.ResourceNotFoundException;
import br.com.fiap.java.FidelisApi.repository.MedicamentoRepository;
import br.com.fiap.java.FidelisApi.repository.PrescricaoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MedicamentoService {

    private final MedicamentoRepository medicamentoRepository;
    private final PrescricaoRepository prescricaoRepository;

    @Cacheable(value = "medicamentos", key = "(#nome != null ? #nome : '') + '_' + #pageable.pageNumber + '_' + #pageable.pageSize + '_' + #pageable.sort")
    public Page<Medicamento> findAll(String nome, Pageable pageable) {
        if (nome != null && !nome.isBlank()) {
            return medicamentoRepository.findByNomeContainingIgnoreCase(nome, pageable);
        }
        return medicamentoRepository.findAll(pageable);
    }

    public Medicamento findById(Long id) {
        return medicamentoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Medicamento não encontrado com id " + id));
    }

    @CacheEvict(value = "medicamentos", allEntries = true)
    public Medicamento create(Medicamento medicamento, Long prescricaoId) {
        Prescricao prescricao = prescricaoRepository.findById(prescricaoId)
                .orElseThrow(() -> new ResourceNotFoundException("Prescrição não encontrada com id " + prescricaoId));
        medicamento.setPrescricao(prescricao);
        return medicamentoRepository.save(medicamento);
    }

    @CacheEvict(value = "medicamentos", allEntries = true)
    public Medicamento update(Long id, Medicamento update) {
        Medicamento existing = findById(id);
        existing.setNome(update.getNome());
        existing.setDescricao(update.getDescricao());
        return medicamentoRepository.save(existing);
    }

    @CacheEvict(value = "medicamentos", allEntries = true)
    public void delete(Long id) {
        Medicamento existing = findById(id);
        medicamentoRepository.delete(existing);
    }
}
