# Diagrama de Classes de Entidade (DCE)

Este documento apresenta o Diagrama de Classes de Entidade (DCE) da aplicação FidelisApi, com base nas classes JPA do pacote `entity`, seus atributos, enums e relacionamentos.

![Diagrama DCE](DCE.png)

```mermaid
classDiagram
    class Clinica {
        +Long id
        +String nome
        +String cnpj
        +String telefone
        +String email
        +String endereco
        +Set~Veterinario~ veterinarios
        +Set~Pet~ pets
    }

    class Tutor {
        +Long id
        +String cpf
        +String nome
        +String email
        +String senha
        +String telefone
        +String endereco
        +LocalDate dataCriacao
        +Set~Pet~ pets
        +Set~Lembrete~ lembretes
    }

    class Veterinario {
        +Long id
        +String cmvv
        +String nome
        +String email
        +String senha
        +String especialidade
        +LocalDate dataCriacao
        +Clinica clinica
        +Set~Consulta~ consultas
        +Set~Vacinacao~ vacinacoes
        +Set~Vermifugacao~ vermifugacoes
    }

    class Usuario {
        +Long id
        +String email
        +String senha
        +Perfil perfil
        +boolean ativo
        +LocalDate dataCriacao
        +Tutor tutor
        +Clinica clinica
    }

    class Pet {
        +Long id
        +String nome
        +String especie
        +String raca
        +SexoPet sexo
        +LocalDate dataNascimento
        +PetStatus status
        +String fotoUrl
        +Tutor tutor
        +Clinica clinica
        +Set~Consulta~ consultas
        +Set~Vacinacao~ vacinacoes
        +Set~Vermifugacao~ vermifugacoes
        +Set~HistoricoPeso~ historicoPeso
        +Set~Comportamento~ comportamentos
        +Set~Recomendacao~ recomendacoes
        +Set~Lembrete~ lembretes
    }

    class Consulta {
        +Long id
        +LocalDateTime dataHora
        +String tipo
        +String diagnostico
        +String observacoes
        +LocalDate dataRetorno
        +Veterinario veterinario
        +Pet pet
        +Set~Exame~ exames
        +Set~Prescricao~ prescricoes
    }

    class Exame {
        +Long id
        +String tipo
        +String descricao
        +String resultado
        +LocalDate data
        +Consulta consulta
    }

    class Prescricao {
        +Long id
        +String dosagem
        +String frequencia
        +Integer duracaoDias
        +String observacao
        +Consulta consulta
        +Set~Medicamento~ medicamentos
    }

    class Medicamento {
        +Long id
        +String nome
        +String descricao
        +Prescricao prescricao
    }

    class Vacinacao {
        +Long id
        +LocalDate dataAplicacao
        +LocalDate dataProxima
        +String vacinaAplicada
        +String observacao
        +Pet pet
        +Veterinario veterinario
    }

    class Vermifugacao {
        +Long id
        +String produto
        +LocalDate dataAplicacao
        +LocalDate dataProxima
        +Pet pet
        +Veterinario veterinario
    }

    class HistoricoPeso {
        +Long id
        +BigDecimal pesoKg
        +LocalDate dataMedicao
        +String observacao
        +Pet pet
    }

    class Comportamento {
        +Long id
        +LocalDate data
        +String descricao
        +Pet pet
    }

    class Recomendacao {
        +Long id
        +String tipo
        +String descricao
        +LocalDate dataRecomendacao
        +Pet pet
    }

    class Lembrete {
        +Long id
        +String tipo
        +String descricao
        +LocalDate dataPrevista
        +LembreteStatus status
        +Tutor tutor
        +Pet pet
    }

    class Perfil {
        <<enumeration>>
        CLINICA
        TUTOR
    }

    class SexoPet {
        <<enumeration>>
        M
        F
    }

    class PetStatus {
        <<enumeration>>
        ATIVO
        INATIVO
    }

    class LembreteStatus {
        <<enumeration>>
        PENDENTE
        CONCLUIDO
    }

    Clinica "1" --> "*" Pet : abriga
    Clinica "1" --> "*" Veterinario : emprega
    Clinica "1" --> "0..*" Usuario : autentica
    Tutor "1" --> "*" Pet : possui
    Tutor "1" --> "*" Lembrete : gera
    Tutor "1" --> "0..*" Usuario : autentica
    Pet "1" --> "*" Consulta : agenda
    Pet "1" --> "*" HistoricoPeso : registra
    Pet "1" --> "*" Vacinacao : recebe
    Pet "1" --> "*" Vermifugacao : recebe
    Pet "1" --> "*" Recomendacao : recebe
    Pet "1" --> "*" Comportamento : registra
    Pet "1" --> "*" Lembrete : tem
    Veterinario "1" --> "*" Consulta : realiza
    Veterinario "1" --> "*" Vacinacao : aplica
    Veterinario "1" --> "*" Vermifugacao : aplica
    Consulta "1" --> "*" Exame : contem
    Consulta "1" --> "*" Prescricao : produz
    Prescricao "1" --> "*" Medicamento : inclui
    Pet ..> SexoPet : usa
    Pet ..> PetStatus : usa
    Usuario ..> Perfil : usa
    Lembrete ..> LembreteStatus : usa
```

## Descrição do diagrama

- `Clinica` tem muitos `Pet` e muitos `Veterinario`, e pode ter usuários de acesso (`Usuario`) vinculados a ela.
- `Tutor` tem muitos `Pet` e muitos `Lembrete`, e também pode ter usuários de acesso vinculados.
- `Usuario` representa o login do sistema e se relaciona opcionalmente com `Tutor` **ou** `Clinica`, conforme o `Perfil` (enum `CLINICA`/`TUTOR`).
- `Pet` é o centro do modelo e compõe consultas, vacinas, vermifugações, recomendações, comportamentos, histórico de peso e lembretes. Usa os enums `SexoPet` e `PetStatus`.
- `Consulta` relaciona um `Veterinario` e um `Pet`, e agrega `Exame` e `Prescricao`.
- `Prescricao` agrega vários `Medicamento`.
- `Lembrete` captura compromissos para `Tutor` e `Pet`, com status controlado pelo enum `LembreteStatus`.
- `HistoricoPeso`, `Recomendacao` e `Comportamento` guardam informações de monitoramento do `Pet`.
