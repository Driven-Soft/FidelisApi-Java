package br.com.fiap.java.FidelisApi.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "FIDELIS_RECOMENDACAO")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Recomendacao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 50)
    private String tipo;

    @Column(length = 5000)
    private String descricao;

    @Column(name = "data_recomendacao", nullable = false)
    private LocalDate dataRecomendacao;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "FIDELIS_PET_id", nullable = false)
    @JsonIgnoreProperties("recomendacoes")
    private Pet pet;
}
