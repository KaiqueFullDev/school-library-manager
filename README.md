# LibQueue - Pendências de Desenvolvimento

# 📋 Quadro de Tarefas

| Tarefa | Responsável | Status |
|---------|-------------|---------|
| Estruturar os arquivos `usuarios.txt`, `livros.txt`, `emprestimos.txt`, `reservas.txt` e `ids.txt` com dados iniciais | Indaia | ⬜ Não iniciado |
| Adicionar mais dados de teste ao acervo e aos usuários | Indaia | ⬜ Não iniciado |
| Implementar `carregarLivros()` | Maria Eduarda | ⬜ Não iniciado |
| Implementar `carregarUsuarios()` | Maria Eduarda | ⬜ Não iniciado |
| Implementar `carregarEmprestimos()` | Maria Eduarda | ⬜ Não iniciado |
| Implementar `carregarReservas()` | Maria Eduarda | ⬜ Não iniciado |
| Implementar `carregarIds()` | Maria Eduarda | ⬜ Não iniciado |
| Implementar `salvarLivro(Livro livro)` | Charles | ⬜ Não iniciado |
| Implementar `salvarUsuario(Usuario usuario)` | Charles | ⬜ Não iniciado |
| Implementar `salvarEmprestimo(Emprestimo emprestimo)` | Charles | ⬜ Não iniciado |
| Implementar `salvarReserva(Reserva reserva)` | Charles | ⬜ Não iniciado |
| Implementar `sobrescreverLivros(...)` | Charles | ⬜ Não iniciado |
| Implementar `sobrescreverUsuarios(...)` | Charles | ⬜ Não iniciado |
| Implementar `sobrescreverEmprestimos(...)` | Charles | ⬜ Não iniciado |
| Implementar `sobrescreverReservas(...)` | Charles | ⬜ Não iniciado |
| Criar construtores auxiliares para reconstrução dos relacionamentos (Usuário, Livro, Título, etc.) | A definir | ⬜ Não iniciado |
| Implementar reconstrução dos relacionamentos após leitura dos arquivos | A definir | ⬜ Não iniciado |
| Criar métodos de persistência na `BibliotecaRepository` para manter sincronização entre memória e arquivos | A definir | ⬜ Não iniciado |
| Ajustar Services para utilizar os novos métodos de persistência | A definir | ⬜ Não iniciado |
| Substituir estruturas de dados personalizadas por estruturas nativas da Collection Framework | Ana Clara | ⬜ Não iniciado |
| Ajustar menu superior da tela de Inventário do Usuário | Kaique | ⬜ Não iniciado |
| Reduzir o efeito de "piscar" durante a navegação entre telas | Kaique | ⬜ Não iniciado |


## Objetivo da Atualização

O sistema deixará de utilizar a classe `DataBaseSeed` como mecanismo de persistência em memória e passará a utilizar arquivos `.txt` para armazenamento permanente dos dados.

A nova arquitetura será composta principalmente por:

* `PersistenceManager` → responsável pela leitura e escrita dos arquivos.
* `BibliotecaRepository` → responsável por manter os dados carregados, reconstruir relacionamentos entre objetos e fornecer acesso às informações do sistema.
* Arquivos:

  * `usuarios.txt`
  * `livros.txt`
  * `emprestimos.txt`
  * `reservas.txt`
  * `ids.txt`

---

# Tarefas Gerais

## 1. Criação de Construtores Auxiliares

**Responsável: Definir**

Criar construtores simplificados para permitir a criação de objetos temporários durante o carregamento dos arquivos.

Exemplos:

```java
Usuario(String id)

Livro(long id)

Titulo(String isbn)
```

Esses construtores serão utilizados apenas durante a leitura dos arquivos, permitindo a criação de objetos "fictícios" que posteriormente terão suas referências corrigidas pelo `BibliotecaRepository`.

---

## 2. Ajustes na Camada de Serviços

**Responsável: Definir**

Após a implementação da persistência em arquivos, os serviços deverão ser revisados.

Necessário:

* Atualizar operações de cadastro.
* Atualizar operações de remoção.
* Atualizar operações de empréstimo.
* Atualizar operações de devolução.
* Atualizar operações de reserva.

Sempre que houver alteração nos dados, ela deverá ser refletida tanto:

* Nas estruturas em memória.
* Nos arquivos de persistência.

---

