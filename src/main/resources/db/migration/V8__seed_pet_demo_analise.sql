INSERT INTO FIDELIS_PET (
    nome, especie, raca, sexo, data_nascimento, status, foto_url,
    FIDELIS_TUTOR_id, FIDELIS_CLINICA_id
)
SELECT
    'Luna Teste IA', 'CACHORRO', 'SRD', 'F',
    DATEADD('YEAR', -5, CURRENT_DATE), 'A', NULL,
    tutor.id, clinica.id
FROM FIDELIS_TUTOR tutor
JOIN FIDELIS_CLINICA clinica
    ON clinica.cnpj = '12.345.678/0001-90'
WHERE tutor.email = 'tutor.teste@fidelis.com.br'
  AND NOT EXISTS (
      SELECT 1
      FROM FIDELIS_PET pet_existente
      WHERE pet_existente.nome = 'Luna Teste IA'
        AND pet_existente.FIDELIS_TUTOR_id = tutor.id
  );

INSERT INTO FIDELIS_CONSULTA (
    data_hora, tipo, diagnostico, observacoes, data_retorno,
    FIDELIS_VETERINARIO_id, FIDELIS_PET_id
)
SELECT
    DATEADD('DAY', -120, CURRENT_TIMESTAMP),
    'Consulta de rotina',
    'Pet ativo e sem alterações relevantes no exame clínico de demonstração.',
    'Tutor relata apetite e ingestão de água habituais. Orientado manter acompanhamento preventivo.',
    DATEADD('DAY', 30, CURRENT_DATE),
    veterinario.id,
    pet.id
FROM FIDELIS_PET pet
JOIN FIDELIS_VETERINARIO veterinario
    ON veterinario.email = 'ana.vet@fidelis.com.br'
WHERE pet.nome = 'Luna Teste IA'
  AND pet.FIDELIS_TUTOR_id = (
      SELECT tutor.id
      FROM FIDELIS_TUTOR tutor
      WHERE tutor.email = 'tutor.teste@fidelis.com.br'
  )
  AND NOT EXISTS (
      SELECT 1
      FROM FIDELIS_CONSULTA consulta_existente
      WHERE consulta_existente.FIDELIS_PET_id = pet.id
        AND consulta_existente.tipo = 'Consulta de rotina'
  );

INSERT INTO FIDELIS_CONSULTA (
    data_hora, tipo, diagnostico, observacoes, data_retorno,
    FIDELIS_VETERINARIO_id, FIDELIS_PET_id
)
SELECT
    DATEADD('DAY', -30, CURRENT_TIMESTAMP),
    'Acompanhamento',
    'Sem alterações relevantes observadas nesta consulta de demonstração.',
    'Tutor relata redução recente nos passeios. Peso acompanhado; manter rotina de atividade e reavaliar no retorno.',
    DATEADD('DAY', 30, CURRENT_DATE),
    veterinario.id,
    pet.id
FROM FIDELIS_PET pet
JOIN FIDELIS_VETERINARIO veterinario
    ON veterinario.email = 'ana.vet@fidelis.com.br'
WHERE pet.nome = 'Luna Teste IA'
  AND pet.FIDELIS_TUTOR_id = (
      SELECT tutor.id
      FROM FIDELIS_TUTOR tutor
      WHERE tutor.email = 'tutor.teste@fidelis.com.br'
  )
  AND NOT EXISTS (
      SELECT 1
      FROM FIDELIS_CONSULTA consulta_existente
      WHERE consulta_existente.FIDELIS_PET_id = pet.id
        AND consulta_existente.tipo = 'Acompanhamento'
  );

INSERT INTO FIDELIS_HISTORICO_PESO (
    peso_kg, data_medicao, observacao, FIDELIS_PET_id
)
SELECT 12.40, DATEADD('DAY', -120, CURRENT_DATE),
       'Pesagem registrada na consulta de rotina (dado de demonstração).', pet.id
