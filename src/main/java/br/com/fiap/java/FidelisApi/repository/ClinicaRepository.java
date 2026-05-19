package br.com.fiap.java.FidelisApi.repository;

import br.com.fiap.java.FidelisApi.entity.Clinica;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClinicaRepository extends JpaRepository<Clinica, Long> {

    Page<Clinica> findByNomeContainingIgnoreCase(String nome, Pageable pageable);

    boolean existsByCnpj(String cnpj);

    boolean existsByEmail(String email);
}