## 3. Métodos de Manipulação de Dados no BibliotecaRepository

**Responsável: Definir**

Criar métodos responsáveis por:

### Livros

* adicionarLivro(...)
* removerLivro(...)

### Usuários

* adicionarUsuario(...)
* removerUsuario(...)

### Empréstimos

* adicionarEmprestimo(...)
* removerEmprestimo(...)

### Reservas

* adicionarReserva(...)
* removerReserva(...)

### Inicialização do Sistema

Implementar método responsável por:

```java
carregarSistema()
```

Fluxo esperado:

1. Carregar livros.
2. Carregar usuários.
3. Carregar empréstimos.
4. Carregar reservas.
5. Carregar IDs válidos.
6. Reconstruir relacionamentos.

---

## 4. Reconstrução dos Relacionamentos

**Responsável: Definir**

Implementar método responsável por substituir referências temporárias pelas referências reais após o carregamento dos arquivos.

Exemplos:

* Emprestimo → Usuario
* Emprestimo → Livro
* Reserva → Usuario
* Reserva → Titulo

Além disso:

* Associar empréstimos aos respectivos usuários.
* Associar reservas aos respectivos usuários.
* Reconstruir a lista de títulos.
* Reconstruir filas de reserva.

---

# Tarefas Individuais

## Charles

### Escrita Incremental (Append)

Implementar:

```java
salvarLivro(Livro livro)

salvarUsuario(Usuario usuario)

salvarEmprestimo(Emprestimo emprestimo)

salvarReserva(Reserva reserva)
```

Cada método deverá adicionar uma nova linha ao arquivo correspondente sem apagar os dados já existentes.

---

### Reescrita Completa

Implementar:

```java
sobrescreverLivros(...)

sobrescreverUsuarios(...)

sobrescreverEmprestimos(...)

sobrescreverReservas(...)
```

Esses métodos serão utilizados em operações de remoção e atualização.

---

## Maria Eduarda

### Leitura dos Arquivos

Implementar:

```java
carregarLivros()

carregarUsuarios()

carregarEmprestimos()

carregarReservas()

carregarIds()
```

Os objetos carregados poderão utilizar referências temporárias (usuários, livros e títulos fictícios).

A reconstrução final dos relacionamentos será feita posteriormente pelo `BibliotecaRepository`.

---

## Indaia

### Estruturação dos Arquivos Iniciais

Migrar os dados atualmente presentes no `DataBaseSeed` para:

* usuarios.txt
* livros.txt
* ids.txt

Também é necessário:

* Adicionar mais usuários para testes.
* Adicionar mais livros e exemplares.
* Garantir diversidade de dados para testes de empréstimo e reserva.

---

## Kaique

### Refinamento das Interfaces

Ajustar:

#### Inventário do Usuário

* Corrigir alinhamento e organização do menu superior.

#### Navegação

* Reduzir o efeito de intermitência (piscar de tela) durante as trocas de páginas.

---

## Ana Clara

### Estruturas de Dados

Substituir gradualmente estruturas implementadas manualmente por estruturas nativas do Java Collections Framework quando apropriado.

Avaliar principalmente:

* List
* ArrayList
* Queue
* PriorityQueue
* Map

Garantir compatibilidade com os DAOs existentes.

---

# Estrutura dos Arquivos

## usuarios.txt

Campos:

```text
id | nome | email | senha | tipo
```

---

## livros.txt

Campos:

```text
id | nome | autor | isbn | genero | descricao | dataPublicacao | disponivel
```

---

## emprestimos.txt

Campos:

```text
id | dataEmprestimo | dataDevolucao | atrasado | idUsuario | idLivro
```

---

## reservas.txt

Campos:

```text
id | idUsuario | isbnTitulo | dataReserva
```

---

## ids.txt

Lista contendo todos os IDs institucionais válidos.

Exemplo:

```text
s000001
s000002
s000003
p000001
p000002
l000001
```

---

# Observações

* A classe `DataBaseSeed` será removida.
* Toda persistência passará a ser feita via arquivos.
* O `PersistenceManager` será responsável apenas pela leitura e escrita dos arquivos.
* O `BibliotecaRepository` será responsável por montar o estado completo do sistema.
* Os relacionamentos entre objetos deverão ser reconstruídos após o carregamento dos dados.
* Novas funcionalidades deverão utilizar o `BibliotecaRepository` como ponto central de acesso aos dados.
