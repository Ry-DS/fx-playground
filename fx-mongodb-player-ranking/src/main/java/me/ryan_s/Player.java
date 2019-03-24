package me.ryan_s;

import com.mongodb.BasicDBObject;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import org.bson.Document;

/**
 * Created by SimplyBallistic on 4/03/2019
 *
 * @author SimplyBallistic
 **/
public class Player {
    private SimpleStringProperty name;
    private SimpleObjectProperty<Position> position;
    private SimpleIntegerProperty result;

    public Player(String name, Position position, int result) {

        this.name = new SimpleStringProperty(name);
        this.position = new SimpleObjectProperty<>(position);
        this.result = new SimpleIntegerProperty(result);

    }

    public Player(Document document) {
        this.name=new SimpleStringProperty(document.getString("name"));
        this.position=new SimpleObjectProperty<>(Position.valueOf(document.getString("position")));
        this.result=new SimpleIntegerProperty(document.getInteger("result"));
    }

    public String getName() {
        return name.get();
    }


    public Position getPosition() {
        return position.get();
    }


    public int getResult() {
        return result.get();
    }

    public void setName(String newValue) {
        name.set(newValue);
    }

    public void setPosition(Position newValue) {
        position.set(newValue);
    }

    public void setResult(int newValue) {
        result.set(newValue);
    }
    public Document toDoc(){
        return new Document().append("name",getName()).append("position",getPosition().name()).append("result",getResult());
    }
    public Player clone(){
        return new Player(getName(),getPosition(),getResult());
    }
}
