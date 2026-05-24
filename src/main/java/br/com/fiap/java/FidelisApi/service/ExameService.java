package br.com.fiap.java.FidelisApi.service;

import br.com.fiap.java.FidelisApi.entity.Consulta;
import br.com.fiap.java.FidelisApi.entity.Exame;
import br.com.fiap.java.FidelisApi.exception.ResourceNotFoundException;
import br.com.fiap.java.FidelisApi.repository.ConsultaRepository;
import br.com.fiap.java.FidelisApi.repository.ExameRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ExameService {

    private final ExameRepository exameRepository;
    private final ConsultaRepository consultaRepository;

    @Cacheable(value = "exames", key = "(#tipo != null ? #tipo : '') + '_' + #pageable.pageNumber + '_' + #pageable.pageSize + '_' + #pageable.sort")
    public Page<Exame> findAll(String tipo, Pageable pageable) {
        if (tipo != null && !tipo.isBlank()) {
            return exameRepository.findByTipoContainingIgnoreCase(tipo, pageable);
        }
        return exameRepository.findAll(pageable);
    }

    public Exame findById(Long id) {
        return exameRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Exame não encontrado com id " + id));
    }

    @CacheEvict(value = "exames", allEntries = true)
    public Exame create(Exame exame, Long consultaId) {
        Consulta consulta = consultaRepository.findById(consultaId)
                .orElseThrow(() -> new ResourceNotFoundException("Consulta não encontrada com id " + consultaId));
        exame.setConsulta(consulta);
        return exameRepository.save(exame);
    }

    @CacheEvict(value = "exames", allEntries = true)
    public Exame update(Long id, Exame update) {
        Exame existing = findById(id);
        existing.setTipo(update.getTipo());
        existing.setDescricao(update.getDescricao());
        existing.setResultado(update.getResultado());
        existing.setData(update.getData());
        return exameRepository.save(existing);
    }

    @CacheEvict(value = "exames", allEntries = true)
    public void delete(Long id) {
        Exame existing = findById(id);
        exameRepository.delete(existing);
    }
}
