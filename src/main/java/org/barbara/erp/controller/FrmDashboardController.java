package org.barbara.erp.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.control.Alert;
import javafx.stage.Stage;
import javafx.scene.Scene;
import java.io.IOException;

public class FrmDashboardController {

    @FXML
    private AnchorPane conteudoPainel;

    private void trocarTela(String fxmlPath) {
        try {
            Parent novaTela = FXMLLoader.load(getClass().getResource(fxmlPath));
            conteudoPainel.getChildren().setAll(novaTela);
        } catch (IOException e) {
            Alert alerta = new Alert(Alert.AlertType.ERROR, "Erro ao carregar a tela: " + fxmlPath + "\n" + e.getMessage());
            alerta.showAndWait();
        }
    }

    @FXML
    public void abrirQuartos() { trocarTela("/views/frmQuartos.fxml"); }
    @FXML
    public void abrirHospedes() { trocarTela("/views/frmHospede.fxml"); }

    @FXML
    public void abrirReservas() {
        trocarTela("/views/frmReservas.fxml");
    }

    @FXML
    public void logout() {
        try {
            // Fecha a tela principal
            Stage stageDashboard = (Stage) conteudoPainel.getScene().getWindow();
            stageDashboard.close();

            // Abre a tela de login novamente
            Parent root = FXMLLoader.load(getClass().getResource("/views/FrmLogin.fxml"));
            Stage stageLogin = new Stage();
            stageLogin.setScene(new Scene(root));
            stageLogin.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}