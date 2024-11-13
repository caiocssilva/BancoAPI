# BancoAPI

**BancoAPI** é um projeto para gerenciamento de contas bancárias e pagamentos. Ele oferece funcionalidades como a criação de contas, gerenciamento de saldos, geração de relatórios de transações, e autenticação de usuários. O projeto também conta com documentação gerada automaticamente utilizando o Swagger.

## Funcionalidades

- **Autenticação de Usuários**: Registro e login de usuários com JWT.
- **Gerenciamento de Contas**: Criação, leitura, atualização e exclusão de contas bancárias.
- **Transações**: Registro de pagamentos e visualização de transações realizadas.
- **Relatórios**: Geração de relatórios de transações.

## Tecnologias Utilizadas

- **Backend**: Java com Spring Boot
- **Banco de Dados**: MySQL
- **Autenticação**: JWT (JSON Web Tokens)
- **Documentação**: Swagger
- **Outras Ferramentas**: Spring Data JPA, Lombok, Spring Security

## Endpoints da API

### 1. **Account Controller**

#### GET `/api/accounts/{id}`
- **Descrição**: Recupera uma conta bancária pelo seu ID.
- **Parâmetros**: `id` (ID da conta).
- **Exemplo de resposta**:
  ```json
  {
    "id": 1,
    "name": "Caio Cesar",
    "accountType": "Poupança",
    "initialBalance": 1600,
    "currentBalance": 1200
  }

### PUT `/api/accounts/{id}`
- **Descrição**: Atualiza os dados de uma conta bancária.
- **Parâmetros**: `id` (ID da conta).
- **Corpo da requisição**:
  ```json
  {
    "name": "Caio Cesar",
    "accountType": "Corrente",
    "initialBalance": 2000
  }

### DELETE `/api/accounts/{id}`
- **Descrição**: Exclui uma conta bancária pelo ID.

### POST `/api/accounts/create`
- **Descrição**: Cria uma nova conta bancária.
- **Corpo da requisição**:
  ```json
  {
    "name": "Novo Cliente",
    "accountType": "Corrente",
    "initialBalance": 1000
  }

### GET `/api/accounts`
- **Descrição**: Recupera todas as contas bancárias.
- **Autenticação**: Requer permissão de `ROLE_ADMIN`.

---

## 2. Auth Controller

### POST `/auth/register`
- **Descrição**: Registra um novo usuário.
- **Corpo da requisição**:
  ```json
  {
    "username": "novousuario",
    "password": "senha123"
  }

### POST `/auth/login`
- **Descrição**: Realiza o login do usuário e retorna um JWT.
- **Corpo da requisição**:
  ```json
  {
    "username": "novousuario",
    "password": "senha123"
  }

## 3. Payment Controller

### POST `/api/payments/create`
- **Descrição**: Registra um pagamento realizado.
- **Corpo da requisição**:
  ```json
  {
    "accountId": 1,
    "amount": 500
  }

### GET `/api/payments`
- **Descrição**: Recupera todos os pagamentos realizados.

### GET `/api/payments/report`
- **Descrição**: Gera um relatório de transações.

---

## Como Rodar o Projeto

### Requisitos
- Java 17 ou superior
- MySQL instalado e configurado

```bash 
$ ./mvnw clean package
```
- Executar aplicação:

```bash 
$ java -jar target/bancoapi-0.0.1-SNAPSHOT.jar
```

### Acesse a API pelo Swagger UI:
Abra o navegador e acesse [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html) para ver a documentação da API e testar os endpoints.

### Contribuindo
1. Faça o fork do repositório.
2. Crie uma branch para sua feature:
```bash
git checkout -b feature/nova-feature
```
3. Faça as modificações necessárias.
4. Faça commit das suas alterações:
```bash
git commit -m "feat: descrição da nova funcionalidade"
```
5. Envie para o repositório remoto:
```bash
git push origin feature/nova-feature
```
6.Abra um Pull Request para que possamos revisar e integrar as alterações

## Licença

Distribuído sob a licença MIT. Veja o arquivo [LICENSE](LICENSE) para mais informações.