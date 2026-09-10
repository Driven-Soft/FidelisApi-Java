# Diagrama de Entidade-Relacionamento (DER)

Este documento apresenta o Diagrama de Entidade-Relacionamento (DER) do banco de dados da aplicação FidelisApi, com base nas entidades JPA mapeadas no código (pacote `entity`). Ele mostra as tabelas, colunas, chaves primárias (PK), chaves estrangeiras (FK), chaves únicas (UK) e a cardinalidade dos relacionamentos.

![Diagrama DER](DER.png)

```mermaid
erDiagram
    FIDELIS_CLINICA ||--o{ FIDELIS_PET : abriga
    FIDELIS_CLINICA ||--o{ FIDELIS_VETERINARIO : emprega
    FIDELIS_CLINICA |o--o{ FIDELIS_USUARIO : autentica
    FIDELIS_TUTOR ||--o{ FIDELIS_PET : possui
    FIDELIS_TUTOR ||--o{ FIDELIS_LEMBRETE : gera
    FIDELIS_TUTOR |o--o{ FIDELIS_USUARIO : autentica
    FIDELIS_PET ||--o{ FIDELIS_CONSULTA : agenda
    FIDELIS_PET ||--o{ FIDELIS_VACINACAO : recebe
    FIDELIS_PET ||--o{ FIDELIS_VERMIFUGACAO : recebe
    FIDELIS_PET ||--o{ FIDELIS_HISTORICO_PESO : registra
    FIDELIS_PET ||--o{ FIDELIS_COMPORTAMENTO : registra
    FIDELIS_PET ||--o{ FIDELIS_RECOMENDACAO : recebe
    FIDELIS_PET ||--o{ FIDELIS_LEMBRETE : tem
    FIDELIS_VETERINARIO ||--o{ FIDELIS_CONSULTA : realiza
    FIDELIS_VETERINARIO ||--o{ FIDELIS_VACINACAO : aplica
    FIDELIS_VETERINARIO ||--o{ FIDELIS_VERMIFUGACAO : aplica
    FIDELIS_CONSULTA ||--o{ FIDELIS_EXAME : contem
    FIDELIS_CONSULTA ||--o{ FIDELIS_PRESCRICAO : gera
    FIDELIS_PRESCRICAO ||--o{ FIDELIS_MEDICAMENTO : inclui

    FIDELIS_CLINICA {
        bigint id PK
        varchar nome
        varchar cnpj UK
        varchar telefone
        varchar email UK
        varchar endereco
    }

    FIDELIS_TUTOR {
        bigint id PK
        varchar cpf UK
        varchar nome
        varchar email UK
        varchar senha
        varchar telefone
        varchar endereco
        date data_criacao
    }

    FIDELIS_VETERINARIO {
        bigint id PK
        varchar cmvv UK
        varchar nome
        varchar email UK
        varchar senha
        varchar especialidade
        date data_criacao
        bigint FIDELIS_CLINICA_id FK
    }

    FIDELIS_USUARIO {
        bigint id PK
        varchar email UK
        varchar senha
        varchar perfil
        boolean ativo
        date data_criacao
        bigint FIDELIS_TUTOR_id FK
        bigint FIDELIS_CLINICA_id FK
    }

    FIDELIS_PET {
        bigint id PK
        varchar nome
        varchar especie
        varchar raca
        char sexo
        date data_nascimento
        char status
        varchar foto_url
        bigint FIDELIS_TUTOR_id FK
        bigint FIDELIS_CLINICA_id FK
    }

    FIDELIS_CONSULTA {
        bigint id PK
        datetime data_hora
        varchar tipo
        varchar diagnostico
        varchar observacoes
        date data_retorno
        bigint FIDELIS_VETERINARIO_id FK
        bigint FIDELIS_PET_id FK
    }

    FIDELIS_EXAME {
        bigint id PK
        varchar tipo
        varchar descricao
        varchar resultado
        date data
        bigint FIDELIS_CONSULTA_id FK
    }

    FIDELIS_PRESCRICAO {
        bigint id PK
        varchar dosagem
        varchar frequencia
        int duracao_dias
        varchar observacao
        bigint FIDELIS_CONSULTA_id FK
    }

    FIDELIS_MEDICAMENTO {
        bigint id PK
        varchar nome
        varchar descricao
        bigint FIDELIS_PRESCRICAO_id FK
    }

    FIDELIS_VACINACAO {
        bigint id PK
        date data_aplicacao
        date data_proxima
        varchar vacina_aplicada
        text observacao
        bigint FIDELIS_PET_id FK
        bigint FIDELIS_VETERINARIO_id FK
    }

    FIDELIS_VERMIFUGACAO {
        bigint id PK
        varchar produto
        date data_aplicacao
        date data_proxima
        bigint FIDELIS_PET_id FK
        bigint FIDELIS_VETERINARIO_id FK
    }

    FIDELIS_HISTORICO_PESO {
        bigint id PK
        decimal peso_kg
        date data_medicao
        varchar observacao
        bigint FIDELIS_PET_id FK
    }

    FIDELIS_COMPORTAMENTO {
        bigint id PK
        date data
        varchar descricao
        bigint FIDELIS_PET_id FK
    }

    FIDELIS_RECOMENDACAO {
        bigint id PK
        varchar tipo
        varchar descricao
        date data_recomendacao
        bigint FIDELIS_PET_id FK
    }

    FIDELIS_LEMBRETE {
        bigint id PK
        varchar tipo
        varchar descricao
        date data_prevista
        char status
        bigint FIDELIS_TUTOR_id FK
        bigint FIDELIS_PET_id FK
    }
```

