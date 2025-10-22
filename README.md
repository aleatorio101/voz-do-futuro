# Voz do Futuro

Sistema de gestão de propostas públicas desenvolvido como parte da AEP do 4º Semestre de Engenharia de Software.

## Sobre o Projeto

O Voz do Futuro é uma aplicação que permite a interação entre cidadãos e funcionários públicos através de um sistema de propostas. Os cidadãos podem submeter propostas para melhorias na cidade, enquanto funcionários públicos podem avaliar e gerenciar essas sugestões.

## Tecnologias Utilizadas

- Java 21
- HttpServer (com.sun.net.httpserver)
- SQLite
- GSON para manipulação de JSON
- JUnit para testes unitários

## Estrutura do Projeto

- `src/`
  - `app/` - Configuração e inicialização do servidor
  - `dao/` - Camada de acesso a dados
  - `handler/` - Manipuladores de requisições HTTP
  - `model/` - Classes de domínio e DTOs
  - `service/` - Lógica de negócio
  - `util/` - Classes utilitárias

## Funcionalidades

### Usuários
- Cadastro de usuários (Cidadão, Funcionário, Admin)
- Autenticação
- Gerenciamento de perfil

### Propostas
- Criação de propostas por cidadãos
- Listagem de propostas
- Avaliação de propostas por funcionários
- Comentários em propostas
