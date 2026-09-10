package br.com.fiap.java.FidelisApi.repository;

import java.util.List;
import java.util.Optional;
import br.com.fiap.java.FidelisApi.entity.Lembrete;
import br.com.fiap.java.FidelisApi.entity.LembreteStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface LembreteRepository extends JpaRepository<Lembrete, Long> {

    Page<Lembrete> findByTipoContainingIgnoreCase(String tipo, Pageable pageable);

    Page<Lembrete> findByStatus(LembreteStatus status, Pageable pageable);

    @Query("SELECT l FROM Lembrete l WHERE l.pet.id = :petId ORDER BY l.dataPrevista DESC")
    List<Lembrete> findByPetId(Long petId);

    Optional<Lembrete> findFirstByPetIdOrderByIdDesc(Long petId);
}
