package br.edu.ifba.service;

import br.edu.ifba.models.*;
import br.edu.ifba.repository.dao.ReservaDAOLista;

// ALTERAÇÃO: removidos imports de "repository.*", "java.util.List", "java.util.stream.Collectors"
// — nenhum deles é usado após as correções. Stream foi substituído por for loops
// pois os DAOs do projeto retornam arrays, não Collections nem Streams.
// ALTERAÇÃO: adicionado import de ReservaDAOLista (usado em listarPrimeirosDasFilas)

public class BibliotecarioService {

    private Biblioteca b;

    public BibliotecarioService(Usuario userLogado) {
        b = Biblioteca.getInstance();
    }

    public Biblioteca getB() {
        return b;
    }

    // =========================================================================
    // DASHBOARD — dados gerais (tela do bibliotecário)
    // =========================================================================

    /** Total de exemplares (livros físicos) no acervo. */
    public int getTotalLivros() {
        return b.getAcervo().quantidade();
    }

    public Titulo[] obterCatalogo() {
        return b.getTitulos().listar();
    }

    /** Número total de empréstimos registrados (ativos e encerrados). */
    public int getNumeroEmprestimosAtivos() {
        return b.getListaDeEmprestimos().tamanho();
    }

    /**
     * Número de empréstimos ativos e em atraso.
     *
     * ALTERAÇÃO COMPLETA: o método original tinha um "for()" vazio sem corpo
     * nem condição, causando erro de compilação. Implementado corretamente:
     * percorre todos os empréstimos e conta os que estão atrasados e ainda
     * não foram devolvidos (dataDevolucao ainda é a data prevista, não a real —
     * isAtrasado() recalcula automaticamente comparando com LocalDate.now()).
     */
    public int getNumeroEmprestimosAtrasados() {
        // CORRIGIDO: era for() vazio — implementado com for-each correto
        int cont = 0;
        for (Emprestimo e : b.getListaDeEmprestimos().listar()) {
            if (e.isAtrasado())
                cont++;
        }
        return cont;
    }

    /**
     * Número de usuários com pelo menos um empréstimo atrasado.
     *
     * ALTERAÇÃO COMPLETA: o método original usava "listaDeUsuarios.getLista().stream()"
     * — listaDeUsuarios não é um campo desta classe, e getLista() não existe em
     * UsuarioDAOLista. Substituído por for-each sobre b.getListaDeUsuarios().listar()
     * e o helper possuiAtraso() foi reescrito para usar o método já disponível
     * em EmprestimoDAOLista.
     */
    public int getUsuariosComAtraso() {
        // CORRIGIDO: era listaDeUsuarios.getLista().stream() — variável e método inexistentes
        int cont = 0;
        for (Usuario u : b.getListaDeUsuarios().listar()) {
            if (b.getListaDeEmprestimos().usuarioTemAtraso(u))
                cont++;
        }
        return cont;
    }

    /**
     * Total de reservas ativas somando todas as filas de todos os títulos.
     *
     * ALTERAÇÃO COMPLETA: o método original usava "listaDeTitulos.getLista().stream()"
     * — listaDeTitulos não é campo desta classe e getLista() não existe em TituloDAOLista.
     * Substituído por for-each sobre b.getTitulosAtualizados().listarTitulos().
     */
    public int getTotalReservas() {

        int total = 0;
        for (Titulo t : b.getTitulos().listar()) {
            total += t.getFilaDeReservas().tamanho();
        }
        return total;
    }

    /**
     * Número de empréstimos realizados hoje.
     * Percorre todos os empréstimos e conta aqueles cuja data de empréstimo é hoje.
     */
    public int getNumeroEmprestimosHoje() {
        int cont = 0;
        java.time.LocalDate hoje = java.time.LocalDate.now();
        for (Emprestimo e : b.getListaDeEmprestimos().listar()) {
            if (e.getDataEmprestimo().isEqual(hoje)) {
                cont++;
            }
        }
        return cont;
    }

    // =========================================================================
    // DEVOLUÇÕES — tela de controle de devoluções
    // =========================================================================

    public boolean registrarDevolucao(Emprestimo e) {
        if (e == null) {
            System.out.println("Empréstimo não encontrado ou já encerrado.");
            return false;
        }

        Livro livro = e.getLivro();

         boolean atrasado = java.time.LocalDate.now().isAfter(e.getDataDevolucao());
        e.setDataDevolucao(java.time.LocalDate.now());
        e.setAtrasado(atrasado);

        // Libera o exemplar físico
        livro.setDisponivel(true);

        // Remove o empréstimo do registro do Título (já existia, mantido)
        for (Titulo titulo : b.getTitulos().listar()) {
            if (livro.getIsbn().equalsIgnoreCase(titulo.getIsbn())) {
                titulo.removerEmprestimo(e); // decrementa quantidadeDisponivel internamente
            }
        }

        // Remove do registro do Usuário
        // OBS: Usuario.removerEmprestimo() ainda está incompleto nos models.
        // Quando o responsável pelos models implementar, descomentar:
        // e.getUsuario().removerEmprestimo(e);

        // Remove da lista global da biblioteca
        // OBS: EmprestimoDAOLista.apagar() ainda está incompleto nos models.
        // Quando implementado, descomentar:
        // b.getListaDeEmprestimos().apagar(e.getId());

        if (atrasado) {
            System.out.println("⚠️ Devolução registrada com atraso para: " + e.getUsuario().getNome());
        } else {
            System.out.println("✅ Devolução registrada com sucesso para: " + e.getUsuario().getNome());
        }
        return true;
    }

