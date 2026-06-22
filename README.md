# 📚 LibQueue - School Library System

![Java](https://img.shields.io/badge/java-%23ED8B00.svg?style=for-the-badge&logo=openjdk&logoColor=white)
![JavaFX](https://img.shields.io/badge/JavaFX-ED8B00?style=for-the-badge&logo=java&logoColor=white)
![Git](https://img.shields.io/badge/git-%23F05033.svg?style=for-the-badge&logo=git&logoColor=white)

O **LibQueue** é um sistema de gerenciamento de biblioteca escolar desenvolvido para otimizar o fluxo de empréstimos, devoluções e reservas de livros. O software foca na experiência do usuário (alunos, professores e bibliotecários) através de uma interface moderna e dinâmica.

---

## 🎓 Origem e Contexto Acadêmico

Este projeto nasceu e está sendo expandido como parte prática das disciplinas do curso de **Sistemas de Informação** no **Instituto Federal da Bahia (IFBA), Campus Vitória da Conquista**.

O histórico de desenvolvimento do sistema está dividido em marcos acadêmicos:

1. **Estrutura de Dados (2026.1):**
   * **Foco:** Implementação da lógica de negócios, gerenciamento de acervo e controle de filas de reserva utilizando persistência em memória. Com o objetivo de consolidar os conceitos teóricos da disciplina, as estruturas de dados utilizadas foram implementadas de forma personalizada. Os dados iniciais da aplicação são povoados em tempo de execução por meio de uma classe semente (`DatabaseSeed`), que simula um banco de dados pré-carregado com livros e usuários.
   * **Docente:** Prof. Claudio Rodolfo Santos de Oliveira ([@claudiorodolfo](https://github.com/claudiorodolfo)).
   * *Nota: desenvolvido em [`archive/ed-final`](https://github.com/Nuillexe/school-library-manager/tree/archive/ed).*

2. **Linguagem de Programação 2 (Em Andamento):**
   * **Foco:** Evolução da arquitetura do backend para inclusão de **persistência de dados real** através da manipulação e armazenamento de arquivos locais. Nesta etapa, a estrutura interna do projeto é refatorada para utilizar exclusivamente as coleções e estruturas de dados nativas da API do Java (Java Collections).
   * **Docente:** Prof. Alexandro dos Santos Silva ([@alexandrossilva](https://github.com/alexandrossilva)).
  * *Nota: desenvolvido em [`archive/Lp2`](https://github.com/Nuillexe/school-library-manager/tree/archive/Lp2).*
---

## 🛠️ Tecnologias e Padrões Utilizados

* **Linguagem:** Java 17+
* **Interface Gráfica:** JavaFX (arquivos FXML e estilização CSS)
* **Padrão de Arquitetura:** MVC (Model-View-Controller)
* **Persistência (LP2):** [Ex: JDBC / MySQL / PostgreSQL - Atualizar conforme implementar]

---

## 🚀 Funcionalidades Principais

* **Autenticação Segura:** Telas de login customizadas com validações de credenciais para diferentes níveis de acesso.
* **Dashboard do Bibliotecário:** Painel geral com indicadores em tempo real (Total de Livros, Empréstimos Ativos, Movimentação Diária e Pendências).
* **Fila de Reserva Dinâmica:** Visualização automatizada dos próximos usuários na fila para retirada de exemplares disputados.
* **Inventário Automatizado:** Criação dinâmica de cards informando o estoque disponível, ISBN e ano de lançamento de cada título.

---

## 📁 Estrutura de Branches do Repositório

Para manter o histórico acadêmico organizado, o repositório utiliza a seguinte estrutura:

* `main`: Branch principal de desenvolvimento ativo (focada em LP2 e persistência).
* `archive/ed-final`: Versão estável do projeto utilizando apenas persistência em memória (Estrutura de Dados).

---

## 🔧 Como Executar o Projeto

### Pré-requisitos
* Java JDK 17 ou superior instalado.
* JavaFX SDK configurado na sua IDE (Eclipse, IntelliJ IDEA ou VS Code).

### Passo a Passo
1. Clone o repositório:
   ```bash
   git clone [https://github.com/SEU-USUARIO/NOME-DO-REPOSITORIO.git](https://github.com/SEU-USUARIO/NOME-DO-REPOSITORIO.git)
Abra o projeto na sua IDE de preferência.

Certifique-se de adicionar as bibliotecas do JavaFX ao Build Path do projeto.

Execute a classe principal (geralmente contendo o método main que inicia o Application do JavaFX).
