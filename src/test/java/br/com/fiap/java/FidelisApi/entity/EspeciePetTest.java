package br.com.fiap.java.FidelisApi.entity;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class EspeciePetTest {

    @Test
    void deveReconhecerEspeciePeloRotuloOuPeloNomeIgnorandoMaiusculas() {
        assertThat(EspeciePet.fromTexto("Cachorro")).contains(EspeciePet.CACHORRO);
        assertThat(EspeciePet.fromTexto("cachorro")).contains(EspeciePet.CACHORRO);
        assertThat(EspeciePet.fromTexto("CACHORRO")).contains(EspeciePet.CACHORRO);
        assertThat(EspeciePet.fromTexto(" gato ")).contains(EspeciePet.GATO);
    }

    @Test
    void deveRejeitarEspecieNaoSuportada() {
        assertThat(EspeciePet.fromTexto("Coelho")).isEmpty();
        assertThat(EspeciePet.fromTexto("")).isEmpty();
        assertThat(EspeciePet.fromTexto(null)).isEmpty();
    }

    @Test
    void deveExporRotuloParaExibicao() {
        assertThat(EspeciePet.CACHORRO.getRotulo()).isEqualTo("Cachorro");
        assertThat(EspeciePet.GATO.getRotulo()).isEqualTo("Gato");
    }
}