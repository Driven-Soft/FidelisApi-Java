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

    @Transactional
    @CacheEvict(value = "consultas", allEntries = true)
    public Consulta update(Long id, Consulta update) {
        Consulta existing = findById(id);
        aplicarAlteracoes(existing, update);
        sincronizarGerados(existing);
        return consultaRepository.save(existing);
    }

    @Transactional
    @CacheEvict(value = "consultas", allEntries = true)
    public Consulta update(Long id, Consulta update, Long veterinarioId) {
        Consulta existing = findById(id);
        existing.setVeterinario(veterinarioRepository.findById(veterinarioId)
                .orElseThrow(() -> new ResourceNotFoundException("Veterinário não encontrado com id " + veterinarioId)));
        aplicarAlteracoes(existing, update);
        sincronizarGerados(existing);
        return consultaRepository.save(existing);
    }

    @Transactional
    @CacheEvict(value = "consultas", allEntries = true)
    public void delete(Long id) {
        Consulta existing = findById(id);
        consultaRepository.delete(existing);
    }

    private void aplicarAlteracoes(Consulta existing, Consulta update) {
        existing.setDataHora(update.getDataHora());
        existing.setTipo(update.getTipo());
        existing.setDiagnostico(update.getDiagnostico());
        existing.setObservacoes(update.getObservacoes());
        existing.setDataRetorno(update.getDataRetorno());
    }

    private void gerarLembreteERecomendacao(Consulta consulta, Pet pet) {
        Lembrete lembrete = Lembrete.builder()
                .tipo("Retorno")
                .descricao(descricaoDoLembrete(consulta))
                .dataPrevista(dataPrevistaDoRetorno(consulta))
                .status(LembreteStatus.PENDENTE)
                .tutor(pet.getTutor())
                .pet(pet)
                .consulta(consulta)
                .build();
        lembreteRepository.save(lembrete);

        Recomendacao recomendacao = Recomendacao.builder()
                .tipo("Cuidado Pós-Consulta")
                .descricao(descricaoDaRecomendacao(consulta, pet))
                .dataRecomendacao(LocalDate.now())
                .pet(pet)
                .consulta(consulta)
                .build();
        recomendacaoRepository.save(recomendacao);
    }

    private void sincronizarGerados(Consulta consulta) {
        lembreteRepository.findByConsultaId(consulta.getId()).stream()
                .filter(lembrete -> lembrete.getStatus() == LembreteStatus.PENDENTE)
                .forEach(lembrete -> {
                    lembrete.setDescricao(descricaoDoLembrete(consulta));
                    lembrete.setDataPrevista(dataPrevistaDoRetorno(consulta));
                });

        recomendacaoRepository.findByConsultaId(consulta.getId())
                .forEach(recomendacao -> recomendacao.setDescricao(
                        descricaoDaRecomendacao(consulta, consulta.getPet())));
    }

    private LocalDate dataPrevistaDoRetorno(Consulta consulta) {
        return consulta.getDataRetorno() != null
                ? consulta.getDataRetorno()
                : consulta.getDataHora().toLocalDate().plusDays(30);
    }

    private String descricaoDoLembrete(Consulta consulta) {
        return "Retorno da consulta de " + consulta.getTipo()
                + " realizada em " + consulta.getDataHora().toLocalDate();
    }

    private String descricaoDaRecomendacao(Consulta consulta, Pet pet) {
        return "Acompanhar " + pet.getNome() + " após consulta de " + consulta.getTipo() + "."
                + (consulta.getObservacoes() != null ? " " + consulta.getObservacoes() : "");
    }
}