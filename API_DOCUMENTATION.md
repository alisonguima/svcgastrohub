# GastroHub - API Documentation

## Overview

GastroHub é uma aplicação backend de gerenciamento de usuários para um sistema de restaurante, construída com Spring Boot, PostgreSQL e seguindo os padrões de arquitetura hexagonal.

**Base URL:** `http://localhost:8080/gastrohub/api/v1`

---

## 📚 User Management API

### 1. Create User
Cria um novo usuário no sistema.

**Endpoint:** `POST /users`

**Request Headers:**
```
Content-Type: application/json
```

**Request Body:**
```json
{
  "name": "Alison Guimaraes",
  "email": "alisonguimaraes@email.com",
  "login": "alisonguimaraes",
  "password": "Gastr0@Hub",
  "userType": "OWNER",
  "address": "Rua Fulano da Silva, 123 - São Paulo, SP"
}
```

**Response (201 - Created):**
```json
{
  "id": "1",
  "name": "Alison Guimaraes",
  "email": "alisonguimaraes@email.com",
  "login": "alisonguimaraes"
}
```

**Error Response (400 - Validation Error):**
```json
{
  "type": "https://api.gastrohub.com/errors/validation-error",
  "title": "Validation Error",
  "status": 400,
  "detail": "One or more fields have validation errors",
  "instance": "/gastrohub/api/v1/users",
  "errors": {
    "email": "Email must be valid",
    "login": "Login is required"
  },
  "timestamp": "2026-04-03T20:50:10Z"
}
```

**Error Response (422 - Duplicate Email):**
```json
{
  "type": "https://api.gastrohub.com/errors/duplicate-resource",
  "title": "Duplicate Resource",
  "status": 422,
  "detail": "Email already in use: alisonguimaraes@email.com",
  "instance": "/gastrohub/api/v1/users",
  "timestamp": "2026-04-03T20:50:10Z"
}
```

---

### 2. Get User by ID
Recupera um usuário específico pelo ID.

**Endpoint:** `GET /users/{id}`

**Path Parameters:**
- `id` (required): Identificador único do usuário (Long)

**Example Request:**
```
GET /gastrohub/api/v1/users/1
```

**Response (200 - OK):**
```json
{
  "id": "1",
  "name": "Alison Guimaraes",
  "email": "alisonguimaraes@email.com",
  "login": "alisonguimaraes",
  "userType": "OWNER",
  "lastUpdateAt": "2026-04-03T20:50:10Z",
  "address": "Rua Fulano da Silva, 123 - São Paulo, SP"
}
```

**Error Response (404 - Not Found):**
```json
{
  "type": "https://api.gastrohub.com/errors/resource-not-found",
  "title": "Resource Not Found",
  "status": 404,
  "detail": "User not found with id: 999",
  "instance": "/gastrohub/api/v1/users/999",
  "timestamp": "2026-04-03T20:50:10Z"
}
```

---

### 3. Get Users by Name
Recupera todos os usuários que correspondem ao nome fornecido (busca parcial, case-insensitive).

**Endpoint:** `GET /users?name={name}`

**Query Parameters:**
- `name` (required): Nome ou parte do nome a ser procurado

**Example Requests:**
```
GET /gastrohub/api/v1/users?name=Alison
GET /gastrohub/api/v1/users?name=ali
GET /gastrohub/api/v1/users?name=ALISON
```

**Response (200 - OK):**
```json
[
  {
    "id": "1",
    "name": "Alison Guimaraes",
    "email": "alisonguimaraes@email.com",
    "login": "alisonguimaraes",
    "userType": "OWNER",
    "lastUpdateAt": "2026-04-03T20:50:10Z",
    "address": "Rua Fulano da Silva, 123 - São Paulo, SP"
  },
  {
    "id": "2",
    "name": "Alison Silva",
    "email": "alisonsilva@email.com",
    "login": "alisonsilva",
    "userType": "CUSTOMER",
    "lastUpdateAt": "2026-04-03T19:30:45Z",
    "address": "Rua Another, 456 - São Paulo, SP"
  }
]
```

**Error Response (404 - No Users Found):**
```json
{
  "type": "https://api.gastrohub.com/errors/resource-not-found",
  "title": "Resource Not Found",
  "status": 404,
  "detail": "User not found: NonExistentName",
  "instance": "/gastrohub/api/v1/users?name=NonExistentName",
  "timestamp": "2026-04-03T20:50:10Z"
}
```

