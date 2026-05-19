package br.com.fiap.java.FidelisApi.repository;

import br.com.fiap.java.FidelisApi.entity.HistoricoPeso;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HistoricoPesoRepository extends JpaRepository<HistoricoPeso, Long> {

    Page<HistoricoPeso> findByPesoKgBetween(java.math.BigDecimal min, java.math.BigDecimal max, Pageable pageable);
}
