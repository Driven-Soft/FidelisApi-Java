package br.com.fiap.java.FidelisApi.repository;

import br.com.fiap.java.FidelisApi.entity.Comportamento;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ComportamentoRepository extends JpaRepository<Comportamento, Long> {

    Page<Comportamento> findByDescricaoContainingIgnoreCase(String descricao, Pageable pageable);
}
