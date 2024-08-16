# API de um sistema de controle de atendimento e informacoes de Pets!

Demostração da implementação de segurança role-based usando Spring Boot e JSON Web Tokens (JWT).

## Recursos

* Cadastro de usuário e login com autenticação JWT;

* Criptografia de senha usando BCrypt;

* Autorização baseada em função com Spring Security;

* Tratamento personalizado de negação de acesso;

* Interface para administrador e cliente;

## Tecnologias

* Spring Boot 3.0

* Spring Security

* JSON Web Tokens (JWT)

* BCrypt

* Maven

## Executando

Para executar esse projeto, vai precisar

* Java JDK 17+

* Docker


1. Navegue até o diretório do projeto:

    ```sh
    cd spring-boot-3-security-jwt
    ```

2. Execute o compose:

    ```sh
    docker compose up -d
    ```

3. Compile o projeto:

    ```sh
    ./mvnw clean install
    ```

4. Execute o projeto:

    ```sh
    ./mvnw spring-boot:run
    ```
