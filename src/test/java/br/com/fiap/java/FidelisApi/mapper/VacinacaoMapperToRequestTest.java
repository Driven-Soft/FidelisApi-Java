package br.com.fiap.java.FidelisApi.mapper;

import br.com.fiap.java.FidelisApi.dto.request.VacinacaoRequest;
import br.com.fiap.java.FidelisApi.entity.Pet;
import br.com.fiap.java.FidelisApi.entity.Vacinacao;
import br.com.fiap.java.FidelisApi.entity.Veterinario;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

class VacinacaoMapperToRequestTest {

    @Test
    void deveConverterVacinacaoEmRequestParaEdicao() {
        Pet pet = Pet.builder().id(5L).nome("Rex").build();
        Veterinario veterinario = Veterinario.builder().id(7L).nome("Dra. Ana").build();
        Vacinacao vacinacao = Vacinacao.builder()
                .id(1L)
                .dataAplicacao(LocalDate.of(2026, 5, 20))
                .dataProxima(LocalDate.of(2027, 5, 20))
                .vacinaAplicada("Antirrábica")
                .observacao("Sem reações")
                .pet(pet)
                .veterinario(veterinario)
                .build();

        VacinacaoRequest request = VacinacaoMapper.toRequest(vacinacao);

        assertThat(request.getDataAplicacao()).isEqualTo(LocalDate.of(2026, 5, 20));
        assertThat(request.getDataProxima()).isEqualTo(LocalDate.of(2027, 5, 20));
        assertThat(request.getVacinaAplicada()).isEqualTo("Antirrábica");
        assertThat(request.getObservacao()).isEqualTo("Sem reações");
        assertThat(request.getPetId()).isEqualTo(5L);
        assertThat(request.getVeterinarioId()).isEqualTo(7L);
    }

    @Test
    void deveAceitarVacinacaoSemPetEVeterinario() {
        VacinacaoRequest request = VacinacaoMapper.toRequest(Vacinacao.builder().vacinaAplicada("Antirrábica").build());

        assertThat(request.getPetId()).isNull();
        assertThat(request.getVeterinarioId()).isNull();
        assertThat(request.getVacinaAplicada()).isEqualTo("Antirrábica");
    }
}