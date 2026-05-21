# Requisitos Técnicos - FidelisApi

## Visão Geral

Este documento detalha como cada requisito técnico foi implementado na aplicação FidelisApi (Sistema de Gerenciamento de Clínica Veterinária).

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

- Configurado em `config/CacheConfig.java`
- Tipo: Simple Cache (Spring Cache)
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

## 6. Tratamento de Erros/Exceções

**Status:** ✓ Implementado

### Detalhes:

- `GlobalExceptionHandler.java` centraliza tratamento de exceções
- Utiliza `@RestControllerAdvice` para interceptar exceções globalmente
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

## 7. Utilização de DTOs

**Status:** ✓ Implementado

### Detalhes:

- Separação clara entre camada de API e domínio
- DTOs Request para entrada de dados
- DTOs Response para saída de dados
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
      └── ... (14 Response DTOs)

mapper/
  ├── ClinicaMapper.java
  ├── PetMapper.java
  └── ... (14 Mappers)
```

---

## 8. Documentação com Swagger/OpenAPI

**Status:** ✓ Implementado

### Detalhes:

- Dependência: `springdoc-openapi-starter-webmvc-ui`
- Configurado em `config/OpenApiConfig.java`
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

## 9. Testes dos Endpoints (Postman/Insomnia)

**Status:** ✓ Implementado

### Detalhes:

- Coleção Postman em `documentos/postman_collection.json`
- Variável `baseUrl` configurável (`http://localhost:8080`)
- Requisições de exemplo para recursos principais

### Requisições Disponíveis:

- Clínicas: Listar, Criar
- Pets: Listar, Criar, Buscar por ID, Atualizar, Deletar
- Tutores: Criar
- Consultas: Listar, Criar
- Veterinários, Prescrições, Vacinações, Vermifugações, Recomendações, Exames, Comportamentos, Histórico de Peso, Lembretes, Medicamentos: Listar

### Como Usar:

1. Importar `documentos/postman_collection.json` no Postman ou Insomnia
2. Ajustar variável `baseUrl` para `http://localhost:8080`
3. Executar requisições para validar endpoints

---

## 10. Arquitetura RESTful

**Status:** ✓ Implementado

### Detalhes:

- Endpoints seguem padrão REST:
  - `GET /api/v1/{recurso}` - Listar com paginação
  - `GET /api/v1/{recurso}/{id}` - Buscar por ID
  - `POST /api/v1/{recurso}` - Criar novo recurso
  - `PUT /api/v1/{recurso}/{id}` - Atualizar recurso
  - `DELETE /api/v1/{recurso}/{id}` - Deletar recurso

### Códigos HTTP Corretos:

- `200 OK` - Sucesso em GET, PUT
- `201 Created` - Sucesso em POST
- `204 No Content` - Sucesso em DELETE
- `400 Bad Request` - Validação falhou
- `404 Not Found` - Recurso não encontrado
- `500 Internal Server Error` - Erro servidor

---

## 11. Persistência em Banco de Dados (H2/Oracle)

**Status:** ✓ Implementado

### Detalhes:

- Utiliza `Spring Data JPA` com Hibernate 7.2.x
- Banco H2 em memória para desenvolvimento
- Suporta migração para Oracle alterando apenas `application.yaml`

### Configuração:

```yaml
spring:
  datasource:
    url: jdbc:h2:mem:fidelisdb
    driver-class-name: org.h2.Driver
    username: sa
  jpa:
    database-platform: org.hibernate.dialect.H2Dialect
    hibernate:
      ddl-auto: create-drop
```

---

## 12. Design Patterns e Boas Práticas

**Status:** ✓ Implementado

### Padrões Utilizados:

- **Repository Pattern** - Acesso a dados via repositórios
- **Service Layer Pattern** - Lógica de negócio isolada
- **DTO Pattern** - Separação entre API e domínio
- **Mapper Pattern** - Conversão entre entidades e DTOs
- **Exception Handler Pattern** - Tratamento centralizado
- **Factory Pattern** - Criação de objetos via mappers

