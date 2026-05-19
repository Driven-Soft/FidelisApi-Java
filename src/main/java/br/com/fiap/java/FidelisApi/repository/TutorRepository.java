package br.com.fiap.java.FidelisApi.repository;

import br.com.fiap.java.FidelisApi.entity.Tutor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TutorRepository extends JpaRepository<Tutor, Long> {

    Page<Tutor> findByNomeContainingIgnoreCase(String nome, Pageable pageable);

    Page<Tutor> findByEmailContainingIgnoreCase(String email, Pageable pageable);

    boolean existsByCpf(String cpf);

    boolean existsByEmail(String email);
}
