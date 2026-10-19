package br.com.fiap.java.FidelisApi.common;

import br.com.fiap.java.FidelisApi.common.CatalogoOpcoes.GrupoEspecie;
import br.com.fiap.java.FidelisApi.common.CatalogoOpcoes.Opcao;
import br.com.fiap.java.FidelisApi.dto.request.ConsultaRequest;
import br.com.fiap.java.FidelisApi.dto.request.VacinacaoRequest;
import br.com.fiap.java.FidelisApi.mapper.ConsultaMapper;
import br.com.fiap.java.FidelisApi.mapper.VacinacaoMapper;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class CatalogoOpcoesTest {

    private static Validator validator;

    @BeforeAll
    static void setUpValidator() {
        validator = Validation.buildDefaultValidatorFactory().getValidator();
    }

    @Test
    void todasAsOpcoesDeTipoDeConsultaDevemSerAceitasPelaValidacao() {
        for (Opcao opcao : CatalogoOpcoes.tiposConsulta()) {
            ConsultaRequest request = new ConsultaRequest();
            request.setTipo(opcao.nome());

            assertThat(validator.validateProperty(request, "tipo")).isEmpty();
        }
    }

    @Test
    void todasAsOpcoesDeVacinaDevemSerAceitasPelaValidacao() {
        for (Opcao opcao : CatalogoOpcoes.vacinas()) {
            VacinacaoRequest request = new VacinacaoRequest();
            request.setVacinaAplicada(opcao.nome());

            assertThat(validator.validateProperty(request, "vacinaAplicada")).isEmpty();
        }
    }

    @Test
    void devePadronizarGrafiaQuandoTextoCorrespondeAUmaOpcao() {
        assertThat(CatalogoOpcoes.normalizarTipoConsulta("check-up")).isEqualTo("Check-up");
        assertThat(CatalogoOpcoes.normalizarTipoConsulta("  EMERGENCIA ")).isEqualTo("Emergência");
        assertThat(CatalogoOpcoes.normalizarTipoConsulta("Consulta   de  rotina")).isEqualTo("Consulta de rotina");
        assertThat(CatalogoOpcoes.normalizarVacina("antirrabica")).isEqualTo("Antirrábica");
        assertThat(CatalogoOpcoes.normalizarVacina("v10 (polivalente canina)")).isEqualTo("V10 (polivalente canina)");
    }

    @Test
    void deveManterTextoDigitadoQuandoNaoConstaNoCatalogo() {
        assertThat(CatalogoOpcoes.normalizarTipoConsulta("Checkup")).isEqualTo("Checkup");
        assertThat(CatalogoOpcoes.normalizarVacina(" Bordetella ")).isEqualTo("Bordetella");
    }

    @Test
    void deveTratarValorNulo() {
        assertThat(CatalogoOpcoes.normalizarTipoConsulta(null)).isNull();
        assertThat(CatalogoOpcoes.normalizarVacina(null)).isNull();
    }

    @Test
    void deveIdentificarGrupoPelaEspecieDoPet() {
        assertThat(CatalogoOpcoes.grupoDaEspecie("Cachorro")).isEqualTo(GrupoEspecie.CAES);
        assertThat(CatalogoOpcoes.grupoDaEspecie("cão")).isEqualTo(GrupoEspecie.CAES);
        assertThat(CatalogoOpcoes.grupoDaEspecie("Gato")).isEqualTo(GrupoEspecie.GATOS);
        assertThat(CatalogoOpcoes.grupoDaEspecie("Coelho")).isEqualTo(GrupoEspecie.TODOS);
        assertThat(CatalogoOpcoes.grupoDaEspecie(null)).isEqualTo(GrupoEspecie.TODOS);
    }

    @Test
    void mappersDevemAplicarAPadronizacaoDoCatalogo() {
        ConsultaRequest consulta = new ConsultaRequest();
        consulta.setTipo("emergencia");

        VacinacaoRequest vacinacao = new VacinacaoRequest();
        vacinacao.setVacinaAplicada("antirrabica");

        assertThat(ConsultaMapper.toEntity(consulta).getTipo()).isEqualTo("Emergência");
        assertThat(VacinacaoMapper.toEntity(vacinacao).getVacinaAplicada()).isEqualTo("Antirrábica");
    }
}