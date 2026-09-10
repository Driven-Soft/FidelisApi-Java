package br.com.fiap.java.FidelisApi.controller.web;

import br.com.fiap.java.FidelisApi.entity.Perfil;
import br.com.fiap.java.FidelisApi.entity.Usuario;
import br.com.fiap.java.FidelisApi.repository.UsuarioRepository;
import br.com.fiap.java.FidelisApi.service.RetencaoService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class DashboardController {

    private final UsuarioRepository usuarioRepository;
    private final RetencaoService retencaoService;

    @GetMapping("/dashboard")
    public String exibirDashboard(Authentication authentication, Model model) {
        Usuario usuario = usuarioRepository.findByEmailComVinculo(authentication.getName())
                .orElseThrow(() -> new IllegalStateException(
                        "Usuário autenticado não encontrado: " + authentication.getName()));

        String nomeExibicao = switch (usuario.getPerfil()) {
            case CLINICA -> usuario.getClinica().getNome();
            case TUTOR -> usuario.getTutor().getNome();
        };

        model.addAttribute("usuario", usuario);
        model.addAttribute("nomeExibicao", nomeExibicao);

        if (usuario.getPerfil() == Perfil.CLINICA) {
            model.addAttribute("qtdPetsEmRisco", retencaoService.contarPetsEmRisco(usuario.getClinica().getId()));
        }

        return "dashboard";
    }
}