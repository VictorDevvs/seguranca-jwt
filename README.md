# Segurança-JWT (backend)

## 🔐 O que é este projeto

Este projeto implementa um backend em Java com Spring Boot + Spring Security + JWT para autenticação e autorização de usuários.  
Suporta:

- Registro de usuário com e-mail/senha  
- Login com e-mail/senha gerando token JWT  
- Login via OAuth2 com provedores externos (Google / GitHub)  
- Endpoints protegidos por token JWT  
- Endpoint `/me` para retornar dados do usuário logado  
- Endpoint para buscar usuário por ID (restrito: cada usuário só pode consultar seu próprio ID)  

---

## 🛠 Tecnologias e dependências

- Java 21 + Spring Boot  
- Spring Data JPA
- Spring web 
- H2 Database
- Lombok
- Validation
- Spring Security + JWT (io.jsonwebtoken)  
- OAuth2 Client (Google, GitHub)  
- Dependências comuns do ecossistema Spring Boot  