## Descrição das entidades e relacionamentos

- **FIDELIS_CLINICA**: cadastro das clínicas veterinárias. Uma clínica emprega vários `FIDELIS_VETERINARIO` e abriga vários `FIDELIS_PET`.
- **FIDELIS_TUTOR**: donos dos pets. Um tutor possui vários `FIDELIS_PET` e gera vários `FIDELIS_LEMBRETE`.
- **FIDELIS_VETERINARIO**: vinculado a uma `FIDELIS_CLINICA`; realiza consultas, aplica vacinações e vermifugações.
- **FIDELIS_USUARIO**: tabela de autenticação/login, associada opcionalmente a um `FIDELIS_TUTOR` ou a uma `FIDELIS_CLINICA` (conforme o perfil de acesso).
- **FIDELIS_PET**: entidade central do modelo. Pertence a um `FIDELIS_TUTOR` e a uma `FIDELIS_CLINICA`; concentra consultas, vacinações, vermifugações, histórico de peso, comportamentos, recomendações e lembretes.
- **FIDELIS_CONSULTA**: relaciona um `FIDELIS_VETERINARIO` e um `FIDELIS_PET`; agrega `FIDELIS_EXAME` e `FIDELIS_PRESCRICAO`.
- **FIDELIS_EXAME**: exames vinculados a uma consulta.
- **FIDELIS_PRESCRICAO**: prescrição médica de uma consulta, que agrega vários `FIDELIS_MEDICAMENTO`.
- **FIDELIS_VACINACAO** e **FIDELIS_VERMIFUGACAO**: procedimentos aplicados a um `FIDELIS_PET` por um `FIDELIS_VETERINARIO`.
- **FIDELIS_HISTORICO_PESO**, **FIDELIS_COMPORTAMENTO** e **FIDELIS_RECOMENDACAO**: registros de acompanhamento vinculados diretamente ao `FIDELIS_PET`.
- **FIDELIS_LEMBRETE**: compromissos/tarefas vinculados a um `FIDELIS_TUTOR` e a um `FIDELIS_PET`, com status controlado (`P` = pendente, `C` = concluído).

> Observação: os relacionamentos de `FIDELIS_USUARIO` com `FIDELIS_TUTOR` e `FIDELIS_CLINICA` são opcionais (colunas FK nulas), pois um usuário se autentica apenas em um dos dois perfis (`Perfil.TUTOR` ou `Perfil.CLINICA`).
