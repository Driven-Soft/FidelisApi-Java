INSERT INTO FIDELIS_CLINICA (nome, cnpj, telefone, email, endereco)
VALUES ('Clínica Fidelis Teste', '12.345.678/0001-90', '(11) 91234-5678',
        'contato@clinicafidelis.com.br', 'Rua de Teste, 100 - São Paulo/SP');

INSERT INTO FIDELIS_TUTOR (cpf, nome, email, senha, telefone, endereco, data_criacao)
VALUES ('123.456.789-00', 'Tutor Teste', 'tutor.teste@fidelis.com.br',
        'senha-legada-nao-usar', '(11) 98888-7777', 'Av. Teste, 200 - São Paulo/SP', CURRENT_DATE);

-- Usuário de login da Clínica (perfil CLINICA)
-- e-mail: clinica@fidelis.com.br | senha: Senha123
INSERT INTO FIDELIS_USUARIO (email, senha, perfil, ativo, data_criacao, FIDELIS_CLINICA_id)
VALUES ('clinica@fidelis.com.br',
        '$2a$10$fcb46n9Cln2OODcsAJart.7I89KJrxSYAf1Ur.t1NQ3wW7EgB1/Ke',
        'CLINICA', TRUE, CURRENT_DATE,
        (SELECT id FROM FIDELIS_CLINICA WHERE cnpj = '12.345.678/0001-90'));

-- Usuário de login do Tutor (perfil TUTOR)
-- e-mail: tutor@fidelis.com.br | senha: Senha123
INSERT INTO FIDELIS_USUARIO (email, senha, perfil, ativo, data_criacao, FIDELIS_TUTOR_id)
VALUES ('tutor@fidelis.com.br',
        '$2a$10$fcb46n9Cln2OODcsAJart.7I89KJrxSYAf1Ur.t1NQ3wW7EgB1/Ke',
        'TUTOR', TRUE, CURRENT_DATE,
        (SELECT id FROM FIDELIS_TUTOR WHERE cpf = '123.456.789-00'));