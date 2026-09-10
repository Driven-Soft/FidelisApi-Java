package br.com.fiap.java.FidelisApi.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "FIDELIS_HISTORICO_PESO")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HistoricoPeso {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "peso_kg", nullable = false, precision = 7, scale = 2)
    private BigDecimal pesoKg;

    @Column(name = "data_medicao", nullable = false)
    private LocalDate dataMedicao;

    @Column(length = 5000)
    private String observacao;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "FIDELIS_PET_id", nullable = false)
    @JsonIgnoreProperties("historicoPeso")
    private Pet pet;
}
