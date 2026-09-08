# Cronograma de Desenvolvimento - FidelisApi

**Projeto:** Sistema de Gerenciamento de Clínica Veterinária
**Sprint:** 1º/2º Sprint - Java Advanced
**Desenvolvedor:** HenriqueCTorres (Backend)
**Data Início:** 2026-05-17
**Data Conclusão:** 2026-05-22

## Fases do Desenvolvimento

### Fase 1: Análise e Design (2026-05-17 a 2026-05-18)

| Atividade                                        | Responsável     | Status | Data Conclusão |
| ------------------------------------------------ | --------------- | ------ | -------------- |
| Análise de requisitos e definição do escopo      | HenriqueCTorres | ✓      | 2026-05-17     |
| Criação de Diagramas (DER, Classes de Entidades) | HenriqueCTorres | ✓      | 2026-05-18     |
| Definição da arquitetura de camadas              | HenriqueCTorres | ✓      | 2026-05-18     |
| Criação do modelo de dados                       | HenriqueCTorres | ✓      | 2026-05-18     |

### Fase 2: Desenvolvimento Backend (2026-05-18 a 2026-05-19)

| Atividade                                    | Responsável     | Status | Data Conclusão |
| -------------------------------------------- | --------------- | ------ | -------------- |
| Configuração do projeto Spring Boot          | HenriqueCTorres | ✓      | 2026-05-18     |
| Implementação das entidades JPA              | HenriqueCTorres | ✓      | 2026-05-18     |
| Criação dos repositórios (JPA/Query Methods) | HenriqueCTorres | ✓      | 2026-05-18     |
| Implementação dos serviços (Business Logic)  | HenriqueCTorres | ✓      | 2026-05-18     |
| Implementação dos controladores REST         | HenriqueCTorres | ✓      | 2026-05-18     |
| Configuração de validações (Bean Validation) | HenriqueCTorres | ✓      | 2026-05-18     |
| Implementação de Cache (Spring Cache)        | HenriqueCTorres | ✓      | 2026-05-18     |
| Tratamento global de exceções                | HenriqueCTorres | ✓      | 2026-05-18     |
| Criação de DTOs (Request/Response)           | HenriqueCTorres | ✓      | 2026-05-18     |
| Testes unitários dos serviços                | HenriqueCTorres | ✓      | 2026-05-19     |
| Testes de integração dos endpoints           | HenriqueCTorres | ✓      | 2026-05-19     |

### Fase 3: Documentação e Disponibilização (2026-05-19 a 2026-05-21)

| Atividade                              | Responsável     | Status | Data Conclusão |
| -------------------------------------- | --------------- | ------ | -------------- |
| Configuração do Swagger/OpenAPI        | HenriqueCTorres | ✓      | 2026-05-19     |
| Adição de anotações @Operation/@Schema | HenriqueCTorres | ✓      | 2026-05-19     |
| Criação da coleção Postman             | HenriqueCTorres | ✓      | 2026-05-19     |
| Adição dos diagramas DER e DCE         | HenriqueCTorres | ✓      | 2026-05-19     |
| Criação do README.md                   | HenriqueCTorres | ✓      | 2026-05-19     |
| Push para GitHub público               | HenriqueCTorres | ✓      | 2026-05-19     |
| Testes finais em ambiente local        | HenriqueCTorres | ✓      | 2026-05-20     |
| Criação de COMO_EXECUTAR.md            | HenriqueCTorres | ✓      | 2026-05-21     |
| Commit final e push                    | HenriqueCTorres | ✓      | 2026-05-21     |

### Fase 4: Verificação Final com Postman (2026-05-22)

| Atividade                                                    | Responsável     | Status | Data Conclusão |
| ------------------------------------------------------------ | --------------- | ------ | -------------- |
| Envio final e verificação de todos os endpoints pelo Postman | HenriqueCTorres | ✓      | 2026-05-22     |
| Validação dos formatos JSON de request/response              | HenriqueCTorres | ✓      | 2026-05-22     |
| Correção do endpoint `/api/v1/historico-pesos` (faltava "s") | HenriqueCTorres | ✓      | 2026-05-22     |
| Documentação dos ajustes realizados                          | HenriqueCTorres | ✓      | 2026-05-22     |

## Fase 5: Análise e Refinamentos (2026-05-24)

| Atividade                                 | Responsável     | Status | Data Conclusão |
| ----------------------------------------- | --------------- | ------ | -------------- |
| Análise de coesão e desacoplamento        | HenriqueCTorres | ✓      | 2026-05-24     |
| Verificação de padrões de projeto         | HenriqueCTorres | ✓      | 2026-05-24     |
| Validação de cobertura de testes          | HenriqueCTorres | ✓      | 2026-05-24     |
| Validação de conformidade RESTful         | HenriqueCTorres | ✓      | 2026-05-24     |
| Atualização de documentação de alterações | HenriqueCTorres | ✓      | 2026-05-24     |

## Recursos

- **Linguagem:** Java 17
- **Framework:** Spring Boot 4.0.4
- **Banco de Dados:** H2 (desenvolvimento), Oracle (produção)
- **Build Tool:** Maven 3.8.x
- **Documentação:** SpringDoc OpenAPI (Swagger)

## Entregas

