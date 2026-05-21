package br.com.fiap.java.FidelisApi.repository;

import br.com.fiap.java.FidelisApi.entity.Pet;
import br.com.fiap.java.FidelisApi.entity.SexoPet;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PetRepository extends JpaRepository<Pet, Long> {

    Page<Pet> findByNomeContainingIgnoreCase(String nome, Pageable pageable);

    Page<Pet> findByEspecieContainingIgnoreCase(String especie, Pageable pageable);

    Page<Pet> findBySexo(SexoPet sexo, Pageable pageable);

    Page<Pet> findByNomeContainingIgnoreCaseAndEspecieContainingIgnoreCase(String nome, String especie, Pageable pageable);
}
