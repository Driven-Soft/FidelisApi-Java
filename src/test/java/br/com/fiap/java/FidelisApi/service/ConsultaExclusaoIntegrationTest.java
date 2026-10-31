package br.com.fiap.java.FidelisApi.service;

import br.com.fiap.java.FidelisApi.entity.Clinica;
import br.com.fiap.java.FidelisApi.entity.Consulta;
import br.com.fiap.java.FidelisApi.entity.EspeciePet;
import br.com.fiap.java.FidelisApi.entity.Pet;
import br.com.fiap.java.FidelisApi.entity.PetStatus;
import br.com.fiap.java.FidelisApi.entity.SexoPet;
import br.com.fiap.java.FidelisApi.entity.Tutor;
import br.com.fiap.java.FidelisApi.entity.Veterinario;
import br.com.fiap.java.FidelisApi.repository.ClinicaRepository;
import br.com.fiap.java.FidelisApi.repository.LembreteRepository;
import br.com.fiap.java.FidelisApi.repository.PetRepository;
import br.com.fiap.java.FidelisApi.repository.RecomendacaoRepository;
import br.com.fiap.java.FidelisApi.repository.TutorRepository;
import br.com.fiap.java.FidelisApi.repository.VeterinarioRepository;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(properties = "fidelis.analise.ia.enabled=false")
@Transactional
class ConsultaExclusaoIntegrationTest {

    @Autowired
    private ConsultaService consultaService;

    @Autowired
    private PetRepository petRepository;

    @Autowired
    private ClinicaRepository clinicaRepository;

    @Autowired
    private TutorRepository tutorRepository;

    @Autowired
    private VeterinarioRepository veterinarioRepository;

    @Autowired
    private LembreteRepository lembreteRepository;

    @Autowired
    private RecomendacaoRepository recomendacaoRepository;

    @Autowired
    private EntityManager entityManager;

    private Pet pet;
    private Veterinario veterinario;

    @BeforeEach
    void setUp() {
        Clinica clinica = clinicaRepository.findAll().get(0);
        Tutor tutor = tutorRepository.findAll().get(0);
        veterinario = veterinarioRepository.findAll().get(0);

        pet = petRepository.save(Pet.builder()
                .nome("Teste Cascata")
                .especie(EspeciePet.CACHORRO)
                .raca("SRD (sem raça definida)")
                .sexo(SexoPet.M)
                .dataNascimento(LocalDate.now().minusYears(2))
                .status(PetStatus.ATIVO)
                .tutor(tutor)
                .clinica(clinica)
                .build());
    }

    @Test
    void deveGerarLembreteERecomendacaoLigadosAConsulta() {
        Consulta consulta = registrarConsulta("Check-up");
        entityManager.flush();
        entityManager.clear();

        assertThat(lembreteRepository.findByConsultaId(consulta.getId())).hasSize(1);
        assertThat(recomendacaoRepository.findByConsultaId(consulta.getId())).hasSize(1);
    }

    @Test
    void deveExcluirLembreteERecomendacaoJuntoComAConsulta() {
        Consulta primeira = registrarConsulta("Check-up");
        Consulta segunda = registrarConsulta("Retorno");
        entityManager.flush();
        entityManager.clear();

        consultaService.delete(primeira.getId());
        entityManager.flush();
        entityManager.clear();

        assertThat(lembreteRepository.findByConsultaId(primeira.getId())).isEmpty();
        assertThat(recomendacaoRepository.findByConsultaId(primeira.getId())).isEmpty();
        assertThat(lembreteRepository.findByConsultaId(segunda.getId())).hasSize(1);
        assertThat(recomendacaoRepository.findByConsultaId(segunda.getId())).hasSize(1);
    }

    private Consulta registrarConsulta(String tipo) {
        Consulta consulta = Consulta.builder()
                .dataHora(LocalDateTime.now())
                .tipo(tipo)
                .build();
        return consultaService.create(consulta, veterinario.getId(), pet.getId());
    }
}