package br.com.fiap.java.FidelisApi.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "FIDELIS_PRESCRICAO")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Prescricao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 10)
    private String dosagem;

    @Column(nullable = false, length = 50)
    private String frequencia;

    @Column(name = "duracao_dias", nullable = false)
    private Integer duracaoDias;

    @Column(columnDefinition = "TEXT")
    private String observacao;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "FIDELIS_CONSULTA_id", nullable = false)
    @JsonIgnoreProperties("prescricoes")
    private Consulta consulta;

    @OneToMany(mappedBy = "prescricao", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnoreProperties("prescricao")
    @Builder.Default
    private Set<Medicamento> medicamentos = new HashSet<>();
}
