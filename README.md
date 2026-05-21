# 📚 School Library System (IFBA)

Repositório destinado ao desenvolvimento do sistema de gerenciamento de biblioteca escolar para a disciplina de **Estrutura de Dados (2026.1)**. O projeto utiliza o padrão **MVC** e interfaces modernas desenvolvidas em **JavaFX**.

---

## 👥 Papéis e Atribuições do Grupo

| Membro | Papel Principal | Atribuições |
| :--- | :--- | :--- |
| Integrante | Papel Principal | Responsabilidades Detalhadas |
| :--- | :--- | :--- |
| **Emanuel** | **Líder / Integração** | Estrutura Maven, Gestão do Git, Integração Controller/View de Usuário e Revisão Geral. |
| **Ana Clara** | **Persistência (DAO)** | Implementação das classes de acesso a dados (CRUD) e Estruturas Dinâmicas. |
| **Charles** | **Service (Negócio)** | Implementação das Regras de Negócio e validações lógicas do sistema. |
| **Kaique** | **Fullstack / Design** | Telas e Controllers de Login/Cadastro e Interfaces do Bibliotecário. |
| **Maria Eduarda** | **ED / DAO** | Implementação da Fila de Prioridade e lógica específica de Reservas. |
| **Indaia** | **Modelagem** | Criação e manutenção das entidades (POJOs) e objetos do sistema. |
| **Nikolas** | **Controller Bibliotecário** | Integração das Views com a lógica de controle do Bibliotecário. |
| **Pedro** | **Front-end Usuário** | Construção das interfaces visuais para Alunos e Professores no Scene Builder. |
---

## 🏗️ Divisão Técnica

### 📂 Estrutura de Dados (`ed`)
*Camada responsável por gerenciar as estruturas de dados customizadas:*
- **Ana Clara**: `ListaDinamica`, `NoDuplo`, `Listavel`.🟢 (Concluído)
- **Maria Eduarda**: `ReservaComparator` (Lógica de prioridade).🟢 (Concluído)

### 📂 Repository (DAO)
*Camada responsável por gerenciar a persistência em memória:*
- **Ana Clara**: `LivroDAOLista`, `UsuarioDAOLista`, `TituloDAOLista`, `EmprestimoDAOLista` e `ReservaDAOLista`.🟢 (Concluído)
- **Maria Eduarda**: `ReservaDAOFilaDePrioridade`.🟢 (Concluído)
- **Emanuel**: `DataBaseSeed`.🟢 (Concluído)

### 📂 Service (Lógica)
*Lógica principal do sistema:*
- **Charles**: `AuthService`, Login e Segurança.🟢 (Concluído))
- **Charles**: `UsuarioService`, Gestão de usuários.🟢 (Concluído)
- **Charles**: `BibliotecarioService`, Operações administrativas.🟢 (Concluído)

### 📂 Models (Entidades)
*Entidades base*
- **Indaia, Maria Eduarda**:`Livro`, `Titulo`, `Usuario`, `Reserva`, `Emprestimo`.🟢 (Concluído)
- **Emanuel**: `Bibiloteca`.🟢 (Concluído)

### 📂 Views (resources/views)
*Interfaces FXML desenvolvidas no Scene Builder:*
- **Kaique**: `AuthViews`.🟢 (Concluído)
- **Pedro**: `UsuarioViews`.🟢 (Concluído)
- **Kaique**: `BibliotecarioViews` 🟢 (Concluído)

### 📂 Controller
*Lógica das telas e integração:*
- **Kaique**: `AuthController`, Controle da tela de Login e Cadastro 🟢 (Concluído)
- **Pedro, e Emanuel**: `UsuarioController` 🟢 (Concluído)
- **Emanuel, e Nikolas**: `BibliotecarioController` 🟢 (Concluído)
  
---

## 🚀 Integração e Revisão Geral
**Responsável:** Emanuel (Líder)
*Garantia da coesão entre os pacotes e funcionamento do ciclo de vida da aplicação.*

---
*Este documento será atualizado conforme a contribuição de cada membro do grupo.*
