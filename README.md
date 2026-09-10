# HelpDesk Service API

![Java](https://img.shields.io/badge/Java-17-orange?style=for-the-badge&logo=openjdk)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.x-6DB33F?style=for-the-badge&logo=springboot)
![Spring Security](https://img.shields.io/badge/Spring%20Security-6.x-6DB33F?style=for-the-badge&logo=springsecurity)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-15-4169E1?style=for-the-badge&logo=postgresql)
![Flyway](https://img.shields.io/badge/Flyway-Migrations-CC0200?style=for-the-badge&logo=flyway)
![Docker](https://img.shields.io/badge/Docker-Enabled-2496ED?style=for-the-badge&logo=docker)
![Maven](https://img.shields.io/badge/Maven-Build-C71A36?style=for-the-badge&logo=apachemaven)
## Sobre o projeto

O HelpDesk Service API é uma aplicação backend para gerenciamento de chamados de suporte técnico, desenvolvida com Java e Spring Boot.

O projeto foi construído com foco em organização de domínio, segurança, controle de acesso, consistência de dados e evolução arquitetural.

A aplicação utiliza PostgreSQL para persistência, Flyway para versionamento do banco de dados e Spring Security com JWT para autenticação e autorização.

A arquitetura segue uma abordagem de Modular Monolith, mantendo os diferentes domínios da aplicação organizados e desacoplados sem introduzir a complexidade operacional de uma arquitetura distribuída.

O projeto também está sendo estruturado para trabalhar com regras de SLA, histórico de alterações, concorrência otimista e processamento assíncrono.

## Tecnologias utilizadas

### Backend

* Java 17
* Spring Boot 3.x
* Spring Web
* Spring Data JPA
* Spring Security
* JWT
* BCrypt
* Bean Validation
* API REST

### Banco de dados

* PostgreSQL 15
* Hibernate / JPA
* Flyway

### Infraestrutura

* Docker
* Docker Compose

### Documentação e testes

* OpenAPI / Swagger
* JUnit
* Mockito

### Mensageria

* RabbitMQ

---

## Arquitetura

A aplicação utiliza uma arquitetura de Modular Monolith organizada por domínio.

```text
                         ┌─────────────────────────┐
                         │        Client           │
                         │  Web / Mobile / REST    │
                         └────────────┬────────────┘
                                      │
                                      │ HTTP REST
                                      ▼
                         ┌─────────────────────────┐
                         │    Spring Boot API      │
                         │        Port 8080        │
                         └───────┬─────────┬───────┘
                                 │         │
                    Persistence  │         │ Authentication
                                 │         │
                                 ▼         ▼
                    ┌───────────────┐   ┌─────────────────┐
                    │  PostgreSQL   │   │ Spring Security │
                    │   Port 5432   │   │      + JWT      │
                    └───────────────┘   └─────────────────┘
                                 │
                                 │
                                 ▼
                    ┌─────────────────────────┐
                    │      Domain Modules     │
                    │                         │
                    │  ┌───────────────────┐  │
                    │  │       User        │  │
                    │  └───────────────────┘  │
                    │                         │
                    │  ┌───────────────────┐  │
                    │  │      Ticket       │  │
                    │  └───────────────────┘  │
                    └─────────────────────────┘
```

### Organização dos módulos

```text
src/main/java/com/helpdesk
│
├── shared
│   ├── config
│   ├── exception
│   └── security
│
├── user
│   ├── controller
│   ├── domain
│   ├── dto
│   ├── repository
│   └── service
│
└── ticket
    ├── controller
    ├── domain
    ├── dto
    ├── repository
    └── service
```

A organização por domínio evita concentrar todas as entidades, services, controllers e repositories em pacotes globais.

Cada módulo possui suas próprias responsabilidades e pode evoluir de forma independente dentro do monólito.

---

## Componentes

| Componente      | Responsabilidade                                             |
| --------------- | ------------------------------------------------------------ |
| Spring Boot API | Exposição dos endpoints REST e processamento das requisições |
| Spring Security | Autenticação e autorização da aplicação                      |
| JWT             | Representação da sessão de autenticação                      |
| PostgreSQL      | Persistência dos dados                                       |
| Flyway          | Versionamento e evolução do schema                           |
| User Module     | Gerenciamento dos usuários                                   |
| Ticket Module   | Gerenciamento dos chamados                                   |
| Docker          | Padronização do ambiente de execução                         |
| RabbitMQ        | Processamento assíncrono e eventos                           |

---

## Autenticação

A API utiliza autenticação stateless baseada em JWT.

Fluxo simplificado:

```text
                    POST /login
                        │
                        ▼
              ┌──────────────────┐
              │    Credentials    │
              │ Email + Password  │
              └─────────┬────────┘
                        │
                        ▼
              ┌──────────────────┐
              │ Spring Security  │
              │ Authentication   │
              └─────────┬────────┘
                        │
                        ▼
                   JWT Token
                        │
                        ▼
              Protected Endpoints
                        │
                        ▼
              JWT Authentication
                        │
                        ▼
                  RBAC / Role
```

As senhas dos usuários não são armazenadas em texto puro. A aplicação utiliza BCrypt para realizar o hash das credenciais.

---

## Controle de acesso

A aplicação utiliza RBAC (Role-Based Access Control) para controlar as operações de acordo com o perfil autenticado.

Os principais perfis são:

| Role         | Responsabilidade                                     |
| ------------ | ---------------------------------------------------- |
| `ADMIN`      | Administração do sistema e gerenciamento de usuários |
| `TECHNICIAN` | Atendimento e gerenciamento de chamados              |
| `CUSTOMER`   | Abertura e acompanhamento dos próprios chamados      |

A autorização é aplicada nos endpoints de acordo com as responsabilidades de cada perfil.

---

## Gerenciamento de usuários

O módulo de usuários é responsável pelas operações relacionadas às contas da aplicação.

Entre as responsabilidades:

* Cadastro de usuários
* Autenticação
* Consulta de usuários
* Definição de roles
* Hash de senha
* Validação de credenciais
* Controle de acesso

Exemplo de estrutura:

```text
User
│
├── id
├── name
├── email
├── password
├── role
├── createdAt
└── updatedAt
```

---

## Tickets

O módulo de tickets representa o núcleo do sistema de Help Desk.

Um chamado possui informações relacionadas ao problema reportado, prioridade, status, responsável e usuário que realizou a abertura.

Fluxo conceitual:

```text
Customer
   │
   │ Abre chamado
   ▼
┌───────────────┐
│     OPEN      │
└───────┬───────┘
        │
        │ Atribuição
        ▼
┌───────────────┐
│   ASSIGNED    │
└───────┬───────┘
        │
        │ Atendimento
        ▼
┌───────────────┐
│ IN_PROGRESS   │
└───────┬───────┘
        │
        │ Resolução
        ▼
┌───────────────┐
│    RESOLVED   │
└───────┬───────┘
        │
        │ Encerramento
        ▼
┌───────────────┐
│     CLOSED    │
└───────────────┘
```

O fluxo de estados será controlado através de regras de negócio, evitando alterações inválidas no ciclo de vida de um chamado.

---

## SLA

Um dos objetivos do projeto é implementar um mecanismo de SLA (Service Level Agreement) para controlar os prazos de atendimento dos chamados.

O SLA considera fatores como:

* Prioridade
* Tempo de primeira resposta
* Tempo de resolução
* Data de abertura
* Data de primeira resposta
* Data de resolução
* Violação do SLA

Fluxo conceitual:

```text
              Ticket Created
                    │
                    ▼
             SLA Calculation
                    │
          ┌─────────┴─────────┐
          │                   │
          ▼                   ▼
   First Response        Resolution
       Deadline             Deadline
          │                   │
          └─────────┬─────────┘
                    │
                    ▼
              SLA Evaluation
                    │
             ┌──────┴──────┐
             │             │
             ▼             ▼
          On Time        Breached
```

O objetivo é transformar SLA em uma regra de negócio do domínio, e não apenas em um campo armazenado no banco.

---

## Concorrência

Chamados podem ser manipulados simultaneamente por diferentes usuários ou processos.

Para ajudar a evitar sobrescritas silenciosas, o projeto utiliza o conceito de Optimistic Locking através do `@Version`.

Exemplo:

```java
@Version
private Long version;
```

Fluxo:

```text
Request A ─────┐
               │
               ▼
          Ticket v1
               │
               ▼
          Update → v2


Request B ─────┐
               │
               ▼
          Ticket v1
               │
               ▼
       Conflict detected
```

Essa estratégia permite detectar alterações concorrentes antes de persistir uma versão desatualizada da entidade.

---

## Histórico e auditoria

O sistema está sendo estruturado para registrar alterações relevantes realizadas nos chamados.

Exemplo de timeline:

```text
10:02  Ticket criado
10:07  Técnico atribuído
10:19  Status → IN_PROGRESS
10:35  Comentário adicionado
11:42  Status → RESOLVED
11:50  Ticket → CLOSED
```

O histórico permite manter rastreabilidade das operações realizadas durante o ciclo de vida do chamado.

---

## Tratamento de erros

A aplicação possui tratamento centralizado de exceções para manter respostas HTTP consistentes.

Entre os cenários tratados:

* Recursos inexistentes
* Dados inválidos
* Erros de autenticação
* Acesso não autorizado
* Operações inválidas
* Conflitos de estado
* Erros de validação

A aplicação utiliza o padrão `ProblemDetail` baseado na RFC 7807.

Exemplo:

```json
{
  "type": "https://api.helpdesk.com/errors/resource-not-found",
  "title": "Resource not found",
  "status": 404,
  "detail": "Ticket não encontrado",
  "instance": "/api/v1/tickets/123"
}
```

---

## Banco de dados

O PostgreSQL é utilizado como banco de dados principal da aplicação.

A evolução do schema é controlada pelo Flyway.

```text
src/main/resources/db/migration
│
├── V1__initial_schema.sql
├── V2__...
├── V3__...
└── ...
```

O Hibernate utiliza `ddl-auto: validate`, permitindo que o schema seja criado e alterado exclusivamente através das migrations versionadas.

Fluxo:

```text
        Flyway Migration
               │
               ▼
          PostgreSQL
               │
               ▼
       Hibernate Validate
               │
               ▼
        Spring Boot API
```

---

## Processamento assíncrono

O projeto possui arquitetura preparada para utilizar RabbitMQ em operações que não precisam bloquear a resposta HTTP.

Exemplos de possíveis eventos:

```text
Ticket Created
Ticket Assigned
Ticket Updated
Ticket Resolved
SLA Breached
Notification Requested
```

Fluxo:

```text
Spring Boot API
       │
       │ Publish Event
       ▼
   RabbitMQ
       │
       ▼
     Queue
       │
       ▼
    Consumer
       │
       ▼
 Asynchronous Task
```

O objetivo é manter operações assíncronas desacopladas do fluxo principal da API.

---

## Estrutura do projeto

```text
helpdesk-service-api/
│
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/helpdesk/
│   │   │       ├── shared/
│   │   │       ├── user/
│   │   │       └── ticket/
│   │   │
│   │   └── resources/
│   │       ├── db/
│   │       │   └── migration/
│   │       └── application.yml
│   │
│   └── test/
│
├── docker-compose.yml
├── pom.xml
├── .gitignore
└── README.md
```

---

## Executando o projeto

### Pré-requisitos

Antes de iniciar a aplicação, certifique-se de possuir:

* Java 17+
* Maven
* Docker
* Docker Compose
* Git

### 1. Clone o repositório

```bash
git clone https://github.com/gbdev7/helpdesk-service-api.git
```

Entre no diretório:

```bash
cd helpdesk-service-api
```

### 2. Inicialize o PostgreSQL

```bash
docker compose up -d
```

Para verificar os containers:

```bash
docker compose ps
```

### 3. Execute a aplicação

Linux/macOS:

```bash
./mvnw spring-boot:run
```

Windows:

```bash
mvnw.cmd spring-boot:run
```

A aplicação será iniciada em:

```text
http://localhost:8080
```

---

## Configuração

As credenciais e secrets utilizados pela aplicação devem ser configurados através de variáveis de ambiente.

Exemplo:

```env
DB_URL=jdbc:postgresql://localhost:5432/helpdesk
DB_USERNAME=postgres
DB_PASSWORD=postgres
JWT_SECRET=your-secret-key
```

Secrets reais não devem ser versionados no repositório.

---

## Serviços e portas

| Serviço             | Endereço                 | Porta |
| ------------------- | ------------------------ | ----: |
| Spring Boot API     | `http://localhost:8080`  |  8080 |
| PostgreSQL          | `localhost`              |  5432 |
| RabbitMQ AMQP       | `localhost`              |  5672 |
| RabbitMQ Management | `http://localhost:15672` | 15672 |

---

## API

A API segue o padrão REST e utiliza versionamento nos endpoints.

Exemplos:

```text
POST   /api/v1/auth/login

POST   /api/v1/users
GET    /api/v1/users
GET    /api/v1/users/{id}

POST   /api/v1/tickets
GET    /api/v1/tickets
GET    /api/v1/tickets/{id}
PATCH  /api/v1/tickets/{id}
```

### Autenticação

```http
POST /api/v1/auth/login
Content-Type: application/json
```

Exemplo:

```json
{
  "email": "usuario@email.com",
  "password": "password"
}
```

Após a autenticação:

```http
Authorization: Bearer <token>
```

---

## Documentação da API

A API utiliza OpenAPI para documentação dos endpoints.

Com a aplicação em execução, a documentação pode ser acessada através do Swagger UI:

```text
http://localhost:8080/swagger-ui.html
```

---

## Testes

A estratégia de testes do projeto contempla diferentes níveis:

```text
                 Tests
                   │
        ┌──────────┴──────────┐
        │                     │
        ▼                     ▼
   Unit Tests          Integration Tests
        │                     │
        ▼                     ▼
 Services / Rules       API / Database
```

Principais cenários:

* Autenticação
* Criação de usuários
* Validação de dados
* Controle de acesso
* Criação de tickets
* Transições de status
* Regras de SLA
* Concorrência
* Tratamento de exceções

---

## Roadmap

### Foundation

* [x] Estrutura inicial do projeto
* [x] Arquitetura modular
* [x] PostgreSQL
* [x] Flyway
* [x] Docker Compose
* [x] Spring Security
* [x] JWT
* [x] BCrypt
* [ ] Bean Validation
* [ ] ProblemDetail
* [ ] RBAC completo

### Ticket Management

* [ ] Criação de tickets
* [ ] Consulta de tickets
* [ ] Atualização de tickets
* [ ] Status
* [ ] Prioridade
* [ ] Categorias
* [ ] Atribuição de técnicos
* [ ] Comentários

### SLA

* [ ] Política de SLA
* [ ] Primeira resposta
* [ ] Prazo de resolução
* [ ] Cálculo de SLA
* [ ] Detecção de violação
* [ ] Métricas

### History & Audit

* [ ] Histórico de alterações
* [ ] Timeline do ticket
* [ ] Auditoria
* [ ] Registro de ações

### Quality

* [ ] Unit tests
* [ ] Integration tests
* [ ] Testcontainers
* [ ] Code coverage
* [ ] Testes de segurança

### Infrastructure

* [ ] Containerização da API
* [ ] CI/CD
* [ ] RabbitMQ
* [ ] Consumers
* [ ] Notificações assíncronas

---

## Decisões arquiteturais

### Modular Monolith

O projeto utiliza Modular Monolith para manter os domínios separados sem introduzir inicialmente a complexidade operacional de microservices.

A arquitetura permite que módulos sejam evoluídos ou extraídos futuramente caso exista uma necessidade real.

### PostgreSQL

Escolhido pela robustez, suporte a transações, integridade referencial e ampla utilização em sistemas backend.

### Flyway

Utilizado para controlar a evolução do schema através de migrations versionadas.

### JWT

Utilizado para autenticação stateless e proteção dos endpoints da API.

### Optimistic Locking

Utilizado para lidar com possíveis atualizações concorrentes de entidades importantes, evitando sobrescritas silenciosas.

### RabbitMQ

Planejado para operações assíncronas e eventos que não precisam bloquear o fluxo principal da API.

### Sem Redis inicialmente

O projeto prioriza a resolução dos problemas de negócio antes da introdução de componentes adicionais.

Redis poderá ser utilizado posteriormente caso exista uma necessidade concreta de cache, rate limiting ou outra funcionalidade que justifique sua adoção.

---

## Objetivos técnicos

O HelpDesk Service API está sendo desenvolvido como um projeto de estudo e portfólio com foco em conceitos utilizados no desenvolvimento de sistemas backend profissionais.

O projeto busca demonstrar conhecimentos em:

* Java
* Spring Boot
* Spring Security
* APIs REST
* JWT
* RBAC
* PostgreSQL
* Flyway
* Modelagem de domínio
* Regras de negócio
* SLA
* Optimistic Locking
* Auditoria
* Testes automatizados
* Docker
* RabbitMQ
* Processamento assíncrono

Mais do que demonstrar quantidade de tecnologias, o objetivo é demonstrar decisões técnicas e a capacidade de construir uma aplicação evolutiva, organizada e consistente.

---

## Status do projeto

🚧 Em desenvolvimento

O projeto está sendo desenvolvido incrementalmente, começando pela fundação da aplicação e evoluindo para gerenciamento de tickets, SLA, auditoria, testes e processamento assíncrono.

---

## Autor

Gabriel Pereira

Desenvolvedor em formação com foco em Backend Java.

GitHub: https://github.com/gbdev7
