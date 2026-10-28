package br.com.fiap.java.FidelisApi.config;

import br.com.fiap.java.FidelisApi.entity.Perfil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class LoginSucessoHandler implements AuthenticationSuccessHandler {

    private static final String AUTORIDADE_CLINICA = "ROLE_" + Perfil.CLINICA.name();

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException {
        boolean clinica = authentication.getAuthorities().stream()
                .anyMatch(autoridade -> AUTORIDADE_CLINICA.equals(autoridade.getAuthority()));
        String destino = clinica ? "/clinica/pets" : "/tutor/pets";
        response.sendRedirect(request.getContextPath() + destino);
    }
}