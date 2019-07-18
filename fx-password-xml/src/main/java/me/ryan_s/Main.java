package me.ryan_s;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import me.ryan_s.data.Table;
import me.ryan_s.data.User;

import java.io.File;
import java.io.IOException;

public class Main extends Application {
    public static File DATA_FILE = new File(System.getProperty("user.dir") + File.separator + "data.xml");
    public static ObjectMapper XML_MAPPER = new XmlMapper();
    public static Table DATA;

    static {
        XML_MAPPER.enable(SerializationFeature.INDENT_OUTPUT);
    }

    public static void main(String[] args) {
        try {
            loadFile();
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
        launch(args);

    }

    public static void loadFile() throws IOException {
        if (!DATA_FILE.createNewFile()) {//file exists
            try {
                DATA = XML_MAPPER.readValue(DATA_FILE, Table.class);
                System.out.println(DATA);
                System.out.println("Loaded XML");
                return;

            } catch (JsonProcessingException e) {
                e.printStackTrace();
                System.out.println("Creating new one instead...");
            }

        }
        //otherwise, create new one.
        DATA = new Table(new User("admin", "admin"));
        saveXml();
    }

    public static void saveXml() throws IOException {
        XML_MAPPER.writeValue(DATA_FILE, DATA);
    }

    public static void show(Stage primaryStage, String fileName, String title, int width, int height) {
        try {
            Parent root = FXMLLoader.load(Main.class.getClassLoader().getResource(fileName + ".fxml"));
            primaryStage.setTitle(title);

            Scene scene;
            primaryStage.setScene(scene = new Scene(root, width, height));
            scene.getStylesheets().add("org/kordamp/bootstrapfx/bootstrapfx.css");

            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static int nextStudentId() {
        return 0;
    }



    @Override
    public void start(Stage primaryStage) throws IOException {
        show(primaryStage, "login", "Login", 300, 275);

    }
}
