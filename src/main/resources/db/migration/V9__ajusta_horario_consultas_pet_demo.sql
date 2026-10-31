UPDATE FIDELIS_CONSULTA
SET data_hora = DATEADD(
    'MINUTE',
    30,
    DATEADD('HOUR', 21, CAST(CAST(data_hora AS DATE) AS TIMESTAMP))
)
WHERE tipo IN ('Consulta de rotina', 'Acompanhamento')
  AND FIDELIS_PET_id IN (
      SELECT pet.id
      FROM FIDELIS_PET pet
      JOIN FIDELIS_TUTOR tutor ON tutor.id = pet.FIDELIS_TUTOR_id
      WHERE pet.nome = 'Luna Teste IA'
        AND tutor.email = 'tutor.teste@fidelis.com.br'
  );
