package br.edu.ifba.service;

import br.edu.ifba.models.*;
import br.edu.ifba.repository.dao.EmprestimoDAOLista;

// ALTERAÇÃO: removidos imports de "repository.*" e "java.util.List" — não são mais usados.
// Os métodos de busca agora retornam arrays (Titulo[]) usando os DAOs existentes,
// em vez de List<Titulo> via stream(), pois TituloDAOLista não expõe stream.
// ALTERAÇÃO: adicionado import de ReservaDAOLista (necessário em contarReservasAtivas)

public class UsuarioService {

    private Biblioteca b;
    private Usuario user;

    public UsuarioService(Usuario userLogado) {
        this.b = Biblioteca.getInstance();
        this.user = userLogado;
    }

    // =========================================================================
    // EMPRÉSTIMO
    // =========================================================================

    /**
     * Tenta emprestar um exemplar do título solicitado ao usuário logado.
     *
     * Regras verificadas em ordem:
     *  1. Usuário não pode ter devoluções em atraso.
     *  2. Usuário não pode ter atingido o limite de empréstimos.
     *  3. Deve existir pelo menos um exemplar disponível do título.
     *
     * @param titulo o título desejado
     * @return true se o empréstimo foi efetuado; false caso contrário
     */
    public boolean pegarEmprestimo(Titulo titulo) {

        // 1. Verificar atraso
        if (usuarioPossuiAtraso()) {
            System.out.println("⚠️ Acesso Bloqueado: Regularize seus atrasos.");
            return false;
        }

        // 2. Verificar limite do plano do usuário
        if (user.getListaEmprestimos().tamanho() >= user.getLimiteLivros()) {
            System.out.println("⚠️ Limite atingido: Você já possui " + user.getLimiteLivros() + " livros.");
            return false;
        }

        // 3. Verificar disponibilidade pelo contador do título
        if (titulo.getQuantidadeDisponivel() <= 0) {
            System.out.println("❌ Indisponível: Não há exemplares deste título no momento.");
            return false;
        }

        // 4. Buscar o exemplar físico disponível (segurança contra NullPointer)
        Livro livroFisicoEmprestado = titulo.getExemplarDisponivel();
        if (livroFisicoEmprestado == null) {
            System.out.println("❌ Erro interno: Quantidade indica disponível, mas exemplar não encontrado.");
            return false;
        }

        // 5. Execução do Empréstimo
        livroFisicoEmprestado.setDisponivel(false);
        Emprestimo emprestimo = new Emprestimo(user, livroFisicoEmprestado);

        // Registro nos 3 pilares de persistência em memória
        user.adicionarEmprestimo(emprestimo);         // perfil do usuário
        b.getListaDeEmprestimos().salvar(emprestimo);// relatório geral da biblioteca
        b.getAcervo().buscarPorId(livroFisicoEmprestado.getId()).setDisponivel(false);
        titulo.registrarEmprestimo(emprestimo);       // controle de estoque do título

        System.out.println("✅ Sucesso! Devolução prevista: " + emprestimo.getDataDevolucao());
        return true;
    }


    public boolean devolucaoDoEmprestimo(Emprestimo emprestimo) {
        // CORRIGIDO: parâmetro era "Livro" sem nome — trocado para "Emprestimo emprestimo"

        if (emprestimo == null) {
            System.out.println("Empréstimo não encontrado.");
            return false;
        }

        Livro livro = emprestimo.getLivro();

        // Marca a data de devolução real e verifica atraso
        boolean atrasado = java.time.LocalDate.now().isAfter(emprestimo.getDataDevolucao());
        emprestimo.setDataDevolucao(java.time.LocalDate.now()); // registra data real
        emprestimo.setAtrasado(atrasado);


        livro.setDisponivel(true);


        Titulo titulo = b.getTitulos().buscarPorNome(livro.getNome());
        if (titulo != null) {
            titulo.removerEmprestimo(emprestimo); // decrementa quantidadeDisponivel dentro de Titulo
        }

        // Remove o empréstimo do registro global da biblioteca
        // Remove do registro do usuário

        if (atrasado) {
            System.out.println("⚠️ Livro devolvido com atraso. Regularize pendências futuras.");
        } else {
            System.out.println("✅ Livro devolvido com sucesso!");
        }

        // Notifica o próximo da fila de reservas deste título
        notificarProximoDaFila(titulo);
        return true;
    }

    // =========================================================================
    // CATÁLOGO
    // =========================================================================

    /**
     * Retorna todos os títulos do catálogo ordenados por nome.
     */
    public Titulo[] obterCatalogo() {
        return b.getTitulos().listar();
    }

    public Titulo[] buscarTituloPorNome(String busca) {
        if (busca == null || busca.isBlank()) {
            return obterCatalogo();
        }
        Titulo[] todos = b.getTitulos().listar();

        int contador = 0;
        for (Titulo t : todos) {
            if (t.getNome().toLowerCase().contains(busca.toLowerCase()))
                contador++;
        }

        Titulo[] resultado = new Titulo[contador];
        int i = 0;
        for (Titulo t : todos) {
            if (t.getNome().toLowerCase().contains(busca.toLowerCase()))
                resultado[i++] = t;
        }
        return resultado;
    }

    public Titulo[] filtrarPorGenero(String genero) {
        if (genero == null || genero.isBlank()) {
            return obterCatalogo();
        }
        return b.getTitulos().buscarPorGenero(genero);
    }

