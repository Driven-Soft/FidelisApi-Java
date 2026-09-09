INSERT INTO FIDELIS_VETERINARIO (cmvv, nome, email, senha, especialidade, data_criacao, FIDELIS_CLINICA_id)
VALUES ('CRMV-SP 12345', 'Dra. Ana Veterinária', 'ana.vet@fidelis.com.br',
        'senha-legada-nao-usar', 'Clínica Geral', CURRENT_DATE,
        (SELECT id FROM FIDELIS_CLINICA WHERE cnpj = '12.345.678/0001-90'));