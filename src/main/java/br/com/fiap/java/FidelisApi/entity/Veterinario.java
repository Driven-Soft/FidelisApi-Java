package br.com.fiap.java.FidelisApi.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "FIDELIS_VETERINARIO", uniqueConstraints = {
        @UniqueConstraint(columnNames = "cmvv"),
        @UniqueConstraint(columnNames = "email")
})
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Veterinario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 13, unique = true)
    private String cmvv;

    @Column(nullable = false, length = 75)
    private String nome;

    @Column(nullable = false, length = 75, unique = true)
    private String email;

    @Column(nullable = false, length = 100)
    private String senha;

    @Column(nullable = false, length = 50)
    private String especialidade;

    @Column(name = "data_criacao", nullable = false)
    private LocalDate dataCriacao;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "FIDELIS_CLINICA_id", nullable = false)
    @JsonIgnoreProperties("veterinarios")
    private Clinica clinica;

    @OneToMany(mappedBy = "veterinario", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnoreProperties("veterinario")
    @Builder.Default
    private Set<Consulta> consultas = new HashSet<>();

    @OneToMany(mappedBy = "veterinario", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnoreProperties("veterinario")
    @Builder.Default
    private Set<Vacinacao> vacinacoes = new HashSet<>();

    @OneToMany(mappedBy = "veterinario", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnoreProperties("veterinario")
    @Builder.Default
    private Set<Vermifugacao> vermifugacoes = new HashSet<>();
}
