# Requisitos Técnicos - FidelisApi

## Visão Geral

Este documento detalha como cada requisito técnico foi implementado na aplicação FidelisApi (Sistema de Gerenciamento de Clínica Veterinária). A aplicação expõe uma **API REST** (`/api/v1/**`) e também uma **camada web com Thymeleaf** (`/login`, `/dashboard`, `/clinica/**`, `/tutor/**`), protegidas por autenticação e autorização baseadas em perfil.

---

## 1. Validação de Campos com Bean Validation

**Status:** ✓ Implementado

### Detalhes:

- Utiliza `jakarta.validation.constraints` para validação declarativa
- Validadores customizados (`@Cpf`, `@Cnpj`) implementados em `validation/`
- Anotações utilizadas:
  - `@NotBlank` - Campos obrigatórios
  - `@Email` - Validação de email
  - `@Size` - Tamanho máximo/mínimo
  - `@Cpf` - Validação de CPF (customizado)
  - `@Cnpj` - Validação de CNPJ (customizado)

### Exemplos:

```java
// PetRequest.java
@NotBlank(message = "Nome do pet é obrigatório")
@Size(max = 30)
private String nome;

// TutorRequest.java
@Cpf
private String cpf;

// ClinicaRequest.java
@Cnpj
private String cnpj;
```

---

## 2. Paginação de Resultados

**Status:** ✓ Implementado

### Detalhes:

- Utiliza `Spring Data JPA` com `Page<T>` e `Pageable`
- Todos os endpoints de listagem suportam paginação
- Parâmetros: `page` (página), `size` (quantidade por página)

### Exemplo:

```java
// ClinicaController.java
@GetMapping
public ResponseEntity<Page<ClinicaResponse>> listar(
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "10") int size
) {
    Pageable pageable = PageRequest.of(page, size);
    Page<ClinicaResponse> response = clinicaService.findAll(nome, pageable)
            .map(ClinicaMapper::toResponse);
    return ResponseEntity.ok(response);
}
```

---

## 3. Ordenação de Resultados

**Status:** ✓ Implementado

### Detalhes:

- Utiliza `Spring Data Sort` para ordenação dinâmica
- Parâmetros: `sort` (campo) e `direction` (ASC/DESC)
- Padrão RESTful implementado

### Exemplo:

```java
@RequestParam(defaultValue = "id") String sort,
@RequestParam(defaultValue = "ASC") String direction

Pageable pageable = PageRequest.of(page, size,
    Sort.Direction.fromString(direction), sort);
```

---

## 4. Busca com Parâmetros

**Status:** ✓ Implementado

### Detalhes:

- Cada controlador implementa filtros específicos
- Utiliza `@RequestParam` para parâmetros opcionais
- Métodos `findAll()` dos serviços implementam lógica de filtro

### Exemplos:

```java
// ClinicaController - Filtro por nome
@RequestParam(required = false) String nome

// PetController - Filtros por nome e espécie
@RequestParam(required = false) String nome,
@RequestParam(required = false) String especie

// ConsultaController - Filtros por tipo, data inicial e final
@RequestParam(required = false) String tipo,
@RequestParam(required = false) LocalDateTime inicio,
@RequestParam(required = false) LocalDateTime fim
```

---

## 5. Uso de Cache para Otimizar Requisições

**Status:** ✓ Implementado

### Detalhes:

- Configurado em `config/CacheConfig.java` (`@EnableCaching`, tipo Simple Cache do Spring)
- Anotações:
  - `@Cacheable("recurso")` - Cacheia resultados de reads
  - `@CacheEvict(value="recurso", allEntries=true)` - Limpa cache em mutações

### Exemplo:

```java
// ClinicaService.java
@Cacheable("clinicas")
public Page<Clinica> findAll(String nome, Pageable pageable) {
    // Query executada apenas na primeira vez
}

@CacheEvict(value = "clinicas", allEntries = true)
public Clinica create(Clinica clinica) {
    // Cache limpado após criar nova clínica
}
```

---

## 6. Autenticação e Autorização

