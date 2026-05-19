package br.com.fiap.java.FidelisApi.repository;

import br.com.fiap.java.FidelisApi.entity.Consulta;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;

public interface ConsultaRepository extends JpaRepository<Consulta, Long> {

    Page<Consulta> findByTipoContainingIgnoreCase(String tipo, Pageable pageable);

    @Query("select c from Consulta c where (:inicio is null or c.dataHora >= :inicio) and (:fim is null or c.dataHora <= :fim)")
    Page<Consulta> findByDataHoraBetween(@Param("inicio") LocalDateTime inicio,
                                         @Param("fim") LocalDateTime fim,
                                         Pageable pageable);
}
