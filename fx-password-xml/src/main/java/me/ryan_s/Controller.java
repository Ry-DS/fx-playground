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
import me.ryan_s.data.FinancialStatus;
import me.ryan_s.data.Member;
import me.ryan_s.data.PlayGrade;
import org.controlsfx.control.Notifications;

import java.io.IOException;

public class Controller {//represents the main window
    //outline all the GUI elements
    @FXML
    private TextField familyNameField;
    @FXML
    private TextField nameField;
    @FXML
    private ComboBox<PlayGrade> gradeBox;
    @FXML
    private ComboBox<FinancialStatus> financialBox;
    @FXML
    private Button saveBtn;
    @FXML
    private TableColumn<Member, Integer> idCol;
    @FXML
    private TableColumn<Member, String> nameCol;
    @FXML
    private TableColumn<Member, String> lastNameCol;
    @FXML
    private TableColumn<Member, PlayGrade> gradeCol;
    @FXML
    private TableColumn<Member, FinancialStatus> financialCol;
    @FXML
    private TableView<Member> table;


    //on start
    public void initialize() {
        //set up both combo boxes
        gradeBox.setItems(FXCollections.observableArrayList(PlayGrade.values()));
        gradeBox.setOnAction(action -> {//this code themes the boxes based on what is currently selected
            Platform.runLater(() -> {
                gradeBox.getStyleClass().removeIf(s -> s.contains("split-menu-btn"));//remove old styling
                gradeBox.getStyleClass().add("split-menu-btn-" + gradeBox.getValue().getStyle());
            });

        });
        //repeat from above on second combo box
        financialBox.setItems(FXCollections.observableArrayList(FinancialStatus.values()));
        financialBox.setOnAction(action -> {
            Platform.runLater(() -> {
                financialBox.getStyleClass().removeIf(s -> s.contains("split-menu-btn"));
                financialBox.getStyleClass().add("split-menu-btn-" + financialBox.getValue().getStyle());
            });

        });
        //set up the table
        setupTable();

    }

    private void setupTable() {
        table.setOnKeyPressed(this::onTableKeyPressed);//set on key pressed listener while in the table, used to delete entries
        table.setItems(FXCollections.observableArrayList(Main.DATA.getMembers()));//set the current items loaded from XML
        //outline how the columns will read the member object
        idCol.setCellValueFactory(new PropertyValueFactory<>("Id"));
        nameCol.setCellValueFactory(new PropertyValueFactory<>("FirstName"));
        lastNameCol.setCellValueFactory(new PropertyValueFactory<>("FamilyName"));
        gradeCol.setCellValueFactory(new PropertyValueFactory<>("PlayGrade"));
        financialCol.setCellValueFactory(new PropertyValueFactory<>("FinancialStatus"));
        //ensure double clicking any entry in the table allows editing
        table.setEditable(true);
        //set name columns to use text fields when editing
        nameCol.setCellFactory(TextFieldTableCell.forTableColumn());
        lastNameCol.setCellFactory(TextFieldTableCell.forTableColumn());
        //for the others we only give set values instead of a text field
        gradeCol.setCellFactory(ComboBoxTableCell.forTableColumn(PlayGrade.values()));
        financialCol.setCellFactory(ComboBoxTableCell.forTableColumn(FinancialStatus.values()));

        //to support editing when clicking row and to update the table
        nameCol.setOnEditCommit(event -> {
            event.getRowValue().setFirstName(event.getNewValue());
            onEdit(event.getRowValue());
        });
        lastNameCol.setOnEditCommit(event -> {
            event.getRowValue().setFamilyName(event.getNewValue());
            onEdit(event.getRowValue());

        });
        gradeCol.setOnEditCommit(event -> {
            event.getRowValue().setPlayGrade(event.getNewValue());
            onEdit(event.getRowValue());
        });
        financialCol.setOnEditCommit(event -> {
            event.getRowValue().setFinancialStatus(event.getNewValue());
            onEdit(event.getRowValue());
        });

    }

    private void onEdit(Member rowValue) {//when an edit is made
        try {
            Main.saveXml();//try save the new changes
        } catch (IOException e) {
            Platform.runLater(() -> {//let user know if we failed
                Notifications.create().text("Failed to save: " + e.getMessage()).showError();
            });

        }

    }

    private void onTableKeyPressed(KeyEvent keyEvent) {//on key pressed in table
        final Member selectedItem = table.getSelectionModel().getSelectedItem();

        if (selectedItem != null)//if something was selected
        {
            if (keyEvent.getCode() == KeyCode.DELETE) {//if the key was delete
                Main.DATA.removeMember(selectedItem);//remove from list
                try {
                    Main.saveXml();//then try save
                    table.setItems(FXCollections.observableArrayList(Main.DATA.getMembers()));//and also update the table
                } catch (Exception e) {//let user know if we failed

                    Notifications.create().text("Failed to delete: " + e.getMessage()).showError();

                }
            }

        }


    }

    public void onSaveClick() {//on save button clicked
        if (nameField.getText().isEmpty() || familyNameField.getText().isEmpty() || gradeBox.getSelectionModel().isEmpty() || financialBox.getSelectionModel().isEmpty()) {
            //if any of the fields are empty, we let user know and stop here
            Platform.runLater(() -> {
                Notifications.create().text("Please fill out all fields").showWarning();
            });
            return;
        }
        //otherwise, construct a member with inputted values
        Member member = new Member(Main.nextMemberId(), familyNameField.getText(), nameField.getText(), gradeBox.getValue(), financialBox.getValue());
        Main.DATA.addMember(member);//add member to the main list
        try {
            Main.saveXml();//try save file
            table.setItems(FXCollections.observableArrayList(Main.DATA.getMembers()));//and update table
        } catch (IOException e) {//let user know if we failed
            Notifications.create().text("Failed to add member: " + e.getMessage()).showError();
        }
    }

    public void onRefresh() {//on refresh button clicked in the file submenu
        try {//try reload the file
            Main.loadFile();
        } catch (IOException e) {//failed? do what we've been doing this whole time with errors
            Notifications.create().text("Failed to refresh: " + e.getMessage());
            return;
        }
        //update table if we didn't fail
        table.setItems(FXCollections.observableArrayList(Main.DATA.getMembers()));
        Notifications.create().text("Refreshed from file").showInformation();//let user know it worked


    }

    public void onLogout() {//on logout pressed
        Main.show(new Stage(), "login", "Login", 300, 275);//open the login screen
        ((Stage) saveBtn.getScene().getWindow()).close();//close the current window
    }
}
