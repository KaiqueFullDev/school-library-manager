package br.edu.ifba.repository.dao;

import br.edu.ifba.models.Livro;
import br.edu.ifba.ed.ListaDinamica;
import br.edu.ifba.ed.Listavel;

public class LivroDAOLista {

    private Listavel<Livro> listaLivros = new ListaDinamica<Livro>();

    public void salvar(Livro l) {
        if (l == null) {
            throw new IllegalArgumentException("Livro não pode ser nulo.");
        }
        listaLivros.anexar(l);
    }

    public Livro buscarPorId(Long id) {
        for (int i = 0; i < listaLivros.tamanho(); i++) {
            Livro l = listaLivros.selecionar(i);
            if (l != null && l.getId().equals(id)) {
                return l;
            }
        }
        return null;
    }

    public Livro[] listar() {
        Livro[] arrayRetorno = new Livro[listaLivros.tamanho()];
        for (int i = 0; i < listaLivros.tamanho(); i++) {
            arrayRetorno[i] = listaLivros.selecionar(i);
        }
        return arrayRetorno;
    }

    public void atualizar(Long id, Livro livroAtualizado) {
        for (int i = 0; i < listaLivros.tamanho(); i++) {
            Livro l = listaLivros.selecionar(i);
            if (l != null && l.getId().equals(id)) {
                listaLivros.atualizar(livroAtualizado, i);
                return;
            }
        }
        throw new IllegalArgumentException("Livro com ID " + id + " não encontrado.");
    }

    public Livro apagar(Long id) {
        for (int i = 0; i < listaLivros.tamanho(); i++) {
            Livro l = listaLivros.selecionar(i);
            if (l != null && l.getId().equals(id)) {
                return listaLivros.apagar(i);
            }
        }
        return null;
    }

    public int contarExemplares(String nome) {
        int contador = 0;
        for (int i = 0; i < listaLivros.tamanho(); i++) {
            Livro l = listaLivros.selecionar(i);
            if (l != null && l.getNome().equalsIgnoreCase(nome)) {
                contador++;
            }
        }
        return contador;
    }

    public int contarDisponiveis(String nome) {
        int contador = 0;
        for (int i = 0; i < listaLivros.tamanho(); i++) {
            Livro l = listaLivros.selecionar(i);
            if (l != null && l.getNome().equalsIgnoreCase(nome) && l.isDisponivel()) {
                contador++;
            }
        }
        return contador;
    }

    /// Selecionar livros disponiveis
    public Livro[] selecionarDisponiveis(){
        int contador = 0;
        Livro l;

        for (int i = 0; i < listaLivros.tamanho(); i++) {
            l = listaLivros.selecionar(i);
            if (l != null && l.isDisponivel())
                contador++;
        }

        Livro[] livrosDisponiveis = new Livro[contador];
        int y = 0;

        for(int i = 0; i < listaLivros.tamanho(); i++){
            l = listaLivros.selecionar(i);
            if (l != null && l.isDisponivel()){
                livrosDisponiveis[y++] = l;
            }
        }

        return livrosDisponiveis;
    }

    /// Selecionar livros Indisponiveis
    public Livro[] selecionarIndisponiveis(){
        int contador = 0;
        Livro l;

        for (int i = 0; i < listaLivros.tamanho(); i++) {
            l = listaLivros.selecionar(i);
            if (l != null && !l.isDisponivel())
                contador++;
        }

        Livro[] livrosIndisponiveis = new Livro[contador];
        int y = 0;

        for(int i = 0; i < listaLivros.tamanho(); i++){
            l = listaLivros.selecionar(i);
            if (l != null && !l.isDisponivel()){
                livrosIndisponiveis[y++] = l;
            }
        }

        return livrosIndisponiveis;
    }

    public Livro selecionar(int i){
        return listaLivros.selecionar(i);
    }

    public int quantidade(){
        return listaLivros.tamanho();
    }

    public int tamanho(){
        return quantidade();
    }

    public void ordenar() {
        for (int i = 0; i < listaLivros.tamanho() - 1; i++) {
            for (int j = 0; j < listaLivros.tamanho() - i - 1; j++) {
                Livro livro1 = listaLivros.selecionar(j);
                Livro livro2 = listaLivros.selecionar(j + 1);

                if (livro1 != null && livro2 != null &&
                        livro1.getNome().compareToIgnoreCase(livro2.getNome()) > 0) {

                    listaLivros.atualizar(livro2, j);
                    listaLivros.atualizar(livro1, j + 1);
                }
            }
        }
    }
}
