package br.com.fiap.java.FidelisApi.service;

import br.com.fiap.java.FidelisApi.entity.Consulta;
import br.com.fiap.java.FidelisApi.entity.EspeciePet;
import br.com.fiap.java.FidelisApi.entity.Pet;
import br.com.fiap.java.FidelisApi.entity.PetStatus;
import br.com.fiap.java.FidelisApi.entity.SexoPet;
import br.com.fiap.java.FidelisApi.entity.Vacinacao;
import br.com.fiap.java.FidelisApi.entity.Veterinario;
import br.com.fiap.java.FidelisApi.repository.ClinicaRepository;
import br.com.fiap.java.FidelisApi.repository.ConsultaRepository;
import br.com.fiap.java.FidelisApi.repository.LembreteRepository;
import br.com.fiap.java.FidelisApi.repository.PetRepository;
import br.com.fiap.java.FidelisApi.repository.RecomendacaoRepository;
import br.com.fiap.java.FidelisApi.repository.TutorRepository;
import br.com.fiap.java.FidelisApi.repository.VacinacaoRepository;
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

@SpringBootTest
@Transactional
class PetExclusaoIntegrationTest {

    @Autowired
    private PetService petService;

    @Autowired
    private ConsultaService consultaService;

    @Autowired
    private VacinacaoService vacinacaoService;

    @Autowired
    private PetRepository petRepository;

    @Autowired
    private ClinicaRepository clinicaRepository;

    @Autowired
    private TutorRepository tutorRepository;

    @Autowired
    private VeterinarioRepository veterinarioRepository;

    @Autowired
    private ConsultaRepository consultaRepository;

    @Autowired
    private LembreteRepository lembreteRepository;

    @Autowired
    private RecomendacaoRepository recomendacaoRepository;

    @Autowired
    private VacinacaoRepository vacinacaoRepository;

    @Autowired
    private EntityManager entityManager;

    private Pet pet;
    private Veterinario veterinario;

    @BeforeEach
    void setUp() {
        veterinario = veterinarioRepository.findAll().get(0);
        pet = petRepository.save(Pet.builder()
                .nome("Teste Exclusao")
                .especie(EspeciePet.GATO)
                .raca("SRD (sem raça definida)")
                .sexo(SexoPet.F)
                .dataNascimento(LocalDate.now().minusYears(1))
                .status(PetStatus.ATIVO)
                .tutor(tutorRepository.findAll().get(0))
                .clinica(clinicaRepository.findAll().get(0))
                .build());
    }

    @Test
    void deveExcluirPetComTodoOHistoricoSemViolarIntegridade() {
        Consulta consulta = consultaService.create(
                Consulta.builder().dataHora(LocalDateTime.now()).tipo("Check-up").build(),
                veterinario.getId(), pet.getId());
        vacinacaoService.create(
                Vacinacao.builder().dataAplicacao(LocalDate.now()).vacinaAplicada("Antirrábica").build(),
                pet.getId(), veterinario.getId());
        entityManager.flush();
        entityManager.clear();

        petService.delete(pet.getId());
        entityManager.flush();
        entityManager.clear();

        assertThat(petRepository.findById(pet.getId())).isEmpty();
        assertThat(consultaRepository.findById(consulta.getId())).isEmpty();
        assertThat(lembreteRepository.findByPetId(pet.getId())).isEmpty();
        assertThat(recomendacaoRepository.findByConsultaId(consulta.getId())).isEmpty();
        assertThat(vacinacaoRepository.findByPetIdComVeterinario(pet.getId())).isEmpty();
    }
}