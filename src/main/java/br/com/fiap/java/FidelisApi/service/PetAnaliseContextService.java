package br.com.fiap.java.FidelisApi.service;

import br.com.fiap.java.FidelisApi.entity.Comportamento;
import br.com.fiap.java.FidelisApi.entity.Consulta;
import br.com.fiap.java.FidelisApi.entity.HistoricoPeso;
import br.com.fiap.java.FidelisApi.entity.Lembrete;
import br.com.fiap.java.FidelisApi.entity.Medicamento;
import br.com.fiap.java.FidelisApi.entity.Pet;
import br.com.fiap.java.FidelisApi.entity.Recomendacao;
import br.com.fiap.java.FidelisApi.entity.Vacinacao;
import br.com.fiap.java.FidelisApi.entity.Vermifugacao;
import br.com.fiap.java.FidelisApi.exception.ResourceNotFoundException;
import br.com.fiap.java.FidelisApi.repository.PetRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.Period;
import java.util.Comparator;

@Service
@RequiredArgsConstructor
public class PetAnaliseContextService {

    private static final int MAX_REGISTROS_POR_TIPO = 10;
    private static final int MAX_TEXTO_POR_CAMPO = 800;

    private final PetRepository petRepository;

    @Transactional(readOnly = true)
    public String construirContexto(Long petId, Long clinicaId) {
        Pet pet = petRepository.findByIdAndClinicaIdComHistoricoPeso(petId, clinicaId)
                .orElseThrow(() -> new ResourceNotFoundException("Pet não encontrado na clínica."));

        StringBuilder contexto = new StringBuilder();
        contexto.append("DADOS DO PET\n")
                .append("Nome: ").append(pet.getNome()).append('\n')
                .append("Espécie: ").append(pet.getEspecie().getRotulo()).append('\n')
                .append("Raça: ").append(pet.getRaca()).append('\n')
                .append("Sexo: ").append(pet.getSexo().getRotulo()).append('\n')
                .append("Idade: ").append(Period.between(pet.getDataNascimento(), LocalDate.now()).getYears())
                .append(" anos\n")
                .append("Status: ").append(pet.getStatus()).append("\n\n");

        contexto.append("HISTÓRICO DE PESO\n");
        pet.getHistoricoPeso().stream()
                .sorted(Comparator.comparing(HistoricoPeso::getDataMedicao).reversed())
                .limit(MAX_REGISTROS_POR_TIPO)
                .forEach(medicao -> contexto.append(medicao.getDataMedicao())
                        .append(": ").append(medicao.getPesoKg()).append(" kg; ")
                        .append(textoSeguro(medicao.getObservacao())).append('\n'));

        contexto.append("\nCONSULTAS\n");
        pet.getConsultas().stream()
                .sorted(Comparator.comparing(Consulta::getDataHora).reversed())
                .limit(MAX_REGISTROS_POR_TIPO)
                .forEach(consulta -> {
                    contexto.append(consulta.getDataHora()).append(" - ")
                            .append(consulta.getTipo()).append('\n')
                            .append("Diagnóstico registrado: ").append(textoSeguro(consulta.getDiagnostico())).append('\n')
                            .append("Observações: ").append(textoSeguro(consulta.getObservacoes())).append('\n');
                    consulta.getExames().stream()
                            .limit(MAX_REGISTROS_POR_TIPO)
                            .forEach(exame -> contexto.append("Exame ")
                                    .append(exame.getTipo()).append(" (").append(exame.getData()).append("): ")
                                    .append(textoSeguro(exame.getDescricao())).append("; resultado: ")
                                    .append(textoSeguro(exame.getResultado())).append('\n'));
                    consulta.getPrescricoes().stream()
                            .limit(MAX_REGISTROS_POR_TIPO)
                            .forEach(prescricao -> {
                                contexto.append("Prescrição registrada: ")
                                        .append(textoSeguro(prescricao.getDosagem())).append(", ")
                                        .append(textoSeguro(prescricao.getFrequencia())).append(", ")
                                        .append(prescricao.getDuracaoDias()).append(" dias. ")
                                        .append(textoSeguro(prescricao.getObservacao())).append('\n');
                                prescricao.getMedicamentos().stream()
                                        .limit(MAX_REGISTROS_POR_TIPO)
                                        .forEach(medicamento -> contexto.append("Medicamento registrado: ")
                                                .append(medicamento.getNome()).append("; ")
                                                .append(textoSeguro(medicamento.getDescricao())).append('\n'));
                            });
                });

        contexto.append("\nVACINAÇÕES\n");
        pet.getVacinacoes().stream()
                .sorted(Comparator.comparing(Vacinacao::getDataAplicacao).reversed())
                .limit(MAX_REGISTROS_POR_TIPO)
                .forEach(vacinacao -> contexto.append(vacinacao.getDataAplicacao())
                        .append(": ").append(vacinacao.getVacinaAplicada())
                        .append("; próxima: ").append(vacinacao.getDataProxima())
                        .append("; ").append(textoSeguro(vacinacao.getObservacao())).append('\n'));

        contexto.append("\nVERMIFUGAÇÕES\n");
        pet.getVermifugacoes().stream()
                .sorted(Comparator.comparing(Vermifugacao::getDataAplicacao).reversed())
                .limit(MAX_REGISTROS_POR_TIPO)
                .forEach(registro -> contexto.append(registro.getDataAplicacao())
                        .append(": ").append(registro.getProduto())
                        .append("; próxima: ").append(registro.getDataProxima()).append('\n'));

        contexto.append("\nCOMPORTAMENTOS REGISTRADOS\n");
        pet.getComportamentos().stream()
                .sorted(Comparator.comparing(Comportamento::getData).reversed())
                .limit(MAX_REGISTROS_POR_TIPO)
                .forEach(registro -> contexto.append(registro.getData())
                        .append(": ").append(textoSeguro(registro.getDescricao())).append('\n'));

        contexto.append("\nRECOMENDAÇÕES REGISTRADAS\n");
        pet.getRecomendacoes().stream()
                .sorted(Comparator.comparing(Recomendacao::getDataRecomendacao).reversed())
                .limit(MAX_REGISTROS_POR_TIPO)
                .forEach(registro -> contexto.append(registro.getDataRecomendacao())
                        .append(" - ").append(registro.getTipo()).append(": ")
                        .append(textoSeguro(registro.getDescricao())).append('\n'));

        contexto.append("\nLEMBRETES\n");
        pet.getLembretes().stream()
                .sorted(Comparator.comparing(Lembrete::getDataPrevista).reversed())
                .limit(MAX_REGISTROS_POR_TIPO)
                .forEach(lembrete -> contexto.append(lembrete.getDataPrevista())
                        .append(" - ").append(lembrete.getTipo())
                        .append(" (").append(lembrete.getStatus()).append("): ")
                        .append(textoSeguro(lembrete.getDescricao())).append('\n'));

        return contexto.toString();
    }

    private String textoSeguro(String texto) {
        if (texto == null || texto.isBlank()) {
            return "Não informado";
        }
        String semControles = texto.replaceAll("[\\p{Cntrl}&&[^\r\n\t]]", " ").trim();
        return semControles.length() <= MAX_TEXTO_POR_CAMPO
                ? semControles
                : semControles.substring(0, MAX_TEXTO_POR_CAMPO) + "...";
    }
}
