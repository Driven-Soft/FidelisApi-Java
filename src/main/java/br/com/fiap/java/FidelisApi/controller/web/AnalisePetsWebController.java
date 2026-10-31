package br.com.fiap.java.FidelisApi.controller.web;

import br.com.fiap.java.FidelisApi.entity.HistoricoPeso;
import br.com.fiap.java.FidelisApi.entity.Clinica;
import br.com.fiap.java.FidelisApi.entity.Pet;
import br.com.fiap.java.FidelisApi.entity.Usuario;
import br.com.fiap.java.FidelisApi.repository.PetRepository;
import br.com.fiap.java.FidelisApi.repository.UsuarioRepository;
import br.com.fiap.java.FidelisApi.service.PetService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Comparator;

@Controller
@RequestMapping("/clinica/analisar-pets")
@RequiredArgsConstructor
public class AnalisePetsWebController {

    private final PetService petService;
    private final PetRepository petRepository;
    private final UsuarioRepository usuarioRepository;

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
        Pet pet = petRepository.findByIdAndClinicaIdComHistoricoPeso(petId, clinicaId)
                .orElseThrow(() -> new AccessDeniedException("Pet não pertence à clínica logada"));

        model.addAttribute("pet", pet);
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
