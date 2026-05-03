# GastroHub

## Descrição
GastroHub é um sistema backend unificado para uma rede local de restaurantes, permitindo gerenciamento eficiente de usuários, autenticação e operações administrativas em um ambiente containerizado.

## Funcionalidades
- **Gerenciamento de Usuários**: CRUD completo (criar, consultar, atualizar, deletar usuários).
- **Tipos de Usuários**: Diferenciação entre proprietários (OWNER) e clientes (CUSTOMER).
- **Busca de Usuários**: Busca por nome com suporte a busca parcial e case-insensitive.
- **Alteração de Senha**: Endpoint exclusivo para atualização segura de senha.
- **Autenticação JWT**: Autenticação segura usando JSON Web Tokens com Spring Security.
- **Validação de Dados**: Validações robustas para entrada de dados.
- **Tratamento de Erros**: Respostas padronizadas com RFC 7807 ProblemDetail.
- **Documentação de API**: Documentação interativa com OpenAPI/Swagger.
- **Banco de Dados Relacional**: Persistência em PostgreSQL com JPA/Hibernate.
- **Context-Path Configurável**: API acessível via `/gastrohub/api/v1`.

## Tecnologias Utilizadas
- **Java 21**
- **Spring Boot 3.5.11**
- **Spring Security** (autenticação e autorização)
- **JWT (JSON Web Tokens)** (geração e validação de tokens)
- **PostgreSQL** (banco de dados relacional)
- **JPA/Hibernate** (mapeamento objeto-relacional)
- **Docker & Docker Compose** (containerização)
- **OpenAPI/Swagger** (documentação de API)
- **JUnit 5 & AssertJ** (testes)
- **MapStruct** (mapeamento de DTOs)
- **Lombok** (redução de boilerplate)

## Pré-requisitos
- **Docker** e **Docker Compose** instalados.
- **Java 21** (para desenvolvimento local).
- **Maven** (para build).

## Como Executar

### Com Docker Compose (Recomendado)
1. Clone o repositório.
2. Navegue até a raiz do projeto.
3. Execute:
   ```bash
   docker-compose up --build
   ```
4. A aplicação estará disponível em `http://localhost:8080/gastrohub/`.
5. Swagger UI estará em `http://localhost:8080/gastrohub/swagger-ui.html`.
6. O banco PostgreSQL estará em `localhost:5432`.

### Desenvolvimento Local
1. Certifique-se de que o PostgreSQL esteja rodando (use Docker ou instalação local).
2. Configure o banco no `application-local.yaml`:
   ```yaml
   spring:
     datasource:
       url: jdbc:postgresql://localhost:5432/gastrohub_db
       username: gastrohub_user
       password: gastrohub_password
   server:
     servlet:
       context-path: /gastrohub
   ```
3. Execute:
   ```bash
   mvn spring-boot:run -Dspring-boot.run.arguments="--spring.profiles.active=local"
   ```
4. A aplicação estará em `http://localhost:8080/gastrohub/api/v1`.

## Documentação da API
- **Swagger UI**: `http://localhost:8080/gastrohub/swagger-ui.html`
- **OpenAPI JSON**: `http://localhost:8080/gastrohub/v3/api-docs`
- **Documentação Detalhada**: Consulte `API_DOCUMENTATION.md`

### Base URL da API
```
http://localhost:8080/gastrohub/api/v1
```

### Endpoints Disponíveis
- `POST /auth/login` - Autenticar usuário e obter JWT token
- `POST /users` - Criar novo usuário
- `GET /users/{id}` - Obter usuário por ID
- `GET /users?name={name}` - Buscar usuários por nome (suporta busca parcial, case-insensitive)
- `PATCH /users/{id}` - Atualizar dados do usuário
- `PATCH /users/{id}/password` - Atualizar senha do usuário
- `DELETE /users/{id}` - Deletar usuário

## 🚨 Tratamento de Erros - RFC 7807 ProblemDetail

A API utiliza o padrão RFC 7807 ProblemDetail para padronizar respostas de erro:

**Exemplo - Erro 400 (Validação):**
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

**Exemplo - Erro 401 (Não Autenticado):**
```json
{
  "type": "https://api.gastrohub.com/errors/unauthorized",
  "title": "Unauthorized",
  "status": 401,
  "detail": "Unauthorized - Invalid or missing token",
  "instance": "/gastrohub/api/v1/users/1",
  "timestamp": "2026-04-03T20:50:10Z"
}
```