**Status:** ✓ Implementado

### Detalhes:

- `Spring Security` com login por formulário (`formLogin`), sessão HTTP (`SessionCreationPolicy.IF_REQUIRED`) e suporte adicional a Basic Auth para chamadas diretas à API
- Senhas armazenadas com hash `BCrypt` (`PasswordEncoder` em `config/SecurityConfig.java`)
- `UsuarioDetailsService` (`service/UsuarioDetailsService.java`) implementa `UserDetailsService`, carregando o `Usuario` pelo e-mail e expondo a authority `ROLE_<PERFIL>` (`CLINICA` ou `TUTOR`)
- Regra de vínculo reforçada em código (além de `CHECK` no banco): um `Usuario` do perfil `CLINICA` precisa estar ligado a uma `Clinica` (e nunca a um `Tutor`), e vice-versa para `TUTOR`
- Autorização por rota configurada em `config/SecurityConfig.java`:
  - Rotas públicas: `/login`, `/acesso-negado`, assets estáticos, Swagger, H2 Console, `/actuator/health` e `/actuator/info`
  - `GET /api/v1/**`: qualquer usuário autenticado
  - Escrita (`POST`/`PUT`/`PATCH`/`DELETE`) em `/api/v1/**`: exige `ROLE_CLINICA`
  - `GET /api/v1/clinicas/{id}/retencao`: exige `ROLE_CLINICA`
  - `/clinica/**` (telas MVC): exige `ROLE_CLINICA`
  - `/tutor/**` (telas MVC): exige `ROLE_TUTOR`
- Acesso negado a uma rota protegida redireciona para `/acesso-negado` (MVC) ou retorna `403` (API)

### Observação:

- O `pom.xml` já inclui as dependências `jjwt-api`/`jjwt-impl`/`jjwt-jackson` para suportar autenticação via token (JWT) no futuro, mas a autenticação em uso atualmente é baseada em sessão (form login), não em JWT.

---

## 7. Tratamento de Erros/Exceções

**Status:** ✓ Implementado

### Detalhes:

- `GlobalExceptionHandler.java` centraliza o tratamento de exceções **da camada de API**
- Utiliza `@RestControllerAdvice(basePackages = "br.com.fiap.java.FidelisApi.controller.api")`, restrito de propósito ao pacote `controller.api` para não conflitar com o tratamento de erros do Spring Security nas telas MVC (`controller.web`)
- Respostas padronizadas em `ApiErrorResponse`

### Tipos de Exceção Tratados:

- `ResourceNotFoundException` - Recurso não encontrado (404)
- `BusinessException` - Erros de negócio
- `ConstraintViolationException` - Violação de constraints
- `MethodArgumentNotValidException` - Validação de request
- Exceções genéricas

### Exemplo:

```java
@ExceptionHandler(ResourceNotFoundException.class)
public ResponseEntity<ApiErrorResponse> handleResourceNotFound(
        ResourceNotFoundException ex) {
    ApiErrorResponse error = new ApiErrorResponse(
            HttpStatus.NOT_FOUND.value(),
            ex.getMessage(),
            System.currentTimeMillis()
    );
    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
}
```

---

## 8. Utilização de DTOs

**Status:** ✓ Implementado

### Detalhes:

- Separação clara entre camada de API e domínio
- DTOs Request para entrada de dados (14 recursos, `Usuario` não expõe Request próprio — é criado via seed/fluxo de autenticação)
- DTOs Response para saída de dados (15, incluindo `UsuarioResponse`)
- Mappers (`mapper/` package) convertem entre Entity e DTO

### Estrutura:

```
dto/
  ├── request/
  │   ├── ClinicaRequest.java
  │   ├── PetRequest.java
  │   └── ... (14 Request DTOs)
  └── response/
      ├── ClinicaResponse.java
      ├── PetResponse.java
      ├── UsuarioResponse.java
      └── ... (15 Response DTOs)

mapper/
  ├── ClinicaMapper.java
  ├── PetMapper.java
  └── ... (14 Mappers)
```

---

