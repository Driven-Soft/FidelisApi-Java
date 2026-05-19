package br.com.fiap.java.FidelisApi.repository;

import br.com.fiap.java.FidelisApi.entity.Lembrete;
import br.com.fiap.java.FidelisApi.entity.LembreteStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LembreteRepository extends JpaRepository<Lembrete, Long> {

    Page<Lembrete> findByTipoContainingIgnoreCase(String tipo, Pageable pageable);

    Page<Lembrete> findByStatus(LembreteStatus status, Pageable pageable);
}
