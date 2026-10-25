package br.com.fiap.java.FidelisApi.service;

import br.com.fiap.java.FidelisApi.entity.Vacinacao;
import br.com.fiap.java.FidelisApi.entity.Veterinario;
import br.com.fiap.java.FidelisApi.exception.ResourceNotFoundException;
import br.com.fiap.java.FidelisApi.repository.PetRepository;
import br.com.fiap.java.FidelisApi.repository.VacinacaoRepository;
import br.com.fiap.java.FidelisApi.repository.VeterinarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class VacinacaoServiceUpdateTest {

    @Mock
    private VacinacaoRepository vacinacaoRepository;

    @Mock
    private PetRepository petRepository;

    @Mock
    private VeterinarioRepository veterinarioRepository;

    @InjectMocks
    private VacinacaoService vacinacaoService;

    private Veterinario veterinarioAtual;
    private Vacinacao existente;
    private Vacinacao alteracoes;

    @BeforeEach
    void setUp() {
        veterinarioAtual = Veterinario.builder().id(1L).nome("Dra. Ana").build();
        existente = Vacinacao.builder()
                .id(10L)
                .dataAplicacao(LocalDate.of(2026, 1, 10))
                .vacinaAplicada("Antirrábica")
                .veterinario(veterinarioAtual)
                .build();
        alteracoes = Vacinacao.builder()
                .dataAplicacao(LocalDate.of(2026, 1, 12))
                .dataProxima(LocalDate.of(2027, 1, 12))
                .vacinaAplicada("V10 (polivalente canina)")
                .observacao("Reforço anual")
                .build();
    }

    @Test
    void deveAtualizarCamposETrocarOVeterinario() {
        Veterinario novo = Veterinario.builder().id(2L).nome("Dr. Bruno").build();
        when(vacinacaoRepository.findById(10L)).thenReturn(Optional.of(existente));
        when(veterinarioRepository.findById(2L)).thenReturn(Optional.of(novo));
        when(vacinacaoRepository.save(any(Vacinacao.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Vacinacao resultado = vacinacaoService.update(10L, alteracoes, 2L);

        assertThat(resultado.getVeterinario()).isEqualTo(novo);
        assertThat(resultado.getVacinaAplicada()).isEqualTo("V10 (polivalente canina)");
        assertThat(resultado.getDataAplicacao()).isEqualTo(LocalDate.of(2026, 1, 12));
        assertThat(resultado.getDataProxima()).isEqualTo(LocalDate.of(2027, 1, 12));
        assertThat(resultado.getObservacao()).isEqualTo("Reforço anual");
    }

    @Test
    void deveLancarExcecaoQuandoNovoVeterinarioNaoExiste() {
        when(vacinacaoRepository.findById(10L)).thenReturn(Optional.of(existente));
        when(veterinarioRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> vacinacaoService.update(10L, alteracoes, 99L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("99");

        verify(vacinacaoRepository, never()).save(any());
    }

    @Test
    void atualizarSemVeterinarioDeveManterOVeterinarioAtual() {
        when(vacinacaoRepository.findById(10L)).thenReturn(Optional.of(existente));
        when(vacinacaoRepository.save(any(Vacinacao.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Vacinacao resultado = vacinacaoService.update(10L, alteracoes);

        assertThat(resultado.getVeterinario()).isEqualTo(veterinarioAtual);
        assertThat(resultado.getVacinaAplicada()).isEqualTo("V10 (polivalente canina)");
    }

    @Test
    void deveExcluirVacinacaoExistente() {
        when(vacinacaoRepository.findById(10L)).thenReturn(Optional.of(existente));

        vacinacaoService.delete(10L);

        verify(vacinacaoRepository).delete(existente);
    }

    @Test
    void deveLancarExcecaoAoExcluirVacinacaoInexistente() {
        when(vacinacaoRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> vacinacaoService.delete(99L))
                .isInstanceOf(ResourceNotFoundException.class);

        verify(vacinacaoRepository, never()).delete(any());
    }
}