FROM FIDELIS_PET pet
WHERE pet.nome = 'Luna Teste IA'
  AND pet.FIDELIS_TUTOR_id = (
      SELECT tutor.id
      FROM FIDELIS_TUTOR tutor
      WHERE tutor.email = 'tutor.teste@fidelis.com.br'
  );

INSERT INTO FIDELIS_HISTORICO_PESO (
    peso_kg, data_medicao, observacao, FIDELIS_PET_id
)
SELECT 12.80, DATEADD('DAY', -60, CURRENT_DATE),
       'Pesagem intermediária para demonstrar evolução do histórico.', pet.id
FROM FIDELIS_PET pet
WHERE pet.nome = 'Luna Teste IA'
  AND pet.FIDELIS_TUTOR_id = (
      SELECT tutor.id
      FROM FIDELIS_TUTOR tutor
      WHERE tutor.email = 'tutor.teste@fidelis.com.br'
  );

INSERT INTO FIDELIS_HISTORICO_PESO (
    peso_kg, data_medicao, observacao, FIDELIS_PET_id
)
SELECT 13.10, DATEADD('DAY', -30, CURRENT_DATE),
       'Pesagem registrada no acompanhamento (dado de demonstração).', pet.id
FROM FIDELIS_PET pet
WHERE pet.nome = 'Luna Teste IA'
  AND pet.FIDELIS_TUTOR_id = (
      SELECT tutor.id
      FROM FIDELIS_TUTOR tutor
      WHERE tutor.email = 'tutor.teste@fidelis.com.br'
  );

INSERT INTO FIDELIS_EXAME (tipo, descricao, resultado, data, FIDELIS_CONSULTA_id)
SELECT
    'Avaliação clínica',
    'Avaliação geral registrada para demonstração das informações clínicas.',
    'Sem alterações relevantes relatadas no registro fictício.',
    DATEADD('DAY', -120, CURRENT_DATE),
    consulta.id
FROM FIDELIS_CONSULTA consulta
JOIN FIDELIS_PET pet ON pet.id = consulta.FIDELIS_PET_id
WHERE pet.nome = 'Luna Teste IA'
  AND consulta.tipo = 'Consulta de rotina';

INSERT INTO FIDELIS_PRESCRICAO (
    dosagem, frequencia, duracao_dias, observacao, FIDELIS_CONSULTA_id
)
SELECT
    'Conforme',
    'Não se aplica - registro de demonstração',
    1,
    'Registro fictício para testar a exibição de prescrições; não administrar.',
    consulta.id
FROM FIDELIS_CONSULTA consulta
JOIN FIDELIS_PET pet ON pet.id = consulta.FIDELIS_Pet_id
WHERE pet.nome = 'Luna Teste IA'
  AND consulta.tipo = 'Consulta de rotina';

INSERT INTO FIDELIS_MEDICAMENTO (nome, descricao, FIDELIS_PRESCRICAO_id)
SELECT
    'Item demonstrativo',
    'Dado fictício de teste. Não corresponde a medicamento ou orientação real.',
    prescricao.id
FROM FIDELIS_PRESCRICAO prescricao
JOIN FIDELIS_CONSULTA consulta ON consulta.id = prescricao.FIDELIS_CONSULTA_id
JOIN FIDELIS_PET pet ON pet.id = consulta.FIDELIS_PET_id
WHERE pet.nome = 'Luna Teste IA'
  AND prescricao.observacao LIKE 'Registro fictício para testar%';

INSERT INTO FIDELIS_COMPORTAMENTO (data, descricao, FIDELIS_PET_id)
SELECT
    DATEADD('DAY', -30, CURRENT_DATE),
    'Tutor relata redução recente nos passeios; apetite e ingestão de água habituais (dado de demonstração).',
    pet.id
FROM FIDELIS_PET pet
WHERE pet.nome = 'Luna Teste IA'
  AND pet.FIDELIS_TUTOR_id = (
      SELECT tutor.id
      FROM FIDELIS_TUTOR tutor
      WHERE tutor.email = 'tutor.teste@fidelis.com.br'
  );

