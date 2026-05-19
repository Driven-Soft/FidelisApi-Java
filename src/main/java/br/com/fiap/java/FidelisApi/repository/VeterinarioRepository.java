package br.com.fiap.java.FidelisApi.repository;

import br.com.fiap.java.FidelisApi.entity.Veterinario;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VeterinarioRepository extends JpaRepository<Veterinario, Long> {

    Page<Veterinario> findByEspecialidadeContainingIgnoreCase(String especialidade, Pageable pageable);

    Page<Veterinario> findByNomeContainingIgnoreCase(String nome, Pageable pageable);

    boolean existsByCmvv(String cmvv);

    boolean existsByEmail(String email);
}
