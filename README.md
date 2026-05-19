# FidelisApi

API REST construída em Spring Boot para gestão de clínica veterinária. O projeto oferece um conjunto completo de endpoints para cadastro e consulta de clínicas, tutores, pets, veterinários, consultas, vacinas, vermifugações, exames, prescrições, recomendações, lembretes e histórico de peso.

## Tecnologias

- Java 21
- Spring Boot 4
- Spring Data JPA
- H2 Database (memória) para desenvolvimento
- SpringDoc OpenAPI / Swagger UI
- Bean Validation (Jakarta Validation)
- Cache simples do Spring
- Estrutura em DTOs para separar entidade e API

## Estrutura do projeto

- `src/main/java/br/com/fiap/java/FidelisApi`
  - `controller/` - endpoints REST
  - `service/` - regras de negócio e persistência
  - `repository/` - interfaces JPA para acesso a dados
  - `entity/` - mapeamento JPA das tabelas
  - `dto/request/` - classes de requisição para entrada de dados
  - `dto/response/` - classes de resposta para saída de dados
  - `exception/` - tratamento global de exceções
  - `config/` - configuração de cache, Swagger e segurança
  - `validation/` - validação personalizada de CPF e CNPJ

## Configuração

O projeto já está configurado para rodar com H2 em memória no arquivo `src/main/resources/application.yaml`.

- Banco: `jdbc:h2:mem:fidelisdb`
- Console H2: `http://localhost:8080/h2-console`
- Swagger UI: `http://localhost:8080/swagger-ui.html`

## Como executar

1. Abrir o terminal na pasta do projeto
2. Executar:

```bash
./mvnw spring-boot:run
```

ou gerar o `jar` e executar:

```bash
./mvnw clean package
java -jar target/FidelisApi-0.0.1-SNAPSHOT.jar
```

## Endpoints principais

As rotas seguem o padrão `/api/{recurso}`. Exemplos:

- `GET /api/pets`
- `POST /api/tutores`
- `GET /api/consultas`
- `POST /api/vacinacoes`
- `GET /api/veterinarios`
- `POST /api/clinicas`

## Validação

- `@Cpf` - valida CPF
- `@Cnpj` - valida CNPJ
- Campos obrigatórios e formatos de texto são validados pelo Bean Validation

## Observações

- A aplicação usa H2 em memória para facilitar testes e desenvolvimento.
- A organização do código segue um padrão de camadas para manter separação entre API, negócio e persistência.
- O projeto está preparado para evoluir para conexão com banco de dados real (Oracle ou outro) apenas ajustando a configuração de `spring.datasource`.
