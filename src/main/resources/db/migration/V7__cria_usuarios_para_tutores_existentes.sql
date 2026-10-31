INSERT INTO FIDELIS_USUARIO (email, senha, perfil, ativo, data_criacao, FIDELIS_TUTOR_id)
SELECT tutor.email, tutor.senha, 'TUTOR', TRUE, tutor.data_criacao, tutor.id
FROM FIDELIS_TUTOR tutor
WHERE NOT EXISTS (
    SELECT 1
    FROM FIDELIS_USUARIO usuario
    WHERE LOWER(usuario.email) = LOWER(tutor.email)
)
AND tutor.id = (
    SELECT MIN(tutor_duplicado.id)
    FROM FIDELIS_TUTOR tutor_duplicado
    WHERE LOWER(tutor_duplicado.email) = LOWER(tutor.email)
      AND NOT EXISTS (
          SELECT 1
          FROM FIDELIS_USUARIO usuario_existente
          WHERE LOWER(usuario_existente.email) = LOWER(tutor_duplicado.email)
      )
);
