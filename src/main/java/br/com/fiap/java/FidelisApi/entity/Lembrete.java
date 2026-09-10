package br.com.fiap.java.FidelisApi.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "FIDELIS_LEMBRETE")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Lembrete {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 50)
    private String tipo;

    @Column(length = 5000)
    private String descricao;

    @Column(name = "data_prevista", nullable = false)
    private LocalDate dataPrevista;

    @Convert(converter = LembreteStatusConverter.class)
    @Column(nullable = false, length = 1)
    private LembreteStatus status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "FIDELIS_TUTOR_id", nullable = false)
    @JsonIgnoreProperties("lembretes")
    private Tutor tutor;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "FIDELIS_PET_id", nullable = false)
    @JsonIgnoreProperties("lembretes")
    private Pet pet;
}
