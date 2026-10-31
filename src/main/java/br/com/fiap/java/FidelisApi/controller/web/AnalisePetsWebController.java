package br.com.fiap.java.FidelisApi.controller.web;

import br.com.fiap.java.FidelisApi.entity.Clinica;
import br.com.fiap.java.FidelisApi.entity.HistoricoPeso;
import br.com.fiap.java.FidelisApi.entity.Pet;
import br.com.fiap.java.FidelisApi.entity.Usuario;
import br.com.fiap.java.FidelisApi.exception.PetAnalysisException;
import br.com.fiap.java.FidelisApi.repository.PetRepository;
import br.com.fiap.java.FidelisApi.repository.UsuarioRepository;
import br.com.fiap.java.FidelisApi.service.PetAnalysisService;
import br.com.fiap.java.FidelisApi.service.PetService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Comparator;

@Controller
@RequestMapping("/clinica/analisar-pets")
@RequiredArgsConstructor
public class AnalisePetsWebController {

    private final PetService petService;
    private final PetRepository petRepository;
    private final UsuarioRepository usuarioRepository;
    private final ObjectProvider<PetAnalysisService> petAnalysisServiceProvider;

    @GetMapping
    public String listar(Authentication authentication, Model model) {
        Long clinicaId = clinicaLogada(authentication).getId();
        model.addAttribute("pets",
                petService.findAllByClinica(clinicaId, PageRequest.of(0, 100, Sort.by("nome"))));
        return "clinica/analise-pets-lista";
    }

    @GetMapping("/{petId}")
    public String analisar(@PathVariable Long petId, Authentication authentication, Model model) {
        Long clinicaId = clinicaLogada(authentication).getId();
        Pet pet = buscarPetDaClinica(petId, clinicaId);
        return carregarTelaAnalise(pet, model);
    }

    @PostMapping("/{petId}")
    public String gerarAnalise(@PathVariable Long petId, Authentication authentication, Model model) {
        Long clinicaId = clinicaLogada(authentication).getId();
        Pet pet = buscarPetDaClinica(petId, clinicaId);
        PetAnalysisService petAnalysisService = petAnalysisServiceProvider.getIfAvailable();
        if (petAnalysisService == null) {
            model.addAttribute("erroAnalise",
                    "A análise com IA não está habilitada. Configure a chave da API e habilite a integração.");
        } else {
            try {
                model.addAttribute("resultadoAnalise", petAnalysisService.analisar(petId, clinicaId));
            } catch (PetAnalysisException exception) {
                model.addAttribute("erroAnalise", exception.getMessage());
            }
        }
        return carregarTelaAnalise(pet, model);
    }

    private Pet buscarPetDaClinica(Long petId, Long clinicaId) {
        return petRepository.findByIdAndClinicaIdComHistoricoPeso(petId, clinicaId)
                .orElseThrow(() -> new AccessDeniedException("Pet não pertence à clínica logada"));
    }

    private String carregarTelaAnalise(Pet pet, Model model) {
        model.addAttribute("pet", pet);
        model.addAttribute("analiseDisponivel", petAnalysisServiceProvider.getIfAvailable() != null);
        model.addAttribute("historicoPesos", pet.getHistoricoPeso().stream()
                .sorted(Comparator.comparing(HistoricoPeso::getDataMedicao).reversed())
                .toList());
        return "clinica/analise-pet";
    }

    private Clinica clinicaLogada(Authentication authentication) {
        Usuario usuario = usuarioRepository.findByEmailComVinculo(authentication.getName())
                .orElseThrow(() -> new IllegalStateException("Usuário autenticado não encontrado"));
        return usuario.getClinica();
    }
}
