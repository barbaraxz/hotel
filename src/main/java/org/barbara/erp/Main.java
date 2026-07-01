package org.barbara.erp;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        // Tentativa usando o carregamento direto da raiz do contexto de classes
        java.net.URL fxmlLocation = getClass().getResource("/views/frmLogin.fxml");

        if (fxmlLocation == null) {
            // Fallback caso o IntelliJ esteja procurando a partir do pacote da Main
            fxmlLocation = getClass().getResource("resources/views/frmLogin.fxml");
        }

        if (fxmlLocation == null) {
            // Segunda alternativa de contingência direta
            fxmlLocation = Main.class.getClassLoader().getResource("views/frmLogin.fxml");
        }

        FXMLLoader fxmlLoader = new FXMLLoader(fxmlLocation);
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("Hotel - Autenticação");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}