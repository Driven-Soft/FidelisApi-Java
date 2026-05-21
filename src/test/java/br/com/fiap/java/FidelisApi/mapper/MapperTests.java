package br.com.fiap.java.FidelisApi.mapper;

import br.com.fiap.java.FidelisApi.dto.request.ConsultaRequest;
import br.com.fiap.java.FidelisApi.dto.request.TutorRequest;
import br.com.fiap.java.FidelisApi.dto.response.ConsultaResponse;
import br.com.fiap.java.FidelisApi.dto.response.TutorResponse;
import br.com.fiap.java.FidelisApi.entity.Clinica;
import br.com.fiap.java.FidelisApi.entity.Consulta;
import br.com.fiap.java.FidelisApi.entity.Pet;
import br.com.fiap.java.FidelisApi.entity.Tutor;
import br.com.fiap.java.FidelisApi.entity.Veterinario;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

class MapperTests {

    @Test
    void shouldMapTutorRequestToEntityAndResponse() {
        TutorRequest request = new TutorRequest();
        request.setCpf("123.456.789-00");
        request.setNome("Ana Silva");
        request.setEmail("ana.silva@example.com");
        request.setSenha("password123");
        request.setTelefone("(11) 99999-9999");
        request.setEndereco("Rua Central, 100");
        request.setDataCriacao(LocalDate.of(2025, 1, 1));

        Tutor entity = TutorMapper.toEntity(request);
        assertThat(entity).isNotNull();
        assertThat(entity.getCpf()).isEqualTo(request.getCpf());
        assertThat(entity.getNome()).isEqualTo(request.getNome());
        assertThat(entity.getEmail()).isEqualTo(request.getEmail());
        assertThat(entity.getTelefone()).isEqualTo(request.getTelefone());
        assertThat(entity.getEndereco()).isEqualTo(request.getEndereco());
        assertThat(entity.getDataCriacao()).isEqualTo(request.getDataCriacao());
        entity.setId(1L);
        TutorResponse response = TutorMapper.toResponse(entity);

        assertThat(response.getId()).isEqualTo(1L);
        assertThat(response.getCpf()).isEqualTo(request.getCpf());
        assertThat(response.getNome()).isEqualTo(request.getNome());
        assertThat(response.getEmail()).isEqualTo(request.getEmail());
        assertThat(response.getTelefone()).isEqualTo(request.getTelefone());
        assertThat(response.getEndereco()).isEqualTo(request.getEndereco());
        assertThat(response.getDataCriacao()).isEqualTo(request.getDataCriacao());
    }

    @Test
    void shouldMapConsultaEntityToResponse() {
        Clinica clinica = Clinica.builder()
                .id(11L)
                .nome("Clinica Alfa")
                .cnpj("12.345.678/0001-90")
                .telefone("(11) 3333-4444")
                .email("contato@clinicaalfa.com")
                .endereco("Av. Saúde, 45")
                .build();

        Veterinario veterinario = Veterinario.builder()
                .id(22L)
                .nome("Dr. Carlos")
                .email("carlos@veterinario.com")
                .cmvv("CMVV1234567")
                .especialidade("Clínica Geral")
                .dataCriacao(LocalDate.of(2025, 2, 2))
                .clinica(clinica)
                .build();

        Pet pet = Pet.builder()
                .id(33L)
                .nome("Bidu")
                .build();

        Consulta consulta = Consulta.builder()
                .id(44L)
                .dataHora(LocalDateTime.of(2025, 3, 3, 10, 30))
                .tipo("Checkup")
                .diagnostico("Saudável")
                .observacoes("Vacinas em dia")
                .dataRetorno(LocalDate.of(2025, 9, 3))
                .veterinario(veterinario)
                .pet(pet)
                .build();

        ConsultaResponse response = ConsultaMapper.toResponse(consulta);

        assertThat(response.getId()).isEqualTo(44L);
        assertThat(response.getTipo()).isEqualTo("Checkup");
        assertThat(response.getVeterinarioId()).isEqualTo(22L);
        assertThat(response.getVeterinarioNome()).isEqualTo("Dr. Carlos");
        assertThat(response.getPetId()).isEqualTo(33L);
        assertThat(response.getPetNome()).isEqualTo("Bidu");
    }

    @Test
    void shouldMapConsultaRequestToEntity() {
        ConsultaRequest request = new ConsultaRequest();
        request.setDataHora(LocalDateTime.of(2025, 4, 4, 14, 0));
        request.setTipo("Retorno");
        request.setDiagnostico("Melhorando");
        request.setObservacoes("Recomendado repouso");
        request.setDataRetorno(LocalDate.of(2025, 5, 1));

        assertThat(ConsultaMapper.toEntity(request))
                .usingRecursiveComparison()
                .ignoringFields("id", "veterinario", "pet", "exames", "prescricoes")
                .isEqualTo(ConsultaMapper.toEntity(request));
    }
}