INSERT INTO FIDELIS_RECOMENDACAO (
    tipo, descricao, data_recomendacao, FIDELIS_PET_id, FIDELIS_CONSULTA_id
)
SELECT
    'Acompanhamento',
    'Registro de demonstração: acompanhar peso e rotina de atividade na próxima consulta.',
    DATEADD('DAY', -30, CURRENT_DATE),
    pet.id,
    consulta.id
FROM FIDELIS_PET pet
JOIN FIDELIS_CONSULTA consulta
    ON consulta.FIDELIS_PET_id = pet.id
   AND consulta.tipo = 'Acompanhamento'
WHERE pet.nome = 'Luna Teste IA';

INSERT INTO FIDELIS_LEMBRETE (
    tipo, descricao, data_prevista, status,
    FIDELIS_TUTOR_id, FIDELIS_PET_id, FIDELIS_CONSULTA_id
)
SELECT
    'Retorno',
    'Lembrete de demonstração para acompanhamento do pet.',
    DATEADD('DAY', 30, CURRENT_DATE),
    'P',
    tutor.id,
    pet.id,
    consulta.id
FROM FIDELIS_TUTOR tutor
JOIN FIDELIS_PET pet ON pet.FIDELIS_TUTOR_id = tutor.id
JOIN FIDELIS_CONSULTA consulta
    ON consulta.FIDELIS_PET_id = pet.id
   AND consulta.tipo = 'Acompanhamento'
WHERE tutor.email = 'tutor.teste@fidelis.com.br'
  AND pet.nome = 'Luna Teste IA';

INSERT INTO FIDELIS_VACINACAO (
    data_aplicacao, data_proxima, vacina_aplicada, observacao,
    FIDELIS_PET_id, FIDELIS_VETERINARIO_id
)
SELECT
    DATEADD('DAY', -120, CURRENT_DATE),
    DATEADD('DAY', 245, CURRENT_DATE),
    'V8',
    'Registro fictício de vacinação para demonstração.',
    pet.id,
    veterinario.id
FROM FIDELIS_PET pet
JOIN FIDELIS_VETERINARIO veterinario
    ON veterinario.email = 'ana.vet@fidelis.com.br'
WHERE pet.nome = 'Luna Teste IA'
  AND pet.FIDELIS_TUTOR_id = (
      SELECT tutor.id
      FROM FIDELIS_TUTOR tutor
      WHERE tutor.email = 'tutor.teste@fidelis.com.br'
  );

INSERT INTO FIDELIS_VACINACAO (
    data_aplicacao, data_proxima, vacina_aplicada, observacao,
    FIDELIS_PET_id, FIDELIS_VETERINARIO_id
)
SELECT
    DATEADD('DAY', -120, CURRENT_DATE),
    DATEADD('DAY', 245, CURRENT_DATE),
    'Antirrábica',
    'Registro fictício de vacinação para demonstração.',
    pet.id,
    veterinario.id
FROM FIDELIS_PET pet
JOIN FIDELIS_VETERINARIO veterinario
    ON veterinario.email = 'ana.vet@fidelis.com.br'
WHERE pet.nome = 'Luna Teste IA'
  AND pet.FIDELIS_TUTOR_id = (
      SELECT tutor.id
      FROM FIDELIS_TUTOR tutor
      WHERE tutor.email = 'tutor.teste@fidelis.com.br'
  );

INSERT INTO FIDELIS_VERMIFUGACAO (
    produto, data_aplicacao, data_proxima, FIDELIS_PET_id, FIDELIS_VETERINARIO_id
)
SELECT
    'Registro demonstrativo',
    DATEADD('DAY', -60, CURRENT_DATE),
    DATEADD('DAY', 120, CURRENT_DATE),
    pet.id,
    veterinario.id
FROM FIDELIS_PET pet
JOIN FIDELIS_VETERINARIO veterinario
    ON veterinario.email = 'ana.vet@fidelis.com.br'
WHERE pet.nome = 'Luna Teste IA'
  AND pet.FIDELIS_TUTOR_id = (
      SELECT tutor.id
      FROM FIDELIS_TUTOR tutor
      WHERE tutor.email = 'tutor.teste@fidelis.com.br'
  );
