package me.ryan_s;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Slider;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.ComboBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import org.controlsfx.control.Notifications;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Comparator;
import java.util.List;

public class Controller {
    //defining elements of GUI
    @FXML
    TableView<Movie> table;
    @FXML
    TableColumn<Movie, String> titleCol;
    @FXML
    TableColumn<Movie, Genre> genreCol;
    @FXML
    TableColumn<Movie, Integer> ratingCol;
    @FXML
    ComboBox<Genre> genreBox;
    @FXML
    ComboBox<Genre> searchGenreBox;
    @FXML
    TextField nameField;
    @FXML
    TextField searchNameField;
    @FXML
    Slider ratingSdr;
    @FXML
    CheckBox searchSliderCheckbox;
    @FXML
    Slider searchRatingSdr;
    //Defining the methods of storing gui data
    private ObservableList<Movie> list;
    private FilteredList<Movie> movies = new FilteredList<>(list = FXCollections.observableArrayList(), p -> true);
    private File file = new File(System.getProperty("user.dir") + "/movies.txt");

    @FXML//run when application starts
    public void initialize() throws InterruptedException {
        //initialise the genre choice boxes with the set genres
        genreBox.setItems(FXCollections.observableArrayList(Genre.values()));
        searchGenreBox.setItems(FXCollections.observableArrayList(Genre.values()));
        //sets up table to display movies properly and support click editing
        setupTable();


        //lets user know we are gonna start reading the file after GUI loads to prevent any confusion of wait times
        Platform.runLater(() -> {
            Notifications.create().text("Reading File...").showInformation();
        });
        readFile();


    }

    private void setupTable() {


        //setting up table to read....
        table.setOnKeyPressed(this::onTableKeyPressed);
        table.setItems(movies);
        ratingCol.setCellValueFactory(new PropertyValueFactory<>("Rating"));
        genreCol.setCellValueFactory(new PropertyValueFactory<>("Genre"));
        titleCol.setCellValueFactory(new PropertyValueFactory<>("Title"));
        table.setEditable(true);

        titleCol.setCellFactory(TextFieldTableCell.forTableColumn());
        genreCol.setCellFactory(ComboBoxTableCell.forTableColumn(Genre.values()));
        //we set numbers here for a limited selection of possible numbers when editing
        ratingCol.setCellFactory(ComboBoxTableCell.forTableColumn(new Integer[]{1, 2, 3, 4, 5}));

        //to support editing when clicking row
        titleCol.setOnEditCommit(event -> {
            event.getRowValue().setTitle(event.getNewValue());
            onEdit(event.getRowValue());
        });
        genreCol.setOnEditCommit(event -> {
            event.getRowValue().setGenre(event.getNewValue());
            onEdit(event.getRowValue());

        });
        ratingCol.setOnEditCommit(event -> {
            event.getRowValue().setRating(event.getNewValue());
            onEdit(event.getRowValue());
        });


    }

    private void onEdit(Movie movie) {
        System.out.println(movie.getTitle());
        Notifications.create().text("Saving...").showInformation();
        try {
            saveFile();
            Platform.runLater(() -> {
                Notifications.create().text("Successfully saved " + movie.getTitle()).showInformation();

            });
        } catch (Exception e) {

            Platform.runLater(() -> {
                Notifications.create().text("Failed to save File: " + e.getMessage()).showError();
            });

        }

    }

