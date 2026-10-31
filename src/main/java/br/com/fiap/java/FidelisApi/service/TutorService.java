package br.com.fiap.java.FidelisApi.service;

import br.com.fiap.java.FidelisApi.entity.Tutor;
import br.com.fiap.java.FidelisApi.entity.Perfil;
import br.com.fiap.java.FidelisApi.entity.Usuario;
import br.com.fiap.java.FidelisApi.exception.BusinessException;
import br.com.fiap.java.FidelisApi.exception.ResourceNotFoundException;
import br.com.fiap.java.FidelisApi.repository.TutorRepository;
import br.com.fiap.java.FidelisApi.repository.UsuarioRepository;
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
public class TutorService {

    private final TutorRepository tutorRepository;
    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    @Cacheable(value = "tutores", key = "(#nome != null ? #nome : '') + '_' + (#email != null ? #email : '') + '_' + #pageable.pageNumber + '_' + #pageable.pageSize + '_' + #pageable.sort")
    @Transactional(readOnly = true)
    public Page<Tutor> findAll(String nome, String email, Pageable pageable) {
        if (email != null && !email.isBlank()) {
            return tutorRepository.findByEmailContainingIgnoreCase(email, pageable);
        }
        if (nome != null && !nome.isBlank()) {
            return tutorRepository.findByNomeContainingIgnoreCase(nome, pageable);
        }
        return tutorRepository.findAll(pageable);
    }

    public Tutor findById(Long id) {
        return tutorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Tutor não encontrado com id " + id));
    }

    @CacheEvict(value = "tutores", allEntries = true)
    @Transactional
    public Tutor create(Tutor tutor) {
        tutor.setCpf(formatarCpf(tutor.getCpf()));
        tutor.setEmail(tutor.getEmail().trim().toLowerCase(java.util.Locale.ROOT));
        tutor.setTelefone(formatarTelefone(tutor.getTelefone()));

        if (tutorRepository.existsByCpf(tutor.getCpf())) {
            throw new BusinessException("CPF já cadastrado.");
        }
        if (tutorRepository.existsByEmailIgnoreCase(tutor.getEmail())
                || usuarioRepository.findByEmailIgnoreCase(tutor.getEmail()).isPresent()) {
            throw new BusinessException("Email já cadastrado.");
        }
        tutor.setSenha(passwordEncoder.encode(tutor.getSenha()));
        Tutor salvo = tutorRepository.save(tutor);

        usuarioRepository.save(Usuario.builder()
                .email(salvo.getEmail())
                .senha(salvo.getSenha())
                .perfil(Perfil.TUTOR)
                .ativo(true)
                .dataCriacao(salvo.getDataCriacao())
                .tutor(salvo)
                .build());
        return salvo;
    }

    @CacheEvict(value = "tutores", allEntries = true)
    @Transactional
    public Tutor update(Long id, Tutor update) {
        Tutor existing = findById(id);
        if (!existing.getCpf().equals(update.getCpf()) && tutorRepository.existsByCpf(update.getCpf())) {
            throw new BusinessException("CPF já cadastrado por outro tutor.");
        }
        if (!existing.getEmail().equals(update.getEmail()) && tutorRepository.existsByEmail(update.getEmail())) {
            throw new BusinessException("Email já cadastrado por outro tutor.");
        }
        existing.setNome(update.getNome());
        existing.setEmail(update.getEmail());
        existing.setSenha(passwordEncoder.encode(update.getSenha()));
        existing.setTelefone(update.getTelefone());
        existing.setEndereco(update.getEndereco());
        existing.setCpf(update.getCpf());
        existing.setDataCriacao(update.getDataCriacao());
        return tutorRepository.save(existing);
    }

    @CacheEvict(value = "tutores", allEntries = true)
    @Transactional
    public void delete(Long id) {
        Tutor existing = findById(id);
        tutorRepository.delete(existing);
    }

    private String formatarCpf(String cpf) {
        String digitos = cpf.replaceAll("\\D", "");
        return digitos.replaceFirst("(\\d{3})(\\d{3})(\\d{3})(\\d{2})", "$1.$2.$3-$4");
    }

    private String formatarTelefone(String telefone) {
        String digitos = telefone.replaceAll("\\D", "");
        if (digitos.length() == 11) {
            return digitos.replaceFirst("(\\d{2})(\\d{5})(\\d{4})", "($1) $2-$3");
        }
        if (digitos.length() == 10) {
            return digitos.replaceFirst("(\\d{2})(\\d{4})(\\d{4})", "($1) $2-$3");
        }
        throw new BusinessException("Telefone deve conter DDD e 10 ou 11 dígitos.");
    }
}
