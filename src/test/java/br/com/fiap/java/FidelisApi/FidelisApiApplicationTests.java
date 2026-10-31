package br.com.fiap.java.FidelisApi;

import br.com.fiap.java.FidelisApi.service.PetAnaliseContextService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;

@SpringBootTest(properties = "fidelis.analise.ia.enabled=false")
class FidelisApiApplicationTests {

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Autowired
	private PetAnaliseContextService petAnaliseContextService;

	@Test
	void contextLoads() {
	}

	@Test
	void petDeDemonstracaoPossuiHistoricoClinicoVinculadoAoTutorDeTeste() {
		Assertions.assertEquals(1, jdbcTemplate.queryForObject("""
				SELECT COUNT(*)
				FROM FIDELIS_PET pet
				JOIN FIDELIS_TUTOR tutor ON tutor.id = pet.FIDELIS_TUTOR_id
				WHERE tutor.email = 'tutor.teste@fidelis.com.br'
				  AND pet.nome = 'Luna Teste IA'
				""", Integer.class));
		Assertions.assertEquals(2, contar("FIDELIS_CONSULTA"));
		Assertions.assertEquals(2, contar("FIDELIS_VACINACAO"));
		Assertions.assertEquals(3, contar("FIDELIS_HISTORICO_PESO"));
		Assertions.assertEquals(2, jdbcTemplate.queryForObject("""
				SELECT COUNT(*)
				FROM FIDELIS_CONSULTA consulta
				JOIN FIDELIS_PET pet ON pet.id = consulta.FIDELIS_PET_id
				JOIN FIDELIS_TUTOR tutor ON tutor.id = pet.FIDELIS_TUTOR_id
				WHERE tutor.email = 'tutor.teste@fidelis.com.br'
				  AND pet.nome = 'Luna Teste IA'
				  AND EXTRACT(HOUR FROM consulta.data_hora) = 21
				  AND EXTRACT(MINUTE FROM consulta.data_hora) = 30
				""", Integer.class));
	}

	@Test
	void contextoDeAnaliseIncluiDadosClinicosSemDadosDoTutor() {
		var pet = jdbcTemplate.queryForMap("""
				SELECT pet.id, pet.FIDELIS_CLINICA_id
				FROM FIDELIS_PET pet
				JOIN FIDELIS_TUTOR tutor ON tutor.id = pet.FIDELIS_TUTOR_id
				WHERE tutor.email = 'tutor.teste@fidelis.com.br'
				  AND pet.nome = 'Luna Teste IA'
				""");
		String contexto = petAnaliseContextService.construirContexto(
				((Number) pet.get("ID")).longValue(),
				((Number) pet.get("FIDELIS_CLINICA_ID")).longValue());

		Assertions.assertTrue(contexto.contains("Nome: Luna Teste IA"));
		Assertions.assertTrue(contexto.contains("Espécie: Cachorro"));
		Assertions.assertTrue(contexto.contains("V8"));
		Assertions.assertTrue(contexto.contains("12.40"));
		Assertions.assertTrue(contexto.contains("Consulta de rotina"));
		Assertions.assertFalse(contexto.contains("Tutor Teste"));
	}

	private int contar(String tabela) {
		String sql = """
				SELECT COUNT(*)
				FROM %s registro
				JOIN FIDELIS_PET pet ON pet.id = registro.FIDELIS_PET_id
				JOIN FIDELIS_TUTOR tutor ON tutor.id = pet.FIDELIS_TUTOR_id
				WHERE tutor.email = 'tutor.teste@fidelis.com.br'
				  AND pet.nome = 'Luna Teste IA'
				""".formatted(tabela);
		return jdbcTemplate.queryForObject(sql, Integer.class);
	}
}
