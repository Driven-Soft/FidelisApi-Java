package br.com.fiap.java.FidelisApi.mapper;

import br.com.fiap.java.FidelisApi.dto.request.ConsultaRequest;
import br.com.fiap.java.FidelisApi.entity.Consulta;
import br.com.fiap.java.FidelisApi.entity.Pet;
import br.com.fiap.java.FidelisApi.entity.Veterinario;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

class ConsultaMapperToRequestTest {

    @Test
    void deveConverterConsultaEmRequestParaEdicao() {
        Pet pet = Pet.builder().id(5L).nome("Rex").build();
        Veterinario veterinario = Veterinario.builder().id(7L).nome("Dra. Ana").build();
        Consulta consulta = Consulta.builder()
                .id(1L)
                .dataHora(LocalDateTime.of(2026, 5, 20, 10, 30))
                .tipo("Check-up")
                .diagnostico("Saudável")
                .observacoes("Sem queixas")
                .dataRetorno(LocalDate.of(2026, 6, 20))
                .pet(pet)
                .veterinario(veterinario)
                .build();

        ConsultaRequest request = ConsultaMapper.toRequest(consulta);

        assertThat(request.getDataHora()).isEqualTo(LocalDateTime.of(2026, 5, 20, 10, 30));
        assertThat(request.getTipo()).isEqualTo("Check-up");
        assertThat(request.getDiagnostico()).isEqualTo("Saudável");
        assertThat(request.getObservacoes()).isEqualTo("Sem queixas");
        assertThat(request.getDataRetorno()).isEqualTo(LocalDate.of(2026, 6, 20));
        assertThat(request.getPetId()).isEqualTo(5L);
        assertThat(request.getVeterinarioId()).isEqualTo(7L);
    }

    @Test
    void deveAceitarConsultaSemPetEVeterinario() {
        ConsultaRequest request = ConsultaMapper.toRequest(Consulta.builder().tipo("Retorno").build());

        assertThat(request.getPetId()).isNull();
        assertThat(request.getVeterinarioId()).isNull();
        assertThat(request.getTipo()).isEqualTo("Retorno");
    }
}