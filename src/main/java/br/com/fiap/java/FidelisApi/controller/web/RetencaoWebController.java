package br.com.fiap.java.FidelisApi.controller.web;

import br.com.fiap.java.FidelisApi.entity.Clinica;
import br.com.fiap.java.FidelisApi.entity.Usuario;
import br.com.fiap.java.FidelisApi.repository.UsuarioRepository;
import br.com.fiap.java.FidelisApi.service.RetencaoService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/clinica/retencao")
@RequiredArgsConstructor
public class RetencaoWebController {

    private final RetencaoService retencaoService;
    private final UsuarioRepository usuarioRepository;

    @GetMapping
    public String listar(Authentication authentication, Model model) {
        Long clinicaId = clinicaLogada(authentication).getId();
        model.addAttribute("petsEmRisco", retencaoService.listarPetsEmRisco(clinicaId));
        return "clinica/retencao";
    }

    private Clinica clinicaLogada(Authentication authentication) {
        Usuario usuario = usuarioRepository.findByEmailComVinculo(authentication.getName())
                .orElseThrow(() -> new IllegalStateException("Usuário autenticado não encontrado"));
        return usuario.getClinica();
    }
}