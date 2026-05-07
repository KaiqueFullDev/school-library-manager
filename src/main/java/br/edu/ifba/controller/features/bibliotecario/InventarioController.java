package br.edu.ifba.controller.features.bibliotecario;

import br.edu.ifba.models.Titulo;
import br.edu.ifba.service.BibliotecarioService;
import br.edu.ifba.util.Sessao;
import br.edu.ifba.util.Tools;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class InventarioController implements Initializable {

    @FXML private Label NomeUsuario;
    @FXML private FlowPane containerLivros;

    private BibliotecarioService service;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        if (Sessao.getUsuarioLogado() != null) {
            this.NomeUsuario.setText(Sessao.getUsuarioLogado().getNome());
            this.service = new BibliotecarioService(Sessao.getUsuarioLogado());
            renderizarInventario();
        }
    }

    private void renderizarInventario() {
        containerLivros.getChildren().clear(); // Limpa cards estáticos

        for (Titulo titulo : service.getB().getTitulos().listar()) {
            VBox card = criarCard(titulo);
            containerLivros.getChildren().add(card);
        }
    }

    /**
     * Cria o elemento visual do Card via código
     */
    private VBox criarCard(Titulo titulo) {
        VBox card = new VBox(15);
        card.setPrefWidth(500);
        card.setPadding(new Insets(25));
        card.setStyle("-fx-background-color: white; -fx-background-radius: 20; " +
                "-fx-border-color: #E0E0E0; -fx-border-radius: 20; -fx-border-width: 1; " +
                "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.05), 10, 0, 0, 5);");

        // Topo: Ícone e Título/Autor
        HBox header = new HBox(15);
        VBox iconBox = new VBox(new Label("📖"));
        iconBox.setAlignment(Pos.CENTER);
        iconBox.setMinSize(60, 60);
        iconBox.setStyle("-fx-background-color: #F3E5F5; -fx-background-radius: 15;");
        ((Label)iconBox.getChildren().get(0)).setStyle("-fx-font-size: 24px;");

        VBox infoBox = new VBox(2);
        Label lblNome = new Label(titulo.getNome());
        lblNome.setStyle("-fx-font-weight: bold; -fx-font-size: 18px;");
        Label lblAutor = new Label(titulo.getAutor());
        lblAutor.setStyle("-fx-text-fill: #888888;");
        infoBox.getChildren().addAll(lblNome, lblAutor);
        header.getChildren().addAll(iconBox, infoBox);

        // ID e Categoria
        HBox tags = new HBox(10);
        Label tagCat = new Label(titulo.getGenero());
        tagCat.setStyle("-fx-background-color: #F8F9FB; -fx-padding: 4 8; -fx-background-radius: 5; -fx-font-size: 12px;");
        tags.getChildren().addAll(tagCat);

        // Estoque
        HBox estoqueBox = new HBox();
        Label txtEstoque = new Label("Disponíveis:");
        HBox spacer = new HBox();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        Label lblQtd = new Label(titulo.getQuantidadeDisponivel() + " exemplares");
        lblQtd.setStyle(titulo.getQuantidadeDeExemplares() > 0 ? "-fx-text-fill: #27AE60; -fx-font-weight: bold;" : "-fx-text-fill: #E74C3C; -fx-font-weight: bold;");
        estoqueBox.getChildren().addAll(txtEstoque, spacer, lblQtd);

        card.getChildren().addAll(header, tags, estoqueBox);
        return card;
    }

    // --- Métodos de Navegação (Cópia e Cola) ---
    @FXML private void handleLogout(MouseEvent event) {
        Sessao.encerrarSessao();
        navegarPara("/views/AuthViews/login.fxml", event);
    }
    @FXML private void handleAdicionarLivro(MouseEvent event) { navegarPara("/views/bibliotecarioViews/adicionarLivro.fxml", event); }
    @FXML private void dashboardController(MouseEvent event) { navegarPara("/views/bibliotecarioViews/dashboard.fxml", event); }
    @FXML private void inventarioController(MouseEvent event) { renderizarInventario(); }
    @FXML private void controleDeReservasController(MouseEvent event) { navegarPara("/views/bibliotecarioViews/controleDeReservas.fxml", event); }
    @FXML private void controleDeEmprestimosController(MouseEvent event) { navegarPara("/views/bibliotecarioViews/controleDeEmprestimos.fxml", event); }

    private void navegarPara(String fxmlPath, MouseEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource(fxmlPath));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Tools.trocarCenaPreservandoJanela(stage, root);
        } catch (IOException e) { e.printStackTrace(); }
    }
}
