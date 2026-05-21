package br.edu.ifba.ed;

public interface Listavel<T> {

    void inserir(T objeto, int posicao);
    void anexar(T objeto);
    T selecionar(int posicao);
    T[] selecionarTodos();
    void atualizar(T objeto, int posicao);
    T apagar(int posicao);
    void limpar();

    int tamanho();
    boolean estaVazia();
    boolean estaCheia();
    String imprimir();
}
