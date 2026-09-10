package br.com.fiap.java.FidelisApi.service;

import br.com.fiap.java.FidelisApi.entity.Pet;
import br.com.fiap.java.FidelisApi.entity.Tutor;
import br.com.fiap.java.FidelisApi.repository.ConsultaRepository;
import br.com.fiap.java.FidelisApi.repository.PetRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RetencaoServiceTest {

    @Mock private PetRepository petRepository;
    @Mock private ConsultaRepository consultaRepository;

    @InjectMocks
    private RetencaoService retencaoService;

    @Test
    void devolveListaVaziaQuandoNenhumPetEmRisco() {
        when(petRepository.findIdsPetsEmRiscoDeRetencao(anyLong(), any())).thenReturn(List.of());

        List<RetencaoService.PetEmRisco> resultado = retencaoService.listarPetsEmRisco(1L);

        assertThat(resultado).isEmpty();
    }

    @Test
    void incluiPetSemNenhumaConsulta() {
        Tutor tutor = Tutor.builder().id(1L).nome("João").build();
        Pet pet = Pet.builder().id(10L).nome("Rex").tutor(tutor).build();

        when(petRepository.findIdsPetsEmRiscoDeRetencao(anyLong(), any())).thenReturn(List.of(10L));
        when(petRepository.findByIdInComTutor(List.of(10L))).thenReturn(List.of(pet));
        when(consultaRepository.findUltimaConsultaPorPetIds(List.of(10L))).thenReturn(List.of());

        List<RetencaoService.PetEmRisco> resultado = retencaoService.listarPetsEmRisco(1L);

        assertThat(resultado).hasSize(1);
        assertThat(resultado.get(0).pet()).isEqualTo(pet);
        assertThat(resultado.get(0).ultimaConsulta()).isNull();
        assertThat(resultado.get(0).diasSemConsulta()).isEqualTo(-1);
    }

    @Test
    void incluiPetComConsultaAntiga() {
        Tutor tutor = Tutor.builder().id(1L).nome("Maria").build();
        Pet pet = Pet.builder().id(20L).nome("Bidu").tutor(tutor).build();
        LocalDateTime ultimaConsulta = LocalDateTime.now().minusDays(120);

        when(petRepository.findIdsPetsEmRiscoDeRetencao(anyLong(), any())).thenReturn(List.of(20L));
        when(petRepository.findByIdInComTutor(List.of(20L))).thenReturn(List.of(pet));
        when(consultaRepository.findUltimaConsultaPorPetIds(List.of(20L)))
                .thenReturn(Collections.singletonList(new Object[]{20L, ultimaConsulta}));

        List<RetencaoService.PetEmRisco> resultado = retencaoService.listarPetsEmRisco(1L);

        assertThat(resultado).hasSize(1);
        assertThat(resultado.get(0).ultimaConsulta()).isEqualTo(ultimaConsulta);
        assertThat(resultado.get(0).diasSemConsulta()).isGreaterThanOrEqualTo(120);
    }

    @Test
    void contarPetsEmRiscoDevolveTamanhoDaLista() {
        when(petRepository.findIdsPetsEmRiscoDeRetencao(anyLong(), any())).thenReturn(List.of(1L, 2L, 3L));

        int total = retencaoService.contarPetsEmRisco(1L);

        assertThat(total).isEqualTo(3);
    }
}