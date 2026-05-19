package br.com.fiap.java.FidelisApi.service;

import br.com.fiap.java.FidelisApi.entity.Clinica;
import br.com.fiap.java.FidelisApi.exception.BusinessException;
import br.com.fiap.java.FidelisApi.exception.ResourceNotFoundException;
import br.com.fiap.java.FidelisApi.repository.ClinicaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ClinicaService {

    private final ClinicaRepository clinicaRepository;

    @Cacheable("clinicas")
    public Page<Clinica> findAll(String nome, Pageable pageable) {
        if (nome == null || nome.isBlank()) {
            return clinicaRepository.findAll(pageable);
        }
        return clinicaRepository.findByNomeContainingIgnoreCase(nome, pageable);
    }

    public Clinica findById(Long id) {
        return clinicaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Clínica não encontrada com id " + id));
    }

    @CacheEvict(value = "clinicas", allEntries = true)
    public Clinica create(Clinica clinica) {
        if (clinicaRepository.existsByCnpj(clinica.getCnpj())) {
            throw new BusinessException("CNPJ já cadastrado.");
        }
        if (clinicaRepository.existsByEmail(clinica.getEmail())) {
            throw new BusinessException("Email da clínica já está em uso.");
        }
        return clinicaRepository.save(clinica);
    }

    @CacheEvict(value = "clinicas", allEntries = true)
    public Clinica update(Long id, Clinica update) {
        Clinica existing = findById(id);
        if (!existing.getCnpj().equals(update.getCnpj()) && clinicaRepository.existsByCnpj(update.getCnpj())) {
            throw new BusinessException("CNPJ já cadastrado por outra clínica.");
        }
        if (!existing.getEmail().equals(update.getEmail()) && clinicaRepository.existsByEmail(update.getEmail())) {
            throw new BusinessException("Email já cadastrado por outra clínica.");
        }
        existing.setNome(update.getNome());
        existing.setCnpj(update.getCnpj());
        existing.setTelefone(update.getTelefone());
        existing.setEmail(update.getEmail());
        existing.setEndereco(update.getEndereco());
        return clinicaRepository.save(existing);
    }

    @CacheEvict(value = "clinicas", allEntries = true)
    public void delete(Long id) {
        Clinica existing = findById(id);
        clinicaRepository.delete(existing);
    }
}
