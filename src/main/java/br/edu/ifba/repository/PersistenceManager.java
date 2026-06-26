package br.edu.ifba.repository;

import br.edu.ifba.models.Emprestimo;
import br.edu.ifba.models.*;
import br.edu.ifba.repository.dao.*;
import java.util.ArrayList;

public class PersistenceManager {

    private static final String PASTA_DADOS_LIVROS = "resources/data/livros.txt";
    private static final String PASTA_DADOS_RESERVAS = "resources/data/reservas.txt";
    private static final String PASTA_DADOS_EMPRESTIMOS = "resources/data/emprestimos.txt";
    private static final String PASTA_DADOS_USUARIOS = "resources/data/usuarios.txt";
    private static final String PASTA_DADOS_IDS = "resources/data/ids.txt";

    public static LivroDAOLista carregarLivros() {
        return null;
    }

    public static UsuarioDAOLista carregarUsuarios() {
        return null;
    }

    public static EmprestimoDAOLista carregarEmprestimos() {
        return null;
    }

    public static ReservaDAOLista carregarReservas() {
        return null;
    }

    public static ArrayList<String> carregarIds(){
        return null;
    }

    public static void salvarLivros(LivroDAOLista livros) {

    }

    public static void salvarUsuario(Usuario u) {

    }

    public static void salvarEmprestimo(Emprestimo e) {

    }

    public static void salvarReserva(Reserva r) {

    }



    public static void sobrescreverEmprestimos(EmprestimoDAOLista listaDeEmprestimos){

    }

    public static void sobrescreverReservas(ReservaDAOLista listaDeReservas){

    }
}
