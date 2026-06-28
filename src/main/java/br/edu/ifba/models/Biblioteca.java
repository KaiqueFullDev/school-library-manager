package br.edu.ifba.models;

import br.edu.ifba.repository.DataBaseSeed;
import br.edu.ifba.repository.dao.*;

/**
 * Classe de gerenciamento global (Singleton) que serve como base de dados em memória.
 */
public class Biblioteca {
    private LivroDAOLista acervo;
    private TituloDAOLista listaDeTitulos;
    private EmprestimoDAOLista listaDeEmprestimos;
    private ReservaDAOLista listaDeReservas;
    private UsuarioDAOLista listaDeUsuarios;

    private static Biblioteca instance; // Instância única global (Padrão Singleton)

    /**
     * Construtor privado para impedir novas instanciações externas diretas via operador 'new'.
     */
    private Biblioteca() {
        // Inicializa as coleções de dados vazias
        this.acervo = new LivroDAOLista();
        this.listaDeUsuarios = new UsuarioDAOLista();
        this.listaDeEmprestimos = new EmprestimoDAOLista();
        this.listaDeReservas = new ReservaDAOLista();

        // Invoca a classe Semente (Seed) para popular o banco de dados inicial na memória
        DataBaseSeed.popularDadosIniciais(this.acervo, this.listaDeUsuarios);

        // Agrupa dinamicamente os exemplares inseridos na semente para gerar o catálogo unificado de Títulos
        this.listaDeTitulos = updateListaDeTitulos(this.acervo);
    }

    /**
     * Recupera de forma segura ou cria a única instância global permitida da Biblioteca.
     */
    public static Biblioteca getInstance() {
        if (instance == null) {
            instance = new Biblioteca();
        }
        return instance;
    }

    // Métodos Getters de acesso aos DAOs globais

    public LivroDAOLista getAcervo() { return acervo; }
    public TituloDAOLista getTitulos() { return listaDeTitulos; }
    public EmprestimoDAOLista getListaDeEmprestimos() { return listaDeEmprestimos; }
    public ReservaDAOLista getListaDeReservas() { return listaDeReservas; }
    public UsuarioDAOLista getListaDeUsuarios() { return listaDeUsuarios; }

    /**
     * Valida se um identificador inserido no cadastro ou login pertence à lista oficial permitida.
     * Checa também os prefixos organizacionais ('p'/'t' para professores, 'l' para bibliotecários).
     */
    public boolean thisIDIsValid(String id) {
        if (id == null || id.isEmpty()) return false;

        char prefixo = id.charAt(0);
        String[] listaParaComparar;

        // Filtra e roteia para o array correto contido na semente de dados estáticos
        if (prefixo == 'p' || prefixo == 't') {
            listaParaComparar = DataBaseSeed.IDS_TEACHERS;
        } else if (prefixo == 'l') {
            listaParaComparar = DataBaseSeed.IDS_LIBRARIANS;
        } else {
            listaParaComparar = DataBaseSeed.IDS_STUDENTS;
        }

        // Varre a lista selecionada para validar o ID do usuário
        for (String s : listaParaComparar) {
            if (s.equals(id)) return true;
        }
        return false;
    }

    /**
     * Processo de mapeamento e agrupamento: Transforma uma lista plana de exemplares físicos (Livros)
     * em uma lista unificada de Títulos agrupados pelo código ISBN correspondente.
     */
    public TituloDAOLista updateListaDeTitulos(LivroDAOLista acervo) {
        TituloDAOLista novaListaDeTitulos = new TituloDAOLista();
        if (acervo == null) return novaListaDeTitulos;

        // Estrutura auxiliar em array para registrar e pular ISBNs que já geraram um agrupamento
        String[] isbnsProcessados = new String[acervo.tamanho()];
        int totalProcessados = 0;

        for (int i = 0; i < acervo.tamanho(); i++) {
            Livro livroAtual = acervo.selecionar(i);
            if (livroAtual == null) continue;

            String isbnAtual = livroAtual.getIsbn();

            // Checa se o ISBN analisado já possui um Título instanciado
            boolean jaProcessado = false;
            for (int j = 0; j < totalProcessados; j++) {
                if (isbnsProcessados[j].equals(isbnAtual)) {
                    jaProcessado = true;
                    break;
                }
            }
            if (jaProcessado) continue; // Pula para o próximo livro do acervo caso já mapeado

            // Registra o novo código ISBN processado
            isbnsProcessados[totalProcessados++] = isbnAtual;

            // Filtra e isola todos os exemplares específicos deste mesmo ISBN dentro do acervo
            LivroDAOLista colecaoExemplares = new LivroDAOLista();
            for (int k = 0; k < acervo.tamanho(); k++) {
                Livro l = acervo.selecionar(k);
                if (l != null && l.getIsbn().equals(isbnAtual)) {
                    colecaoExemplares.salvar(l);
                }
            }

            // Filtra as transações globais da persistência para acoplar apenas o histórico deste ISBN
            EmprestimoDAOLista emprestimosFiltrados = filtrarEmprestimosPorIsbn(isbnAtual);
            ReservaDAOFilaDePrioridade reservasFiltradas = filtrarReservasPorIsbn(isbnAtual);

            // Adiciona o novo objeto Titulo agregado completo na nova coleção catalogada
            novaListaDeTitulos.salvar(new Titulo(colecaoExemplares, emprestimosFiltrados, reservasFiltradas));
        }
        return novaListaDeTitulos;
    }

    // Métodos utilitários de filtros auxiliares lineares

    private EmprestimoDAOLista filtrarEmprestimosPorIsbn(String isbn) {
        EmprestimoDAOLista filtrada = new EmprestimoDAOLista();
        for (Emprestimo e : this.listaDeEmprestimos.listar()) {
            if (e.getLivro() != null && e.getLivro().getIsbn().equals(isbn)) {
                filtrada.salvar(e);
            }
        }
        return filtrada;
    }

    private ReservaDAOFilaDePrioridade filtrarReservasPorIsbn(String isbn) {
        ReservaDAOFilaDePrioridade filtrada = new ReservaDAOFilaDePrioridade();
        for (Reserva r : this.listaDeReservas.listar()) {
            if (r.getTitulo() != null && r.getTitulo().getIsbn().equals(isbn)) {
                filtrada.salvar(r);
            }
        }
        return filtrada;
    }

    public int contarTotalEmprestimos() {
        return listaDeEmprestimos.tamanho();
    }
}