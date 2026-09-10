package br.com.fiap.java.FidelisApi.controller.web;

import br.com.fiap.java.FidelisApi.entity.Pet;
import br.com.fiap.java.FidelisApi.entity.Tutor;
import br.com.fiap.java.FidelisApi.entity.Usuario;
import br.com.fiap.java.FidelisApi.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/tutor/pets")
@RequiredArgsConstructor
public class TutorPetWebController {

    private final PetRepository petRepository;
    private final ConsultaRepository consultaRepository;
    private final VacinacaoRepository vacinacaoRepository;
    private final LembreteRepository lembreteRepository;
    private final UsuarioRepository usuarioRepository;

    @GetMapping
    public String listar(Authentication authentication, Model model) {
        Long tutorId = tutorLogado(authentication).getId();
        model.addAttribute("pets", petRepository.findByTutorIdComClinica(tutorId));
        return "tutor/pets-lista";
    }

    @GetMapping("/{id}")
    public String detalhe(@PathVariable Long id, Authentication authentication, Model model) {
        Long tutorId = tutorLogado(authentication).getId();

        Pet pet = petRepository.findByIdAndTutorId(id, tutorId)
                .orElseThrow(() -> new AccessDeniedException("Este pet não pertence ao tutor logado."));

        model.addAttribute("pet", pet);
        model.addAttribute("consultas", consultaRepository.findByPetIdComVeterinario(id));
        model.addAttribute("vacinacoes", vacinacaoRepository.findByPetIdComVeterinario(id));
        model.addAttribute("lembretes", lembreteRepository.findByPetId(id));

        return "tutor/pets-detalhe";
    }

    private Tutor tutorLogado(Authentication authentication) {
        Usuario usuario = usuarioRepository.findByEmailComVinculo(authentication.getName())
                .orElseThrow(() -> new IllegalStateException("Usuário autenticado não encontrado"));
        return usuario.getTutor();
    }
}