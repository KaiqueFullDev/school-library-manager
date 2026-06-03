package br.edu.ifba.models;

import java.time.LocalDate;
import java.util.Arrays;

import br.edu.ifba.repository.dao.EmprestimoDAOLista;
import br.edu.ifba.repository.dao.LivroDAOLista;
import br.edu.ifba.repository.dao.ReservaDAOFilaDePrioridade;

public class Titulo {

    private String nome;
    private String autor;
    private String isbn;
    private String genero;
    private String descricao;
    private LocalDate dataPublicacao;
    private int quantidade;
    private int quantidadeDeReservas;
    private int quantidadeDisponivel;

    private LivroDAOLista listaDeExemplares;
    private ReservaDAOFilaDePrioridade filaDeReservas;
    private EmprestimoDAOLista listaDeEmprestimos;

    public Titulo(LivroDAOLista listaDeExemplares, EmprestimoDAOLista listaDeEmprestimos, ReservaDAOFilaDePrioridade filaDeReserva) {

        // Quando a lista estiver vazia
        if(listaDeExemplares == null || listaDeExemplares.tamanho() == 0){
            throw new IllegalArgumentException("Lista de exemplares vazia");
        }

        Livro modelo = listaDeExemplares.selecionar(0);

        this.listaDeExemplares = listaDeExemplares;

        this.nome = modelo.getNome();
        this.isbn = modelo.getIsbn();
        this.genero = modelo.getGenero();
        this.descricao = modelo.getDescricao();
        this.dataPublicacao = modelo.getDataPublicacao();
        this.autor = modelo.getAutor();

        this.quantidade = listaDeExemplares.tamanho();
        this.quantidadeDeReservas = 0;
        this.quantidadeDisponivel = contarQuantidadeDisponivel();

        // Quantidade maxima de Emprestimos precisa ser a quantidade disponivel
        this.listaDeEmprestimos = listaDeEmprestimos;
        this.filaDeReservas = filaDeReserva;
    }

    // --- Getters ---
    public String getNome() {
        return nome;
    }
    public String getAutor() {
        return autor;
    }
    public String getIsbn() {
        return isbn;
    }
    public String getGenero() {
        return genero;
    }
    public String getDescricao() {
        return descricao;
    }
    public LocalDate getDataPublicacao() {
        return dataPublicacao;
    }
    public int getQuantidadeDeExemplares() {
        return quantidade;
    }

    public int getQuantidadeDisponivel() {
        return quantidadeDisponivel;
    }

    private int contarQuantidadeDisponivel(){
        int cont = 0;
        // Percorre os exemplares que este título possui
        for (Livro l : listaDeExemplares.listar()) {
            if (l.isDisponivel()) { // Verifica o booleano do livro físico
                cont++;
            }
        }
        return cont;
    }

    public ReservaDAOFilaDePrioridade getFilaDeReservas() {
        System.out.println(Arrays.toString(filaDeReservas.listar()));
        System.out.println("-----------");
        return filaDeReservas;
    }

    public EmprestimoDAOLista getListaDeEmprestimos() {
        return listaDeEmprestimos;
    }

    public LivroDAOLista getListaDeExemplares() {
        return listaDeExemplares;
    }

    // Retorna um exemplar disponível
    public Livro getExemplarDisponivel(){

        Livro[] lista = listaDeExemplares.listar();

        // Percorre todos os exemplares
        for(int i = 0; i < lista.length; i++){

            if(lista[i] != null && lista[i].isDisponivel()){
                return lista[i];
            }
        }

        return null;
    }

    // Retornar exception caso quantidade disponvel seja null
    public void registrarEmprestimo(Emprestimo novoEmprestimo){

        if(novoEmprestimo == null || quantidadeDisponivel <= 0){
            throw new IllegalStateException("Não há exemplares disponíveis");
        }

        listaDeEmprestimos.salvar(novoEmprestimo);

        quantidadeDisponivel--;
    }

    public Emprestimo removerEmprestimo(Emprestimo e){

        if(e == null){
            return null;
        }

        // Percorre a lista de empréstimos
        for(int i = 0; i < listaDeEmprestimos.tamanho(); i++){

            Emprestimo emprestimo = listaDeEmprestimos.selecionar(i);

            if(emprestimo != null && e.getId() == emprestimo.getId()){

                listaDeEmprestimos.remover(i);

                quantidadeDisponivel++;

                return emprestimo;
            }
        }

        return null;
    }

    public void addLivro(Livro l){
        if(l==null){
            throw new IllegalArgumentException();
        }

        if(!l.getIsbn().equalsIgnoreCase(this.getIsbn())){
            throw new IllegalArgumentException("Este não é um exemplar desse titulo");
        }

        this.listaDeExemplares.salvar(l);
        quantidade=this.listaDeExemplares.tamanho();
        quantidadeDisponivel++;

    }
}
