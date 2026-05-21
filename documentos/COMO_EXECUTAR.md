# Como Executar a FidelisApi

## Pré-requisitos

- **Java 17 ou superior** instalado
- **Maven 3.8.x ou superior** (incluído via wrapper `mvnw.cmd`)
- Git (para clonar o repositório)

## 1. Clonar o Repositório

```bash
git clone https://github.com/Driven-Soft/FidelisApi-Java.git
cd FidelisApi-Java
```

## 2. Compilar e Testar

### Compile o projeto

```bash
.\mvnw.cmd clean package
```

### Execute apenas os testes

```bash
.\mvnw.cmd clean test
```

## 3. Iniciar a Aplicação

### Opção A: Via Maven Wrapper (recomendado)

```powershell
.\mvnw.cmd spring-boot:run
```

### Opção B: Via JAR gerado

```powershell
.\mvnw.cmd clean package
java -jar target/FidelisApi-0.0.1-SNAPSHOT.jar
```

A aplicação iniciará em: **http://localhost:8080**

## 4. Acessar os Recursos

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

### Health Endpoint (Verificar Status)

```
http://localhost:8080/actuator/health
```

## 5. Testar com Postman ou Insomnia

1. Importe o arquivo: `documentos/postman_collection.json`
2. Defina a variável `baseUrl` como: `http://localhost:8080`
3. Execute as requisições de exemplo

## 6. Estrutura de Endpoints

A API segue o padrão: `/api/v1/{recurso}`

### Exemplos de Endpoints:

- **Clínicas**
  - `GET /api/v1/clinicas` - Listar clínicas (com paginação e ordenação)
  - `POST /api/v1/clinicas` - Criar clínica
  - `GET /api/v1/clinicas/{id}` - Buscar clínica por ID
  - `PUT /api/v1/clinicas/{id}` - Atualizar clínica
  - `DELETE /api/v1/clinicas/{id}` - Deletar clínica

- **Tutores**
  - `GET /api/v1/tutores` - Listar tutores
  - `POST /api/v1/tutores` - Criar tutor
  - `GET /api/v1/tutores/{id}` - Buscar tutor
  - `PUT /api/v1/tutores/{id}` - Atualizar tutor
  - `DELETE /api/v1/tutores/{id}` - Deletar tutor

- **Pets**
  - `GET /api/v1/pets` - Listar pets
  - `POST /api/v1/pets` - Criar pet
  - `GET /api/v1/pets/{id}` - Buscar pet
  - `PUT /api/v1/pets/{id}` - Atualizar pet
  - `DELETE /api/v1/pets/{id}` - Deletar pet

- **Consultas**
  - `GET /api/v1/consultas` - Listar consultas
  - `POST /api/v1/consultas` - Criar consulta
  - `GET /api/v1/consultas/{id}` - Buscar consulta por ID
  - `PUT /api/v1/consultas/{id}` - Atualizar consulta
  - `DELETE /api/v1/consultas/{id}` - Deletar consulta

- **Tutores**
  - `GET /api/v1/tutores` - Listar tutores
  - `POST /api/v1/tutores` - Criar tutor
  - `GET /api/v1/tutores/{id}` - Buscar tutor
  - `PUT /api/v1/tutores/{id}` - Atualizar tutor
  - `DELETE /api/v1/tutores/{id}` - Deletar tutor

- **Pets**
  - `GET /api/v1/pets` - Listar pets
  - `POST /api/v1/pets` - Criar pet
  - `GET /api/v1/pets/{id}` - Buscar pet
  - `PUT /api/v1/pets/{id}` - Atualizar pet
  - `DELETE /api/v1/pets/{id}` - Deletar pet

- **Consultas**
  - `GET /api/v1/consultas` - Listar consultas
  - `POST /api/v1/consultas` - Criar consulta
  - Etc...

## 7. Parâmetros de Busca Disponíveis

### Paginação

- `page=0` - Número da página (começa em 0)
- `size=10` - Quantidade de registros por página

### Ordenação

- `sort=id` - Campo para ordenar (padrão: `id`)
- `direction=ASC` - Direção (ASC ou DESC, padrão: ASC)

### Busca

- `nome=Clínica` - Buscar por nome (exemplo para clínicas)
- `especialidade=Cardiologia` - Buscar por especialidade (exemplo para veterinários)

### Exemplo de Requisição Completa

```
GET http://localhost:8080/api/v1/clinicas?page=0&size=5&sort=nome&direction=ASC
```

## 8. Recursos Documentados

- **Documentação Técnica**: `documentos/REQUISITOS_TECNICOS.md`
- **Cronograma**: `documentos/CRONOGRAMA.md`
- **Diagrama de Classes**: `documentos/DCE.png`
- **Diagrama Entidade-Relacionamento**: `documentos/DER.png`
- **Coleção Postman**: `documentos/postman_collection.json`

## 9. Troubleshooting

### Porta 8080 já em uso

Se receber erro de porta em uso:

```bash
# Encontre o processo usando a porta 8080 e mate-o, ou
# Mude a porta em application.yaml (server.port: 8081)
```

### Java não encontrado

```bash
# Verifique a versão instalada:
java -version

# Se não estiver instalado, baixe em:
https://www.oracle.com/java/technologies/downloads/#java17
```

### Maven não encontrado

O wrapper `mvnw.cmd` será usado automaticamente. Não precisa instalar Maven globalmente.

## 10. Contato / Suporte

Repositório: https://github.com/Driven-Soft/FidelisApi-Java