### Princípios:

- **SOLID** - Aplicado em toda arquitetura
- **DRY** (Don't Repeat Yourself) - Reutilização de código
- **KISS** (Keep It Simple, Stupid) - Simplicidade no design

---

## 13. Banco de Dados - Estrutura

**Status:** ✓ Implementado

### Tabelas Criadas (14 entities):

1. `fidelis_clinica` - Clínicas veterinárias
2. `fidelis_tutor` - Tutores de pets
3. `fidelis_pet` - Pets (animais)
4. `fidelis_veterinario` - Veterinários
5. `fidelis_consulta` - Consultas veterinárias
6. `fidelis_exame` - Exames realizados
7. `fidelis_prescricao` - Prescrições de medicamentos
8. `fidelis_medicamento` - Medicamentos prescritos
9. `fidelis_vacinacao` - Histórico de vacinações
10. `fidelis_vermifugacao` - Histórico de vermifugações
11. `fidelis_recomendacao` - Recomendações para pets
12. `fidelis_comportamento` - Registros de comportamento
13. `fidelis_lembrete` - Lembretes para tutores
14. `fidelis_historico_peso` - Histórico de peso

### Relacionamentos:

- Clínica 1 → \* Pets
- Clínica 1 → \* Veterinários
- Tutor 1 → \* Pets
- Pet 1 → \* Consultas, Vacinações, etc.
- Consulta 1 → \* Exames, Prescrições

---

## 14. Testes Unitários e Integração

**Status:** ✓ Implementado

### Detalhes:

- Todos os testes passam: `./mvnw test`
- Testes cobrem:
  - Mapeadores (Mapper tests)
  - Serviços (Service tests)
  - Repositórios (JPA tests)
  - Controllers (Integration tests)

### Execução:

```bash
./mvnw test -DskipTests=false
```

**Resultado:** BUILD SUCCESS ✓

---

## 15. Documentação e Entrega

**Status:** ✓ Implementado

### Artefatos Entregues:

1. ✓ Código-fonte (GitHub)
2. ✓ README.md com instruções
3. ✓ Cronograma de desenvolvimento
4. ✓ Diagramas (DER.png, DCE.md)
5. ✓ Coleção Postman (postman_collection.json)
6. ✓ Documentação de requisitos (este arquivo)
7. ✓ Testes passando (Maven tests)

---

## Resumo de Conformidade

| Requisito            | Status | Evidência                            |
| -------------------- | ------ | ------------------------------------ |
| Bean Validation      | ✓      | `dto/request/`, `validation/`        |
| Paginação            | ✓      | `Page<T>`, `Pageable` em controllers |
| Ordenação            | ✓      | `Sort` implementado                  |
| Busca com Parâmetros | ✓      | `@RequestParam` em endpoints         |
| Cache                | ✓      | `@Cacheable`, `@CacheEvict`          |
| Tratamento de Erros  | ✓      | `GlobalExceptionHandler.java`        |
| DTOs                 | ✓      | `dto/request/`, `dto/response/`      |
| Swagger/OpenAPI      | ✓      | `/swagger-ui.html`, anotações        |
| Testes Postman       | ✓      | `documentos/postman_collection.json` |
| APIs RESTful         | ✓      | Endpoints seguem padrão REST         |
| Banco de Dados       | ✓      | 14 tabelas, relacionamentos          |
| Design Patterns      | ✓      | Repository, Service, Mapper, etc.    |
| Documentação         | ✓      | README, Cronograma, Requisitos       |
| Código no GitHub     | ✓      | Repositório público                  |
| Testes Passando      | ✓      | `./mvnw test` SUCCESS                |

---

**Atualizado em:** 21/05/2026
**Status Final:** PROJETO PRONTO PARA ENTREGA ✓
