package br.edu.ifba.repository.dao;

import br.edu.ifba.models.Usuario;
import br.edu.ifba.ed.ListaDinamica;
import br.edu.ifba.ed.Listavel;

public class UsuarioDAOLista {

    private Listavel<Usuario> listaDeUsuarios = new ListaDinamica<>();

    //  Salvar usuário
    public void salvar(Usuario u) {
        if (u == null) {
            throw new IllegalArgumentException("Usuário não pode ser nulo.");
        }
        listaDeUsuarios.anexar(u);
    }

    //  Buscar por ID
    public Usuario buscarPorId(String id) {
        for (int i = 0; i < listaDeUsuarios.tamanho(); i++) {
            Usuario u = listaDeUsuarios.selecionar(i);
            if (u.getId().equals(id)) {
                return u;
            }
        }
        return null;
    }

    //  Listar todos
    public Usuario[] listar() {
        Usuario[] arrayRetorno = new Usuario[listaDeUsuarios.tamanho()];

        for (int i = 0; i < listaDeUsuarios.tamanho(); i++) {
            arrayRetorno[i] = listaDeUsuarios.selecionar(i);
        }

        return arrayRetorno;
    }

    //  Atualizar usuário pelo ID
    public void atualizar(String id, Usuario usuarioAtualizado) {
        for (int i = 0; i < listaDeUsuarios.tamanho(); i++) {
            Usuario u = listaDeUsuarios.selecionar(i);

            if (u.getId().equals(id)) {
                listaDeUsuarios.atualizar(usuarioAtualizado, i);
                return;
            }
        }

        throw new IllegalArgumentException("Usuário com ID " + id + " não encontrado.");
    }

    //  Apagar usuário pelo ID
    public Usuario apagar(String id) {
        for (int i = 0; i < listaDeUsuarios.tamanho(); i++) {
            Usuario u = listaDeUsuarios.selecionar(i);

            if (u.getId().equals(id)) {
                return listaDeUsuarios.apagar(i);
            }
        }
        return null;
    }
}
