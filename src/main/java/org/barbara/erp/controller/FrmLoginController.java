package org.barbara.erp.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.barbara.erp.dao.UsuarioDAO;
import org.barbara.erp.dao.UsuarioDAOImpl;
import org.barbara.erp.model.Usuario;

import java.io.IOException;

public class FrmLoginController {

    //  ATRIBUTOS
    @FXML
    private TextField campoUsuario;
    @FXML
    private PasswordField campoSenha;

    // Instância da DAO que criámos para buscar no banco
    private final UsuarioDAO usuarioDAO = new UsuarioDAOImpl();

    // MÉTODO DE INICIALIZAÇÃO
    @FXML
    public void initialize() {
    }

    @FXML
    private void autenticar() {
        String txtUser = campoUsuario.getText();
        String txtPass = campoSenha.getText();

        // Faz a consulta no banco de dados
        Usuario usuarioLogado = usuarioDAO.autenticar(txtUser, txtPass);

        if (usuarioLogado != null) {
            exibirAlerta(Alert.AlertType.INFORMATION, "Sucesso", "Bem-vindo ao sistema!");
            abrirTela();
        } else {
            exibirAlerta(Alert.AlertType.ERROR, "Erro de Autenticação", "Usuário ou senha inválidos.");
        }
    }

    //
    private void abrirTela() {
        try {
            // Carrega o FXML da tela de reservas que já está pronta
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/FrmDashboard.fxml"));            Parent root = loader.load();

            Stage stage = (Stage) campoUsuario.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Gerenciamento de Reservas - Hotel");
            stage.centerOnScreen();
            stage.show();

        } catch (IOException e) {
            exibirAlerta(Alert.AlertType.ERROR, "Erro", "Não foi possível abrir a tela de reservas: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Método para exibir caixas de mensagem na tela
    private void exibirAlerta(Alert.AlertType tipo, String titulo, String mensagem) {
        Alert alerta = new Alert(tipo);
        alerta.setTitle(titulo);
        alerta.setHeaderText(null);
        alerta.setContentText(mensagem);
        alerta.showAndWait();
    }
}