---

### 4. Update User
Atualiza as informações de um usuário existente.

**Endpoint:** `PATCH /users/{id}`

**Path Parameters:**
- `id` (required): Identificador único do usuário (Long)

**Request Body:**
```json
{
  "name": "Alison Guimaraes Updated",
  "email": "alison.updated@email.com",
  "login": "alisonguimaraes",
  "userType": "OWNER",
  "address": "Rua Updated, 789 - São Paulo, SP"
}
```

**Response (204 - No Content):**
- Sem corpo de resposta

**Error Response (400 - Invalid Data):**
```json
{
  "type": "https://api.gastrohub.com/errors/validation-error",
  "title": "Validation Error",
  "status": 400,
  "detail": "One or more fields have validation errors",
  "instance": "/gastrohub/api/v1/users/1",
  "errors": {
    "email": "Email must be valid"
  },
  "timestamp": "2026-04-03T20:50:10Z"
}
```

**Error Response (404 - User Not Found):**
```json
{
  "type": "https://api.gastrohub.com/errors/resource-not-found",
  "title": "Resource Not Found",
  "status": 404,
  "detail": "User not found with id: 999",
  "instance": "/gastrohub/api/v1/users/999",
  "timestamp": "2026-04-03T20:50:10Z"
}
```

**Error Response (422 - Duplicate Email):**
```json
{
  "type": "https://api.gastrohub.com/errors/duplicate-resource",
  "title": "Duplicate Resource",
  "status": 422,
  "detail": "Email already in use: email@example.com",
  "instance": "/gastrohub/api/v1/users/1",
  "timestamp": "2026-04-03T20:50:10Z"
}
```

---

### 5. Update Password
Atualiza a senha de um usuário existente.

**Endpoint:** `PATCH /users/{id}/password`

**Path Parameters:**
- `id` (required): Identificador único do usuário (Long)

**Request Body:**
```json
{
  "currentPassword": "Gastr0@Hub",
  "newPassword": "Gastr1@Hub"
}
```

**Response (204 - No Content):**
- Sem corpo de resposta

**Error Response (400 - Invalid Data):**
```json
{
  "type": "https://api.gastrohub.com/errors/validation-error",
  "title": "Validation Error",
  "status": 400,
  "detail": "One or more fields have validation errors",
  "instance": "/gastrohub/api/v1/users/1/password",
  "errors": {
    "newPassword": "Password must be at least 8 characters"
  },
  "timestamp": "2026-04-03T20:50:10Z"
}
```

**Error Response (422 - Invalid Current Password):**
```json
{
  "type": "https://api.gastrohub.com/errors/invalid-request",
  "title": "Invalid Request",
  "status": 422,
  "detail": "Current password is incorrect",
  "instance": "/gastrohub/api/v1/users/1/password",
  "timestamp": "2026-04-03T20:50:10Z"
}
```

**Error Response (404 - User Not Found):**
```json
{
  "type": "https://api.gastrohub.com/errors/resource-not-found",
  "title": "Resource Not Found",
  "status": 404,
  "detail": "User not found with id: 999",
  "instance": "/gastrohub/api/v1/users/999/password",
  "timestamp": "2026-04-03T20:50:10Z"
}
```

---

### 6. Delete User
Remove um usuário do sistema.

**Endpoint:** `DELETE /users/{id}`

**Path Parameters:**
- `id` (required): Identificador único do usuário (Long)

**Example Request:**
```
DELETE /gastrohub/api/v1/users/1
```

**Response (204 - No Content):**
- Sem corpo de resposta

**Error Response (404 - User Not Found):**
```json
{
  "type": "https://api.gastrohub.com/errors/resource-not-found",
  "title": "Resource Not Found",
  "status": 404,
  "detail": "User not found with id: 999",
  "instance": "/gastrohub/api/v1/users/999",
  "timestamp": "2026-04-03T20:50:10Z"
}
```

---

## 🔐 Autenticação

Atualmente, a API não possui autenticação habilitada. Todos os endpoints são públicos.

**Nota:** Em uma aplicação de produção, implementar autenticação OAuth2 ou JWT é recomendado.

---

## 📊 Data Types

### UserType Enum
```
OWNER     - Proprietário do restaurante
CUSTOMER  - Cliente
```

