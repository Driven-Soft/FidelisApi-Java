package br.com.fiap.java.FidelisApi.repository;

import java.util.List;
import java.util.Optional;
import br.com.fiap.java.FidelisApi.entity.Vacinacao;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface VacinacaoRepository extends JpaRepository<Vacinacao, Long> {

    Page<Vacinacao> findByVacinaAplicadaContainingIgnoreCase(String vacinaAplicada, Pageable pageable);

    @Query("SELECT v FROM Vacinacao v JOIN FETCH v.veterinario WHERE v.pet.id = :petId ORDER BY v.dataAplicacao DESC")
    List<Vacinacao> findByPetIdComVeterinario(@Param("petId") Long petId);

    @Query("SELECT v FROM Vacinacao v JOIN FETCH v.pet JOIN FETCH v.veterinario WHERE v.id = :id AND v.pet.clinica.id = :clinicaId")
    Optional<Vacinacao> findByIdAndPetClinicaId(@Param("id") Long id, @Param("clinicaId") Long clinicaId);
}