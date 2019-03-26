package me.ryan_s;

import com.mongodb.ConnectionString;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;
import com.mongodb.reactivestreams.client.MongoClient;
import com.mongodb.reactivestreams.client.MongoClients;
import com.mongodb.reactivestreams.client.Success;
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
import javafx.util.converter.IntegerStringConverter;
import org.bson.Document;
import org.controlsfx.control.Notifications;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import java.util.function.Predicate;

public class Controller {
    @FXML
    TableView<Player> table;
    @FXML TableColumn<Player,String> nameCol;
    @FXML TableColumn<Player,Position> posCol;
    @FXML TableColumn<Player, Integer> resultCol;
    @FXML ComboBox<Position> posBox;
    @FXML ComboBox<Position> searchPosBox;
    @FXML TextField nameField;
    @FXML TextField searchNameField;
    @FXML Slider resultSdr;
    @FXML CheckBox searchSliderCheckbox;
    @FXML Slider searchResultSdr;
    private MongoClient mongoClient;
    final String URL="mongodb+srv://ryan:sam0012@db1-wuzcm.gcp.mongodb.net/test?retryWrites=true";
    private ObservableList<Player> list;
    private FilteredList<Player> players= new FilteredList<>(list=FXCollections.observableArrayList(),p->true);
    @FXML
    public void initialize() throws InterruptedException {
        posBox.setItems(FXCollections.observableArrayList(Position.values()));
        searchPosBox.setItems(FXCollections.observableArrayList(Position.values()));

        setupTable();



        Platform.runLater(()->{
        Notifications.create().text("Connecting to Database...").showInformation();});
        initDatabase();


    }

