package br.com.fiap.java.FidelisApi.repository;

import br.com.fiap.java.FidelisApi.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    Optional<Usuario> findByEmailIgnoreCase(String email);

    @Query("SELECT u FROM Usuario u LEFT JOIN FETCH u.tutor LEFT JOIN FETCH u.clinica WHERE u.email = :email")
    Optional<Usuario> findByEmailComVinculo(String email);
}