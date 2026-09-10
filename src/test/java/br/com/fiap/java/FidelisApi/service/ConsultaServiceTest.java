package br.com.fiap.java.FidelisApi.service;

import br.com.fiap.java.FidelisApi.entity.*;
import br.com.fiap.java.FidelisApi.repository.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ConsultaServiceTest {

    @Mock private ConsultaRepository consultaRepository;
    @Mock private VeterinarioRepository veterinarioRepository;
    @Mock private PetRepository petRepository;
    @Mock private LembreteRepository lembreteRepository;
    @Mock private RecomendacaoRepository recomendacaoRepository;

    @InjectMocks
    private ConsultaService consultaService;

    private Tutor tutor;
    private Pet pet;
    private Veterinario veterinario;

    @BeforeEach
    void setUp() {
        tutor = Tutor.builder().id(1L).nome("João Silva").build();
        pet = Pet.builder().id(1L).nome("Rex").tutor(tutor).build();
        veterinario = Veterinario.builder().id(1L).nome("Dra. Ana").build();
    }

    @Test
    void deveGerarLembreteComDataRetornoInformada() {
        Consulta consulta = Consulta.builder()
                .dataHora(LocalDateTime.of(2026, 1, 10, 10, 0))
                .tipo("Checkup")
                .dataRetorno(LocalDate.of(2026, 2, 1))
                .build();

        when(veterinarioRepository.findById(1L)).thenReturn(Optional.of(veterinario));
        when(petRepository.findById(1L)).thenReturn(Optional.of(pet));
        when(consultaRepository.save(any(Consulta.class))).thenAnswer(inv -> inv.getArgument(0));

        consultaService.create(consulta, 1L, 1L);

        ArgumentCaptor<Lembrete> captor = ArgumentCaptor.forClass(Lembrete.class);
        verify(lembreteRepository).save(captor.capture());

        Lembrete lembrete = captor.getValue();
        assertThat(lembrete.getDataPrevista()).isEqualTo(LocalDate.of(2026, 2, 1));
        assertThat(lembrete.getStatus()).isEqualTo(LembreteStatus.PENDENTE);
        assertThat(lembrete.getTutor()).isEqualTo(tutor);
        assertThat(lembrete.getPet()).isEqualTo(pet);
    }

    @Test
    void deveGerarLembreteComData30DiasQuandoNaoInformada() {
        Consulta consulta = Consulta.builder()
                .dataHora(LocalDateTime.of(2026, 1, 10, 10, 0))
                .tipo("Checkup")
                .dataRetorno(null)
                .build();

        when(veterinarioRepository.findById(1L)).thenReturn(Optional.of(veterinario));
        when(petRepository.findById(1L)).thenReturn(Optional.of(pet));
        when(consultaRepository.save(any(Consulta.class))).thenAnswer(inv -> inv.getArgument(0));

        consultaService.create(consulta, 1L, 1L);

        ArgumentCaptor<Lembrete> captor = ArgumentCaptor.forClass(Lembrete.class);
        verify(lembreteRepository).save(captor.capture());

        assertThat(captor.getValue().getDataPrevista()).isEqualTo(LocalDate.of(2026, 2, 9)); // 10/jan + 30 dias
    }

    @Test
    void deveGerarUmaRecomendacaoDeCuidado() {
        Consulta consulta = Consulta.builder()
                .dataHora(LocalDateTime.of(2026, 1, 10, 10, 0))
                .tipo("Checkup")
                .observacoes("Pet apresentou leve mancar na pata traseira.")
                .build();

        when(veterinarioRepository.findById(1L)).thenReturn(Optional.of(veterinario));
        when(petRepository.findById(1L)).thenReturn(Optional.of(pet));
        when(consultaRepository.save(any(Consulta.class))).thenAnswer(inv -> inv.getArgument(0));

        consultaService.create(consulta, 1L, 1L);

        ArgumentCaptor<Recomendacao> captor = ArgumentCaptor.forClass(Recomendacao.class);
        verify(recomendacaoRepository).save(captor.capture());

        Recomendacao recomendacao = captor.getValue();
        assertThat(recomendacao.getTipo()).isEqualTo("Cuidado Pós-Consulta");
        assertThat(recomendacao.getDescricao()).contains("mancar na pata traseira");
        assertThat(recomendacao.getPet()).isEqualTo(pet);
    }
}