package me.ryan_s;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.util.Duration;
import org.controlsfx.control.Notifications;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.stream.Collectors;

public class Controller {
    //defining GUI vars
    @FXML TextField nameField;
    @FXML ComboBox<Car> carBox;
    @FXML ComboBox<Integer> dayBox;
    @FXML ToggleGroup hasInsurance;
    @FXML Label costLbl;
    @FXML Label returnLbl;
    //our own vars to store data
    private List<Car> carTypes=new ArrayList<>();//array to store cars
    private List<Integer> days=new ArrayList<>();
    public static final double CAR_PRICE_PER_DAY=50;
    public static final double ADDITIONAL_CAR_PRICE_PER_DAY=30;




    @FXML//run when the gui is started
    public void initialize() {
        try {
            readFiles();
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Failed to read files, application now closing...");
            Platform.runLater(()->{
                Notifications.create().title("Error").text("Failed to read cartypes and days.txt. Are they in the same location as the program?").position(Pos.BOTTOM_LEFT)
                        .hideAfter(Duration.seconds(2)).owner(Main.mainStage()).showError();

            });


        }

    }



    //when clear button is pressed
    public void onClearPress() {
        nameField.setText("");
        carBox.getSelectionModel().select(null);
        dayBox.getSelectionModel().select(null);

    }
    //when confirm button is pressed
    public void onConfirmPress(){
        Entry entry = createEntry();
        if(entry==null)
            return;
        costLbl.setText(NumberFormat.getCurrencyInstance().format(entry.getCost()));
        returnLbl.setText(entry.getReturnDay().get(Calendar.DAY_OF_MONTH)+"/"+entry.getReturnDay().get(Calendar.MONTH)+"/"
                +entry.getReturnDay().get(Calendar.YEAR));
        try {
            writeToFile(entry);
        } catch (IOException e) {
            e.printStackTrace();
            Notifications.create().title("Error").text("Failed to read CSV on saving: " + entry.getCustomerName()).position(Pos.BOTTOM_LEFT)
                    .hideAfter(Duration.seconds(2)).owner(Main.mainStage()).showError();
        }


    }

    private void writeToFile(Entry entry) throws IOException {
        File save=new File(System.getProperty("user.dir"),"/entries.csv");
        List<String> contents=new ArrayList<>();
        System.out.println(save.getPath());
        if(!save.createNewFile())
            contents=Files.readAllLines(save.toPath());
        else contents.add("Customer Name,Car Type,Days,Insurance,Cost");
        contents.add(entry.getCustomerName()+","+entry.getCarType().getName()+","+entry.getDays()+","+entry.isHasInsurance()+","+entry.getCost());

        try(FileWriter writer=new FileWriter(save)){

            for (String s : contents) {
                writer.write(s+"\n");
            }
            writer.flush();

        } catch (IOException e) {

            e.printStackTrace();
            Notifications.create().title("Error").text("Failed to read CSV on saving: " + entry.getCustomerName()).position(Pos.BOTTOM_LEFT)
                    .hideAfter(Duration.seconds(2)).owner(Main.mainStage()).showError();
        }
        Notifications.create().title("Sucess").text("Saved File in CSV as: " + entry.getCustomerName()).position(Pos.BOTTOM_LEFT)
                .hideAfter(Duration.seconds(2)).owner(Main.mainStage()).show();
    }

    private Entry createEntry(){
        if(nameField.getText().isEmpty()||carBox.getSelectionModel().isEmpty()||dayBox.getSelectionModel().isEmpty())
            return null;
        return new Entry(nameField.getText(),carBox.getSelectionModel().getSelectedItem(),dayBox.getSelectionModel().getSelectedItem(),
                ((RadioButton)hasInsurance.getSelectedToggle()).getText().equals("Yes"));
    }
    private void readFiles() throws Exception {
        List<String> carFile = Files.readAllLines(Paths.get(System.getProperty("user.dir"), "cartype.txt"));
        List<String> daysFile = Files.readAllLines(Paths.get(System.getProperty("user.dir"), "days.txt"));
        int indexOfSpace=carFile.indexOf("");
        System.out.println(indexOfSpace);
        List<String> cars=carFile.subList(0,indexOfSpace);
        List<String> prices=carFile.subList(indexOfSpace+1,carFile.size());

        for (int i = 0; i < cars.size(); i++) {
            carTypes.add(Car.parse(cars.get(i),prices.get(i)));
        }
        carBox.setItems(FXCollections.observableArrayList(carTypes));

        days=daysFile.stream().map(Integer::parseInt).collect(Collectors.toList());
        dayBox.setItems(FXCollections.observableArrayList(days));






    }
}
