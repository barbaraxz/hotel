package org.barbara.erp;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        Scene scene = new Scene(FXMLLoader.load(getClass().getResource("/views/frmQuartos.fxml")));
        stage.setTitle("Cadastro de quartos");
        stage.setScene(scene);
        stage.show();
    }

    void main() {

    }

    private void lauch() {
    }
}