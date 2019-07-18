package me.ryan_s;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.controlsfx.control.Notifications;

import java.io.IOException;

public class LoginController {
    @FXML
    private Button loginBtn;
    @FXML
    private TextField userField;
    @FXML
    private TextField passwordField;
    public void initialize() {
        Platform.runLater(() -> {
            Notifications.create().text("Welcome").showInformation();
        });

        loginBtn.setOnAction(event -> {
            ((Stage) loginBtn.getScene().getWindow()).close();
            try {
                Main.show(new Stage(), "main", "Student Library Manager", 850, 400);
            } catch (IOException e) {

                e.printStackTrace();
            }
        });

    }
}
