package br.com.fiap.java.FidelisApi.common;

import br.com.fiap.java.FidelisApi.entity.EspeciePet;

import java.text.Normalizer;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public final class CatalogoOpcoes {

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

    private static final List<Opcao> RACAS = List.of(
            new Opcao("SRD (sem raça definida)", GrupoEspecie.TODOS),
            new Opcao("Akita", GrupoEspecie.CAES),
            new Opcao("Beagle", GrupoEspecie.CAES),
            new Opcao("Border Collie", GrupoEspecie.CAES),
            new Opcao("Boxer", GrupoEspecie.CAES),
            new Opcao("Bulldog Francês", GrupoEspecie.CAES),
            new Opcao("Bulldog Inglês", GrupoEspecie.CAES),
            new Opcao("Cavalier King Charles Spaniel", GrupoEspecie.CAES),
            new Opcao("Chihuahua", GrupoEspecie.CAES),
            new Opcao("Cocker Spaniel", GrupoEspecie.CAES),
            new Opcao("Dachshund (Salsicha)", GrupoEspecie.CAES),
            new Opcao("Dálmata", GrupoEspecie.CAES),
            new Opcao("Doberman", GrupoEspecie.CAES),
            new Opcao("Golden Retriever", GrupoEspecie.CAES),
            new Opcao("Husky Siberiano", GrupoEspecie.CAES),
            new Opcao("Labrador Retriever", GrupoEspecie.CAES),
            new Opcao("Lhasa Apso", GrupoEspecie.CAES),
            new Opcao("Maltês", GrupoEspecie.CAES),
            new Opcao("Pastor Alemão", GrupoEspecie.CAES),
            new Opcao("Pastor de Shetland", GrupoEspecie.CAES),
            new Opcao("Pinscher", GrupoEspecie.CAES),
            new Opcao("Pit Bull", GrupoEspecie.CAES),
            new Opcao("Poodle", GrupoEspecie.CAES),
            new Opcao("Pug", GrupoEspecie.CAES),
            new Opcao("Rottweiler", GrupoEspecie.CAES),
            new Opcao("Schnauzer", GrupoEspecie.CAES),
            new Opcao("Shih Tzu", GrupoEspecie.CAES),
            new Opcao("Spitz Alemão (Lulu da Pomerânia)", GrupoEspecie.CAES),
            new Opcao("Staffordshire Bull Terrier", GrupoEspecie.CAES),
            new Opcao("Weimaraner", GrupoEspecie.CAES),
            new Opcao("Yorkshire Terrier", GrupoEspecie.CAES),
            new Opcao("Abissínio", GrupoEspecie.GATOS),
            new Opcao("Angorá", GrupoEspecie.GATOS),
            new Opcao("Azul Russo", GrupoEspecie.GATOS),
            new Opcao("Bengal", GrupoEspecie.GATOS),
            new Opcao("Birmanês", GrupoEspecie.GATOS),
            new Opcao("British Shorthair", GrupoEspecie.GATOS),
            new Opcao("Exótico", GrupoEspecie.GATOS),
            new Opcao("Himalaio", GrupoEspecie.GATOS),
            new Opcao("Maine Coon", GrupoEspecie.GATOS),
            new Opcao("Norueguês da Floresta", GrupoEspecie.GATOS),
            new Opcao("Persa", GrupoEspecie.GATOS),
            new Opcao("Ragdoll", GrupoEspecie.GATOS),
            new Opcao("Scottish Fold", GrupoEspecie.GATOS),
            new Opcao("Siamês", GrupoEspecie.GATOS),
            new Opcao("Siberiano", GrupoEspecie.GATOS),
            new Opcao("Sphynx", GrupoEspecie.GATOS)
    );

    private CatalogoOpcoes() {
    }

    public static List<Opcao> tiposConsulta() {
        return TIPOS_CONSULTA;
    }

    public static List<Opcao> vacinas() {
        return VACINAS;
    }

    public static List<Opcao> racas() {
        return RACAS;
    }

    public static List<Opcao> especies() {
        return Arrays.stream(EspeciePet.values())
                .map(especie -> new Opcao(especie.getRotulo(), grupoDaEspecie(especie)))
                .toList();
    }

    public static GrupoEspecie grupoDaEspecie(EspeciePet especie) {
        if (especie == null) {
            return GrupoEspecie.TODOS;
        }
        return switch (especie) {
            case CACHORRO -> GrupoEspecie.CAES;
            case GATO -> GrupoEspecie.GATOS;
        };
    }

    public static String normalizarTipoConsulta(String valor) {
        return normalizar(valor, TIPOS_CONSULTA);
    }

    public static String normalizarVacina(String valor) {
        return normalizar(valor, VACINAS);
    }

    public static String normalizarRaca(String valor) {
        return normalizar(valor, RACAS);
    }

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