package me.ryan_s;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import me.ryan_s.data.User;
import org.controlsfx.control.Notifications;

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


    }

    public void onLogin() {
        User user = new User(userField.getText(), passwordField.getText());
        if (!Main.DATA.getUsers().contains(user)) {
            Platform.runLater(() -> {
                Notifications.create().text("Incorrect Username or Password").showError();
            });


        } else {


            Main.show(new Stage(), "main", "Student Library Manager", 850, 400);
            ((Stage) loginBtn.getScene().getWindow()).close();
        }
    }
}
