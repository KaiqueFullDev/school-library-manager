package br.edu.ifba.models;

import java.time.LocalDate;

public class Livro {

    private Long id;
    private static long idCount=0;
    private String nome;
    private String autor;
    private String isbn;
    private String genero;
    private String descricao;
    private LocalDate dataPublicacao;
    private boolean disponivel;

    // Construtor da classe Livro
    public Livro( String nome, String autor, String isbn, String genero,
                 String descricao, LocalDate dataPublicacao) {

        this.id = ++idCount;
        this.nome = nome;
        this.autor = autor;
        this.isbn = isbn;
        this.genero = genero;
        this.descricao = descricao;
        this.dataPublicacao = dataPublicacao;

        // Ao criar um livro, ele já inicia como disponível
        this.disponivel = true;
    }

    // Retorna o identificador do livro
    public Long getId() {
        return id;
    }

    // Retorna o nome/título do livro
    public String getNome() {
        return nome;
    }

    // Retorna o autor do livro
    public String getAutor() {
        return autor;
    }

    public String getIsbn() {
        return isbn;
    }

    // Retorna o gênero do livro
    public String getGenero() {
        return genero;
    }

    public String getDescricao() {
        return descricao;
    }

    public LocalDate getDataPublicacao() {
        return dataPublicacao;
    }

    // Verifica se o livro está disponível para empréstimo
    public boolean isDisponivel() {
        return disponivel;
    }

    // Altera o status de disponibilidade do livro
    public void setDisponivel(boolean disponivel) {
        this.disponivel = disponivel;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setAutor(String autor) {
        this.autor = autor;
    }

    public void setDataPublicacao(LocalDate dataPublicacao) {
        this.dataPublicacao = dataPublicacao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public void setGenero(String genero) {
        this.genero = genero;
    }

    // Compara dois livros com base no id
    @Override
    public boolean equals(Object obj) {

        // Verifica se é o mesmo objeto na memória
        if (this == obj) {
            return true;
        }

        // Verifica se o objeto é nulo ou de outra classe
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }

        Livro outro = (Livro) obj;

        // Se o id for nulo, não é possível considerar iguais
        if (this.id == null) {
            return false;
        }

        // Dois livros são iguais se tiverem o mesmo id
        return this.id.equals(outro.id);
    }

    // Representação textual do objeto Livro
    @Override
    public String toString() {
        return "Livro{" +
                "id=" + id +
                ", nome='" + nome + '\'' +
                ", disponivel=" + disponivel +
                '}';
    }
}