### GetUserResponse Schema
```json
{
  "id": "string",
  "name": "string",
  "email": "string (email format)",
  "login": "string",
  "userType": "OWNER | CUSTOMER",
  "lastUpdateAt": "string (ISO 8601 format: YYYY-MM-DDTHH:mm:ssZ)",
  "address": "string"
}
```

### CreateUserRequest Schema
```json
{
  "name": "string (required)",
  "email": "string (required, must be valid email)",
  "login": "string (required, unique)",
  "password": "string (required, at least 8 characters with uppercase, lowercase, number and special char)",
  "userType": "string (required, OWNER or CUSTOMER)",
  "address": "string (required)"
}
```

---

## 🚨 Error Handling - RFC 7807 ProblemDetail

A API utiliza o padrão RFC 7807 ProblemDetail para padronizar respostas de erro. Todos os erros seguem a estrutura abaixo:

```json
{
  "type": "https://api.gastrohub.com/errors/{error-type}",
  "title": "Human-readable title",
  "status": 400|404|422|500,
  "detail": "Specific error message",
  "instance": "/gastrohub/api/v1/...",
  "errors": { "field": "error message" },  // Only for validation errors
  "timestamp": "2026-04-03T20:50:10Z"
}
```

### Error Types
- `validation-error` (400) - Falha na validação de campos
- `duplicate-resource` (422) - Email ou login já existentes
- `resource-not-found` (404) - Recurso não encontrado
- `invalid-request` (422) - Requisição inválida (ex: senha incorreta)
- `internal-server-error` (500) - Erro interno do servidor

---

## 🛠️ Swagger/OpenAPI

A documentação interativa dos endpoints está disponível em:

```
http://localhost:8080/gastrohub/swagger-ui.html
```

Especificação OpenAPI:
```
http://localhost:8080/gastrohub/v3/api-docs
```

---

## 📝 Exemplos de Uso com cURL

### Criar usuário
```bash
curl -X POST http://localhost:8080/gastrohub/api/v1/users \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Alison Guimaraes",
    "email": "alisonguimaraes@email.com",
    "login": "alisonguimaraes",
    "password": "Gastr0@Hub",
    "userType": "OWNER",
    "address": "Rua Fulano da Silva, 123 - São Paulo, SP"
  }'
```

### Recuperar usuário por ID
```bash
curl -X GET http://localhost:8080/gastrohub/api/v1/users/1
```

### Buscar usuários por nome
```bash
curl -X GET "http://localhost:8080/gastrohub/api/v1/users?name=Alison"
```

### Atualizar usuário
```bash
curl -X PATCH http://localhost:8080/gastrohub/api/v1/users/1 \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Alison Updated",
    "email": "alison.updated@email.com"
  }'
```

### Atualizar senha
```bash
curl -X PATCH http://localhost:8080/gastrohub/api/v1/users/1/password \
  -H "Content-Type: application/json" \
  -d '{
    "currentPassword": "Gastr0@Hub",
    "newPassword": "Gastr1@Hub"
  }'
```

### Deletar usuário
```bash
curl -X DELETE http://localhost:8080/gastrohub/api/v1/users/1
```

---

## 📋 Códigos de Status HTTP

| Código | Descrição |
|--------|-----------|
| 200 | OK - Requisição bem-sucedida |
| 201 | Created - Recurso criado com sucesso |
| 204 | No Content - Operação bem-sucedida sem conteúdo de resposta |
| 400 | Bad Request - Dados inválidos ou mal formatados (validação) |
| 404 | Not Found - Recurso não encontrado |
| 422 | Unprocessable Entity - Erro de negócio (ex: email duplicado) |
| 500 | Internal Server Error - Erro interno do servidor |

---

## 🔄 Fluxo de Teste Recomendado

1. **POST /gastrohub/api/v1/users** - Criar usuário
2. **GET /gastrohub/api/v1/users/{id}** - Recuperar usuário criado
3. **GET /gastrohub/api/v1/users?name=** - Buscar por nome
4. **PATCH /gastrohub/api/v1/users/{id}** - Atualizar dados
5. **PATCH /gastrohub/api/v1/users/{id}/password** - Atualizar senha
6. **DELETE /gastrohub/api/v1/users/{id}** - Deletar usuário

---

## 📞 Suporte

Para questões ou problemas, consulte a documentação interativa do Swagger em `/gastrohub/swagger-ui.html`.

---

**Última atualização:** 03 de Abril de 2026
**Versão da API:** 1.0

