# Como Executar a FidelisApi

## Pré-requisitos

- **Java 17 ou superior** instalado
- **Maven** — não é necessário instalar, o projeto já inclui o Maven Wrapper (`mvnw` / `mvnw.cmd`)
- **Git** (para clonar o repositório)

## 1. Clonar o Repositório

```bash
git clone https://github.com/Driven-Soft/FidelisApi-Java.git
cd FidelisApi-Java
```

## 2. Compilar e Testar

### Linux / macOS

```bash
./mvnw clean package
./mvnw clean test
```

### Windows

```powershell
.\mvnw.cmd clean package
.\mvnw.cmd clean test
```

## 3. Iniciar a Aplicação

### Opção A: Via Maven Wrapper (recomendado)

```bash
./mvnw spring-boot:run        # Linux/macOS
.\mvnw.cmd spring-boot:run     # Windows
```

### Opção B: Via JAR gerado

```bash
./mvnw clean package
java -jar target/FidelisApi-0.0.1-SNAPSHOT.jar
```

A aplicação sobe em **http://localhost:8080**, usando o perfil padrão com banco **H2 em memória**. As migrations do **Flyway** (`src/main/resources/db/migration`) rodam automaticamente na inicialização, criando as tabelas e inserindo os dados de teste (clínica, tutor, veterinário e usuários de login).

> O driver Oracle (`ojdbc11`) já está no `pom.xml` para quando o projeto for apontado para um banco Oracle real — basta trocar `spring.datasource.url`/`driver-class-name` em `application.yaml` (ou usar variáveis de ambiente/profile) e ajustar a sintaxe das migrations se necessário.

## 4. Login na Aplicação

A aplicação agora tem **autenticação via Spring Security** (form login + senha criptografada com BCrypt), com dois perfis de acesso: `CLINICA` e `TUTOR`. Ao acessar a raiz (`/`) ou qualquer rota protegida sem estar logado, você é redirecionado para `/login`.

**Usuários de teste (criados pelas migrations Flyway):**

| Perfil  | E-mail                    | Senha      |
|---------|---------------------------|------------|
| CLINICA | clinica@fidelis.com.br    | Senha123   |
| TUTOR   | tutor@fidelis.com.br      | Senha123   |

Após o login, você é redirecionado para `/dashboard`. As rotas sob `/clinica/**` exigem o perfil `CLINICA` e as rotas sob `/tutor/**` exigem o perfil `TUTOR`; o acesso indevido leva à página `/acesso-negado`.

## 5. Telas Disponíveis (MVC / Thymeleaf)

Além da API REST, o projeto tem uma camada web com Thymeleaf:

| Rota                              | Perfil   | Descrição                                       |
|------------------------------------|----------|--------------------------------------------------|
| `/login`                           | Público  | Tela de login                                     |
| `/dashboard`                       | Autenticado | Painel inicial pós-login                       |
| `/clinica/pets`                    | CLINICA  | Listagem de pets da clínica                       |
| `/clinica/pets/novo`               | CLINICA  | Cadastro de novo pet                              |
| `/clinica/pets/{id}/editar`        | CLINICA  | Edição de pet existente                           |
| `/clinica/consultas/nova`          | CLINICA  | Registro de nova consulta                         |
| `/clinica/consultas/{id}/confirmacao` | CLINICA | Confirmação de consulta registrada             |
| `/clinica/retencao`                | CLINICA  | Alerta de retenção/churn (pets sem consulta há 90+ dias) |
| `/tutor/pets`                      | TUTOR    | Listagem dos pets do tutor logado                 |
| `/tutor/pets/{id}`                 | TUTOR    | Detalhe/histórico de um pet do tutor              |
| `/acesso-negado`                   | Público  | Página exibida em caso de acesso não autorizado   |

## 6. Acessar os Recursos de API e Infraestrutura

### Swagger UI (Documentação e Teste de Endpoints)

```
http://localhost:8080/swagger-ui.html
```

### H2 Console (Banco de Dados em Memória)

```
http://localhost:8080/h2-console
```

**Configuração do H2:**

- JDBC URL: `jdbc:h2:mem:fidelisdb`
- User: `sa`
- Password: (deixe em branco)

### Health e Info (Actuator)

```
http://localhost:8080/actuator/health
http://localhost:8080/actuator/info
```

## 7. Testar a API com Postman ou Insomnia

