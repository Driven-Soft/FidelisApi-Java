package br.com.fiap.java.FidelisApi.repository;

import br.com.fiap.java.FidelisApi.entity.Pet;
import br.com.fiap.java.FidelisApi.entity.SexoPet;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface PetRepository extends JpaRepository<Pet, Long> {

    Page<Pet> findByNomeContainingIgnoreCase(String nome, Pageable pageable);

    Page<Pet> findByEspecieContainingIgnoreCase(String especie, Pageable pageable);

    Page<Pet> findBySexo(SexoPet sexo, Pageable pageable);

    Page<Pet> findByNomeContainingIgnoreCaseAndEspecieContainingIgnoreCase(String nome, String especie, Pageable pageable);

    @Query("SELECT p FROM Pet p JOIN FETCH p.tutor WHERE p.clinica.id = :clinicaId")
    Page<Pet> findByClinicaIdComTutor(@Param("clinicaId") Long clinicaId, Pageable pageable);

    @Query("SELECT p FROM Pet p JOIN FETCH p.clinica WHERE p.tutor.id = :tutorId")
    List<Pet> findByTutorIdComClinica(@Param("tutorId") Long tutorId);

    @Query("SELECT p FROM Pet p WHERE p.id = :id AND p.tutor.id = :tutorId")
    Optional<Pet> findByIdAndTutorId(@Param("id") Long id, @Param("tutorId") Long tutorId);

    @Query("SELECT p.id FROM Pet p LEFT JOIN Consulta c ON c.pet = p " +
           "WHERE p.clinica.id = :clinicaId GROUP BY p.id " +
           "HAVING MAX(c.dataHora) < :limite OR MAX(c.dataHora) IS NULL")
    List<Long> findIdsPetsEmRiscoDeRetencao(@Param("clinicaId") Long clinicaId, @Param("limite") LocalDateTime limite);

    @Query("SELECT p FROM Pet p JOIN FETCH p.tutor WHERE p.id IN :ids")
    List<Pet> findByIdInComTutor(@Param("ids") List<Long> ids);
}