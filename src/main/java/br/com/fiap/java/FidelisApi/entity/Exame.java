package br.com.fiap.java.FidelisApi.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "FIDELIS_EXAME")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Exame {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 50)
    private String tipo;

    @Column(length = 5000)
    private String descricao;

    @Column(length = 5000)
    private String resultado;

    @Column(nullable = false)
    private LocalDate data;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "FIDELIS_CONSULTA_id", nullable = false)
    @JsonIgnoreProperties("exames")
    private Consulta consulta;
}
