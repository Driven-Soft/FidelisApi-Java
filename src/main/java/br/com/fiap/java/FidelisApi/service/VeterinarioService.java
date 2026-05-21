package br.com.fiap.java.FidelisApi.service;

import br.com.fiap.java.FidelisApi.entity.Clinica;
import br.com.fiap.java.FidelisApi.entity.Veterinario;
import br.com.fiap.java.FidelisApi.exception.BusinessException;
import br.com.fiap.java.FidelisApi.exception.ResourceNotFoundException;
import br.com.fiap.java.FidelisApi.repository.ClinicaRepository;
import br.com.fiap.java.FidelisApi.repository.VeterinarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class VeterinarioService {

    private final VeterinarioRepository veterinarioRepository;
    private final ClinicaRepository clinicaRepository;
    private final PasswordEncoder passwordEncoder;

    @Cacheable("veterinarios")
    public Page<Veterinario> findAll(String nome, String especialidade, Pageable pageable) {
        if (especialidade != null && !especialidade.isBlank()) {
            return veterinarioRepository.findByEspecialidadeContainingIgnoreCase(especialidade, pageable);
        }
        if (nome != null && !nome.isBlank()) {
            return veterinarioRepository.findByNomeContainingIgnoreCase(nome, pageable);
        }
        return veterinarioRepository.findAll(pageable);
    }

    public Veterinario findById(Long id) {
        return veterinarioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Veterinário não encontrado com id " + id));
    }

    @CacheEvict(value = "veterinarios", allEntries = true)
    @Transactional
    public Veterinario create(Veterinario veterinario, Long clinicaId) {
        Clinica clinica = clinicaRepository.findById(clinicaId)
                .orElseThrow(() -> new ResourceNotFoundException("Clínica não encontrada com id " + clinicaId));
        if (veterinarioRepository.existsByCmvv(veterinario.getCmvv())) {
            throw new BusinessException("CMVV já cadastrado.");
        }
        if (veterinarioRepository.existsByEmail(veterinario.getEmail())) {
            throw new BusinessException("Email já cadastrado.");
        }
        veterinario.setClinica(clinica);
        veterinario.setSenha(passwordEncoder.encode(veterinario.getSenha()));
        return veterinarioRepository.save(veterinario);
    }

    @CacheEvict(value = "veterinarios", allEntries = true)
    @Transactional
    public Veterinario update(Long id, Veterinario update) {
        Veterinario existing = findById(id);
        if (!existing.getCmvv().equals(update.getCmvv()) && veterinarioRepository.existsByCmvv(update.getCmvv())) {
            throw new BusinessException("CMVV já cadastrado por outro veterinário.");
        }
        if (!existing.getEmail().equals(update.getEmail()) && veterinarioRepository.existsByEmail(update.getEmail())) {
            throw new BusinessException("Email já cadastrado por outro veterinário.");
        }
        existing.setNome(update.getNome());
        existing.setEmail(update.getEmail());
        existing.setSenha(passwordEncoder.encode(update.getSenha()));
        existing.setEspecialidade(update.getEspecialidade());
        existing.setCmvv(update.getCmvv());
        existing.setDataCriacao(update.getDataCriacao());
        return veterinarioRepository.save(existing);
    }

    @CacheEvict(value = "veterinarios", allEntries = true)
    @Transactional
    public void delete(Long id) {
        Veterinario existing = findById(id);
        veterinarioRepository.delete(existing);
    }
}
