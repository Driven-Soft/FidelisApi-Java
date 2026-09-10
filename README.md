# FidelisApi

<p align="center">
  <img src="src/main/resources/static/images/fidelis-logo.svg" alt="Logo Fidelis" width="180">
</p>

## Equipe Driven Soft

### Integrantes

| Nome                               | RM        |
| ---------------------------------- | --------- |
| Felipe Bezerra Beatrici            | RM 564723 |
| Max Hayashi Batista                | RM 563717 |
| Henrique Cunha Torres              | RM 565119 |
| Lucas da Silva Lima                | RM 562118 |
| Yasmin Nathalin Miranda dos Santos | RM 561365 |

<hr/>

Sistema de gestão para clínicas veterinárias, construído em Spring Boot. O projeto oferece uma **API REST** completa para cadastro e consulta de clínicas, tutores, pets, veterinários, consultas, vacinas, vermifugações, exames, prescrições, medicamentos, recomendações, lembretes e histórico de peso — além de uma **camada web (Thymeleaf)** com login e telas próprias para os perfis de Clínica e Tutor.

**Repositório GitHub:** [https://github.com/Driven-Soft/FidelisApi-Java](https://github.com/Driven-Soft/FidelisApi-Java)

## Vídeo de demonstração

> **Link:** [Demonstração da solução FidelisApi - Driven Soft (Challenge - 2026)](https://www.youtube.com/watch?v=BTbOPOLEyM0)

O vídeo apresenta a página inicial, autenticação com os dois perfis, cadastro de pet, registro de consulta, geração de lembrete e recomendação, vacinação, retenção, histórico do Tutor e bloqueio de acesso entre perfis.

## Tecnologias

- Java 17
- Spring Boot 4
- Spring Data JPA / Hibernate
- Spring Security (login por formulário, senhas com BCrypt, autorização por perfil)
- Flyway (versionamento e migração do schema do banco)
- H2 Database em memória para desenvolvimento e demonstração
- Driver Oracle `ojdbc11` declarado no `pom.xml` para uma futura configuração Oracle; a execução atual utiliza H2 e as migrations não foram validadas como implantação Oracle
- Thymeleaf (telas web)
- SpringDoc OpenAPI / Swagger UI
- Bean Validation (Jakarta Validation), com validadores customizados de CPF e CNPJ
- Cache simples do Spring (`@Cacheable` / `@CacheEvict`)
- Lombok
- Estrutura em DTOs para separar entidade e API

## Funcionalidades principais

- CRUD completo dos recursos do domínio, com paginação, ordenação e busca por parâmetros
- Autenticação por sessão com dois perfis de acesso: **Clínica** e **Tutor**, cada um com suas próprias telas e permissões
- Geração automática de **lembrete** e **recomendação** ao registrar uma nova consulta
- Alerta de **retenção/churn**: identifica pets sem consulta há 90 dias ou mais
- Página pública de boas-vindas em `/home`, com acesso ao login
- Documentação interativa via Swagger UI e coleção Postman pronta para uso

## Estrutura do projeto

- `src/main/java/br/com/fiap/java/FidelisApi`
  - `controller/api/` - endpoints REST (`/api/v1/**`)
  - `controller/web/` - telas MVC com Thymeleaf (login, dashboard, telas de Clínica e Tutor)
  - `service/` - regras de negócio e persistência
  - `repository/` - interfaces JPA para acesso a dados
  - `entity/` - mapeamento JPA das tabelas (15 entidades, incluindo `Usuario`)
  - `dto/request/` - classes de requisição para entrada de dados
  - `dto/response/` - classes de resposta para saída de dados
  - `mapper/` - conversão entre entidades e DTOs
  - `exception/` - tratamento global de exceções (escopo restrito à API)
  - `config/` - configuração de cache, Swagger/OpenAPI e segurança
  - `validation/` - validações de CPF, CNPJ, datas, textos e regras entre campos
- `src/main/resources`
  - `application.yaml` - configuração da aplicação (datasource, JPA, Flyway, Swagger, Actuator)
  - `db/migration/` - scripts versionados do Flyway (schema + dados de teste)

## Configuração

O projeto já está configurado para rodar com H2 em memória no arquivo `src/main/resources/application.yaml`, com o schema criado automaticamente pelas migrations do Flyway ao subir a aplicação.

- Banco: `jdbc:h2:mem:fidelisdb`
- Console H2: `http://localhost:8080/h2-console`
- Swagger UI: `http://localhost:8080/swagger-ui.html`
- Tela de login: `http://localhost:8080/login`
- Página pública: `http://localhost:8080/home`

## Como executar

Para instruções detalhadas de execução, veja: **[COMO_EXECUTAR.md](documentos/requisitos/COMO_EXECUTAR.md)**

Resumidamente:

```bash
# Clonar repositório
git clone https://github.com/Driven-Soft/FidelisApi-Java.git
cd FidelisApi-Java

# Compilar e executar (Linux/macOS)
./mvnw spring-boot:run

# Compilar e executar (Windows)
.\mvnw.cmd spring-boot:run
```

Ou gerar o `jar` e executar:

```bash
./mvnw clean package
java -jar target/FidelisApi-0.0.1-SNAPSHOT.jar
```

A aplicação estará disponível em: **http://localhost:8080**

## Login e perfis de acesso

A página inicial pública está disponível em `/home` e a raiz `/` redireciona para ela. Para acessar o dashboard e as áreas protegidas, use o login. As migrations do Flyway já criam usuários de teste:

| Perfil  | E-mail                 | Senha    |
| ------- | ---------------------- | -------- |
| Clínica | clinica@fidelis.com.br | Senha123 |
| Tutor   | tutor@fidelis.com.br   | Senha123 |

Após o login você é redirecionado para `/dashboard`. Rotas em `/clinica/**` exigem perfil Clínica e rotas em `/tutor/**` exigem perfil Tutor.

A API REST é administrativa e exige o perfil `CLINICA` para leitura e escrita. O perfil `TUTOR` utiliza as telas web autorizadas.

## Coleção Postman / Insomnia

Há uma coleção em `documentos/api/postman_collection.json`, cobrindo autenticação, CRUD dos recursos principais e os fluxos de negócio (geração automática de lembrete/recomendação, alerta de retenção).

- Importe `documentos/api/postman_collection.json` no Postman ou Insomnia.
- Ajuste a variável `baseUrl` para `http://localhost:8080` antes de executar as requisições.
- Autentique-se como `CLINICA` antes de chamar endpoints protegidos da API. O perfil `TUTOR` recebe `403` na API e utiliza a camada web.

## Testes

Para executar a suíte de testes automatizados do projeto:

```bash
./mvnw test
```

Os testes cobrem contexto da aplicação, mappers, services, repositórios, segurança (login/perfis/proteção de rotas), fluxos de negócio e validações de Pet, Vacinação, Consulta, Clínica, Tutor e Veterinário. A suíte atual possui 79 testes aprovados e utiliza H2 em memória.

## Endpoints principais

As rotas da API seguem o padrão `/api/v1/{recurso}`. Exemplos:

- `GET /api/v1/pets`
- `POST /api/v1/tutores`
- `GET /api/v1/consultas`
- `POST /api/v1/vacinacoes`
- `GET /api/v1/veterinarios`
- `POST /api/v1/clinicas`
- `GET /api/v1/clinicas/{id}/retencao` - pets em risco de retenção/churn (perfil Clínica)

Leituras (`GET`) e escritas (`POST`/`PUT`/`PATCH`/`DELETE`) exigem perfil `CLINICA`. O perfil `TUTOR` utiliza a camada web autorizada.

## Validação

- `@Cpf` - valida CPF
- `@Cnpj` - valida CNPJ
- Campos obrigatórios, tamanhos e formatos de texto são validados pelo Bean Validation
- Pets: nome, espécie, raça, data de nascimento plausível e campos obrigatórios
- Vacinações: vacina, data de aplicação, próxima dose e coerência entre datas
- Consultas: tipo, datas de retorno, diagnóstico, observações e vínculos obrigatórios
- Clínica, Tutor e Veterinário: nomes, e-mails, telefones, endereços, identificadores e datas
- Os formulários web também possuem validação nativa do navegador, mas o backend permanece como validação definitiva

## Documentação adicional

- **Como executar**: [`documentos/requisitos/COMO_EXECUTAR.md`](documentos/requisitos/COMO_EXECUTAR.md)
- **Requisitos técnicos**: [`documentos/requisitos/REQUISITOS_TECNICOS.md`](documentos/requisitos/REQUISITOS_TECNICOS.md)
- **Diagrama Entidade-Relacionamento (DER)**: [`documentos/arquitetura/DER.md`](documentos/arquitetura/DER.md)
- **Diagrama de Classes (DCE)**: [`documentos/arquitetura/DCE.md`](documentos/arquitetura/DCE.md)
- **Coleção Postman**: [`documentos/api/postman_collection.json`](documentos/api/postman_collection.json)

## Observações

- A aplicação usa H2 em memória para facilitar testes e desenvolvimento, com schema controlado por migrations Flyway versionadas em `db/migration`.
- A organização do código segue um padrão de camadas para manter separação entre API, negócio e persistência.
- O driver Oracle `ojdbc11` está incluído no `pom.xml`, mas a aplicação desta Sprint utiliza H2 em memória. Uma futura migração para Oracle exigirá configurar o datasource, revisar as migrations conforme o dialeto Oracle e validar o ambiente de implantação.
