package br.com.fiap.java.FidelisApi.service;

import br.com.fiap.java.FidelisApi.entity.Consulta;
import br.com.fiap.java.FidelisApi.entity.Prescricao;
import br.com.fiap.java.FidelisApi.exception.ResourceNotFoundException;
import br.com.fiap.java.FidelisApi.repository.ConsultaRepository;
import br.com.fiap.java.FidelisApi.repository.PrescricaoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PrescricaoService {

    private final PrescricaoRepository prescricaoRepository;
    private final ConsultaRepository consultaRepository;

    @Cacheable(value = "prescricoes", key = "(#dosagem != null ? #dosagem : '') + '_' + #pageable.pageNumber + '_' + #pageable.pageSize + '_' + #pageable.sort")
    public Page<Prescricao> findAll(String dosagem, Pageable pageable) {
        if (dosagem != null && !dosagem.isBlank()) {
            return prescricaoRepository.findByDosagemContainingIgnoreCase(dosagem, pageable);
        }
        return prescricaoRepository.findAll(pageable);
    }

    public Prescricao findById(Long id) {
        return prescricaoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Prescrição não encontrada com id " + id));
    }

    @CacheEvict(value = "prescricoes", allEntries = true)
    public Prescricao create(Prescricao prescricao, Long consultaId) {
        Consulta consulta = consultaRepository.findById(consultaId)
                .orElseThrow(() -> new ResourceNotFoundException("Consulta não encontrada com id " + consultaId));
        prescricao.setConsulta(consulta);
        return prescricaoRepository.save(prescricao);
    }

    @CacheEvict(value = "prescricoes", allEntries = true)
    public Prescricao update(Long id, Prescricao update) {
        Prescricao existing = findById(id);
        existing.setDosagem(update.getDosagem());
        existing.setFrequencia(update.getFrequencia());
        existing.setDuracaoDias(update.getDuracaoDias());
        existing.setObservacao(update.getObservacao());
        return prescricaoRepository.save(existing);
    }

    @CacheEvict(value = "prescricoes", allEntries = true)
    public void delete(Long id) {
        Prescricao existing = findById(id);
        prescricaoRepository.delete(existing);
    }
}
