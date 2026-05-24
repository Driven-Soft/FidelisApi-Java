package br.com.fiap.java.FidelisApi.service;

import br.com.fiap.java.FidelisApi.entity.Clinica;
import br.com.fiap.java.FidelisApi.exception.BusinessException;
import br.com.fiap.java.FidelisApi.exception.ResourceNotFoundException;
import br.com.fiap.java.FidelisApi.repository.ClinicaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ClinicaServiceTest {

    @Mock
    private ClinicaRepository clinicaRepository;

    @InjectMocks
    private ClinicaService clinicaService;

    private Clinica clinica;
    private Pageable pageable;

    @BeforeEach
    void setUp() {
        clinica = Clinica.builder()
                .id(1L)
                .nome("Clínica Vet Total")
                .cnpj("12.345.678/0001-90")
                .telefone("(11) 3333-4444")
                .email("contato@vetotal.com.br")
                .endereco("Rua das Flores, 100")
                .build();
        pageable = PageRequest.of(0, 10, Sort.by("id"));
    }

    @Test
    void findAll_semFiltro_deveRetornarTodasClinicas() {
        Page<Clinica> page = new PageImpl<>(List.of(clinica));
        when(clinicaRepository.findAll(pageable)).thenReturn(page);

        Page<Clinica> result = clinicaService.findAll(null, pageable);

        assertThat(result.getContent()).hasSize(1);
        verify(clinicaRepository).findAll(pageable);
    }

    @Test
    void findAll_filtrandoPorNome_deveUsarQueryMethod() {
        Page<Clinica> page = new PageImpl<>(List.of(clinica));
        when(clinicaRepository.findByNomeContainingIgnoreCase("Vet", pageable)).thenReturn(page);

        Page<Clinica> result = clinicaService.findAll("Vet", pageable);

        assertThat(result.getContent()).hasSize(1);
        verify(clinicaRepository).findByNomeContainingIgnoreCase("Vet", pageable);
    }

    @Test
    void findById_existente_deveRetornarClinica() {
        when(clinicaRepository.findById(1L)).thenReturn(Optional.of(clinica));

        Clinica result = clinicaService.findById(1L);

        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getNome()).isEqualTo("Clínica Vet Total");
    }

    @Test
    void findById_inexistente_deveLancarResourceNotFoundException() {
        when(clinicaRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> clinicaService.findById(99L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("99");
    }

    @Test
    void create_cnpjNovo_deveSalvar() {
        when(clinicaRepository.existsByCnpj(clinica.getCnpj())).thenReturn(false);
        when(clinicaRepository.existsByEmail(clinica.getEmail())).thenReturn(false);
        when(clinicaRepository.save(clinica)).thenReturn(clinica);

        Clinica result = clinicaService.create(clinica);

        assertThat(result).isNotNull();
        assertThat(result.getCnpj()).isEqualTo("12.345.678/0001-90");
        verify(clinicaRepository).save(clinica);
    }

    @Test
    void create_cnpjDuplicado_deveLancarBusinessException() {
        when(clinicaRepository.existsByCnpj(clinica.getCnpj())).thenReturn(true);

        assertThatThrownBy(() -> clinicaService.create(clinica))
                .isInstanceOf(BusinessException.class)
                .hasMessageContaining("CNPJ");

        verify(clinicaRepository, never()).save(any());
    }

    @Test
    void create_emailDuplicado_deveLancarBusinessException() {
        when(clinicaRepository.existsByCnpj(clinica.getCnpj())).thenReturn(false);
        when(clinicaRepository.existsByEmail(clinica.getEmail())).thenReturn(true);

        assertThatThrownBy(() -> clinicaService.create(clinica))
                .isInstanceOf(BusinessException.class)
                .hasMessageContaining("Email");

        verify(clinicaRepository, never()).save(any());
    }

    @Test
    void update_dadosValidos_deveAtualizarClinica() {
        Clinica updated = Clinica.builder()
                .nome("Clínica Vet Nova")
                .cnpj("12.345.678/0001-90")
                .telefone("(11) 9999-8888")
                .email("contato@vetotal.com.br")
                .endereco("Av. Nova, 200")
                .build();

        when(clinicaRepository.findById(1L)).thenReturn(Optional.of(clinica));
        when(clinicaRepository.save(any(Clinica.class))).thenAnswer(inv -> inv.getArgument(0));

        Clinica result = clinicaService.update(1L, updated);

        assertThat(result.getNome()).isEqualTo("Clínica Vet Nova");
        assertThat(result.getEndereco()).isEqualTo("Av. Nova, 200");
    }

    @Test
    void delete_existente_deveRemoverClinica() {
        when(clinicaRepository.findById(1L)).thenReturn(Optional.of(clinica));

        clinicaService.delete(1L);

        verify(clinicaRepository).delete(clinica);
    }

    @Test
    void delete_inexistente_deveLancarResourceNotFoundException() {
        when(clinicaRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> clinicaService.delete(99L))
                .isInstanceOf(ResourceNotFoundException.class);

        verify(clinicaRepository, never()).delete(any());
    }
}
