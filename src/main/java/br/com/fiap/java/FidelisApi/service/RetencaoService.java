package br.com.fiap.java.FidelisApi.service;

import br.com.fiap.java.FidelisApi.entity.Pet;
import br.com.fiap.java.FidelisApi.repository.ConsultaRepository;
import br.com.fiap.java.FidelisApi.repository.PetRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RetencaoService {

    private static final int DIAS_LIMITE_RETENCAO = 90;

    private final PetRepository petRepository;
    private final ConsultaRepository consultaRepository;

    public record PetEmRisco(Pet pet, LocalDateTime ultimaConsulta) {

        public long diasSemConsulta() {
            if (ultimaConsulta == null) {
                return -1; // nunca teve consulta
            }
            return Duration.between(ultimaConsulta, LocalDateTime.now()).toDays();
        }
    }

    public List<PetEmRisco> listarPetsEmRisco(Long clinicaId) {
        LocalDateTime limite = LocalDateTime.now().minusDays(DIAS_LIMITE_RETENCAO);

        List<Long> ids = petRepository.findIdsPetsEmRiscoDeRetencao(clinicaId, limite);
        if (ids.isEmpty()) {
            return List.of();
        }

        List<Pet> pets = petRepository.findByIdInComTutor(ids);

        Map<Long, LocalDateTime> ultimasConsultas = consultaRepository.findUltimaConsultaPorPetIds(ids).stream()
                .collect(Collectors.toMap(
                        row -> (Long) row[0],
                        row -> (LocalDateTime) row[1]
                ));

        return pets.stream()
                .map(pet -> new PetEmRisco(pet, ultimasConsultas.get(pet.getId())))
                .sorted(Comparator.comparing(
                        PetEmRisco::ultimaConsulta,
                        Comparator.nullsFirst(Comparator.naturalOrder())
                ))
                .toList();
    }

    public int contarPetsEmRisco(Long clinicaId) {
        LocalDateTime limite = LocalDateTime.now().minusDays(DIAS_LIMITE_RETENCAO);
        return petRepository.findIdsPetsEmRiscoDeRetencao(clinicaId, limite).size();
    }
}