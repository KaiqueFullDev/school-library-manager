package br.edu.ifba.models;

import br.edu.ifba.enums.TipoUsuario;
import br.edu.ifba.repository.dao.EmprestimoDAOLista;

/**
 * Classe que representa um usuário do sistema (Aluno, Professor ou Bibliotecário).
 */
public class Usuario {
    private String id;
    private String nome;
    private String email;
    private String senha;
    public TipoUsuario tipo;
    private int limiteLivros;
    private EmprestimoDAOLista listaEmprestimos; // Lista encadeada/dinâmica contendo os empréstimos ativos do usuário

    /**
     * Construtor parametrizado para registrar um novo usuário com cálculo automático do limite de livros.
     */
    public Usuario(String id, String nome, String email, String senha, TipoUsuario tipo) {
        this.id = id;
        this.nome = nome;
        this.email = email;
        this.tipo = tipo;
        this.senha = senha;

        // Define a quantidade máxima de livros permitidos simultaneamente baseada na categoria
        if (tipo == TipoUsuario.ALUNO) {
            this.limiteLivros = 3;
        } else if (tipo == TipoUsuario.PROFESSOR) {
            this.limiteLivros = 4;
        } else {
            this.limiteLivros = 5; // Evita o valor padrão zero para bibliotecários ou administradores
        }

        this.listaEmprestimos = new EmprestimoDAOLista();
    }

    // Getters e Setters comentados

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getSenha() { return senha; }
    public void setSenha(String senha) { this.senha = senha; }

    public TipoUsuario getTipo() { return tipo; }
    public void setTipo(TipoUsuario tipo) { this.tipo = tipo; }

    public int getLimiteLivros() { return limiteLivros; }
    public void setLimiteLivros(int limiteLivros) { this.limiteLivros = limiteLivros; }

    public EmprestimoDAOLista getListaEmprestimos() { return listaEmprestimos; }

    /**
     * Remove um empréstimo específico da lista de pendências do usuário (usado no processo de devolução).
     */
    public Emprestimo removerEmprestimo(Emprestimo e) {
        if (e == null) {
            return null;
        }

        // Percorre a estrutura customizada usando loops e índices
        for (int i = 0; i < listaEmprestimos.tamanho(); i++) {
            Emprestimo emp = listaEmprestimos.selecionar(i);

            // Como o ID é do tipo primitivo 'long', a comparação deve usar o operador '=='
            if (emp != null && emp.getId() == e.getId()) {
                return listaEmprestimos.remover(i);
            }
        }
        return null;
    }

    /**
     * Varre todos os empréstimos ativos do usuário para checar se algum deles ultrapassou o prazo de devolução.
     * @return true se houver pelo menos um empréstimo atrasado; false caso contrário.
     */
    public boolean temEmprestimoAtrasado() {
        for (int i = 0; i < listaEmprestimos.tamanho(); i++) {
            Emprestimo emp = listaEmprestimos.selecionar(i);

            if (emp != null && emp.isAtrasado()) {
                return true; // Bloqueia ações caso encontre uma pendência
            }
        }
        return false;
    }

    /**
     * Valida a igualdade entre usuários com base em seus identificadores únicos (String id).
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }

        Usuario outro = (Usuario) obj;

        if (this.id == null || outro.id == null) {
            return false;
        }

        return this.id.equals(outro.id);
    }

    @Override
    public String toString() {
        return "Usuario{" +
                "id='" + id + '\'' +
                ", nome='" + nome + '\'' +
                ", email='" + email + '\'' +
                ", tipo=" + tipo +
                '}';
    }
}