**Exemplo - Erro 422 (Duplicata):**
```json
{
  "type": "https://api.gastrohub.com/errors/duplicate-resource",
  "title": "Duplicate Resource",
  "status": 422,
  "detail": "Email already in use: test@example.com",
  "instance": "/gastrohub/api/v1/users",
  "timestamp": "2026-04-03T20:50:10Z"
}
```

**Exemplo - Erro 404 (Não Encontrado):**
```json
{
  "type": "https://api.gastrohub.com/errors/resource-not-found",
  "title": "Resource Not Found",
  "status": 404,
  "detail": "User not found with id: 99999",
  "instance": "/gastrohub/api/v1/users/99999",
  "timestamp": "2026-04-03T20:50:10Z"
}
```

## Estrutura do Projeto
```
svcgastrohub/
├── src/
│   ├── main/
│   │   ├── java/com/restaurant/gastrohub/
│   │   │   ├── adapter/          # Camada de adaptadores (controllers, persistence)
│   │   │   ├── application/      # Camada de aplicação (domain, services, ports)
│   │   │   ├── config/           # Configurações (Security, OpenAPI)
│   │   │   └── GastrohubApplication.java
│   │   └── resources/            # application.yaml, etc.
│   └── test/                     # Testes unitários
├── docker-compose.yml            # Configuração Docker
├── Dockerfile                    # Build da aplicação
├── pom.xml                       # Dependências Maven
├── API_DOCUMENTATION.md          # Documentação da API
└── README.md                     # Este arquivo
```
## Testes
Execute os testes com:
```bash
mvn test
```

Os testes cobrem:
- **UserControllerTest**: Testes dos endpoints (sucesso e erro)
- **UserServiceTest**: Testes da lógica de negócio
- **UserPostgresTest**: Testes de persistência
- **GlobalExceptionHandlerTest**: Testes do tratamento de exceções
- **UserEntityTest**: Testes da entidade de usuário
- **AuthenticationServiceTest**: Testes do serviço de autenticação
- **JwtTokenProviderTest**: Testes da geração e validação de tokens JWT
- **AuthControllerTest**: Testes do controller de autenticação

Todos os testes são unitários e utilizam Mockito para isolamento.

## 🔐 Autenticação JWT

A aplicação implementa autenticação segura usando **Spring Security** com **JWT (JSON Web Tokens)**:

### Como Funciona

1. **Login**: O usuário faz login com suas credenciais em `POST /auth/login`
2. **Token**: Recebe um JWT token válido por 24 horas
3. **Requisições**: Inclui o token no header `Authorization: Bearer {token}` em requisições protegidas
4. **Validação**: O filtro JWT valida o token em cada requisição

### Exemplo de Fluxo

```bash
1. Fazer login:
POST /api/v1/auth/login
{
  "login": "johndoe",
  "password": "Password123!"
}

Resposta:
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "userId": 1,
  "login": "johndoe",
  "type": "Bearer"
}

2. Usar o token:
GET /api/v1/users/1
Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...
```

### Endpoints Públicos
- `POST /api/v1/auth/login` - Obter token JWT
- `POST /api/v1/users` - Criar novo usuário (sem autenticação)
- Swagger UI - `/swagger-ui.html`

### Endpoints Protegidos
Todos os outros endpoints requerem um token JWT válido no header `Authorization`.

Para mais detalhes, consulte **JWT_AUTHENTICATION_GUIDE.md**.

---


1. Faça um fork do projeto.
2. Crie uma branch para sua feature (`git checkout -b feature/nova-feature`).
3. Commit suas mudanças (`git commit -am 'Adiciona nova feature'`).
4. Push para a branch (`git push origin feature/nova-feature`).
5. Abra um Pull Request.

## Licença
Este projeto está licenciado sob a Apache License 2.0. Consulte o arquivo LICENSE para mais detalhes.

## Contato
- **Email**: alisonguimaraes17@gmail.com
- **LinkedIn**: https://www.linkedin.com/in/alison-guimaraes/
- **GitHub**: https://github.com/alisonguima/svcgastrohub

---

**Última atualização**: 03 de Abril de 2026  
**Versão**: 1.0.0

