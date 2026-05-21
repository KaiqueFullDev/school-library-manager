package br.edu.ifba.repository.dao;

import br.edu.ifba.models.Reserva;
import br.edu.ifba.models.Titulo;
import br.edu.ifba.models.Usuario;
import br.edu.ifba.ed.ListaDinamica;
import br.edu.ifba.ed.Listavel;

public class ReservaDAOLista {

    private Listavel<Reserva> listaReservas = new ListaDinamica<>();

    // Salvar reserva
    public void salvar(Reserva r) {
        if (r == null) {
            throw new IllegalArgumentException("Reserva não pode ser nula.");
        }
        listaReservas.anexar(r);
    }

    // Listar todas reservas
    public Reserva[] listar() {

        Reserva[] array = new Reserva[listaReservas.tamanho()];

        for (int i = 0; i < listaReservas.tamanho(); i++) {
            array[i] = listaReservas.selecionar(i);
        }

        return array;
    }

    // Buscar por título
    public Reserva[] buscarPorTitulo(Titulo t) {

        int contador = 0;

        for (int i = 0; i < listaReservas.tamanho(); i++) {
            Reserva r = listaReservas.selecionar(i);

            if (r != null && r.getTitulo() != null && r.getTitulo().equals(t)) {
                contador++;
            }
        }

        Reserva[] resultado = new Reserva[contador];
        int j = 0;

        for (int i = 0; i < listaReservas.tamanho(); i++) {
            Reserva r = listaReservas.selecionar(i);

            if (r != null && r.getTitulo() != null && r.getTitulo().equals(t)) {
                resultado[j++] = r;
            }
        }

        return resultado;
    }

    // Buscar por usuário
    public Reserva[] buscarPorUsuario(Usuario u) {

        int contador = 0;

        for (int i = 0; i < listaReservas.tamanho(); i++) {
            Reserva r = listaReservas.selecionar(i);

            if (r != null && r.getUsuario() != null && r.getUsuario().equals(u)) {
                contador++;
            }
        }

        Reserva[] resultado = new Reserva[contador];
        int j = 0;

        for (int i = 0; i < listaReservas.tamanho(); i++) {
            Reserva r = listaReservas.selecionar(i);

            if (r != null && r.getUsuario() != null && r.getUsuario().equals(u)) {
                resultado[j++] = r;
            }
        }

        return resultado;
    }

    // Atualizar reserva
    public void atualizar(long id, Reserva nova) {

        for (int i = 0; i < listaReservas.tamanho(); i++) {

            Reserva r = listaReservas.selecionar(i);

            if (r != null && r.getId() == id) {
                listaReservas.atualizar(nova, i);
                return;
            }
        }

        throw new IllegalArgumentException("Reserva com ID " + id + " não encontrada.");
    }

    // Apagar reserva
    public Reserva apagar(long id) {

        for (int i = 0; i < listaReservas.tamanho(); i++) {

            Reserva r = listaReservas.selecionar(i);

            if (r != null && r.getId() == id) {
                return listaReservas.apagar(i);
            }
        }

        return null;
    }
    public int tamanho() {
        return listaReservas.tamanho();
    }
}
