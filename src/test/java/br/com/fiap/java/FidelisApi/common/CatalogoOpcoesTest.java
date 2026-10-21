package br.com.fiap.java.FidelisApi.common;

import br.com.fiap.java.FidelisApi.common.CatalogoOpcoes.GrupoEspecie;
import br.com.fiap.java.FidelisApi.common.CatalogoOpcoes.Opcao;
import br.com.fiap.java.FidelisApi.dto.request.ConsultaRequest;
import br.com.fiap.java.FidelisApi.dto.request.PetRequest;
import br.com.fiap.java.FidelisApi.dto.request.VacinacaoRequest;
import br.com.fiap.java.FidelisApi.entity.EspeciePet;
import br.com.fiap.java.FidelisApi.entity.Pet;
import br.com.fiap.java.FidelisApi.exception.BusinessException;
import br.com.fiap.java.FidelisApi.mapper.ConsultaMapper;
import br.com.fiap.java.FidelisApi.mapper.PetMapper;
import br.com.fiap.java.FidelisApi.mapper.VacinacaoMapper;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

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
    void todasAsOpcoesDeRacaDevemSerAceitasPelaValidacao() {
        for (Opcao opcao : CatalogoOpcoes.racas()) {
            PetRequest request = new PetRequest();
            request.setRaca(opcao.nome());

            assertThat(validator.validateProperty(request, "raca")).isEmpty();
        }
    }

    @Test
    void catalogoDeRacasNaoDeveTerNomesRepetidos() {
        Set<String> nomes = new HashSet<>();
        for (Opcao opcao : CatalogoOpcoes.racas()) {
            assertThat(nomes.add(opcao.nome().toLowerCase())).isTrue();
        }
    }

    @Test
    void devePadronizarGrafiaQuandoTextoCorrespondeAUmaOpcao() {
        assertThat(CatalogoOpcoes.normalizarTipoConsulta("check-up")).isEqualTo("Check-up");
        assertThat(CatalogoOpcoes.normalizarTipoConsulta("  EMERGENCIA ")).isEqualTo("Emergência");
        assertThat(CatalogoOpcoes.normalizarTipoConsulta("Consulta   de  rotina")).isEqualTo("Consulta de rotina");
        assertThat(CatalogoOpcoes.normalizarVacina("antirrabica")).isEqualTo("Antirrábica");
        assertThat(CatalogoOpcoes.normalizarVacina("v10 (polivalente canina)")).isEqualTo("V10 (polivalente canina)");
        assertThat(CatalogoOpcoes.normalizarRaca("rottweiler")).isEqualTo("Rottweiler");
        assertThat(CatalogoOpcoes.normalizarRaca("  siames ")).isEqualTo("Siamês");
        assertThat(CatalogoOpcoes.normalizarRaca("labrador   retriever")).isEqualTo("Labrador Retriever");
    }

    @Test
    void deveManterTextoDigitadoQuandoNaoConstaNoCatalogo() {
        assertThat(CatalogoOpcoes.normalizarTipoConsulta("Checkup")).isEqualTo("Checkup");
        assertThat(CatalogoOpcoes.normalizarVacina(" Bordetella ")).isEqualTo("Bordetella");
        assertThat(CatalogoOpcoes.normalizarRaca("Vira-lata")).isEqualTo("Vira-lata");
    }

    @Test
    void deveTratarValorNulo() {
        assertThat(CatalogoOpcoes.normalizarTipoConsulta(null)).isNull();
        assertThat(CatalogoOpcoes.normalizarVacina(null)).isNull();
        assertThat(CatalogoOpcoes.normalizarRaca(null)).isNull();
    }

    @Test
    void deveIdentificarGrupoPelaEspecieDoPet() {
        assertThat(CatalogoOpcoes.grupoDaEspecie(EspeciePet.CACHORRO)).isEqualTo(GrupoEspecie.CAES);
        assertThat(CatalogoOpcoes.grupoDaEspecie(EspeciePet.GATO)).isEqualTo(GrupoEspecie.GATOS);
        assertThat(CatalogoOpcoes.grupoDaEspecie(null)).isEqualTo(GrupoEspecie.TODOS);
    }

    @Test
    void deveListarAsEspeciesSuportadasComSeusGrupos() {
        assertThat(CatalogoOpcoes.especies())
                .extracting(Opcao::nome)
                .containsExactly("Cachorro", "Gato");
        assertThat(CatalogoOpcoes.especies())
                .extracting(Opcao::grupo)
                .containsExactly(GrupoEspecie.CAES, GrupoEspecie.GATOS);
    }

    @Test
    void mappersDevemAplicarAPadronizacaoDoCatalogo() {
        ConsultaRequest consulta = new ConsultaRequest();
        consulta.setTipo("emergencia");

        VacinacaoRequest vacinacao = new VacinacaoRequest();
        vacinacao.setVacinaAplicada("antirrabica");

        PetRequest petRequest = new PetRequest();
        petRequest.setEspecie("cachorro");
        petRequest.setRaca("labrador retriever");
        Pet pet = PetMapper.toEntity(petRequest);

        assertThat(ConsultaMapper.toEntity(consulta).getTipo()).isEqualTo("Emergência");
        assertThat(VacinacaoMapper.toEntity(vacinacao).getVacinaAplicada()).isEqualTo("Antirrábica");
        assertThat(pet.getEspecie()).isEqualTo(EspeciePet.CACHORRO);
        assertThat(pet.getRaca()).isEqualTo("Labrador Retriever");
    }

    @Test
    void mapperDePetDeveRejeitarEspecieNaoSuportada() {
        PetRequest petRequest = new PetRequest();
        petRequest.setEspecie("Coelho");
        petRequest.setRaca("Mini Lop");

        assertThatThrownBy(() -> PetMapper.toEntity(petRequest))
                .isInstanceOf(BusinessException.class);
    }
}