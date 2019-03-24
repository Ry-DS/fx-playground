package me.ryan_s;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;

import java.util.Optional;

//boilerplate code for a JavaFX application.
//you can see where the action happens in the Controller Class
public class Main extends Application {

    private static Stage MAIN_STAGE;

    @Override
    public void start(Stage primaryStage) throws Exception{
        MAIN_STAGE = primaryStage;
        Parent root = FXMLLoader.load(getClass().getResource("main.fxml"));
        primaryStage.setTitle("Athletics Carnival Event Tracker");
        primaryStage.setScene(new Scene(root, 640, 300));
        primaryStage.setOnCloseRequest(event -> {
            //make a confirmation dialog to prevent closing
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Are you sure?");
            alert.setHeaderText("Do you wish to quit?");
            alert.setContentText("You may have unsaved work");
            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == ButtonType.CANCEL) {//if they pressed cancel we stop the event
                event.consume();
            }


        });
        primaryStage.show();
    }

    public static Stage getMainStage() {
        return MAIN_STAGE;
    }

    public static void main(String[] args) {
        launch(args);
    }
}
