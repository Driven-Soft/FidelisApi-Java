package br.com.fiap.java.FidelisApi.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "FIDELIS_VACINACAO")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Vacinacao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "data_aplicacao", nullable = false)
    private LocalDate dataAplicacao;

    @Column(name = "data_proxima")
    private LocalDate dataProxima;

    @Column(name = "vacina_aplicada", nullable = false, length = 50)
    private String vacinaAplicada;

    @Column(columnDefinition = "TEXT")
    private String observacao;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "FIDELIS_PET_id", nullable = false)
    @JsonIgnoreProperties("vacinacoes")
    private Pet pet;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "FIDELIS_VETERINARIO_id", nullable = false)
    @JsonIgnoreProperties("vacinacoes")
    private Veterinario veterinario;
}
