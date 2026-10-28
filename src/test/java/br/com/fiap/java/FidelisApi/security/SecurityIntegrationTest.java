package br.com.fiap.java.FidelisApi.security;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.formLogin;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class SecurityIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    private static final String EMAIL_CLINICA = "clinica@fidelis.com.br";
    private static final String EMAIL_TUTOR = "tutor@fidelis.com.br";
    private static final String SENHA = "Senha123";

    @Test
    void deveRedirecionarParaLoginQuandoAnonimoAcessaDashboard() throws Exception {
        mockMvc.perform(get("/dashboard").accept(MediaType.TEXT_HTML))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login"));
    }

    @Test
    void deveAutenticarClinicaEIrParaListaDePets() throws Exception {
        mockMvc.perform(formLogin("/login").user("email", EMAIL_CLINICA).password(SENHA))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/clinica/pets"));
    }

    @Test
    void deveAutenticarTutorEIrParaMeusPets() throws Exception {
        mockMvc.perform(formLogin("/login").user("email", EMAIL_TUTOR).password(SENHA))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/tutor/pets"));
    }

    @Test
    void deveFalharComSenhaIncorreta() throws Exception {
        mockMvc.perform(formLogin("/login").user("email", EMAIL_CLINICA).password("senhaErrada"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login?falha=true"));
    }

    @Test
    void tutorNaoDeveAcessarTelasDaClinica() throws Exception {
        mockMvc.perform(get("/clinica/pets").with(httpBasic(EMAIL_TUTOR, SENHA)))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/acesso-negado"));
    }

    @Test
    void clinicaNaoDeveAcessarTelasDoTutor() throws Exception {
        mockMvc.perform(get("/tutor/pets").with(httpBasic(EMAIL_CLINICA, SENHA)))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/acesso-negado"));
    }

    @Test
    void apiDeveExigirAutenticacaoParaLeitura() throws Exception {
        mockMvc.perform(get("/api/v1/pets"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void clinicaPodeLerApi() throws Exception {
        mockMvc.perform(get("/api/v1/pets").with(httpBasic(EMAIL_CLINICA, SENHA)))
                .andExpect(status().isOk());
    }

    @Test
    void tutorNaoPodeLerApi() throws Exception {
        mockMvc.perform(get("/api/v1/pets").with(httpBasic(EMAIL_TUTOR, SENHA)))
                .andExpect(status().isForbidden());
    }

    @Test
    void tutorNaoPodeEscreverNaApi() throws Exception {
        mockMvc.perform(post("/api/v1/pets")
                        .with(httpBasic(EMAIL_TUTOR, SENHA))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isForbidden());
    }
}