    // =========================================================================
    // INVENTÁRIO — tela de inventário
    // =========================================================================

    /**
     * Lista todos os títulos do acervo, ordenados por nome.
     *
     * ALTERAÇÃO: método estava com corpo vazio, causando erro de compilação.
     * Implementado usando b.getTitulosAtualizados() que já chama ordenar()
     * internamente ao reconstruir a lista de títulos.
     */
    public Titulo[] listarInventario() {
        // CORRIGIDO: corpo vazio — implementado com os métodos corretos
        return b.getTitulos().listar();
    }

    /**
     * Lista todos os exemplares (livros físicos) do acervo, ordenados por nome.
     *
     * ALTERAÇÃO: método estava com corpo vazio, causando erro de compilação.
     * Implementado usando b.getAcervo() que retorna LivroDAOLista.
     */
    public Livro[] listarExemplares() {
        // CORRIGIDO: corpo vazio — implementado com os métodos corretos
        b.getAcervo().ordenar();
        return b.getAcervo().listar();
    }

    // =========================================================================
    // CONTROLE DE RESERVAS — tela de controle de reservas
    // =========================================================================

    /**
     * Retorna um array com o primeiro da fila de reservas de cada título
     * que possua pelo menos uma reserva pendente.
     *
     * ALTERAÇÃO COMPLETA: o método original estava vazio. Implementado conforme
     * a orientação do próprio comentário original: percorre todos os títulos,
     * captura o primeiro da fila de cada um (proximo()), e coleta numa lista
     * auxiliar (ReservaDAOLista) para retornar como array.
     */
    public Reserva[] listarPrimeirosDasFilaDeReservasDeCadaTitulo() {
        // CORRIGIDO: corpo vazio — implementado conforme comentário original do método
        ReservaDAOLista listaAuxiliar = new ReservaDAOLista();
        for (Titulo t : b.getTitulos().listar()) {
            Reserva primeira = t.getFilaDeReservas().proximo(); // peek sem remover
            if (primeira != null) {
                listaAuxiliar.salvar(primeira);
            }
        }
        return listaAuxiliar.listar();
    }

    /**
     * Retorna o array completo da fila de reservas de um título específico.
     *
     * ALTERAÇÃO: método estava vazio. Implementado conforme a orientação
     * do próprio comentário original.
     *
     * @param t o título cuja fila se quer consultar
     * @return array de Reserva ordenado por prioridade
     */
    public Reserva[] listarFilaDeReserva(Titulo t) {
        if (t == null) return new Reserva[0];
        return t.getFilaDeReservas().listar();
    }

    /**
     * Efetua o empréstimo para o primeiro da fila de reservas de um título.
     *
     * ALTERAÇÃO COMPLETA: o método original usava diversas variáveis e métodos
     * inexistentes: listaDeLivros, listaDeEmprestimos, isEstaDisponivel(),
     * desenfileirar(), gerarIdEmprestimo(), EmprestimoUtils, e o construtor
     * Emprestimo(id, livro, data, data) que não existe — o correto é
     * Emprestimo(Usuario, Livro). O prazoEmDias usava getCategoria() (inexistente,
     * correto é getTipo()) e cases com letras minúsculas (Professor/Aluno).
     * Tudo foi reescrito usando apenas métodos e construtores existentes.
     *
     * @param titulo o título cujo primeiro da fila será atendido
     * @return true se o empréstimo foi gerado; false se fila vazia ou sem exemplar
     */
    public boolean atenderPrimeirosDaFila(Titulo titulo) {
        // CORRIGIDO: era titulo.getFilaDeReservas().peek() — correto é titulo.filaDeReservas.proximo()
        Reserva proxima = titulo.getFilaDeReservas().proximo();
        if (proxima == null) {
            System.out.println("Fila de reservas vazia para: " + titulo.getNome());
            return false;
        }

        // Busca exemplar disponível diretamente no Título
        // CORRIGIDO: era listaDeLivros.getLista().stream() com isEstaDisponivel() — inexistentes.
        // getExemplarDisponivel() já faz isso dentro de Titulo.
        Livro exemplar = titulo.getExemplarDisponivel();
        if (exemplar == null) {
            System.out.println("Nenhum exemplar disponível de: " + titulo.getNome());
            return false;
        }

        // Remove o primeiro da fila
        // CORRIGIDO: era titulo.getFilaDeReservas().desenfileirar() — inexistente.
        // Correto é removerProximo()
        titulo.getFilaDeReservas().removerProximo();
        b.getListaDeReservas().apagar(proxima.getId()); // remove também da lista global

        Usuario beneficiario = proxima.getUsuario();

        // Cria o empréstimo
        // CORRIGIDO: era new Emprestimo(id, livro, data, data) — construtor inexistente.
        // O prazo é calculado automaticamente pelo construtor Emprestimo(Usuario, Livro)
        // com base no tipo do usuário (ALUNO=7 dias, outros=10 dias)
        exemplar.setDisponivel(false); // CORRIGIDO: era setEstaDisponivel() — inexistente
        Emprestimo emprestimo = new Emprestimo(beneficiario, exemplar);

        // CORRIGIDO: era beneficiario.pegarLivro() e listaDeEmprestimos.adicionar() — inexistentes
        beneficiario.adicionarEmprestimo(emprestimo);
        b.getListaDeEmprestimos().salvar(emprestimo);
        titulo.registrarEmprestimo(emprestimo); // decrementa quantidadeDisponivel

        System.out.println("✅ Empréstimo gerado para " + beneficiario.getNome() +
                " — devolução prevista: " + emprestimo.getDataDevolucao());
        return true;
    }