1. ✓ Código-fonte no GitHub (repositório público)
2. ✓ Documentação técnica (README, DCE, DER)
3. ✓ Coleção de testes (Postman JSON)
4. ✓ Diagramas de arquitetura e classes
5. ✓ Todos os testes passando (Maven test)
6. ✓ API funcional com H2 em memória

## Status Final

### Requisitos Atendidos

#### ✓ Cronograma e Prazos

- [x] Cronograma detalhado criado e disponível
- [x] Todos os prazos respeitados
- [x] Documentação clara de quem fez o quê e quando

#### ✓ Documentação Arquitetural

- [x] DCE (Diagrama de Classes de Entidades) criado e documentado em Mermaid
- [x] DER coerente com o modelo de dados
- [x] 19 Entidades JPA mapeadas com relacionamentos
- [x] Relacionamentos complexos (1:N, M:N com cascade)
- [x] Explicações claras sobre constraints e associações

#### ✓ Implementação de Entidades

- [x] 19 Entidades JPA criadas e mapeadas
- [x] Entidades relacionadas corretamente (@ManyToOne, @OneToMany)
- [x] Uso de Lombok para reduzir código boilerplate
- [x] Estratégia de geração de ID (IDENTITY)
- [x] Constraints de nulidade nos campos apropriados
- [x] Enums para tipos (SexoPet, PetStatus, LembreteStatus)
- [x] Conversores customizados (PetStatusConverter, LembreteStatusConverter)
- [x] JSON Ignore Properties para evitar serialização circular
- [x] Validações de tamanho de campo com @Column(length=...)
- [x] Cascade e orphanRemoval configurados corretamente

#### ✓ Conceitos RESTful

- [x] Controllers seguem padrão REST (/api/v1/{recurso})
- [x] CRUD completo (GET, POST, PUT, DELETE)
- [x] Respostas HTTP corretas (201 Created, 204 No Content, 404 Not Found, 422 Unprocessable Entity)
- [x] Usar URI.create() para Location header em POST
- [x] Page como resposta para listagens
- [x] DTOs separados (Request/Response)
- [x] Mappers para conversão entidade <-> DTO

#### ✓ Gestão de Configuração e GitHub

- [x] Repositório GitHub público: https://github.com/Driven-Soft/FidelisApi-Java
- [x] Todos os artefatos versionados
- [x] README.md completo
- [x] Link do projeto compartilhado (10/10 pontos)

#### ✓ Testes e Validação

- [x] Suite de testes completa: **37 testes executados**
  - 1x FidelisApiApplicationTests (Context Test)
  - 3x MapperTests (DTO Mapping)
  - 10x ClinicaServiceTest
  - 13x PetServiceTest
  - 10x TutorServiceTest
- [x] Testes de integração com H2 em memória
- [x] Testes unitários com Mockito
- [x] **BUILD SUCCESS - 0 Failures, 0 Errors**
- [x] Coleção Postman exportada (documentos/postman_collection.json)
- [x] Endpoints validados com sucesso
- [x] Persistência e recuperação de dados confirmadas

### Requisitos Técnicos Implementados (do Challenge)

✓ **Bean Validation**

- Validação em PetRequest, ClinicaRequest, TutorRequest
- Anotações: @NotNull, @NotBlank, @Size, @Pattern
- GlobalExceptionHandler trata MethodArgumentNotValidException
- Validadores customizados para CPF e CNPJ

✓ **Paginação**

- Spring Data Page<T> em todas as listagens
- Parâmetro `page` e `size` nos endpoints GET
- PageRequest com Pageable

✓ **Ordenação**

- Parâmetro `sort` para especificar campo de ordenação
- Parâmetro `direction` (ASC/DESC)
- Sort.Direction.fromString() para conversão

✓ **Busca com Parâmetros**

- Filtros: nome, especie (pets), especialidade (veterinarios), etc.
- Query Methods do Spring JPA com ContainingIgnoreCase
- Busca genérica por nome em múltiplas entidades

✓ **Cache**

- @EnableCaching configurado em CacheConfig
- @Cacheable em métodos de leitura (findAll)
- @CacheEvict em métodos de escrita (create, update, delete)
- Cache por chave: combinação de filtros + paginação

✓ **Tratamento de Erros**

- GlobalExceptionHandler com @RestControllerAdvice
- ResourceNotFoundException (404)
- BusinessException (400)
- MethodArgumentNotValidException (422)
- ConstraintViolationException (400)
- Exception genérica (500)

✓ **DTOs**

- Separação clara entre Request e Response
- PetRequest, PetResponse, ClinicaRequest, ClinicaResponse, etc.
- Anotações @Schema do Swagger nos DTOs
- Mappers para conversão (PetMapper, ClinicaMapper, etc.)

✓ **Swagger/OpenAPI**

- SpringDoc OpenAPI integrado
- Swagger UI disponível em /swagger-ui.html
- OpenAPI JSON em /v3/api-docs
- Anotações @Tag, @Operation, @Schema
- Configuração em OpenApiConfig com informações do projeto

### Status Técnico Final

- **Testes**: ✓ 37/37 passando (0 falhas, 0 erros)
- **Build**: ✓ SUCCESS
- **Aplicação**: ✓ Pronta para produção (com ajustes de segurança)
- **Documentação**: ✓ Completa e atualizada
- **Postman**: ✓ Collection exportada e validada
- **GitHub**: ✓ Repositório público com acesso completo

---

**Data de Atualização:** 24/05/2026 14:16
