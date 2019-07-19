package me.ryan_s;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import me.ryan_s.data.User;
import org.controlsfx.control.Notifications;

public class LoginController {
    //represents the login window, we define the gui elements here
    @FXML
    private Button loginBtn;
    @FXML
    private TextField userField;
    @FXML
    private TextField passwordField;

    public void initialize() {//on start
        Platform.runLater(() -> {
            Notifications.create().text("Welcome, default login is: Username: admin  Password: admin").showInformation();//Only because this is not a real program, let assessor know how to login
        });


    }

    public void onLogin() {//on login button pressed
        User user = new User(userField.getText(), passwordField.getText());//create a new user to compare
        if (!Main.DATA.getUsers().contains(user)) {//try see if this user exists in the database
            Platform.runLater(() -> {//if not, let user know it doesnt. Also conveniently handles empty fields
                Notifications.create().text("Incorrect Username or Password").showError();
            });


        } else {//otherwise, if they do exist


            Main.show(new Stage(), "main", "Sports Club Member Manager", 850, 400);//show the main window
            ((Stage) loginBtn.getScene().getWindow()).close();//close the login window
        }
    }
}
