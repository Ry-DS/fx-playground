package me.ryan_s;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.GridPane;
import javafx.util.Duration;
import org.controlsfx.control.Notifications;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

public class Controller {
    //The @FXML tag allows JavaFX to automatically inject the elements into the variables we specify below
    //everything here represents an actual ui element in the gui
    @FXML private TextField nameField;
    @FXML private ToggleGroup gender;
    @FXML private RadioButton maleButton;
    @FXML private RadioButton femaleButton;

    @FXML private ComboBox<Event> eventBox;
    @FXML
    private ChoiceBox<String> ageBox;
    @FXML
    private ChoiceBox<String> houseBox;
    @FXML private Label unitLabel;
    @FXML private Button confirmButton;
    @FXML
    private Label confirmContainer;
    @FXML private Button saveButton;
    @FXML
    private Label saveContainer;
    @FXML private TextArea resultArea;
    @FXML
    private GridPane resultPane;
    //this is where we begin the variables we will use for this application. They don't point to a gui element and instead store the data needed
    //The ages list stores the reading of the ages.txt
    private List<String> agesFile;
    //The events list stores the reading of the events.txt
    private List<String> eventsFile;
    //Once we read the above list, we parse everything into the below list with some code later shown so we can easily use it
    private List<Event> events=new ArrayList<>();
    //This is used to parse a time specified into a string to show the user and to save into files
    private SimpleDateFormat format=new SimpleDateFormat();



    @FXML
    public void initialize() {//This is run by JavaFX when the application starts
        CompletableFuture.runAsync(() -> {//We use a completable future which runs tasks async (separate from the main ui thread) so nothing freezes as the gui loads
            try {
                readFiles();//we try to load the files
            } catch (IOException e) {//failed?
                e.printStackTrace();
                System.err.println("Failed to read files! Application will now exit...");
                System.exit(-1);//just exit and print to console the error.
                return;
            }
            loadData();//we made it here without failing? We load the files.
        });
        //input houses
        houseBox.setItems(FXCollections.observableArrayList(Arrays.asList("Blackwood", "Cottrell", "Kororoit", "Rothwell")));
        //add tooltips to show if buttons are disabled, let users know to fill fields
        Tooltip buttonTip = new Tooltip("Fill out all fields in order to use this button");
        saveContainer.setTooltip(buttonTip);
        confirmContainer.setTooltip(buttonTip);


        System.out.println("Finished initialise");//not visible to user


    }


