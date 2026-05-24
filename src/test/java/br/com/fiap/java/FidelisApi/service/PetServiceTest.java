package br.com.fiap.java.FidelisApi.service;

import br.com.fiap.java.FidelisApi.entity.*;
import br.com.fiap.java.FidelisApi.exception.ResourceNotFoundException;
import br.com.fiap.java.FidelisApi.repository.ClinicaRepository;
import br.com.fiap.java.FidelisApi.repository.PetRepository;
import br.com.fiap.java.FidelisApi.repository.TutorRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PetServiceTest {

    @Mock
    private PetRepository petRepository;

    @Mock
    private TutorRepository tutorRepository;

    @Mock
    private ClinicaRepository clinicaRepository;

    @InjectMocks
    private PetService petService;

    private Pet pet;
    private Tutor tutor;
    private Clinica clinica;
    private Pageable pageable;

    @BeforeEach
    void setUp() {
        tutor = Tutor.builder().id(1L).nome("João Silva").build();
        clinica = Clinica.builder().id(1L).nome("Clínica Vet").build();
        pet = Pet.builder()
                .id(1L)
                .nome("Rex")
                .especie("Cachorro")
                .raca("Labrador")
                .sexo(SexoPet.M)
                .dataNascimento(LocalDate.of(2020, 1, 1))
                .status(PetStatus.ATIVO)
                .tutor(tutor)
                .clinica(clinica)
                .build();
        pageable = PageRequest.of(0, 10, Sort.by("id"));
    }

    @Test
    void findAll_semFiltro_deveRetornarTodosPets() {
        Page<Pet> page = new PageImpl<>(List.of(pet));
        when(petRepository.findAll(pageable)).thenReturn(page);

        Page<Pet> result = petService.findAll(null, null, pageable);

        assertThat(result.getContent()).hasSize(1);
        assertThat(result.getContent().get(0).getNome()).isEqualTo("Rex");
        verify(petRepository).findAll(pageable);
    }

    @Test
    void findAll_filtrandoPorNome_deveUsarQueryMethod() {
        Page<Pet> page = new PageImpl<>(List.of(pet));
        when(petRepository.findByNomeContainingIgnoreCase("Rex", pageable)).thenReturn(page);

        Page<Pet> result = petService.findAll("Rex", null, pageable);

        assertThat(result.getContent().get(0).getNome()).isEqualTo("Rex");
        verify(petRepository).findByNomeContainingIgnoreCase("Rex", pageable);
    }

    @Test
    void findAll_filtrandoPorEspecie_deveUsarQueryMethod() {
        Page<Pet> page = new PageImpl<>(List.of(pet));
        when(petRepository.findByEspecieContainingIgnoreCase("Cachorro", pageable)).thenReturn(page);

        Page<Pet> result = petService.findAll(null, "Cachorro", pageable);

        assertThat(result.getContent().get(0).getEspecie()).isEqualTo("Cachorro");
        verify(petRepository).findByEspecieContainingIgnoreCase("Cachorro", pageable);
    }

    @Test
    void findAll_filtrandoPorNomeEEspecie_deveUsarQueryMethodCombinado() {
        Page<Pet> page = new PageImpl<>(List.of(pet));
        when(petRepository.findByNomeContainingIgnoreCaseAndEspecieContainingIgnoreCase("Rex", "Cachorro", pageable))
                .thenReturn(page);

        Page<Pet> result = petService.findAll("Rex", "Cachorro", pageable);

        assertThat(result.getContent()).hasSize(1);
        verify(petRepository).findByNomeContainingIgnoreCaseAndEspecieContainingIgnoreCase("Rex", "Cachorro", pageable);
    }

    @Test
    void findById_existente_deveRetornarPet() {
        when(petRepository.findById(1L)).thenReturn(Optional.of(pet));

        Pet result = petService.findById(1L);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getNome()).isEqualTo("Rex");
    }

    @Test
    void findById_inexistente_deveLancarResourceNotFoundException() {
        when(petRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> petService.findById(99L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("99");
    }

    @Test
    void create_comTutorEClinicaValidos_deveSalvarPet() {
        when(tutorRepository.findById(1L)).thenReturn(Optional.of(tutor));
        when(clinicaRepository.findById(1L)).thenReturn(Optional.of(clinica));
        when(petRepository.save(any(Pet.class))).thenReturn(pet);

        Pet newPet = Pet.builder()
                .nome("Rex")
                .especie("Cachorro")
                .raca("Labrador")
                .sexo(SexoPet.M)
                .dataNascimento(LocalDate.of(2020, 1, 1))
                .status(PetStatus.ATIVO)
                .build();

        Pet result = petService.create(newPet, 1L, 1L);

        assertThat(result).isNotNull();
        assertThat(result.getTutor()).isEqualTo(tutor);
        assertThat(result.getClinica()).isEqualTo(clinica);
        verify(petRepository).save(newPet);
    }

    @Test
    void create_comTutorInexistente_deveLancarResourceNotFoundException() {
        when(tutorRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> petService.create(pet, 99L, 1L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("99");

        verify(petRepository, never()).save(any());
    }

    @Test
    void create_comClinicaInexistente_deveLancarResourceNotFoundException() {
        when(tutorRepository.findById(1L)).thenReturn(Optional.of(tutor));
        when(clinicaRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> petService.create(pet, 1L, 99L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("99");

        verify(petRepository, never()).save(any());
    }

    @Test
    void update_existente_deveAtualizarCampos() {
        Pet updated = Pet.builder()
                .nome("Max")
                .especie("Gato")
                .raca("Siamês")
                .sexo(SexoPet.F)
                .dataNascimento(LocalDate.of(2021, 6, 15))
                .status(PetStatus.ATIVO)
                .build();

        when(petRepository.findById(1L)).thenReturn(Optional.of(pet));
        when(petRepository.save(any(Pet.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Pet result = petService.update(1L, updated);

        assertThat(result.getNome()).isEqualTo("Max");
        assertThat(result.getEspecie()).isEqualTo("Gato");
        verify(petRepository).save(pet);
    }

    @Test
    void update_inexistente_deveLancarResourceNotFoundException() {
        when(petRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> petService.update(99L, pet))
                .isInstanceOf(ResourceNotFoundException.class);

        verify(petRepository, never()).save(any());
    }

    @Test
    void delete_existente_deveRemoverPet() {
        when(petRepository.findById(1L)).thenReturn(Optional.of(pet));

        petService.delete(1L);

        verify(petRepository).delete(pet);
    }

    @Test
    void delete_inexistente_deveLancarResourceNotFoundException() {
        when(petRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> petService.delete(99L))
                .isInstanceOf(ResourceNotFoundException.class);

        verify(petRepository, never()).delete(any());
    }
}