1. Importe o arquivo: `documentos/api/postman_collection.json`
2. Defina a variável `baseUrl` como: `http://localhost:8080`
3. A collection já cobre autenticação, perfis e os principais fluxos de CRUD/negócio (ex.: geração automática de lembrete/recomendação, alerta de retenção)
4. Como a API usa sessão de login (form login), autentique-se primeiro via `/login` (ou use o suporte a Basic Auth já habilitado) antes de chamar os endpoints protegidos

## 8. Estrutura de Endpoints da API

A API segue o padrão: `/api/v1/{recurso}`. Leituras (`GET`) exigem apenas usuário autenticado; escritas (`POST`/`PUT`/`PATCH`/`DELETE`) exigem perfil `CLINICA`.

- **Clínicas** — `/api/v1/clinicas` (`GET`, `POST`, `GET /{id}`, `PUT /{id}`, `DELETE /{id}`)
- **Tutores** — `/api/v1/tutores` (`GET`, `POST`, `GET /{id}`, `PUT /{id}`, `DELETE /{id}`)
- **Veterinários** — `/api/v1/veterinarios` (`GET`, `POST`, `GET /{id}`, `PUT /{id}`, `DELETE /{id}`)
- **Pets** — `/api/v1/pets` (`GET`, `POST`, `GET /{id}`, `PUT /{id}`, `DELETE /{id}`)
- **Consultas** — `/api/v1/consultas` (`GET`, `POST`, `GET /{id}`, `PUT /{id}`, `DELETE /{id}`)
- **Exames** — `/api/v1/exames`
- **Prescrições** — `/api/v1/prescricoes`
- **Medicamentos** — `/api/v1/medicamentos`
- **Vacinações** — `/api/v1/vacinacoes`
- **Vermifugações** — `/api/v1/vermifugacoes`
- **Histórico de Peso** — `/api/v1/historico-peso`
- **Comportamentos** — `/api/v1/comportamentos`
- **Recomendações** — `/api/v1/recomendacoes`
- **Lembretes** — `/api/v1/lembretes`
- **Retenção/Churn** — `GET /api/v1/clinicas/{id}/retencao` (exige perfil `CLINICA`) — lista pets sem consulta há 90+ dias

## 9. Parâmetros de Busca Disponíveis

### Paginação

- `page=0` — Número da página (começa em 0)
- `size=10` — Quantidade de registros por página

### Ordenação

- `sort=id` — Campo para ordenar (padrão: `id`)
- `direction=ASC` — Direção (`ASC` ou `DESC`, padrão: `ASC`)

### Busca

- `nome=Clínica` — Buscar por nome (exemplo para clínicas)
- `especialidade=Cardiologia` — Buscar por especialidade (exemplo para veterinários)

### Exemplo de Requisição Completa

```
GET http://localhost:8080/api/v1/clinicas?page=0&size=5&sort=nome&direction=ASC
```

## 10. Recursos Documentados

- **Documentação Técnica**: `documentos/requisitos/REQUISITOS_TECNICOS.md`
- **Cronograma**: `documentos/requisitos/CRONOGRAMA.md`
- **Diagrama de Classes (DCE)**: `documentos/arquitetura/DCE.png` / `DCE.md`
- **Diagrama Entidade-Relacionamento (DER)**: `documentos/arquitetura/DER.png` / `DER.md`
- **Coleção Postman**: `documentos/api/postman_collection.json`

## 11. Troubleshooting

### Porta 8080 já em uso

```bash
# Encontre o processo usando a porta 8080 e finalize-o, ou
# mude a porta em application.yaml (server.port: 8081)
```

### Java não encontrado

```bash
java -version

# Se não estiver instalado, baixe em:
# https://www.oracle.com/java/technologies/downloads/#java17
```

### Erro de acesso (403 / redirecionado para /acesso-negado)

Verifique se está logado com um usuário do perfil correto para a rota (`CLINICA` ou `TUTOR`) — veja a tabela de credenciais de teste na seção 4.

### Falha ao aplicar migrations do Flyway

Se o schema do H2 ficar inconsistente entre execuções, apague o container (a base é em memória e recriada a cada `restart`) ou, em caso de banco persistente, verifique o histórico em `flyway_schema_history` antes de adicionar novas migrations.

### Maven não encontrado

Não é necessário instalar Maven — o wrapper (`mvnw`/`mvnw.cmd`) baixa e usa a versão correta automaticamente.

## 12. Contato / Suporte

Repositório: https://github.com/Driven-Soft/FidelisApi-Java