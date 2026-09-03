# 🎧 HelpDesk REST API

API RESTful para gestão de chamados de suporte e controle de SLA (Service Level Agreement), desenvolvida com Java 17 e Spring Boot 3. O projeto é focado em boas práticas de arquitetura de software, modularidade, segurança e escalabilidade.

---

## 📌 Proposta do Projeto

O objetivo do sistema é fornecer uma solução completa para centralizar e organizar atendimentos de TI/Suporte Técnico. A API gerencia desde o autocadastro e perfilamento de usuários até o ciclo de vida completo de um chamado, integrando regras de negócio complexas como prazos de atendimento (SLA), controle de permissões por perfil (RBAC) e histórico de interações.

---

## 🛠️ Tecnologias e Ferramentas Utilizadas

* **Linguagem:** Java 17
* **Framework:** Spring Boot 3.2.3
* **Segurança:** Spring Security & JJWT (Java JWT)
* **Padrão de Senhas:** BCrypt Hashing
* **Banco de Dados:** PostgreSQL
* **Migração de Dados:** Flyway
* **Persistência:** Spring Data JPA / Hibernate
* **Containerização:** Docker & Docker Compose
* **Build Tool:** Maven

---

## 🚀 O que já foi desenvolvido (Status Atual)

Atualmente, o projeto conta com as seguintes funcionalidades implementadas e validadas:

* **Infraestrutura e Banco de Dados:**
    * Subida do ambiente PostgreSQL via Docker Compose.
    * Versionamento do schema do banco através de migrations do Flyway.
* **Módulo de Usuários (Domain User):**
    * Operações de CRUD para gestão de usuários.
    * Perfis de acesso definidos (`ADMIN`, `TECHNICIAN`, `CUSTOMER`).
* **Segurança e Autenticação (JWT):**
    * Arquitetura de segurança Stateless.
    * Criptografia de senhas com `BCryptPasswordEncoder`.
    * Autenticação via `POST /api/v1/auth/login` retornando Token Bearer JWT.
    * Filtro de segurança customizado (`SecurityFilter`) para validação do Token nas rotas protegidas.
* **Tratamento de Exceções:**
    * Handler global de exceções (`GlobalExceptionHandler`) para respostas padronizadas da API.

---

## 📋 Próximos Passos (Em Desenvolvimento)

- [ ] **Etapa 5:** Módulo de Chamados (`Ticket`) e Regras de Negócio de SLA.
- [ ] **Etapa 6:** Histórico de Comentários, Interações e Troca de Status.
- [ ] **Etapa 7:** Testes Unitários e de Integração (JUnit 5 & Mockito).
- [ ] **Etapa 8:** Documentação da API com OpenAPI / Swagger.

---