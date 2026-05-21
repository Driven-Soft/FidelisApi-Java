# Diagrama de Classes de Entidade

Este documento apresenta o diagrama de classes das entidades principais da aplicação FidelisApi. Ele descreve as entidades JPA e seus relacionamentos.

```mermaid
classDiagram
    class Clinica {
        +Long id
        +String nome
        +String cnpj
        +String telefone
        +String endereco
        +List~Pet~ pets
        +List~Veterinario~ veterinarios
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

    class Pet {
        +Long id
        +String nome
        +String especie
        +String raca
        +SexoPet sexo
        +LocalDate dataNascimento
        +PetStatus status
        +String fotoUrl
        +Clinica clinica
        +Tutor tutor
        +Set~Consulta~ consultas
        +Set~HistoricoPeso~ historicoPeso
        +Set~Vacinacao~ vacinacoes
        +Set~Vermifugacao~ vermifugacoes
        +Set~Recomendacao~ recomendacoes
        +Set~Comportamento~ comportamentos
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
        +String resultado
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
        +String vacinaAplicada
        +LocalDate dataAplicacao
        +LocalDate dataProxima
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

    class Recomendacao {
        +Long id
        +String tipo
        +String descricao
        +LocalDate dataRecomendacao
        +Pet pet
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

    class Lembrete {
        +Long id
        +String tipo
        +String descricao
        +LocalDate dataPrevista
        +LembreteStatus status
        +Tutor tutor
        +Pet pet
    }

    Clinica "1" --> "*" Pet : abriga
    Clinica "1" --> "*" Veterinario : emprega
    Tutor "1" --> "*" Pet : possui
    Tutor "1" --> "*" Lembrete : gera
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
    Consulta "1" --> "*" Exame : contém
    Consulta "1" --> "*" Prescricao : produz
    Prescricao "1" --> "*" Medicamento : inclui
```

## Descrição do diagrama

- `Clinica` tem muitos `Pet` e muitos `Veterinario`.
- `Tutor` tem muitos `Pet` e muitos `Lembrete`.
- `Pet` é o centro do modelo e compõe consultas, vacinas, vermifugações, recomendações, comportamentos, histórico de peso e lembretes.
- `Consulta` relaciona um `Veterinario` e um `Pet`, e agrega `Exame` e `Prescricao`.
- `Prescricao` agrega vários `Medicamento`.
- `Lembrete` captura compromissos para `Tutor` e `Pet`.
- `HistoricoPeso`, `Recomendacao` e `Comportamento` guardam informações de monitoramento do `Pet`.
