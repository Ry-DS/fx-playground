package me.ryan_s;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.ComboBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import me.ryan_s.data.ExamResult;
import me.ryan_s.data.House;
import me.ryan_s.data.Student;
import org.controlsfx.control.Notifications;

import java.io.IOException;

public class Controller {
    @FXML
    private TextField familyNameField;
    @FXML
    private TextField nameField;
    @FXML
    private ComboBox<House> houseBox;
    @FXML
    private ComboBox<ExamResult> examBox;
    @FXML
    private Button saveBtn;
    @FXML
    private TableColumn<Student, Integer> idCol;
    @FXML
    private TableColumn<Student, String> nameCol;
    @FXML
    private TableColumn<Student, String> lastNameCol;
    @FXML
    private TableColumn<Student, House> houseCol;
    @FXML
    private TableColumn<Student, ExamResult> examCol;
    @FXML
    private TableView<Student> table;



    public void initialize() throws IOException {
        houseBox.setItems(FXCollections.observableArrayList(House.values()));
        houseBox.setOnAction(action -> {
            Platform.runLater(() -> {
                houseBox.getStyleClass().removeIf(s -> s.contains("split-menu-btn"));
                houseBox.getStyleClass().add("split-menu-btn-" + houseBox.getValue().getStyle());
            });

        });
        examBox.setItems(FXCollections.observableArrayList(ExamResult.values()));
        setupTable();

    }

    private void setupTable() {
        table.setOnKeyPressed(this::onTableKeyPressed);
        table.setItems(FXCollections.observableArrayList(Main.DATA.getStudents()));
        table.setItems(FXCollections.observableArrayList(Main.DATA.getStudents()));
        idCol.setCellValueFactory(new PropertyValueFactory<>("Id"));
        nameCol.setCellValueFactory(new PropertyValueFactory<>("FirstName"));
        lastNameCol.setCellValueFactory(new PropertyValueFactory<>("FamilyName"));
        houseCol.setCellValueFactory(new PropertyValueFactory<>("House"));
        examCol.setCellValueFactory(new PropertyValueFactory<>("ExamStatus"));
        table.setEditable(true);

        nameCol.setCellFactory(TextFieldTableCell.forTableColumn());
        lastNameCol.setCellFactory(TextFieldTableCell.forTableColumn());
        //we set numbers here for a limited selection of possible numbers when editing
        houseCol.setCellFactory(ComboBoxTableCell.forTableColumn(House.values()));
        examCol.setCellFactory(ComboBoxTableCell.forTableColumn(ExamResult.values()));

        //to support editing when clicking row
        nameCol.setOnEditCommit(event -> {
            event.getRowValue().setFirstName(event.getNewValue());
            onEdit(event.getRowValue());
        });
        lastNameCol.setOnEditCommit(event -> {
            event.getRowValue().setFamilyName(event.getNewValue());
            onEdit(event.getRowValue());

        });
        houseCol.setOnEditCommit(event -> {
            event.getRowValue().setHouse(event.getNewValue());
            onEdit(event.getRowValue());
        });
        examCol.setOnEditCommit(event -> {
            event.getRowValue().setExamStatus(event.getNewValue());
            onEdit(event.getRowValue());
        });

    }

    private void onEdit(Student rowValue) {
        try {
            Main.saveXml();
        } catch (IOException e) {
            Platform.runLater(() -> {
                Notifications.create().text("Failed to save: " + e.getMessage()).showError();
            });

        }

    }

    private void onTableKeyPressed(KeyEvent keyEvent) {
        final Student selectedItem = table.getSelectionModel().getSelectedItem();

        if (selectedItem != null)//if something was selected
        {
            if (keyEvent.getCode() == KeyCode.DELETE) {
                Main.DATA.removeStudent(selectedItem);//remove from list
                try {
                    Main.saveXml();
                    table.setItems(FXCollections.observableArrayList(Main.DATA.getStudents()));
                } catch (Exception e) {

                    Notifications.create().text("Failed to delete: " + e.getMessage()).showError();

                }
            }

        }


    }

    public void onSaveClick() {
        if (nameField.getText().isEmpty() || familyNameField.getText().isEmpty() || houseBox.getSelectionModel().isEmpty() || examBox.getSelectionModel().isEmpty()) {
            Platform.runLater(() -> {
                Notifications.create().text("Please fill out all fields").showWarning();
            });
            return;
        }
        Student student = new Student(Main.nextStudentId(), familyNameField.getText(), nameField.getText(), houseBox.getValue(), examBox.getValue());
        Main.DATA.addStudent(student);
        try {
            Main.saveXml();
            table.setItems(FXCollections.observableArrayList(Main.DATA.getStudents()));
        } catch (IOException e) {
            Notifications.create().text("Failed to add student: " + e.getMessage()).showError();
        }
    }

    public void onRefresh() {
        try {
            Main.loadFile();
        } catch (IOException e) {
            Notifications.create().text("Failed to refresh: " + e.getMessage());
            return;
        }
        table.setItems(FXCollections.observableArrayList(Main.DATA.getStudents()));
        Notifications.create().text("Refreshed from file").showInformation();


    }

    public void onLogout() {
        Main.show(new Stage(), "login", "Login", 300, 275);
        ((Stage) saveBtn.getScene().getWindow()).close();
    }
}
