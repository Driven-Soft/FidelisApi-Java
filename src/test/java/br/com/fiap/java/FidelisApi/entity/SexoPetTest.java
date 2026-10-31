package br.com.fiap.java.FidelisApi.entity;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class SexoPetTest {

    @Test
    void deveExibirSexoPorExtensoPreservandoSiglasInternas() {
        assertThat(SexoPet.M.getRotulo()).isEqualTo("Macho");
        assertThat(SexoPet.F.getRotulo()).isEqualTo("Fêmea");
        assertThat(SexoPet.M.toString()).isEqualTo("Macho");
        assertThat(SexoPet.F.toString()).isEqualTo("Fêmea");
    }

    @Test
    void deveAceitarSiglasETextosPorExtenso() {
        assertThat(SexoPet.fromTexto("M")).isEqualTo(SexoPet.M);
        assertThat(SexoPet.fromTexto("Fêmea")).isEqualTo(SexoPet.F);
    }
}