## 9. Documentação com Swagger/OpenAPI

**Status:** ✓ Implementado

### Detalhes:

- Dependência: `springdoc-openapi-starter-webmvc-ui`
- Configurado em `config/OpenApiConfig.java` (título, versão, contato, licença e link para o repositório)
- Anotações adicionadas:
  - `@Tag` - Agrupamento de endpoints
  - `@Operation` - Descrição de operações
  - `@Schema` - Descrição de modelos

### Endpoints Swagger:

- **UI:** `http://localhost:8080/swagger-ui.html`
- **JSON:** `http://localhost:8080/v3/api-docs`

### Exemplo:

```java
@Tag(name = "Pet")
@RestController
@RequestMapping("/api/v1/pets")
public class PetController {

    @GetMapping
    @Operation(summary = "Listar pets", description = "Retorna uma página de pets")
    public ResponseEntity<Page<PetResponse>> listar(...) { }
}
```

---

## 10. Testes dos Endpoints (Postman/Insomnia)

**Status:** ✓ Implementado

### Detalhes:

- Coleção Postman em `documentos/api/postman_collection.json`
- Variável `baseUrl` configurável (`http://localhost:8080`)
- Cobre cenários de autenticação/perfis, CRUD dos principais recursos e os fluxos de negócio das últimas sprints (geração automática de lembrete/recomendação ao registrar consulta, alerta de retenção/churn)

### Como Usar:

1. Importar `documentos/api/postman_collection.json` no Postman ou Insomnia
2. Ajustar variável `baseUrl` para `http://localhost:8080`
3. Autenticar-se (login por sessão ou Basic Auth) antes de chamar endpoints protegidos
4. Executar requisições para validar endpoints

---

## 11. Arquitetura RESTful

**Status:** ✓ Implementado

### Detalhes:

- Endpoints seguem padrão REST:
  - `GET /api/v1/{recurso}` - Listar com paginação
  - `GET /api/v1/{recurso}/{id}` - Buscar por ID
  - `POST /api/v1/{recurso}` - Criar novo recurso
  - `PUT /api/v1/{recurso}/{id}` - Atualizar recurso
  - `DELETE /api/v1/{recurso}/{id}` - Deletar recurso
- Endpoint de negócio adicional fora do CRUD padrão: `GET /api/v1/clinicas/{id}/retencao` (pets sem consulta há 90+ dias)

### Códigos HTTP Corretos:

- `200 OK` - Sucesso em GET, PUT
- `201 Created` - Sucesso em POST
- `204 No Content` - Sucesso em DELETE
- `400 Bad Request` - Validação falhou
- `403 Forbidden` - Autenticado, mas sem permissão (perfil incorreto)
- `404 Not Found` - Recurso não encontrado
- `500 Internal Server Error` - Erro servidor

---

## 12. Persistência em Banco de Dados (H2/Oracle) com Flyway

**Status:** ✓ Implementado

### Detalhes:

- Utiliza `Spring Data JPA` com Hibernate
- Banco H2 em memória para desenvolvimento
- Schema **versionado por migrations Flyway** (`src/main/resources/db/migration`), com `ddl-auto: validate` — o Hibernate não gera mais o schema, apenas valida se ele bate com as entidades
- Suporta migração para Oracle (driver `ojdbc11` já presente no `pom.xml`), bastando ajustar `spring.datasource.url`/`driver-class-name` em `application.yaml`

### Migrations existentes:

| Arquivo | Conteúdo |
|---|---|
| `V1__create_domain_tables.sql` | Cria as tabelas de domínio (clínica, tutor, pet, veterinário, consulta, exame, prescrição, medicamento, vacinação, vermifugação, histórico de peso, comportamento, recomendação, lembrete) |
| `V2__create_security_table.sql` | Cria a tabela `FIDELIS_USUARIO`, com vínculo opcional a `FIDELIS_TUTOR` ou `FIDELIS_CLINICA` |
| `V3__seed_usuarios_teste.sql` | Insere clínica, tutor e usuários de teste (`clinica@fidelis.com.br` / `tutor@fidelis.com.br`, senha `Senha123`) |
| `V4__seed_veterinario_teste.sql` | Insere um veterinário de teste vinculado à clínica seed |