    public void readFile() {
        list.clear();//clear anything currently in the list, we are refreshing

        Platform.runLater(() -> {
            Notifications.create().text("Fetching Movies...").showInformation();
        });
        try {//try, if we encounter an error we let the user know

            if (!file.exists())//if file doesn't exist...
                file.createNewFile();//create one
            List<String> fileContents = Files.readAllLines(file.toPath());//read all contents to list array
            for (int i = 3; i < fileContents.size(); i += 4) {//we start on line 4 and skip 4 lines every time. the previous 3 lines is a movie
                String name = fileContents.get(i - 3);
                int rating = Integer.parseInt(fileContents.get(i - 2));
                Genre genre = Genre.parse(fileContents.get(i - 1));

                list.add(new Movie(name, rating, genre));//construct from 3 previous lines and add to list

            }
            list.sort(Comparator.comparingInt(o -> o.getGenre().ordinal()));//sorts the list based on genre and order it is in enum
            Platform.runLater(() -> {
                Notifications.create().text("Finished Loading Movies").showInformation();
                table.refresh();//get the table to display everything
            });
        } catch (Exception e) {
            Platform.runLater(() -> {//failed? let user know and error message
                Notifications.create().text("Failed to load movies: " + e.getMessage());
            });
        }


    }

    public void onSaveClick() {//on save called when user enters new movie
        if (nameField.getText().isEmpty() || genreBox.getSelectionModel().isEmpty()) {//if fields are empty, nothing to save, let user know
            Notifications.create().text("Please fill out the Name and Genre Fields").showWarning();
            return;
        }
        Movie movie = new Movie(nameField.getText(), (int) ratingSdr.getValue(), genreBox.getValue());//construct movie from fields
        Notifications.create().text("Saving to File...").showInformation();
        list.add(movie);
        list.sort(Comparator.comparingInt(o -> o.getGenre().ordinal()));//sorts the list based on genre and order it is in enum
        try {//try saving...let user know if it fails
            saveFile();
            Platform.runLater(() -> {
                Notifications.create().text("Successfully saved " + movie.getTitle()).showInformation();

            });
        } catch (Exception e) {

            Platform.runLater(() -> {
                Notifications.create().text("Failed to save File: " + e.getMessage()).showError();
            });

        }


    }

    private void saveFile() throws IOException {//called when saving across different points of application
        FileWriter writer = new FileWriter(file);//init writer to write to file
        for (Movie movie : list) {//for every movie
            writer.write(movie.getTitle() + "\n");//print title, rating and genre with new line
            writer.write(movie.getRating() + "\n");
            writer.write(movie.getGenre() + "\n");
            writer.write("\n");//new line at bottom to separate movie entries
        }
        writer.flush();//flush means we write to file
        writer.close();//dispose to save memory

    }

    public void onTableKeyPressed(KeyEvent keyEvent) {//when user presses del key on a row
        final Movie selectedItem = table.getSelectionModel().getSelectedItem();

        if (selectedItem != null)//if something was selected
        {
            if (keyEvent.getCode() == KeyCode.DELETE) {
                Notifications.create().text("Deleting " + selectedItem.getTitle() + "...").showInformation();
                list.remove(selectedItem);//remove from list
                list.sort(Comparator.comparingInt(o -> o.getGenre().ordinal()));//sorts the list based on genre and order it is in enum
                try {//try saving after deletion
                    saveFile();
                    Platform.runLater(() -> {
                        Notifications.create().text("Successfully deleted " + selectedItem.getTitle()).showInformation();

                    });
                } catch (Exception e) {

                    Platform.runLater(() -> {
                        Notifications.create().text("Failed to save: " + e.getMessage()).showError();
                    });

                }
            }

        }
    }

    public void clearSearchPosition() {//this is for the clear button, letting the user stop searching based on genre
        searchGenreBox.setValue(null);
    }

    public void onSearch() {//called when search button is pressed
        movies.setPredicate(p -> {//we sort the filtered list based on what fields are empty/being used. If its empty every movie is allowed through. Otherwise, we compare
            boolean useName = searchNameField.getText().isEmpty() || p.getTitle().toLowerCase().contains(searchNameField.getText().toLowerCase());//ignore caps
            boolean useResult = !searchSliderCheckbox.isSelected() || p.getRating() == (int) searchRatingSdr.getValue();
            boolean usePosition = searchGenreBox.getValue() == null || p.getGenre() == searchGenreBox.getValue();
            return useName && usePosition && useResult;
        });


    }

}

