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
        +String nome
        +String cpf
        +String email
        +String telefone
        +String endereco
        +List~Pet~ pets
        +List~Lembrete~ lembretes
    }

    class Pet {
        +Long id
        +String nome
        +String especie
        +String raca
        +SexoPet sexo
        +LocalDate dataNascimento
        +Double pesoAtual
        +Clinica clinica
        +Tutor tutor
        +List~Consulta~ consultas
        +List~HistoricoPeso~ historicoPeso
        +List~Vacinacao~ vacinacoes
        +List~Vermifugacao~ vermifugacoes
        +List~Recomendacao~ recomendacoes
        +List~Comportamento~ comportamentos
        +List~Lembrete~ lembretes
    }

    class Veterinario {
        +Long id
        +String nome
        +String crmv
        +String email
        +String telefone
        +String especialidade
        +Clinica clinica
        +List~Consulta~ consultas
        +List~Vacinacao~ vacinacoes
        +List~Vermifugacao~ vermifugacoes
    }

    class Consulta {
        +Long id
        +String tipo
        +LocalDateTime dataHora
        +String descricao
        +Veterinario veterinario
        +Pet pet
        +List~Exame~ exames
        +List~Prescricao~ prescricoes
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
        +String freQuencia
        +String duracao
        +Consulta consulta
        +List~Medicamento~ medicamentos
    }

    class Medicamento {
        +Long id
        +String nome
        +String descricao
        +String posologia
        +Prescricao prescricao
    }

    class Vacinacao {
        +Long id
        +String vacina
        +LocalDate dataAplicacao
        +Pet pet
        +Veterinario veterinario
    }

    class Vermifugacao {
        +Long id
        +String produto
        +LocalDate dataAplicacao
        +Pet pet
        +Veterinario veterinario
    }

    class Recomendacao {
        +Long id
        +String descricao
        +Pet pet
    }

    class HistoricoPeso {
        +Long id
        +Double pesoKg
        +LocalDate dataRegistro
        +Pet pet
    }

    class Comportamento {
        +Long id
        +String descricao
        +Pet pet
    }

    class Lembrete {
        +Long id
        +String titulo
        +String descricao
        +LocalDateTime dataHora
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