### Configuração:

```yaml
spring:
  datasource:
    url: jdbc:h2:mem:fidelisdb;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE
    driver-class-name: org.h2.Driver
    username: sa
  jpa:
    hibernate:
      ddl-auto: validate
    open-in-view: false
  flyway:
    enabled: true
    locations: classpath:db/migration
    baseline-on-migrate: true
    validate-on-migrate: true
```

> `open-in-view: false` está habilitado propositalmente: a leitura de coleções `@OneToMany` precisa ser feita dentro da transação de serviço, evitando `LazyInitializationException` nas telas MVC.

---

## 13. Camada Web (Thymeleaf)

**Status:** ✓ Implementado

### Detalhes:

- Além da API REST, o projeto tem telas server-side renderizadas com **Thymeleaf**, no pacote `controller/web`
- Login por formulário integrado ao Spring Security, com redirecionamento por perfil após autenticação

### Telas disponíveis:

| Rota | Perfil | Controller |
|---|---|---|
| `/login` | Público | `LoginController` |
| `/acesso-negado` | Público | `LoginController` |
| `/dashboard` | Autenticado | `DashboardController` |
| `/clinica/pets`, `/clinica/pets/novo`, `/clinica/pets/{id}/editar` | CLINICA | `PetWebController` |
| `/clinica/consultas/nova`, `/clinica/consultas/{id}/confirmacao` | CLINICA | `ConsultaWebController` |
| `/clinica/retencao` | CLINICA | `RetencaoWebController` |
| `/tutor/pets`, `/tutor/pets/{id}` | TUTOR | `TutorPetWebController` |

---

## 14. Design Patterns e Boas Práticas

**Status:** ✓ Implementado

### Padrões Utilizados:

- **Repository Pattern** - Acesso a dados via repositórios Spring Data JPA
- **Service Layer Pattern** - Lógica de negócio isolada (inclui serviços de domínio como `RetencaoService` e `UsuarioDetailsService`)
- **DTO Pattern** - Separação entre API e domínio
- **Mapper Pattern** - Conversão entre entidades e DTOs
- **Exception Handler Pattern** - Tratamento centralizado, restrito à camada de API
- **Factory Pattern** - Criação de objetos via mappers

### Princípios:

- **SOLID** - Aplicado em toda arquitetura
- **DRY** (Don't Repeat Yourself) - Reutilização de código
- **KISS** (Keep It Simple, Stupid) - Simplicidade no design

---

## 15. Banco de Dados - Estrutura

**Status:** ✓ Implementado

### Tabelas Criadas (15 entities):

1. `fidelis_clinica` - Clínicas veterinárias
2. `fidelis_tutor` - Tutores de pets
3. `fidelis_pet` - Pets (animais)
4. `fidelis_veterinario` - Veterinários
5. `fidelis_usuario` - Usuários de login (perfil `CLINICA` ou `TUTOR`)
6. `fidelis_consulta` - Consultas veterinárias
7. `fidelis_exame` - Exames realizados
8. `fidelis_prescricao` - Prescrições de medicamentos
9. `fidelis_medicamento` - Medicamentos prescritos
10. `fidelis_vacinacao` - Histórico de vacinações
11. `fidelis_vermifugacao` - Histórico de vermifugações
12. `fidelis_recomendacao` - Recomendações para pets
13. `fidelis_comportamento` - Registros de comportamento
14. `fidelis_lembrete` - Lembretes para tutores
15. `fidelis_historico_peso` - Histórico de peso

### Relacionamentos:

- Clínica 1 → \* Pets, Veterinários e (opcionalmente) Usuários
- Tutor 1 → \* Pets, Lembretes e (opcionalmente) Usuários
- Pet 1 → \* Consultas, Vacinações, Vermifugações, Histórico de Peso, Comportamentos, Recomendações, Lembretes
- Consulta 1 → \* Exames, Prescrições
- Prescrição 1 → \* Medicamentos

> Diagramas completos em `documentos/arquitetura/DER.md` (modelo de dados) e `documentos/arquitetura/DCE.md` (modelo de classes).

---

## 16. Regras de Negócio Automatizadas

**Status:** ✓ Implementado

### Detalhes:

- Ao registrar uma nova `Consulta` (`ConsultaService`), o sistema gera automaticamente:
  - Um `Lembrete` (status `PENDENTE`) para o retorno/acompanhamento do pet
  - Uma `Recomendacao` associada à consulta
- `RetencaoService` identifica pets "em risco de churn": aqueles sem nenhuma consulta registrada nos últimos 90 dias (ou que nunca tiveram consulta), expostos via `GET /api/v1/clinicas/{id}/retencao` e pela tela `/clinica/retencao`

---

## 17. Testes Unitários e Integração

**Status:** ✓ Implementado

### Detalhes:

- Todos os testes passam: `./mvnw test`
- Testes cobrem:
  - Mapeadores (Mapper tests)
  - Serviços (Service tests)
  - Repositórios (JPA tests)
  - Controllers (Integration tests)
  - Segurança (login, perfis e proteção de rotas MVC/API)
  - Fluxos de negócio (geração automática de lembrete/recomendação, alerta de retenção/churn)

### Execução:

```bash
./mvnw test
```

**Resultado:** BUILD SUCCESS ✓

---

## 18. Documentação e Entrega

**Status:** ✓ Implementado

### Artefatos Entregues:

1. ✓ Código-fonte (GitHub)
2. ✓ README.md com instruções
3. ✓ Cronograma de desenvolvimento (`documentos/requisitos/CRONOGRAMA.md`)
4. ✓ Diagramas DER e DCE (`documentos/arquitetura/`)
5. ✓ Coleção Postman (`documentos/api/postman_collection.json`)
6. ✓ Documentação de requisitos (este arquivo)
7. ✓ Guia de execução (`documentos/requisitos/COMO_EXECUTAR.md`)
8. ✓ Testes passando (Maven tests)

---

## Resumo de Conformidade

| Requisito                    | Status | Evidência                                                        |
| ----------------------------- | ------ | ----------------------------------------------------------------- |
| Bean Validation                | ✓      | `dto/request/`, `validation/`                                     |
| Paginação                      | ✓      | `Page<T>`, `Pageable` em controllers                              |
| Ordenação                      | ✓      | `Sort` implementado                                                |
| Busca com Parâmetros           | ✓      | `@RequestParam` em endpoints                                       |
| Cache                          | ✓      | `@Cacheable`, `@CacheEvict`                                        |
| Autenticação e Autorização     | ✓      | `SecurityConfig`, `UsuarioDetailsService`, perfis `CLINICA`/`TUTOR`|
| Tratamento de Erros            | ✓      | `GlobalExceptionHandler.java` (escopo `controller.api`)            |
| DTOs                           | ✓      | `dto/request/`, `dto/response/`                                    |
| Swagger/OpenAPI                | ✓      | `/swagger-ui.html`, anotações                                      |
| Testes Postman                 | ✓      | `documentos/api/postman_collection.json`                           |
| APIs RESTful                   | ✓      | Endpoints seguem padrão REST                                       |
| Banco de Dados                 | ✓      | 15 tabelas, migrations Flyway, relacionamentos                     |
| Camada Web (Thymeleaf)         | ✓      | `controller/web`, telas por perfil                                 |
| Regras de Negócio Automatizadas| ✓      | Geração de lembrete/recomendação, alerta de retenção                |
| Design Patterns                | ✓      | Repository, Service, Mapper, etc.                                  |
| Documentação                   | ✓      | README, Cronograma, Requisitos, Como Executar                      |
| Código no GitHub               | ✓      | Repositório público                                                 |
| Testes Passando                | ✓      | `./mvnw test` SUCCESS                                               |

---

**Atualizado em:** 10/09/2026
**Status Final:** PROJETO PRONTO PARA ENTREGA ✓