    // =========================================================================
    // RESERVAS
    // =========================================================================


    public boolean fazerReserva(Titulo titulo) {
        if (usuarioPossuiAtraso()) {
            System.out.println("⚠️ Acesso Bloqueado. Você possui livros em atraso. " +
                    "Regularize suas devoluções para fazer novas reservas.");
            return false;
        }

        if (contarReservasAtivas() >= 3) {
            System.out.println("Você já atingiu o limite de 3 reservas simultâneas.");
            return false;
        }

        // Reserva exige dois parâmetros: Reserva(Usuario, Titulo)
        Reserva reserva = new Reserva(user, titulo);

        titulo.getFilaDeReservas().salvar(reserva);

        // Registra também na lista global de reservas da biblioteca
        b.getListaDeReservas().salvar(reserva);

        System.out.println("✅ Reserva realizada! Você está na posição " +
                titulo.getFilaDeReservas().posicao(reserva) + " da fila.");
        return true;
    }

    /**
     * Cancela a reserva do usuário logado no título informado.
     * Busca a reserva do usuário na fila do título e a remove pelo ID.
     *
     * ALTERAÇÃO: o método original chamava titulo.getFilaDeReservas().remover(user)
     * — método remover(Usuario) inexistente em ReservaDAOFilaDePrioridade.
     * Agora percorre a fila, encontra a reserva do usuário e a remove pelo ID.
     *
     * @param titulo o título cuja reserva será cancelada
     * @return true se cancelada; false se não havia reserva deste usuário
     */
    public boolean desistirDaReserva(Titulo titulo) {

        for (Reserva r : titulo.getFilaDeReservas().listar()) {
            if (r.getUsuario().getId().equals(user.getId())) {
                titulo.getFilaDeReservas().apagar(r.getId());
                b.getListaDeReservas().apagar(r.getId());
                System.out.println("✅ Reserva cancelada com sucesso.");
                return true;
            }
        }
        System.out.println("Nenhuma reserva encontrada para este título.");
        return false;
    }

    // =========================================================================
    // STATUS DO USUÁRIO
    // =========================================================================

    /**
     * Verifica se o usuário logado possui algum empréstimo ativo com devolução atrasada.
     *
     * ALTERAÇÃO: o método original chamava user.getEmprestimos().anyMatch() —
     * getEmprestimos() retorna Emprestimo[] (array), não Stream, portanto
     * anyMatch() não existe nele. Substituído pela chamada a
     * b.getListaDeEmprestimos().usuarioTemAtraso(user), que já faz exatamente
     * essa verificação de forma correta dentro de EmprestimoDAOLista.
     */
    public boolean usuarioPossuiAtraso() {
        return b.getListaDeEmprestimos().usuarioTemAtraso(user);
    }

    /**
     * Verifica se o usuário atingiu o limite de empréstimos simultâneos.
     *
     * ALTERAÇÃO: era user.getListaEmprestimos().size() — EmprestimoDAOLista
     * não possui size(), o método correto é tamanho().
     */
    public boolean atingiuLimiteDeEmprestimos() {

        return user.getListaEmprestimos().tamanho() >= user.getLimiteLivros();
    }

    public boolean esseLivroFoiPegoEmprestado(String isbn) {
        EmprestimoDAOLista listaDeEmprestimos= user.getListaEmprestimos();
        for (Emprestimo e: listaDeEmprestimos.listar()){
            if(e.getLivro().getIsbn().equals(isbn)){
                return true;
            }
        }
        return false;

    }

    public boolean esseLivroFoiFeitoAReserva(String isbn) {
        // Buscamos todas as reservas na biblioteca
        // Ou, se o seu 'user' tiver uma lista própria: user.getListaReservas()
        for (Reserva r : b.getListaDeReservas().listar()) {
            // Verifica se a reserva pertence ao usuário logado E se o ISBN bate
            if (r.getUsuario().getId().equals(user.getId()) &&
                    r.getTitulo().getIsbn().equals(isbn)) {
                return true;
            }
        }
        return false;
    }

    // =========================================================================
    // Helpers privados
    // =========================================================================

    /**
     * Conta quantas reservas ativas o usuário possui no total (em todos os títulos).
     *
     * ALTERAÇÃO COMPLETA: o método original usava listaDeTitulos.getLista().stream()
     * e t.getFilaDeReservas().stream() — ambos inexistentes (getLista() não existe
     * em TituloDAOLista, e ReservaDAOFilaDePrioridade não tem stream()).
     * Substituído por dois for aninhados usando listarTitulos() e listar().
     */
    private int contarReservasAtivas() {
        int cont = 0;
        for (Titulo t : b.getTitulos().listar()) {
            for (Reserva r : t.getFilaDeReservas().listar()) {
                if (r.getUsuario().getId().equals(user.getId()))
                    cont++;
            }
        }
        return cont;
    }

    /**
     * Notifica o próximo da fila de reservas que o título está disponível.
     *
     * ALTERAÇÃO: era titulo.getFilaDeReservas().peek() — método inexistente.
     * O nome correto em ReservaDAOFilaDePrioridade é proximo().
     */
    private void notificarProximoDaFila(Titulo titulo) {
        if (titulo == null) return;

        Reserva proxima = titulo.getFilaDeReservas().proximo();
        if (proxima != null) {
            System.out.println("📢 Notificando " + proxima.getUsuario().getNome() +
                    ": o título '" + titulo.getNome() + "' está disponível para retirada!");
        }
    }
}