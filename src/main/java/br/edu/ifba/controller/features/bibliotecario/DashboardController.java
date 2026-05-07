package br.edu.ifba.controller.features.bibliotecario;

import br.edu.ifba.models.Reserva;
import br.edu.ifba.models.Usuario;
import br.edu.ifba.service.BibliotecarioService;
import br.edu.ifba.util.Sessao;
import br.edu.ifba.util.Tools;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class DashboardController implements Initializable {

    @FXML private Label userLabel, lblQtdAcervo, lblQtdEmprestimos, lblQtdHoje,
            lblQtdAtrasos, lblQtdReservas, lblQtdUsuariosAtraso, lblFilaContagem;

    @FXML private ListView<Reserva> lvFilaReserva;

    // CORREÇÃO: O tipo deve ser BorderPane para coincidir com o FXML
    @FXML private BorderPane mainContainer;

    private BibliotecarioService service;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Usuario logado = Sessao.getUsuarioLogado();

        if (logado != null) {
            userLabel.setText(logado.getNome());
            this.service = new BibliotecarioService(logado);
            carregarDadosDashboard();
            configurarListaDeReservas();
        }
    }

    private void carregarDadosDashboard() {
        try {
            lblQtdAcervo.setText(String.valueOf(service.getTotalLivros()));
            lblQtdEmprestimos.setText(String.valueOf(service.getNumeroEmprestimosAtivos()));
            lblQtdAtrasos.setText(String.valueOf(service.getNumeroEmprestimosAtrasados()));
            lblQtdReservas.setText(String.valueOf(service.getTotalReservas()));
            lblQtdUsuariosAtraso.setText(String.valueOf(service.getUsuariosComAtraso()));
            lblQtdHoje.setText(String.valueOf(service.getNumeroEmprestimosHoje()));
            lblFilaContagem.setText("(" + service.getTotalReservas() + ")");
        } catch (Exception e) {
            System.err.println("Erro ao carregar dados do Service: " + e.getMessage());
        }
    }

    private void configurarListaDeReservas() {
        // Busca as reservas reais do service
        ObservableList<Reserva> reservas = FXCollections.observableArrayList(service.listarPrimeirosDasFilaDeReservasDeCadaTitulo());
        lvFilaReserva.setItems(reservas);

        lvFilaReserva.setCellFactory(param -> new ListCell<>() {
            @Override
            protected void updateItem(Reserva reserva, boolean empty) {
                super.updateItem(reserva, empty);
                if (empty || reserva == null) {
                    setText(null);
                    setGraphic(null);
                    setStyle("-fx-background-color: transparent;");
                } else {
                    setText(reserva.getUsuario().getNome() + " — " + reserva.getTitulo().getNome());
                    setStyle("-fx-background-color: #F8F9FB; -fx-padding: 8; -fx-background-radius: 5; -fx-margin: 2;");
                }
            }
        });
    }

    @FXML
    private void handleLogout() {
        Sessao.encerrarSessao();
        navegarPara("/views/AuthViews/login.fxml");
    }

    @FXML private void onNavDashboard() { /* Página atual */ }
    @FXML private void onNavInventario() { navegarPara("/views/bibliotecarioViews/inventario.fxml"); }
    @FXML private void onNavReservas() { navegarPara("/views/bibliotecarioViews/controleDeReservas.fxml"); }
    @FXML private void onNavEmprestimos() { navegarPara("/views/bibliotecarioViews/controleDeEmprestimos.fxml"); }

    private void navegarPara(String fxmlPath) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource(fxmlPath));
            // Pega o Stage através do mainContainer (BorderPane)
            Stage stage = (Stage) mainContainer.getScene().getWindow();
            Tools.trocarCenaPreservandoJanela(stage, root);
        } catch (IOException e) {
            System.err.println("Erro ao navegar: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
