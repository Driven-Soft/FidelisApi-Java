package br.com.fiap.java.FidelisApi.service;

import br.com.fiap.java.FidelisApi.entity.Tutor;
import br.com.fiap.java.FidelisApi.exception.BusinessException;
import br.com.fiap.java.FidelisApi.exception.ResourceNotFoundException;
import br.com.fiap.java.FidelisApi.repository.TutorRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TutorServiceTest {

    @Mock
    private TutorRepository tutorRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private TutorService tutorService;

    private Tutor tutor;
    private Pageable pageable;

    @BeforeEach
    void setUp() {
        tutor = Tutor.builder()
                .id(1L)
                .cpf("123.456.789-09")
                .nome("Maria Souza")
                .email("maria@email.com")
                .senha("senha123")
                .telefone("(11) 98765-4321")
                .endereco("Rua Azul, 50")
                .dataCriacao(LocalDate.of(2025, 1, 15))
                .build();
        pageable = PageRequest.of(0, 10, Sort.by("id"));
    }

    @Test
    void findAll_semFiltro_deveRetornarTodosTutores() {
        Page<Tutor> page = new PageImpl<>(List.of(tutor));
        when(tutorRepository.findAll(pageable)).thenReturn(page);

        Page<Tutor> result = tutorService.findAll(null, null, pageable);

        assertThat(result.getContent()).hasSize(1);
        verify(tutorRepository).findAll(pageable);
    }

    @Test
    void findAll_filtrandoPorNome_deveUsarQueryMethod() {
        Page<Tutor> page = new PageImpl<>(List.of(tutor));
        when(tutorRepository.findByNomeContainingIgnoreCase("Maria", pageable)).thenReturn(page);

        Page<Tutor> result = tutorService.findAll("Maria", null, pageable);

        assertThat(result.getContent().get(0).getNome()).isEqualTo("Maria Souza");
        verify(tutorRepository).findByNomeContainingIgnoreCase("Maria", pageable);
    }

    @Test
    void findAll_filtrandoPorEmail_deveUsarQueryMethod() {
        Page<Tutor> page = new PageImpl<>(List.of(tutor));
        when(tutorRepository.findByEmailContainingIgnoreCase("maria", pageable)).thenReturn(page);

        Page<Tutor> result = tutorService.findAll(null, "maria", pageable);

        assertThat(result.getContent().get(0).getEmail()).isEqualTo("maria@email.com");
        verify(tutorRepository).findByEmailContainingIgnoreCase("maria", pageable);
    }

    @Test
    void findById_existente_deveRetornarTutor() {
        when(tutorRepository.findById(1L)).thenReturn(Optional.of(tutor));

        Tutor result = tutorService.findById(1L);

        assertThat(result.getNome()).isEqualTo("Maria Souza");
    }

    @Test
    void findById_inexistente_deveLancarResourceNotFoundException() {
        when(tutorRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> tutorService.findById(99L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("99");
    }

    @Test
    void create_dadosNovos_deveSalvar() {
        when(tutorRepository.existsByCpf(tutor.getCpf())).thenReturn(false);
        when(tutorRepository.existsByEmail(tutor.getEmail())).thenReturn(false);
        when(passwordEncoder.encode(any())).thenReturn("encodedPassword");
        when(tutorRepository.save(any(Tutor.class))).thenReturn(tutor);

        Tutor result = tutorService.create(tutor);

        assertThat(result).isNotNull();
        verify(passwordEncoder).encode(any());
        verify(tutorRepository).save(tutor);
    }

    @Test
    void create_cpfDuplicado_deveLancarBusinessException() {
        when(tutorRepository.existsByCpf(tutor.getCpf())).thenReturn(true);

        assertThatThrownBy(() -> tutorService.create(tutor))
                .isInstanceOf(BusinessException.class)
                .hasMessageContaining("CPF");

        verify(tutorRepository, never()).save(any());
    }

    @Test
    void create_emailDuplicado_deveLancarBusinessException() {
        when(tutorRepository.existsByCpf(tutor.getCpf())).thenReturn(false);
        when(tutorRepository.existsByEmail(tutor.getEmail())).thenReturn(true);

        assertThatThrownBy(() -> tutorService.create(tutor))
                .isInstanceOf(BusinessException.class)
                .hasMessageContaining("Email");

        verify(tutorRepository, never()).save(any());
    }

    @Test
    void delete_existente_deveRemoverTutor() {
        when(tutorRepository.findById(1L)).thenReturn(Optional.of(tutor));

        tutorService.delete(1L);

        verify(tutorRepository).delete(tutor);
    }

    @Test
    void delete_inexistente_deveLancarResourceNotFoundException() {
        when(tutorRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> tutorService.delete(99L))
                .isInstanceOf(ResourceNotFoundException.class);

        verify(tutorRepository, never()).delete(any());
    }
}
