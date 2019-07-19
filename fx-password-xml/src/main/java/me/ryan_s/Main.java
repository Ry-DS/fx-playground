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
import me.ryan_s.data.Member;
import me.ryan_s.data.Table;
import me.ryan_s.data.User;

import java.io.File;
import java.io.IOException;

public class Main extends Application {
    public static File DATA_FILE = new File(System.getProperty("user.dir") + File.separator + "data.xml");//where the data is stored, avaliable to user
    public static ObjectMapper XML_MAPPER = new XmlMapper();//api to handle xml files
    public static Table DATA;//the main data object

    static {
        XML_MAPPER.enable(SerializationFeature.INDENT_OUTPUT);//tells api to pretty print xml
    }

    public static void main(String[] args) {
        try {
            loadFile();//try to load the file
        } catch (IOException e) {
            e.printStackTrace();//if we failed, no point trying to open app. For a real app, we could open an exception window for reporting online
            return;
        }
        launch(args);

    }

    public static void loadFile() throws IOException {//try loading the file
        if (!DATA_FILE.createNewFile()) {//file exists
            try {//try to read it and create a data object
                DATA = XML_MAPPER.readValue(DATA_FILE, Table.class);
                System.out.println(DATA);
                System.out.println("Loaded XML");//for debugging
                return;

            } catch (JsonProcessingException e) {//failed? continue below and...
                e.printStackTrace();
                System.out.println("Creating new one instead...");
            }

        }
        //...create new one instead
        DATA = new Table(new User("admin", "admin"));
        saveXml();
    }

    public static void saveXml() throws IOException {//save file
        XML_MAPPER.writeValue(DATA_FILE, DATA);//using api
    }

    public static void show(Stage primaryStage, String fileName, String title, int width, int height) {//special method allowing you to switch windows
        try {
            Parent root = FXMLLoader.load(Main.class.getClassLoader().getResource(fileName + ".fxml"));//find the valid gui plan
            primaryStage.setTitle(title);//set the title

            Scene scene;
            primaryStage.setScene(scene = new Scene(root, width, height));
            scene.getStylesheets().add("org/kordamp/bootstrapfx/bootstrapfx.css");//apply fancy styles to give special buttons and combo boxes
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static int nextMemberId() {//finds a unique member id for any new member
        int highest = 0;
        for (Member member : DATA.getMembers()) {//goes through every member to find the one with the highest id, then goes one up to guarantee an easy and unique code for every member
            if (member.getId() > highest)
                highest = member.getId();
        }
        return ++highest;
    }



    @Override
    public void start(Stage primaryStage) {
        show(primaryStage, "login", "Login", 300, 275);

    }
}
