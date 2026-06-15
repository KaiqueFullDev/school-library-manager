package br.edu.ifba.service;

import br.edu.ifba.models.Biblioteca;
import br.edu.ifba.models.Usuario;
import br.edu.ifba.enums.TipoUsuario;

// ALTERAÇÃO: removido import de UsuarioDAOLista — não era usado diretamente aqui
// (o acesso é sempre via b.getListaDeUsuarios())

public class AuthService {
    private static Biblioteca b=Biblioteca.getInstance();

    /**
     * Realiza o login do usuário.
     * Verifica se o email e a senha fornecidos correspondem a algum usuário
     * já cadastrado em Biblioteca.listaDeUsuarios.
     *
     * @return o Usuario autenticado, ou null se as credenciais forem inválidas
     */

    public static Usuario login(String email, String senha) {
        for (Usuario u : b.getListaDeUsuarios().listar()) {
            System.out.println(u);
            if (u.getEmail().equalsIgnoreCase(email) && u.getSenha().equals(senha)) {
                System.out.println("Login realizado com sucesso! Bem-vindo(a), " + u.getNome() + ".");
                return u;
            }
        }
        System.out.println("Email ou senha inválidos.");
        return null;
    }

    /**
     * Realiza o cadastro de um novo usuário.
     * Verifica se o ID fornecido é válido consultando Biblioteca.thisIDIsValid().
     * Verifica também se o email já não está em uso.
     *
     * @return o novo Usuario cadastrado, ou null se o cadastro falhar
     */
    public static Usuario cadastro(String nome,String email, String senha, String id) {

        // Valida o ID junto ao banco de IDs da biblioteca
        if (!b.thisIDIsValid(id)) {
            System.out.println("ID inválido ou não autorizado.");
            return null;
        }



        // Verifica se o email já está cadastrado e se há algum usuario com o msm id
        for (Usuario u : b.getListaDeUsuarios().listar()) {
            if (u.getEmail().equalsIgnoreCase(email)) {
                System.out.println("Este email já está em uso.");
                return null;
            }
            if (u.getId().equalsIgnoreCase(id)) {
                System.out.println("Ja há no sistema usuario com este id.");
                return null;
            }

        }

        // Determina a categoria com base no prefixo do ID
        TipoUsuario categoria = resolverCategoria(id);

        Usuario novoUsuario = new Usuario(id, nome, email, senha, categoria);
        b.getListaDeUsuarios().salvar(novoUsuario);
        System.out.println(b.getListaDeUsuarios());
        System.out.println("Cadastro realizado com sucesso! Bem-vindo(a), " + nome + ".");
        return novoUsuario;
    }

    /**
     * Infere a categoria do usuário a partir do prefixo do ID.
     * e thisIDIsValid() também checa charAt(0) == 'p' para professores.
     * O código original usava 't', o que fazia professores serem cadastrados como ALUNO.
     */

    private static TipoUsuario resolverCategoria(String id) {
        if (id.charAt(0)=='t') {
            return TipoUsuario.PROFESSOR;
        } else if (id.charAt(0) == 'l') {
            return TipoUsuario.BIBLIOTECARIO;
        } else {
            return TipoUsuario.ALUNO;       // cobre 's' e qualquer outro prefixo válido
        }
    }
}