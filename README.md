# 🏦 Fintech API - Sistema Bancário

API RESTful desenvolvida em **Java 21** e **Spring Boot 3** para simular operações bancárias essenciais. O projeto foca em boas práticas de engenharia de software, incluindo arquitetura limpa, segurança com JWT, migrações de banco de dados e conteinerização completa.

---

## 🚀 Tecnologias Utilizadas

* **Java 21** (LTS)
* **Spring Boot 3.3.5**
* **Spring Security + JWT** (Autenticação e Autorização)
* **Spring Data JPA** (Persistência de dados)
* **PostgreSQL** (Banco de Dados)
* **Flyway** (Versionamento e Migração de Banco de Dados)
* **Docker & Docker Compose** (Ambiente de Desenvolvimento)
* **Swagger / OpenAPI** (Documentação viva da API)
* **Lombok** (Redução de código boilerplate)

---

## ⚙️ Arquitetura e Funcionalidades

O sistema gerencia contas bancárias, transações financeiras e usuários com diferentes permissões.

### Funcionalidades Principais:
* **Autenticação:** Cadastro de usuários e Login via Token JWT (JSON Web Token).
* **Gestão de Contas:** Criação de conta, consulta de saldo e extrato.
* **Operações Financeiras:**
    * Depósito.
    * Saque (com validação de saldo).
    * Transferência entre contas (transacional / atômico).
* **Segurança:** Endpoints protegidos; apenas o dono da conta (ou admin) pode acessar seus dados.

---

## 🛠️ Como Executar o Projeto

A maneira mais recomendada de rodar a aplicação é utilizando **Docker**, pois garante que o ambiente (Java e Banco de Dados) esteja configurado perfeitamente.

### Pré-requisitos
* [Docker](https://www.docker.com/) e Docker Compose instalados.

### 🐳 Rodando com Docker (Recomendado)

1.  Clone o repositório e entre na pasta:
    ```bash
    git clone [https://github.com/SEU-USUARIO/fintech-api.git](https://github.com/SEU-USUARIO/fintech-api.git)
    cd fintech-api
    ```

2.  Suba os containers (App + Banco):
    ```bash
    docker-compose up --build
    ```
    *Isso irá compilar o projeto, criar a imagem Docker, subir o PostgreSQL e iniciar a API na porta 8080.*

3.  Aguarde aparecer a mensagem: `Started FintechApplication`.

---

## 📚 Documentação da API (Swagger)

Com a aplicação rodando, acesse a documentação interativa para testar os endpoints:

👉 **[http://localhost:8080/swagger-ui/index.html](http://localhost:8080/swagger-ui/index.html)**

### Como Autenticar no Swagger:
1.  Vá no endpoint `POST /auth/login` (ou `/register` para criar um usuário).
2.  Copie o **token** gerado na resposta (sem as aspas).
3.  Clique no botão **Authorize 🔓** no topo da página.
4.  Cole o token e clique em **Authorize**.
5.  Agora você pode testar os endpoints protegidos (como criar conta ou transferir).

---

## 🗄️ Estrutura do Banco de Dados

O banco é gerenciado automaticamente pelo **Flyway**. Ao iniciar, ele cria as seguintes tabelas:

* **usuarios:** Credenciais e roles (ADMIN/USER).
* **conta:** Dados bancários (Saldo, Titular, CPF).
* **transacao:** Histórico de operações (Depósitos, Saques, Transferências).

---

## 📝 Exemplo de JSON (Criação de Conta)

**POST** `/contas`
```json
{
  "titular": "João da Silva",
  "cpf": "123.456.789-00",
  "dataDeNascimento": "1990-05-20",
  "valorInicial": 100.00
}