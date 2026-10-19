package br.com.fiap.java.FidelisApi.common;

import java.text.Normalizer;
import java.util.List;
import java.util.Locale;

/**
 * Catálogo de opções predefinidas usadas nos formulários (tipo de consulta e
 * vacina aplicada).
 *
 * As listas servem de apoio à digitação: o usuário pode escolher uma opção ou
 * informar outro valor manualmente. Por isso os campos continuam sendo texto
 * livre no banco, e o catálogo também é usado para padronizar a grafia quando o
 * texto digitado corresponde a uma opção existente (ex.: "check-up" vira
 * "Check-up"), evitando valores duplicados.
 */
public final class CatalogoOpcoes {

    /** Grupo de espécies a que uma opção se aplica. */
    public enum GrupoEspecie {
        TODOS,
        CAES,
        GATOS
    }

    public record Opcao(String nome, GrupoEspecie grupo) {
    }

    private static final List<Opcao> TIPOS_CONSULTA = List.of(
            new Opcao("Consulta de rotina", GrupoEspecie.TODOS),
            new Opcao("Check-up", GrupoEspecie.TODOS),
            new Opcao("Retorno", GrupoEspecie.TODOS),
            new Opcao("Emergência", GrupoEspecie.TODOS),
            new Opcao("Vacinação", GrupoEspecie.TODOS),
            new Opcao("Vermifugação", GrupoEspecie.TODOS),
            new Opcao("Cirurgia", GrupoEspecie.TODOS),
            new Opcao("Pós-operatório", GrupoEspecie.TODOS),
            new Opcao("Exame laboratorial", GrupoEspecie.TODOS),
            new Opcao("Dermatologia", GrupoEspecie.TODOS),
            new Opcao("Odontologia", GrupoEspecie.TODOS),
            new Opcao("Ortopedia", GrupoEspecie.TODOS),
            new Opcao("Cardiologia", GrupoEspecie.TODOS),
            new Opcao("Oftalmologia", GrupoEspecie.TODOS)
    );

    private static final List<Opcao> VACINAS = List.of(
            new Opcao("Antirrábica", GrupoEspecie.TODOS),
            new Opcao("V8 (polivalente canina)", GrupoEspecie.CAES),
            new Opcao("V10 (polivalente canina)", GrupoEspecie.CAES),
            new Opcao("Gripe canina (tosse dos canis)", GrupoEspecie.CAES),
            new Opcao("Giárdia", GrupoEspecie.CAES),
            new Opcao("Leishmaniose", GrupoEspecie.CAES),
            new Opcao("V3 (tríplice felina)", GrupoEspecie.GATOS),
            new Opcao("V4 (quádrupla felina)", GrupoEspecie.GATOS),
            new Opcao("V5 (quíntupla felina)", GrupoEspecie.GATOS),
            new Opcao("FeLV (leucemia felina)", GrupoEspecie.GATOS)
    );

    private CatalogoOpcoes() {
    }

    public static List<Opcao> tiposConsulta() {
        return TIPOS_CONSULTA;
    }

    public static List<Opcao> vacinas() {
        return VACINAS;
    }

    /**
     * Converte o nome da espécie do pet no grupo usado para filtrar opções.
     * Espécies desconhecidas (ou nulas) retornam {@link GrupoEspecie#TODOS},
     * ou seja, nenhuma opção é escondida.
     */
    public static GrupoEspecie grupoDaEspecie(String especie) {
        if (especie == null) {
            return GrupoEspecie.TODOS;
        }
        String chave = chaveDeComparacao(especie);
        if (chave.startsWith("gat")) {
            return GrupoEspecie.GATOS;
        }
        if (chave.startsWith("cach") || chave.equals("cao") || chave.equals("caes")) {
            return GrupoEspecie.CAES;
        }
        return GrupoEspecie.TODOS;
    }

    public static String normalizarTipoConsulta(String valor) {
        return normalizar(valor, TIPOS_CONSULTA);
    }

    public static String normalizarVacina(String valor) {
        return normalizar(valor, VACINAS);
    }

    /**
     * Remove espaços extras e, se o texto equivaler a uma opção do catálogo
     * (ignorando maiúsculas, minúsculas e acentos), devolve a grafia oficial
     * da opção. Textos que não constam no catálogo são mantidos como digitados.
     */
    private static String normalizar(String valor, List<Opcao> catalogo) {
        if (valor == null) {
            return null;
        }
        String limpo = valor.trim().replaceAll("\\s+", " ");
        String chave = chaveDeComparacao(limpo);
        return catalogo.stream()
                .map(Opcao::nome)
                .filter(nome -> chaveDeComparacao(nome).equals(chave))
                .findFirst()
                .orElse(limpo);
    }

    private static String chaveDeComparacao(String texto) {
        return Normalizer.normalize(texto, Normalizer.Form.NFD)
                .replaceAll("\\p{M}", "")
                .toLowerCase(Locale.ROOT);
    }
}