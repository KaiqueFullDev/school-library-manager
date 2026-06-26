package br.edu.ifba.repository;
import br.edu.ifba.models.Livro;
import br.edu.ifba.enums.TipoUsuario;
import br.edu.ifba.models.Usuario;
import br.edu.ifba.repository.dao.LivroDAOLista;
import br.edu.ifba.repository.dao.UsuarioDAOLista;
import java.time.LocalDate;

public class DataBaseSeed{
    ///-----------------------------------IDs-------------------------------------------------///
    public static final String[] IDS_STUDENTS = {"s000001","s000002","s000003","s000004"};
    public static final String[] IDS_TEACHERS = {"p000001","p000002","p000003","p000004"};
    public static final String[] IDS_LIBRARIANS = {"l000001"};


    public LivroDAOLista listaDeLivros= new LivroDAOLista();
    public UsuarioDAOLista listaDeUsuarios= new UsuarioDAOLista();

    public static void popularDadosIniciais(LivroDAOLista acervo, UsuarioDAOLista usuarios) {
        /// --- POPULAR LIVROS -------------------------------------------------------------------------------------
        // --- 1. LIVROS COM MUITOS EXEMPLARES (Para testar múltiplos empréstimos) ---
        // "Código Limpo" - 10 exemplares
        for (int i = 0; i < 10; i++) {
            acervo.salvar(new Livro("Código Limpo", "Robert C. Martin", "9788576082323",
                    "Programação", "Um guia prático para escrita de código.", LocalDate.of(2008, 8, 1)));
        }

        // "Java: Como Programar" - 8 exemplares
        for (int i = 0; i < 8; i++) {
            acervo.salvar(new Livro("Java: Como Programar", "Paul Deitel", "9780134694726",
                    "Programação", "O guia definitivo para Java.", LocalDate.of(2016, 1, 1)));
        }

        // --- 2. LIVROS COM POUCOS EXEMPLARES (Para testar a Fila de Reserva rápido) ---
        // "Estruturas de Dados" - Apenas 2 exemplares
        acervo.salvar(new Livro("Estruturas de Dados e Algoritmos", "Thomas H. Cormen", "9788575226937",
                "Computação", "Essencial para algoritmos.", LocalDate.of(2010, 5, 10)));
        acervo.salvar(new Livro("Estruturas de Dados e Algoritmos", "Thomas H. Cormen", "9788575226937",
                "Computação", "Essencial para algoritmos.", LocalDate.of(2010, 5, 10)));

        // "Design Patterns" - Apenas 1 exemplar (O primeiro que pegar já bloqueia para os outros)
        acervo.salvar(new Livro("Padrões de Projeto", "Erich Gamma", "9788573076103",
                "Engenharia de Software", "As soluções clássicas do GoF.", LocalDate.of(1994, 10, 21)));

        // --- 3. DIVERSOS OUTROS TÍTULOS (Para volume de dados no catálogo) ---
        String[] nomes = {"Arquitetura Limpa", "O Programador Pragmático", "Refatoração", "Sistemas Operacionais", "Redes de Computadores"};
        String[] autores = {"Robert C. Martin", "Andrew Hunt", "Martin Fowler", "Tanenbaum", "James Kurose"};
        String[] isbns = {"111", "222", "333", "444", "555"};

        for (int i = 0; i < nomes.length; i++) {
            // Criando 6 exemplares de cada um desses para dar volume (30 livros aqui)
            for (int j = 0; j < 6; j++) {
                acervo.salvar(new Livro(nomes[i], autores[i], isbns[i],
                        "Tecnologia", "Descrição genérica do livro " + nomes[i], LocalDate.now()));
            }
        }
        /// --------------------------------- POPULAR USUÁRIOS ------------------------------------------------///
        // Alunos
        usuarios.salvar(new Usuario("s000001", "João Silva", "joao.silva@email.com", "123456", TipoUsuario.ALUNO));
        usuarios.salvar(new Usuario("s000002", "Maria Oliveira", "maria.oliveira@email.com", "abc123", TipoUsuario.ALUNO));
        usuarios.salvar(new Usuario("s000003", "Ana Silva", "ana.silva@email.com", "senha123", TipoUsuario.ALUNO));

        // Professores
        usuarios.salvar(new Usuario("p000001", "Carlos Souza", "carlos.souza@email.com", "prof123", TipoUsuario.PROFESSOR));
        usuarios.salvar(new Usuario("p000002", "Ana Pereira", "ana.pereira@email.com", "senha456", TipoUsuario.PROFESSOR));

        // Bibliotecário (Admin)
        usuarios.salvar(new Usuario("l000001", "Admin BiblioQueue", "admin@biblioteca.com", "admin", TipoUsuario.BIBLIOTECARIO));}
}