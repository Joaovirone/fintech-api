# 🏦 Fintech API - Sistema Bancário Completo

API RESTful robusta desenvolvida para simular operações bancárias reais. O projeto foca em **Arquitetura Limpa**, **Segurança (JWT)** e **Conteinerização**. O diferencial deste sistema é a implementação completa de **RBAC (Role-Based Access Control)**, onde administradores possuem privilégios elevados sobre a gestão de contas.

---

## 📋 Índice

1. [Visão Geral e Arquitetura](#-visão-geral-e-arquitetura)
2. [Tecnologias Utilizadas](#-tecnologias-utilizadas)
3. [Instalação e Execução (Docker)](#-instalação-e-execução-passo-a-passo)
4. [Documentação da API (Swagger)](#-documentação-interativa-swagger)
5. [Guia de Uso (Tutoriais)](#-guia-de-uso-exemplos-práticos)
    - [Cenário 1: Fluxo do Administrador](#1-o-super-usuário-admin)
    - [Cenário 2: Fluxo do Cliente (User)](#2-o-cliente-bancário-user)
    - [Cenário 3: Operações Financeiras](#3-realizando-transações)
6. [Banco de Dados](#-estrutura-do-banco-de-dados)

---

## 🏛️ Visão Geral e Arquitetura

O sistema é dividido em camadas bem definidas para garantir a manutenção e escalabilidade:

* **Controller:** Ponto de entrada REST. Recebe JSON, valida dados (DTOs) e repassa para o Service.
* **Service:** Coração da regra de negócio. Realiza validações (saldo, titularidade), cálculos e orquestra transações.
* **Repository:** Camada de persistência usando **Spring Data JPA**.
* **Model:** Entidades que espelham as tabelas do banco de dados.
* **Security:** Filtros e configurações que interceptam requisições para validar Tokens JWT.

### Regras de Negócio Principais
1.  **Unicidade:** Um usuário só pode ter **uma** conta bancária (Relação 1:1).
2.  **Segurança:** Todas as senhas são criptografadas (BCrypt) antes de ir ao banco.
3.  **Transacionalidade:** Transferências são atômicas (`@Transactional`). Se o depósito falhar, o saque é desfeito automaticamente.
4.  **Permissões (Roles):**
    * **USER:** Cria conta, vê saldo próprio, transfere.
    * **ADMIN:** Lista todas as contas do banco, deleta contas, acessa dados globais.

---

## 🚀 Tecnologias Utilizadas

* **Linguagem:** Java 21 (LTS)
* **Framework:** Spring Boot 3.3.5
* **Segurança:** Spring Security + JWT (JSON Web Token)
* **Banco de Dados:** PostgreSQL 16
* **Migração de Dados:** Flyway (Versionamento de Schema SQL)
* **Infraestrutura:** Docker & Docker Compose
* **Documentação:** SpringDoc OpenAPI (Swagger UI)
* **Ferramentas:** Lombok, Maven.

---

## 🐳 Instalação e Execução (Passo a Passo)

A aplicação é **Dockerizada**. Isso significa que você não precisa instalar Java ou Postgres na sua máquina, apenas o Docker.

### Pré-requisitos
* [Docker Desktop](https://www.docker.com/products/docker-desktop/) instalado e rodando.

### Como Rodar

1.  **Clone o repositório:**
    ```bash
    git clone [https://github.com/Joaovirone/fintech-api.git](https://github.com/Joaovirone/fintech-api.git)
    cd fintech-api
    ```

2.  **Suba o ambiente com Docker Compose:**
    Este comando irá baixar as imagens, compilar o projeto Java (Maven wrapper), criar o banco de dados e conectar tudo.
    ```bash
    docker-compose up --build
    ```

3.  **Aguarde a inicialização:**
    O processo pode levar alguns minutos na primeira vez. Aguarde até ver a mensagem no terminal:
    `Started FintechApplication in X seconds`

---

## 📚 Documentação Interativa (Swagger)

Com a aplicação rodando, você pode testar todos os endpoints sem precisar de Postman ou Insomnia.

👉 **Acesse:** [http://localhost:8080/swagger-ui/index.html](http://localhost:8080/swagger-ui/index.html)

**Como Autenticar no Swagger:**
1.  Faça Login no endpoint `/auth/login`.
2.  Copie o token gerado (string longa).
3.  Clique no botão **Authorize 🔓** no topo da página.
4.  Cole o token e confirme. Agora o cadeado ficará fechado 🔒.

---

## 🧪 Guia de Uso (Exemplos Práticos)

Aqui estão os roteiros para testar as funcionalidades do sistema.

### 1. O Super Usuário (ADMIN)
*O Admin tem poderes para gerenciar o banco.*

**A. Criar um Admin**
* **Endpoint:** `POST /auth/register`
* **Payload:**
    ```json
    {
      "login": "chefe",
      "senha": "123",
      "role": "ADMIN"
    }
    ```

**B. Logar como Admin**
* **Endpoint:** `POST /auth/login`
* **Payload:** `{"login": "chefe", "senha": "123"}`
* ⚠️ **Ação:** Copie o Token e autorize no Swagger.

**C. Listar Todas as Contas (Exclusivo Admin)**
* **Endpoint:** `GET /contas`
* **Resultado:** Retorna uma lista de todas as contas do banco. (Se um usuário comum tentar, recebe erro 403).

---

### 2. O Cliente Bancário (USER)
*O usuário comum só gerencia a própria conta.*

**A. Criar Usuário**
* **Endpoint:** `POST /auth/register`
* **Payload:**
    ```json
    {
      "login": "joao_cliente",
      "senha": "123",
      "role": "USER"
    }
    ```

**B. Logar**
* **Endpoint:** `POST /auth/login` com os dados acima.
* ⚠️ **Ação:** Faça **Logout** do Admin no Swagger e coloque o Token do João.

**C. Criar Conta Bancária**
* **Endpoint:** `POST /contas`
* **Payload:**
    ```json
    {
      "titular": "João da Silva",
      "cpf": "123.123.123-00",
      "dataDeNascimento": "1990-05-20",
      "valorInicial": 1000.00
    }
    ```

---

### 3. Realizando Transações

Vamos simular uma transferência entre contas. (Crie um segundo usuário "Maria" seguindo os passos acima para ter para quem transferir).

* Suponha: Conta João (ID 1) -> Conta Maria (ID 2).

**Transferência (Logado como João):**
* **Endpoint:** `POST /contas/transferir`
* **Payload (JSON) ou Parâmetros (Query):**
    * `idOrigem`: 1
    * `idDestino`: 2
    * `valor`: 200.00
* **Resultado:** O saldo do João cai para 800 e o da Maria sobe para 200 (se ela começou com 0).

**Consultar Extrato:**
* **Endpoint:** `GET /contas/{id}/extrato`
* **Resultado:** Mostra o histórico detalhado da operação.

---

### 🗄️ Estrutura do Banco de Dados

O banco é gerenciado automaticamente pelo **Flyway**. As tabelas principais são:

| Tabela | Descrição |
| :--- | :--- |
| **`usuarios`** | Armazena Login, Senha (Hash) e Role. |
| **`conta`** | Contém Saldo, Titular, CPF e chave estrangeira para `usuarios`. |
| **`transacao`** | Registro imutável de saques, depósitos e transferências. |

---

## 🤝 Contribuição

Projeto desenvolvido para fins educacionais. Sinta-se à vontade para fazer um fork e enviar Pull Requests com melhorias!

---
Desenvolvido com ☕(Java) e Spring Boot.