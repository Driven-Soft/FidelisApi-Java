package br.com.fiap.java.FidelisApi.service;

import br.com.fiap.java.FidelisApi.entity.Consulta;
import br.com.fiap.java.FidelisApi.entity.Lembrete;
import br.com.fiap.java.FidelisApi.entity.LembreteStatus;
import br.com.fiap.java.FidelisApi.entity.Pet;
import br.com.fiap.java.FidelisApi.entity.Recomendacao;
import br.com.fiap.java.FidelisApi.entity.Veterinario;
import br.com.fiap.java.FidelisApi.exception.ResourceNotFoundException;
import br.com.fiap.java.FidelisApi.repository.ConsultaRepository;
import br.com.fiap.java.FidelisApi.repository.LembreteRepository;
import br.com.fiap.java.FidelisApi.repository.PetRepository;
import br.com.fiap.java.FidelisApi.repository.RecomendacaoRepository;
import br.com.fiap.java.FidelisApi.repository.VeterinarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class ConsultaService {

    private final ConsultaRepository consultaRepository;
    private final VeterinarioRepository veterinarioRepository;
    private final PetRepository petRepository;
    private final LembreteRepository lembreteRepository;
    private final RecomendacaoRepository recomendacaoRepository;

    @Cacheable(value = "consultas", key = "(#tipo != null ? #tipo : '') + '_' + (#inicio != null ? #inicio.toString() : '') + '_' + (#fim != null ? #fim.toString() : '') + '_' + #pageable.pageNumber + '_' + #pageable.pageSize + '_' + #pageable.sort")
    public Page<Consulta> findAll(String tipo, LocalDateTime inicio, LocalDateTime fim, Pageable pageable) {
        if (tipo != null && !tipo.isBlank()) {
            return consultaRepository.findByTipoContainingIgnoreCase(tipo, pageable);
        }
        return consultaRepository.findByDataHoraBetween(inicio, fim, pageable);
    }

    public Consulta findById(Long id) {
        return consultaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Consulta não encontrada com id " + id));
    }

    @Transactional
    @CacheEvict(value = "consultas", allEntries = true)
    public Consulta create(Consulta consulta, Long veterinarioId, Long petId) {
        Veterinario veterinario = veterinarioRepository.findById(veterinarioId)
                .orElseThrow(() -> new ResourceNotFoundException("Veterinário não encontrado com id " + veterinarioId));
        Pet pet = petRepository.findById(petId)
                .orElseThrow(() -> new ResourceNotFoundException("Pet não encontrado com id " + petId));

        consulta.setVeterinario(veterinario);
        consulta.setPet(pet);
        Consulta salva = consultaRepository.save(consulta);

        gerarLembreteERecomendacao(salva, pet);

        return salva;
    }

    private void gerarLembreteERecomendacao(Consulta consulta, Pet pet) {
        LocalDate dataRetorno = consulta.getDataRetorno() != null
                ? consulta.getDataRetorno()
                : consulta.getDataHora().toLocalDate().plusDays(30);

        Lembrete lembrete = Lembrete.builder()
                .tipo("Retorno")
                .descricao("Retorno da consulta de " + consulta.getTipo()
                        + " realizada em " + consulta.getDataHora().toLocalDate())
                .dataPrevista(dataRetorno)
                .status(LembreteStatus.PENDENTE)
                .tutor(pet.getTutor())
                .pet(pet)
                .build();
        lembreteRepository.save(lembrete);

        Recomendacao recomendacao = Recomendacao.builder()
                .tipo("Cuidado Pós-Consulta")
                .descricao("Acompanhar " + pet.getNome() + " após consulta de " + consulta.getTipo() + "."
                        + (consulta.getObservacoes() != null ? " " + consulta.getObservacoes() : ""))
                .dataRecomendacao(LocalDate.now())
                .pet(pet)
                .build();
        recomendacaoRepository.save(recomendacao);
    }

    @CacheEvict(value = "consultas", allEntries = true)
    public Consulta update(Long id, Consulta update) {
        Consulta existing = findById(id);
        existing.setDataHora(update.getDataHora());
        existing.setTipo(update.getTipo());
        existing.setDiagnostico(update.getDiagnostico());
        existing.setObservacoes(update.getObservacoes());
        existing.setDataRetorno(update.getDataRetorno());
        return consultaRepository.save(existing);
    }

    @CacheEvict(value = "consultas", allEntries = true)
    public void delete(Long id) {
        Consulta existing = findById(id);
        consultaRepository.delete(existing);
    }
}