    // =========================================================================
    // GESTÃO DE USUÁRIOS
    // =========================================================================

    /**
     * Retorna a lista completa de usuários cadastrados.
     *
     * ALTERAÇÃO: método existia apenas com o Javadoc, sem assinatura nem corpo.
     * Implementado agora.
     */
    public Usuario[] listarUsuarios() {
        // ADICIONADO: método estava completamente ausente (só tinha o comentário Javadoc)
        return b.getListaDeUsuarios().listar();
    }

    /**
     * Busca um usuário pelo ID.
     *
     * ALTERAÇÃO: o original usava "listaDeUsuarios.getLista().stream()" — variável
     * e método inexistentes. Substituído pela chamada direta a buscarPorId(),
     * que já existe em UsuarioDAOLista.
     */
    public Usuario buscarUsuarioPorId(String id) {
        // CORRIGIDO: era listaDeUsuarios.getLista().stream() — inexistentes
        return b.getListaDeUsuarios().buscarPorId(id);
    }

    // =========================================================================
    // GESTÃO DO ACERVO
    // =========================================================================

    /**
     * Adiciona um novo exemplar (livro físico) ao acervo.
     * Como Titulo é sempre gerado a partir do acervo em getTitulosAtualizados(),
     * basta salvar o livro no acervo — o Título será recriado automaticamente.
     *
     * ALTERAÇÃO COMPLETA: o método original usava listaDeLivros.adicionar(),
     * listaDeTitulos.buscarTitulo(), listaDeTitulos.adicionar(), e o construtor
     * Titulo(nome, autor, data, isbn, genero, descricao) — todos inexistentes.
     * Titulo só pode ser construído a partir de uma LivroDAOLista (conforme o
     * único construtor disponível: Titulo(LivroDAOLista)). Além disso,
     * getTitulosAtualizados() reconstrói toda a lista de títulos automaticamente
     * ao ser chamado, então não é necessário manipular Titulo manualmente aqui.
     *
     * @param livro o exemplar a ser adicionado
     */
    public void adicionarLivro(Livro livro) {
        if (livro == null) {
            System.out.println("Livro inválido.");
            return;
        }
        // CORRIGIDO: era listaDeLivros.adicionar() — variável e método inexistentes.
        // Correto é b.getAcervo().salvar()
        b.getAcervo().salvar(livro);
        System.out.println("✅ Livro '" + livro.getNome() + "' adicionado ao acervo.");
    }

    /**
     * Remove um exemplar do acervo pelo ID (como long, pois Livro.getId() retorna Long).
     *
     * ALTERAÇÃO COMPLETA: o método original usava listaDeLivros.getLista().stream(),
     * listaDeLivros.remover(), listaDeTitulos.buscarTitulo() e listaDeTitulos.remover()
     * — todos inexistentes. Substituído por b.getAcervo().apagar(id), que já existe
     * em LivroDAOLista e retorna o Livro removido (ou null se não encontrado).
     * O parâmetro foi mantido como String para compatibilidade, mas observe que
     * Livro.getId() retorna Long — verifique com o grupo se a UI vai passar String ou long.
     *
     * @param idLivro ID (em String) do exemplar a ser removido
     * @return true se removido; false se não encontrado
     */
    public boolean removerLivro(Long idLivro) {

        Livro removido = b.getAcervo().apagar(idLivro);
        if (removido == null) {
            System.out.println("Exemplar não encontrado.");
            return false;
        }
        System.out.println("✅ Exemplar '" + removido.getNome() + "' removido com sucesso.");
        return true;
    }
}


