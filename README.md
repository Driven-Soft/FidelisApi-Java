# FidelisApi

API REST construída em Spring Boot para gestão de clínica veterinária. O projeto oferece um conjunto completo de endpoints para cadastro e consulta de clínicas, tutores, pets, veterinários, consultas, vacinas, vermifugações, exames, prescrições, recomendações, lembretes e histórico de peso.

**Repositório GitHub:** [https://github.com/Driven-Soft/FidelisApi-Java](https://github.com/Driven-Soft/FidelisApi-Java)

## Tecnologias

- Java 17
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

Para instruções detalhadas de execução, veja: **[COMO_EXECUTAR.md](documentos/COMO_EXECUTAR.md)**

Resumidamente:

```powershell
# Clonar repositório
git clone https://github.com/Driven-Soft/FidelisApi-Java.git
cd FidelisApi-Java

# Compilar e executar
.\mvnw.cmd spring-boot:run
```

Ou gerar o `jar` e executar:

```powershell
.\mvnw.cmd clean package
java -jar target/FidelisApi-0.0.1-SNAPSHOT.jar
```

A aplicação estará disponível em: **http://localhost:8080**

## Coleção Postman / Insomnia

Há uma coleção de exemplo em `documentos/postman_collection.json` que contém requisições básicas (listar, criar, buscar por id, atualizar, deletar) para os recursos principais.

- Importe `documentos/postman_collection.json` no Postman ou Insomnia.
- Ajuste a variável `baseUrl` para `http://localhost:8080` antes de executar as requisições.

## Testes

Para executar a suíte de testes automatizados do projeto:

```bash
./mvnw test
```

Os testes usam H2 em memória e são executáveis localmente.

## Endpoints principais

As rotas seguem o padrão `/api/v1/{recurso}`. Exemplos:

- `GET /api/v1/pets`
- `POST /api/v1/tutores`
- `GET /api/v1/consultas`
- `POST /api/v1/vacinacoes`
- `GET /api/v1/veterinarios`
- `POST /api/v1/clinicas`

## Validação

- `@Cpf` - valida CPF
- `@Cnpj` - valida CNPJ
- Campos obrigatórios e formatos de texto são validados pelo Bean Validation

## Observações

- A aplicação usa H2 em memória para facilitar testes e desenvolvimento.
- A organização do código segue um padrão de camadas para manter separação entre API, negócio e persistência.
- O projeto está preparado para evoluir para conexão com banco de dados real (Oracle ou outro) apenas ajustando a configuração de `spring.datasource`.
