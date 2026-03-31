# GastroHub

## Descrição
GastroHub é um sistema backend unificado para uma rede local de restaurantes, permitindo gerenciamento eficiente de usuários, autenticação e operações administrativas em um ambiente containerizado.

## Funcionalidades
- **Gerenciamento de Usuários**: CRUD completo (criar, listar, atualizar, deletar usuários).
- **Tipos de Usuários**: Diferenciação entre proprietários (OWNER) e clientes (CUSTOMER).
- **Validação de Dados**: Validações robustas para entrada de dados.
- **Tratamento de Erros**: Respostas padronizadas para erros.
- **Documentação de API**: Documentação interativa com OpenAPI/Swagger.
- **Banco de Dados Relacional**: Persistência em PostgreSQL com JPA/Hibernate.

## Tecnologias Utilizadas
- **Java 21**
- **Spring Boot 3.5.11**
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
4. A aplicação estará disponível em `http://localhost:8080`.
5. O banco PostgreSQL estará em `localhost:5432`.

### Desenvolvimento Local
1. Certifique-se de que o PostgreSQL esteja rodando (use Docker ou instalação local).
2. Configure o banco no `application-local.yaml`.
3. Execute:
   ```bash
   mvn spring-boot:run
   ```
4. A aplicação estará em `http://localhost:8080`.

## Documentação da API
- **Swagger UI**: `http://localhost:8080/swagger-ui/index.html`
- **OpenAPI JSON**: `http://localhost:8080/v3/api-docs`
- **Documentação Detalhada**: Consulte `API_DOCUMENTATION.md`.

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

## Requisitos Não Funcionais Atendidos
- **RNF02**: Persistência em banco relacional (PostgreSQL) com JPA/Hibernate.
- **RNF03**: Documentação de API com OpenAPI/Swagger e exemplos de requests/responses.

## Testes
Execute os testes com:
```bash
mvn test
```

## Contribuição
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
