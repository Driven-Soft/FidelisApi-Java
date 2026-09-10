package br.com.fiap.java.FidelisApi.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "FIDELIS_MEDICAMENTO")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Medicamento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 50)
    private String nome;

    @Column(length = 5000)
    private String descricao;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "FIDELIS_PRESCRICAO_id", nullable = false)
    @JsonIgnoreProperties("medicamentos")
    private Prescricao prescricao;
}
