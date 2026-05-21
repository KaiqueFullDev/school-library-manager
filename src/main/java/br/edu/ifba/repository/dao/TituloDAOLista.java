package br.edu.ifba.repository.dao;
import br.edu.ifba.models.Titulo;
import br.edu.ifba.ed.ListaDinamica;
import br.edu.ifba.ed.Listavel;


public class TituloDAOLista {

    private Listavel<Titulo> listaTitulos = new ListaDinamica<>();

    public void salvar(Titulo t) {
        if (t == null) {
            throw new IllegalArgumentException("Título não pode ser nulo.");
        }
        listaTitulos.anexar(t);
    }

    public Titulo[] listar() {
        Titulo[] arrayRetorno = new Titulo[listaTitulos.tamanho()];

        for (int i = 0; i < listaTitulos.tamanho(); i++) {
            arrayRetorno[i] = listaTitulos.selecionar(i);
        }
        return arrayRetorno;
    }

    public Titulo buscarPorNome(String nome) {
        for (int i = 0; i < listaTitulos.tamanho(); i++) {
            Titulo t = listaTitulos.selecionar(i);
            if (t.getNome().equalsIgnoreCase(nome)) {
                return t;
            }
        }
        return null;
    }

    public Titulo[] buscarPorGenero(String genero) {
        int contador = 0;

        for (int i = 0; i< listaTitulos.tamanho(); i++) {
            Titulo t = listaTitulos.selecionar(i);
            if (t.getGenero().equalsIgnoreCase(genero)) {
                contador++;
            }
        }
        Titulo[] arrayRetorno = new Titulo[contador];
        int indice = 0;

        for (int i = 0; i < listaTitulos.tamanho(); i++) {
            Titulo t = listaTitulos.selecionar(i);
            if (t.getGenero().equalsIgnoreCase(genero)) {
                arrayRetorno[indice++] = t;
            }
        }
        return arrayRetorno;
    }

    public void atualizar(String isbn, Titulo tituloAtualizado) {
        for (int i = 0; i < listaTitulos.tamanho(); i++) {
            Titulo t = listaTitulos.selecionar(i);
            if (t.getIsbn().equals(isbn)) {
                listaTitulos.atualizar(tituloAtualizado, i);
                return;
            }
        }
        throw new IllegalArgumentException("Título com ISBN " + isbn + " não encontrado.");
    }

    public Titulo apagarPorIsbn(String isbn) {
        for (int i = 0; i < listaTitulos.tamanho(); i++) {
            Titulo t = listaTitulos.selecionar(i);
            if (t.getIsbn().equals(isbn)) {
                return listaTitulos.apagar(i);
            }
        }
        return null;
    }

    public void ordenar() {
        for (int i = 0; i < listaTitulos.tamanho() - 1; i++) {
            for (int j = 0; j < listaTitulos.tamanho() - i - 1; j++) {
                Titulo titulo1 = listaTitulos.selecionar(j);
                Titulo titulo2 = listaTitulos.selecionar(j + 1);

                if (titulo1.getNome().compareToIgnoreCase(titulo2.getNome()) > 0) {

                    listaTitulos.atualizar(titulo2, j);
                    listaTitulos.atualizar(titulo1, j + 1);
                }
            }
        }
    }
}



