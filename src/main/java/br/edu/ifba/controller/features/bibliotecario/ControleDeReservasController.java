package br.edu.ifba.controller.features.bibliotecario;

import br.edu.ifba.util.Sessao;
import br.edu.ifba.util.Tools;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class ControleDeReservasController implements Initializable {
    
    @FXML
    private void handleLogout(MouseEvent event) {
        try {
            Sessao.encerrarSessao();
            System.out.println("Logout realizado. Redirecionando para login...");
            
            Parent root = FXMLLoader.load(getClass().getResource("/views/AuthViews/login.fxml"));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Tools.trocarCenaPreservandoJanela(stage, root);
        } catch (IOException e) {
            System.err.println("Erro ao fazer logout: " + e.getMessage());
            e.printStackTrace();
        }
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
        System.out.println("Já está na página de Controle de Reservas");
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
            Tools.trocarCenaPreservandoJanela(stage, root);
        } catch (IOException e) {
            System.err.println("Erro ao navegar para " + fxmlPath + ": " + e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        System.out.println("Controle de Reservas inicializado");
    }
}
