package br.com.fiap.java.FidelisApi.repository;

import br.com.fiap.java.FidelisApi.entity.Consulta;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface ConsultaRepository extends JpaRepository<Consulta, Long> {

    @Query("SELECT CASE WHEN COUNT(c) > 0 THEN true ELSE false END FROM Consulta c " +
           "WHERE c.veterinario.clinica.id = :clinicaId AND c.dataHora = :dataHora " +
           "AND (:consultaId IS NULL OR c.id <> :consultaId)")
    boolean existeConflitoDeHorario(@Param("clinicaId") Long clinicaId,
                                    @Param("dataHora") LocalDateTime dataHora,
                                    @Param("consultaId") Long consultaId);

    Page<Consulta> findByTipoContainingIgnoreCase(String tipo, Pageable pageable);

    @Query("select c from Consulta c where (:inicio is null or c.dataHora >= :inicio) and (:fim is null or c.dataHora <= :fim)")
    Page<Consulta> findByDataHoraBetween(@Param("inicio") LocalDateTime inicio,
                                         @Param("fim") LocalDateTime fim,
                                         Pageable pageable);

    @Query("SELECT c FROM Consulta c JOIN FETCH c.veterinario WHERE c.pet.id = :petId ORDER BY c.dataHora DESC")
    List<Consulta> findByPetIdComVeterinario(@Param("petId") Long petId);

    @Query("SELECT c FROM Consulta c JOIN FETCH c.pet JOIN FETCH c.veterinario WHERE c.id = :id")
    Optional<Consulta> findByIdComPetEVeterinario(@Param("id") Long id);

    @Query("SELECT c FROM Consulta c JOIN FETCH c.pet JOIN FETCH c.veterinario WHERE c.id = :id AND c.pet.clinica.id = :clinicaId")
    Optional<Consulta> findByIdAndPetClinicaId(@Param("id") Long id, @Param("clinicaId") Long clinicaId);

    @Query("SELECT c.pet.id, MAX(c.dataHora) FROM Consulta c WHERE c.pet.id IN :ids GROUP BY c.pet.id")
    List<Object[]> findUltimaConsultaPorPetIds(@Param("ids") List<Long> ids);
}