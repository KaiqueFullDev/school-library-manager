package br.edu.ifba.controller.features.bibliotecario;

import br.edu.ifba.util.Sessao;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;


public class AdicionarLivroController implements Initializable {
    
    @FXML
    private BorderPane rootPane;

    @FXML
    private TextField txtTitulo;

    @FXML
    private TextField txtIsbn;

    @FXML
    private TextField txtAutor;

    @FXML
    private TextField txtAno;

    @FXML
    private TextField txtIdExemplar;

    @FXML
    private void handleLogout(MouseEvent event) {
        try {
            Sessao.encerrarSessao();
            System.out.println("Logout realizado. Redirecionando para login...");
            
            Parent root = FXMLLoader.load(getClass().getResource("/views/AuthViews/login.fxml"));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.centerOnScreen();
            stage.show();
        } catch (IOException e) {
            System.err.println("Erro ao fazer logout: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void handleSalvarLivro(ActionEvent event) {
        System.out.println("Salvando livro...");
        // TODO: Implementar lógica de salvamento de livro
    }

    @FXML
    private void handleVoltar(MouseEvent event) {
        navegarPara("/views/bibliotecarioViews/inventario.fxml", event);
    }

    @FXML
    private void dashboardController(MouseEvent event) {
        navegarPara("/views/bibliotecarioViews/dashboard.fxml", event);
    }

    @FXML
    private void inventarioController(MouseEvent event) {
        navegarPara("/views/bibliotecarioViews/inventario.fxml", event);
    }

    @FXML
    private void controleDeReservasController(MouseEvent event) {
        navegarPara("/views/bibliotecarioViews/controleDeReservas.fxml", event);
    }

    @FXML
    private void controleDeEmprestimosController(MouseEvent event) {
        navegarPara("/views/bibliotecarioViews/controleDeEmprestimos.fxml", event);
    }

    private void navegarPara(String fxmlPath, MouseEvent event) {
        try {
            System.out.println("Navegando para: " + fxmlPath);
            Parent root = FXMLLoader.load(getClass().getResource(fxmlPath));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            
            // Preserva o estado de maximização
            boolean estaMaximizada = stage.isMaximized();
            
            Scene scene = new Scene(root);
            stage.setScene(scene);
            
            // Restaura o estado de maximização
            if (estaMaximizada) {
                stage.setMaximized(false);
                stage.setMaximized(true);
            }
        } catch (IOException e) {
            System.err.println("Erro ao navegar para " + fxmlPath + ": " + e.getMessage());
            e.printStackTrace();
        }
    }



    @Override
    public void initialize(URL location, ResourceBundle resources) {
        System.out.println("Adicionar Livro inicializado");
    }
}