    private void setupTable() {



        table.setOnKeyPressed(this::onTableKeyPressed);
        table.setItems(players);
        resultCol.setCellValueFactory(new PropertyValueFactory<>("Result"));
        posCol.setCellValueFactory(new PropertyValueFactory<>("Position"));
        nameCol.setCellValueFactory(new PropertyValueFactory<>("Name"));
        table.setEditable(true);
        nameCol.setCellFactory(TextFieldTableCell.forTableColumn());
        posCol.setCellFactory(ComboBoxTableCell.forTableColumn(Position.values()));
        resultCol.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));


        nameCol.setOnEditCommit(event -> {
            Player old=event.getRowValue().clone();
            event.getRowValue().setName(event.getNewValue());
            onEdit(old,event.getRowValue());
        });
        posCol.setOnEditCommit(event -> {
            Player old=event.getRowValue().clone();
            event.getRowValue().setPosition(event.getNewValue());
            onEdit(old,event.getRowValue());

        });
        resultCol.setOnEditCommit(event -> {
            Player old=event.getRowValue().clone();
            event.getRowValue().setResult(event.getNewValue());
            onEdit(old,event.getRowValue());
        });



    }

    private void onEdit(Player player,Player newPlayer) {
        System.out.println(player.getName());
        Notifications.create().text("Sending to Database...").showInformation();
        Document update=newPlayer.toDoc();
        Document search=player.toDoc();
        mongoClient.getDatabase("player_results").getCollection("players").replaceOne(search,
                update).subscribe(new Subscriber<UpdateResult>() {
            @Override
            public void onSubscribe(Subscription s) {
                s.request(1);
            }

            @Override
            public void onNext(UpdateResult updateResult) {
                Platform.runLater(()->{
                    if(updateResult.wasAcknowledged())
                    Notifications.create().text("Successfully Updated Player: "+newPlayer.getName()).showInformation();
                    else Notifications.create().text("Failed to find Player: "+player.getName()+" in database").showError();
                });
            }

            @Override
            public void onError(Throwable t) {
                t.printStackTrace();
                Platform.runLater(()->{
                    Notifications.create().text("Failed: "+t.getMessage()).showError();
                });

            }

            @Override
            public void onComplete() {

            }
        });

    }

    public void refreshDatabase() {



        list.clear();
        Platform.runLater(()->{
            Notifications.create().text("Fetching Players...").showInformation();});
        mongoClient.getDatabase("player_results").getCollection("players").find().subscribe(new Subscriber<Document>() {
            @Override
            public void onSubscribe(Subscription s) {
                s.request(Integer.MAX_VALUE);
            }

            @Override
            public void onNext(Document document) {
                list.add(new Player(document));

            }

            @Override
            public void onError(Throwable t) {
                t.printStackTrace();
                Platform.runLater(()->{
                    Notifications.create().text("Failed to load players: "+t.getMessage());
                });

            }

            @Override
            public void onComplete() {
                Platform.runLater(()->{
                    Notifications.create().text("Finished Loading Players").showInformation();
                    table.refresh();
                });

            }
        });




    }
    private void initDatabase() throws InterruptedException {
        mongoClient = MongoClients.create(new ConnectionString(URL));
        Thread.sleep(1000);//maybe better way.
        mongoClient.listDatabaseNames().subscribe(new Subscriber<String>() {
            @Override
            public void onSubscribe(Subscription s) {

                s.request(1);
            }

            @Override
            public void onNext(String s) {
                Platform.runLater(()->{
                    Notifications.create().text("Connected to: "+s).showInformation();
                    refreshDatabase();
                });


            }

            @Override
            public void onError(Throwable t) {
                t.printStackTrace();
                Platform.runLater(()-> Notifications.create().text("Failed: "+t.getMessage()).showError());
            }

            @Override
            public void onComplete() {

            }
        });

    }
    public void onSaveClick(){
        if(nameField.getText().isEmpty()||posBox.getSelectionModel().isEmpty()){
            Notifications.create().text("Please fill out the Name and Position Fields").showWarning();
            return;
        }
        Player player=new Player(nameField.getText(),posBox.getValue(), (int) resultSdr.getValue());
        Notifications.create().text("Saving to Database...").showInformation();
        mongoClient.getDatabase("player_results").getCollection("players").insertOne(player.toDoc()).subscribe(new Subscriber<Success>() {
            @Override
            public void onSubscribe(Subscription s) {
                s.request(1);
            }

            @Override
            public void onNext(Success success) {
                Platform.runLater(()->{
                    Notifications.create().text("Successfully saved "+player.getName()).showInformation();
                    list.add(player);
                   // nameField.setText("");
                    //posBox.getSelectionModel().select(null);
                });
            }

            @Override
            public void onError(Throwable t) {
                t.printStackTrace();
                Platform.runLater(()->{
                    Notifications.create().text("Failed to save Player: "+t.getMessage()).showError();
                });


            }

            @Override
            public void onComplete() {

            }
        });
    }
    public void onTableKeyPressed(KeyEvent keyEvent){
        final Player selectedItem = table.getSelectionModel().getSelectedItem();

        if ( selectedItem != null )
        {
            if ( keyEvent.getCode()==KeyCode.DELETE)
            {
                Notifications.create().text("Deleting "+selectedItem.getName()+"...").showInformation();
                mongoClient.getDatabase("player_results").getCollection("players").deleteOne(selectedItem.toDoc()).subscribe(new Subscriber<DeleteResult>() {
                    @Override
                    public void onSubscribe(Subscription s) {
                        s.request(1);
                    }

                    @Override
                    public void onNext(DeleteResult deleteResult) {
                            Platform.runLater(()->{
                                if(deleteResult.wasAcknowledged()){
                                Notifications.create().text("Successfully deleted player").showInformation();
                                list.remove(selectedItem);
                                }else Notifications.create().text("Failed to find player to delete").showWarning();

                            });
                    }

                    @Override
                    public void onError(Throwable t) {
                        t.printStackTrace();
                        Platform.runLater(()->{
                            Notifications.create().text("Failed: "+t.getMessage()).showError();
                        });
                    }

                    @Override
                    public void onComplete() {

                    }
                });
            }

        }
    }
    public void clearSearchPosition(){
        searchPosBox.setValue(null);
    }
    public void onSearch(){
        players.setPredicate(p->{
            boolean useName=searchNameField.getText().isEmpty()||p.getName().contains(searchNameField.getText());
            boolean useResult=!searchSliderCheckbox.isSelected()||p.getResult()==(int)searchResultSdr.getValue();
            boolean usePosition=searchPosBox.getValue()==null||p.getPosition()==searchPosBox.getValue();
            return useName&&usePosition&&useResult;
        });


    }

}