    public void onEventModify() {
        //This is run by JavaFX when the eventBox is modified
        Event selected = eventBox.getSelectionModel().getSelectedItem();//we get the selected event
        unitLabel.setText(selected.getUnit().getUnitName());//set the unit text to what the event uses
        if (resultPane.getChildren().size() > 1)
            resultPane.getChildren().remove(1);//clear the result pane from previous uses of the eventBox
        resultPane.getChildren().add(selected.getUnit().getInput().get());//put in the a new node which the event supports
    }
    public void onConfirmClicked() {//run when confirm button clicked
        Entry entry = createEntry();//we create an entry, which captures all current data in gui
        resultArea.setText(format.format(new Date())//we format a new date, which is current time
                //we just fill everything else according to the entry
                + "\nName: " + entry.getName()
                + "\nGender: " + entry.getGender()
                + "\nYear Level: " + entry.getYearLevel()
                + "\nHouse: " + entry.getHouse()
                + "\nEvent: " + entry.getEvent().getName()
                + "\nResult: " + entry.getResult() + " " + entry.getEvent().getUnit().getUnitName()

        );
    }
    public void onGUIAction() {//when the gui is interacted with, used to disable and enable the buttons
        if (createEntry() != null) {//if this is not null, all fields are filled

            confirmButton.setDisable(false);
            saveButton.setDisable(false);

        } else {//otherwise we disable them since clicking them would throw an error
            confirmButton.setDisable(true);
            saveButton.setDisable(true);
        }
    }
    public void onSaveClicked() {//on save button click
        confirmButton.fire();//virtually press the confirm button too since we want to fill up the text area
        Entry entry = createEntry();//we grab a snapshot
        String fileName = entry.getName() + "-" + entry.getEvent().getName() + ".txt";//define a file name to be inputted name and event name


        CompletableFuture<Boolean> task = CompletableFuture.supplyAsync(() -> {//async task to stop blocking ui thread
            String dir = System.getProperty("user.dir") + File.separator + "saves";//this grabs the current folder the program is running in + a new saves folder
            new File(dir).mkdir();//make the saves folder if it doesn't exist
            File save = new File(dir, fileName);//we reference the file we are about to make
            File csv = new File(dir, "total.csv");//we reference the excel sheet if any
            Set<String> csvContents = new LinkedHashSet<>();//to store the excel sheet contents later. Set so there are no duplicates
            try {
                if (!csv.createNewFile())//if we failed to create the excel sheet, it exists already
                    csvContents.addAll(Files.readAllLines(Paths.get(csv.getPath())));//so we load it into the set
                else {
                    csvContents.add("Name,Gender,Event,Year Level,House,Result,Type,Date,Time");//else, we jsut created it so we add the header

                }
                csvContents.add(String.join(",", entry.getName(), entry.getGender(), entry.getEvent().getName(), entry.getYearLevel(), entry.getHouse(),
                        entry.getResult(), entry.getEvent().getUnit().getUnitName(), format.format(entry.getTimeStamp()).replaceFirst(" ", "")));
                //we then make a new entry into the excel sheet with all the details of the entry
            } catch (IOException e) {//we failed at some point above?
                e.printStackTrace();//print error
                Platform.runLater(() -> Platform.runLater(() -> Notifications.create().title("Error").text("Failed to read CSV on saving: " + fileName).position(Pos.BOTTOM_LEFT)
                        .hideAfter(Duration.seconds(2)).owner(Main.getMainStage()).showError()));//let the user know. It is likely their fault if we are in production
                return false;//we stop here, returning false which is used later
            }
            System.out.println(save.getPath());//debugging
            try {

                if (!save.createNewFile())//we try to make a new save file
                    Platform.runLater(() -> Platform.runLater(() -> Notifications.create().title("Warning").text("Overwriting file: " + fileName).position(Pos.BOTTOM_LEFT)
                            .hideAfter(Duration.seconds(2)).owner(Main.getMainStage()).showWarning()));//if it already exists, we let the user know we are overwriting their old file, otherwise, we just made it in the if statement
                csv.createNewFile();//we create the csv file aswell as we are about to write to it
            } catch (IOException e) {//failed?
                //alert user and stop like above
                e.printStackTrace();
                Platform.runLater(() -> Notifications.create().title("Error").text("Failed to create file: " + e.getMessage()).position(Pos.BOTTOM_LEFT)
                        .hideAfter(Duration.seconds(2)).owner(Main.getMainStage()).showError());

                return false;
            }

            try (FileWriter writer = new FileWriter(save); FileWriter csvWriter = new FileWriter(csv)) {//now we write. This try statement automatically closes the streams and unlocks the file after execution to make sure there is no resource loss.
                writer.write(resultArea.getText());//write to the file the contents of the result text we made in the confirm button press
                writer.flush();//flush means we write to the file here
                for (String s : csvContents) {//for every line in the csv buffer, we write it into the actual file with a new line at the end
                    csvWriter.write(s + "\n");
                }
                csvWriter.flush();//like above, we write
            } catch (Exception e) {//error?
                //as above, we alert user
                e.printStackTrace();
                Platform.runLater(() -> Notifications.create().title("Error").text("Failed to write to file: " + e.getMessage()).position(Pos.BOTTOM_LEFT)
                        .hideAfter(Duration.seconds(2)).owner(Main.getMainStage()).showError());
                return false;

            }
            //we were sucessful, so we return true for once.
            return true;
        });
        task.thenAccept(result -> {//this is automatically run when the async thread finishes
            if (result)//if we returned true from above, we print a sucess message
                Platform.runLater(() ->//and give a sucess prompt using the file name.
                        Notifications.create().title("Success!").text("Saved as: " + fileName).position(Pos.BOTTOM_LEFT)
                                .hideAfter(Duration.seconds(2)).owner(Main.getMainStage()).showInformation());
        });
        //by the way, Platform.runLater schedules the given code for the main thread, because doing anything ui on another thread could break something



    }

    private void loadData() {//we do this at the beginning
        ageBox.setItems(FXCollections.observableArrayList(agesFile));//we set the ageBox contents to the ages file we read
        eventsFile.forEach(s -> events.add(Event.parse(s)));//for every line in the events file, we make an event out of it and add it to the events
        events = Collections.unmodifiableList(events);//We make these unmodifiable since there is no reason for them to change anymore. Good practise as it outlines errors down the track if they do ever change
        agesFile=Collections.unmodifiableList(agesFile);
        eventBox.setItems(FXCollections.observableArrayList(events));//we popoulate the actual ui element with the events we made earlier



    }

    private void readFiles() throws IOException {//run at the beginning to read files
        String dir = System.getProperty("user.dir");//represents the main folder the program is running in
        agesFile = Files.readAllLines(Paths.get(dir, "ages.txt"));//load the file into the ages list
        eventsFile = Files.readAllLines(Paths.get(dir, "events.txt"));//as above for events


    }

    private Entry createEntry() {//this is where we create a snapshot of the entered data. Used for saving and for populating the result area.

        if (!nameField.getText().isEmpty() && gender.getSelectedToggle() != null &&
                !eventBox.getSelectionModel().isEmpty() && !ageBox.getSelectionModel().isEmpty() && !houseBox.getSelectionModel().isEmpty() && !eventBox.getSelectionModel().getSelectedItem().
                getUnit().read(resultPane.getChildren().get(1)).isEmpty()) {
/*
            if...
            Name field has some text
            one of the genders is selected
            The event selection isn't empty
            The age selection isn't empty
            The House selection isn't empty
            The custom unit field in the result pane isn't empty

            Then we can enable both buttons

             */

            return new Entry(nameField.getText(), ((RadioButton) gender.getSelectedToggle()).getText(), eventBox.getSelectionModel().getSelectedItem(), houseBox.getSelectionModel().getSelectedItem(),
                    ageBox.getSelectionModel().getSelectedItem(), eventBox.getSelectionModel().getSelectedItem().getUnit().read(resultPane.getChildren().get(1)));
            //it simply just reads all fields and makes an object out of it
        }
        return null;//return null if any fields are empty since we can't make an entry with them empty
    }

}
