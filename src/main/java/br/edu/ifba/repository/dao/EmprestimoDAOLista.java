package br.edu.ifba.repository.dao;

import br.edu.ifba.models.Emprestimo;
import br.edu.ifba.models.Usuario;
import br.edu.ifba.ed.ListaDinamica;
import br.edu.ifba.ed.Listavel;

public class EmprestimoDAOLista {
    private Listavel<Emprestimo> listaEmprestimos;

    public EmprestimoDAOLista(int numeroMaximoDeEmprestimos){
        listaEmprestimos=new ListaDinamica<Emprestimo>(numeroMaximoDeEmprestimos);
    }

    public EmprestimoDAOLista(){
        listaEmprestimos=new ListaDinamica<Emprestimo>();
    }

    public void salvar(Emprestimo e) {
        if (e == null) {
            throw new IllegalArgumentException("Empréstimo não pode ser nulo.");
        }
        listaEmprestimos.anexar(e);
    }

    public Emprestimo[] listar() {
        Emprestimo[] arrayRetorno = new Emprestimo[listaEmprestimos.tamanho()];
        for (int i =0; i<listaEmprestimos.tamanho(); i++) {
            arrayRetorno[i] = (Emprestimo) listaEmprestimos.selecionar(i);
        }
        return arrayRetorno;
    }

    public Emprestimo[] buscarPorUsuario(Usuario u) {
        int contador = 0;
        for (int i = 0; i < listaEmprestimos.tamanho(); i++) {
            Emprestimo e = (Emprestimo) listaEmprestimos.selecionar(i);
            if (e.getUsuario().equals(u)) {
                contador++;
            }
        }

        Emprestimo[] arrayRetorno = new Emprestimo[contador];
        int indice = 0;

        for (int i = 0; i < listaEmprestimos.tamanho(); i++) {
            Emprestimo e = (Emprestimo) listaEmprestimos.selecionar(i);
            if (e.getUsuario().equals(u)) {
                arrayRetorno[indice++] = e;
            }
        }
        return arrayRetorno;
    }

    public boolean usuarioTemAtraso(Usuario u) {
        for (int  i = 0; i<listaEmprestimos.tamanho(); i++) {
            Emprestimo e = (Emprestimo) listaEmprestimos.selecionar(i);
            if (e.getUsuario().equals(u) && e.isAtrasado()) {
                return true;
            }
        }
        return false;
    }

    public int contarEmprestimosAtivos(Usuario u) {
        int contador = 0;
        for (int i=0; i<listaEmprestimos.tamanho(); i++) {
            Emprestimo e = (Emprestimo) listaEmprestimos.selecionar(i);
            if (e.getUsuario().equals(u) && !e.getLivro().isDisponivel()) {
                contador++;
            }
        }
        return contador;
    }

    public int tamanho(){
        return listaEmprestimos.tamanho();
    }

    public Emprestimo selecionar(int i){
        return listaEmprestimos.selecionar(i);
    }

    public Emprestimo remover(int i){
        return listaEmprestimos.apagar(i);
    }

    public Emprestimo apagarPorId(long id) {
        for (int i = 0; i < listaEmprestimos.tamanho(); i++) {
            Emprestimo e = listaEmprestimos.selecionar(i);
            if (e.getId()==id) {
                return listaEmprestimos.apagar(i);
            }
        }
        return null;
    }
}
