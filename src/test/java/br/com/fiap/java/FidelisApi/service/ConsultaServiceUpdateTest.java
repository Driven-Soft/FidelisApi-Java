package br.com.fiap.java.FidelisApi.service;

import br.com.fiap.java.FidelisApi.entity.Consulta;
import br.com.fiap.java.FidelisApi.entity.Veterinario;
import br.com.fiap.java.FidelisApi.exception.ResourceNotFoundException;
import br.com.fiap.java.FidelisApi.repository.ConsultaRepository;
import br.com.fiap.java.FidelisApi.repository.LembreteRepository;
import br.com.fiap.java.FidelisApi.repository.PetRepository;
import br.com.fiap.java.FidelisApi.repository.RecomendacaoRepository;
import br.com.fiap.java.FidelisApi.repository.VeterinarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ConsultaServiceUpdateTest {

    @Mock
    private ConsultaRepository consultaRepository;

    @Mock
    private VeterinarioRepository veterinarioRepository;

    @Mock
    private PetRepository petRepository;

    @Mock
    private LembreteRepository lembreteRepository;

    @Mock
    private RecomendacaoRepository recomendacaoRepository;

    @InjectMocks
    private ConsultaService consultaService;

    private Veterinario veterinarioAtual;
    private Consulta existente;
    private Consulta alteracoes;

    @BeforeEach
    void setUp() {
        veterinarioAtual = Veterinario.builder().id(1L).nome("Dra. Ana").build();
        existente = Consulta.builder()
                .id(10L)
                .dataHora(LocalDateTime.of(2026, 1, 10, 9, 0))
                .tipo("Check-up")
                .diagnostico("Saudável")
                .veterinario(veterinarioAtual)
                .build();
        alteracoes = Consulta.builder()
                .dataHora(LocalDateTime.of(2026, 1, 11, 14, 30))
                .tipo("Retorno")
                .diagnostico("Recuperado")
                .observacoes("Sem queixas")
                .dataRetorno(LocalDate.of(2026, 3, 1))
                .build();
    }

    @Test
    void deveAtualizarCamposETrocarOVeterinario() {
        Veterinario novo = Veterinario.builder().id(2L).nome("Dr. Bruno").build();
        when(consultaRepository.findById(10L)).thenReturn(Optional.of(existente));
        when(veterinarioRepository.findById(2L)).thenReturn(Optional.of(novo));
        when(consultaRepository.save(any(Consulta.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Consulta resultado = consultaService.update(10L, alteracoes, 2L);

        assertThat(resultado.getVeterinario()).isEqualTo(novo);
        assertThat(resultado.getTipo()).isEqualTo("Retorno");
        assertThat(resultado.getDiagnostico()).isEqualTo("Recuperado");
        assertThat(resultado.getObservacoes()).isEqualTo("Sem queixas");
        assertThat(resultado.getDataHora()).isEqualTo(LocalDateTime.of(2026, 1, 11, 14, 30));
        assertThat(resultado.getDataRetorno()).isEqualTo(LocalDate.of(2026, 3, 1));
    }

    @Test
    void deveLancarExcecaoQuandoNovoVeterinarioNaoExiste() {
        when(consultaRepository.findById(10L)).thenReturn(Optional.of(existente));
        when(veterinarioRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> consultaService.update(10L, alteracoes, 99L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("99");

        verify(consultaRepository, never()).save(any());
    }

    @Test
    void atualizarSemVeterinarioDeveManterOVeterinarioAtual() {
        when(consultaRepository.findById(10L)).thenReturn(Optional.of(existente));
        when(consultaRepository.save(any(Consulta.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Consulta resultado = consultaService.update(10L, alteracoes);

        assertThat(resultado.getVeterinario()).isEqualTo(veterinarioAtual);
        assertThat(resultado.getTipo()).isEqualTo("Retorno");
    }

    @Test
    void deveExcluirConsultaExistente() {
        when(consultaRepository.findById(10L)).thenReturn(Optional.of(existente));

        consultaService.delete(10L);

        verify(consultaRepository).delete(existente);
    }

    @Test
    void deveLancarExcecaoAoExcluirConsultaInexistente() {
        when(consultaRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> consultaService.delete(99L))
                .isInstanceOf(ResourceNotFoundException.class);

        verify(consultaRepository, never()).delete(any());
    }
}