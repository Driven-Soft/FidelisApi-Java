package br.com.fiap.java.FidelisApi.repository;

import br.com.fiap.java.FidelisApi.entity.Vermifugacao;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VermifugacaoRepository extends JpaRepository<Vermifugacao, Long> {

    Page<Vermifugacao> findByProdutoContainingIgnoreCase(String produto, Pageable pageable);
}
