package br.com.fiap.java.FidelisApi.service;

import br.com.fiap.java.FidelisApi.entity.Consulta;
import br.com.fiap.java.FidelisApi.entity.Clinica;
import br.com.fiap.java.FidelisApi.entity.Lembrete;
import br.com.fiap.java.FidelisApi.entity.LembreteStatus;
import br.com.fiap.java.FidelisApi.entity.Pet;
import br.com.fiap.java.FidelisApi.entity.Recomendacao;
import br.com.fiap.java.FidelisApi.entity.Veterinario;
import br.com.fiap.java.FidelisApi.exception.BusinessException;
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
import java.util.List;
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
    private Clinica clinica;

    @BeforeEach
    void setUp() {
        clinica = Clinica.builder().id(1L).nome("Clínica").build();
        veterinarioAtual = Veterinario.builder().id(1L).nome("Dra. Ana").clinica(clinica).build();
        existente = Consulta.builder()
                .id(10L)
                .dataHora(LocalDateTime.of(2026, 1, 10, 9, 0))
                .tipo("Check-up")
                .diagnostico("Saudável")
                .veterinario(veterinarioAtual)
                .pet(Pet.builder().id(5L).nome("Rex").build())
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
        Veterinario novo = Veterinario.builder().id(2L).nome("Dr. Bruno").clinica(clinica).build();
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
    void deveImpedirAtualizacaoParaHorarioOcupadoNaClinica() {
        when(consultaRepository.findById(10L)).thenReturn(Optional.of(existente));
        when(consultaRepository.existeConflitoDeHorario(1L, alteracoes.getDataHora(), 10L)).thenReturn(true);

        assertThatThrownBy(() -> consultaService.update(10L, alteracoes))
                .isInstanceOf(BusinessException.class)
                .hasMessageContaining("já possui uma consulta");

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
    void deveAtualizarLembretePendenteGeradoPelaConsulta() {
        Lembrete pendente = Lembrete.builder()
                .id(1L)
                .tipo("Retorno")
                .descricao("antiga")
                .dataPrevista(LocalDate.of(2026, 2, 9))
                .status(LembreteStatus.PENDENTE)
                .build();
        when(consultaRepository.findById(10L)).thenReturn(Optional.of(existente));
        when(lembreteRepository.findByConsultaId(10L)).thenReturn(List.of(pendente));
        when(consultaRepository.save(any(Consulta.class))).thenAnswer(invocation -> invocation.getArgument(0));

        consultaService.update(10L, alteracoes);

        assertThat(pendente.getDataPrevista()).isEqualTo(LocalDate.of(2026, 3, 1));
        assertThat(pendente.getDescricao()).contains("Retorno").contains("2026-01-11");
    }

    @Test
    void semDataDeRetornoDeveReagendarLembreteEm30Dias() {
        Lembrete pendente = Lembrete.builder()
                .id(1L)
                .tipo("Retorno")
                .descricao("antiga")
                .dataPrevista(LocalDate.of(2026, 2, 9))
                .status(LembreteStatus.PENDENTE)
                .build();
        Consulta semRetorno = Consulta.builder()
                .dataHora(LocalDateTime.of(2026, 1, 11, 14, 30))
                .tipo("Retorno")
                .build();
        when(consultaRepository.findById(10L)).thenReturn(Optional.of(existente));
        when(lembreteRepository.findByConsultaId(10L)).thenReturn(List.of(pendente));
        when(consultaRepository.save(any(Consulta.class))).thenAnswer(invocation -> invocation.getArgument(0));

        consultaService.update(10L, semRetorno);

        assertThat(pendente.getDataPrevista()).isEqualTo(LocalDate.of(2026, 2, 10));
    }

    @Test
    void naoDeveAlterarLembreteJaConcluido() {
        Lembrete concluido = Lembrete.builder()
                .id(1L)
                .tipo("Retorno")
                .descricao("antiga")
                .dataPrevista(LocalDate.of(2026, 2, 9))
                .status(LembreteStatus.CONCLUIDO)
                .build();
        when(consultaRepository.findById(10L)).thenReturn(Optional.of(existente));
        when(lembreteRepository.findByConsultaId(10L)).thenReturn(List.of(concluido));
        when(consultaRepository.save(any(Consulta.class))).thenAnswer(invocation -> invocation.getArgument(0));

        consultaService.update(10L, alteracoes);

        assertThat(concluido.getDataPrevista()).isEqualTo(LocalDate.of(2026, 2, 9));
        assertThat(concluido.getDescricao()).isEqualTo("antiga");
    }

    @Test
    void deveAtualizarRecomendacaoGeradaPelaConsulta() {
        Recomendacao recomendacao = Recomendacao.builder()
                .id(1L)
                .tipo("Cuidado Pós-Consulta")
                .descricao("antiga")
                .dataRecomendacao(LocalDate.of(2026, 1, 10))
                .build();
        when(consultaRepository.findById(10L)).thenReturn(Optional.of(existente));
        when(recomendacaoRepository.findByConsultaId(10L)).thenReturn(List.of(recomendacao));
        when(consultaRepository.save(any(Consulta.class))).thenAnswer(invocation -> invocation.getArgument(0));

        consultaService.update(10L, alteracoes);

        assertThat(recomendacao.getDescricao())
                .contains("Rex")
                .contains("Retorno")
                .contains("Sem queixas");
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