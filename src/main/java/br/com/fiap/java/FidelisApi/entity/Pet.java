package br.com.fiap.java.FidelisApi.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "FIDELIS_PET")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Pet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 30)
    private String nome;

    @Column(nullable = false, length = 20)
    private String especie;

    @Column(nullable = false, length = 20)
    private String raca;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 1)
    private SexoPet sexo;

    @Column(name = "data_nascimento", nullable = false)
    private LocalDate dataNascimento;

    @Column(length = 20)
    private String status;

    @Column(name = "foto_url", length = 255)
    private String fotoUrl;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "FIDELIS_TUTOR_id", nullable = false)
    @JsonIgnoreProperties("pets")
    private Tutor tutor;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "FIDELIS_CLINICA_id", nullable = false)
    @JsonIgnoreProperties("pets")
    private Clinica clinica;

    @OneToMany(mappedBy = "pet", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnoreProperties("pet")
    @Builder.Default
    private Set<Consulta> consultas = new HashSet<>();

    @OneToMany(mappedBy = "pet", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnoreProperties("pet")
    @Builder.Default
    private Set<Vacinacao> vacinacoes = new HashSet<>();

    @OneToMany(mappedBy = "pet", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnoreProperties("pet")
    @Builder.Default
    private Set<Vermifugacao> vermifugacoes = new HashSet<>();

    @OneToMany(mappedBy = "pet", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnoreProperties("pet")
    @Builder.Default
    private Set<HistoricoPeso> historicoPeso = new HashSet<>();

    @OneToMany(mappedBy = "pet", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnoreProperties("pet")
    @Builder.Default
    private Set<Comportamento> comportamentos = new HashSet<>();

    @OneToMany(mappedBy = "pet", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnoreProperties("pet")
    @Builder.Default
    private Set<Recomendacao> recomendacoes = new HashSet<>();

    @OneToMany(mappedBy = "pet", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnoreProperties("pet")
    @Builder.Default
    private Set<Lembrete> lembretes = new HashSet<>();
}
