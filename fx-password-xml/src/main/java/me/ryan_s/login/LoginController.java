package me.ryan_s.login;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;

public class LoginController {
    @FXML
    private Button loginBtn;

    public void initialize() {
        loginBtn.setOnAction(event -> {
            ((Stage) loginBtn.getScene().getWindow()).close();
            Parent root = null;
            try {
                root = FXMLLoader.load(getClass().getClassLoader().getResource("main.fxml"));
            } catch (IOException e) {
                e.printStackTrace();
            }
            Stage primaryStage = new Stage();
            primaryStage.setTitle("Student Library Manager");
            primaryStage.setScene(new Scene(root, 600, 400));
            primaryStage.show();
        });

    }
}
