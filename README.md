# 📦 Marketplace – Sistema de Cadastro com Persistência em Arquivos

Este projeto consiste no desenvolvimento de um sistema de *Marketplace* simples, voltado para o **cadastro e gerenciamento de Lojas, Compradores e Produtos**, com foco na modularização da arquitetura e persistência dos dados em arquivos `.txt`. Ele foi desenvolvido como parte de uma disciplina acadêmica, estruturado em duas sprints, utilizando práticas de engenharia de software e organização via Azure DevOps.

---

## ⚙️ Funcionalidades Principais

- ✅ **CRUD de Lojas**  
  Permite cadastrar, listar e remover lojas, com dados persistidos em arquivos.

- ✅ **CRUD de Compradores**  
  Gerenciamento de compradores com operações básicas de inserção, listagem e exclusão.

- ✅ **CRUD de Produtos com vínculo à Loja**  
  Cada produto é associado à loja que o cadastrou. O sistema permite visualizar os produtos de forma filtrada por loja.

- ✅ **Persistência em Arquivos (.txt)**  
  Todos os dados manipulados no sistema são armazenados localmente em arquivos, garantindo que não sejam perdidos após o encerramento da aplicação.

- ✅ **Implementação do Padrão Facade**  
  Facilita a integração entre as camadas do sistema (Controller, Model, View, Repository), promovendo desacoplamento e reuso de código.

- ✅ **Interface de Texto (CLI)**  
  Menu interativo com navegação orientada ao usuário, permitindo o uso do sistema sem conhecimento técnico prévio.

---

## 🧱 Arquitetura do Projeto

O projeto segue o modelo de arquitetura em camadas, dividindo responsabilidades da seguinte forma:

- **Model** – Representa as entidades do sistema.
- **Repository** – Responsável pela leitura e escrita em arquivos.
- **Controller** – Camada de lógica de negócio.
- **View** – Interface com o usuário via terminal.
- **Facade** – Centraliza o acesso às funcionalidades do sistema de forma simplificada.

---

## 🧪 Testes

Nesta primeira release, os testes foram realizados manualmente, cobrindo:

- Validação da criação e integridade dos objetos.
- Persistência correta dos dados em arquivos.
- Funcionamento da lógica de negócio via Facade.
- Navegação e uso da interface textual.
- Tratamento de erros e exceções.

Em versões futuras, está prevista a adição de **testes automatizados com JUnit**, além de ferramentas de cobertura de testes.

---

## 🚀 Futuras Expansões

- ✅ Adoção de testes automatizados (JUnit).
- ✅ Integração com interface gráfica (GUI).
- ✅ Filtros e relatórios de produtos por loja.
- ✅ Autenticação de usuários (Lojistas e Compradores).

---

## 👥 Equipe

- **PO Sprint 1:** Pedro  
- **PO Sprint 2:** Wagner

---

## 📁 Organização do Repositório

