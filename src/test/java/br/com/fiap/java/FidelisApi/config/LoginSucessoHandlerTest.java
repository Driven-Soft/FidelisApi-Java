package br.com.fiap.java.FidelisApi.config;

import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class LoginSucessoHandlerTest {

    private final LoginSucessoHandler handler = new LoginSucessoHandler();

    @Test
    void clinicaDeveIrParaListaDePetsDaClinica() throws Exception {
        MockHttpServletResponse response = new MockHttpServletResponse();

        handler.onAuthenticationSuccess(new MockHttpServletRequest(), response, autenticacao("ROLE_CLINICA"));

        assertThat(response.getRedirectedUrl()).isEqualTo("/clinica/pets");
    }

    @Test
    void tutorDeveIrParaMeusPets() throws Exception {
        MockHttpServletResponse response = new MockHttpServletResponse();

        handler.onAuthenticationSuccess(new MockHttpServletRequest(), response, autenticacao("ROLE_TUTOR"));

        assertThat(response.getRedirectedUrl()).isEqualTo("/tutor/pets");
    }

    private Authentication autenticacao(String papel) {
        return new UsernamePasswordAuthenticationToken(
                "usuario", "senha", List.of(new SimpleGrantedAuthority(papel)));
    }
}