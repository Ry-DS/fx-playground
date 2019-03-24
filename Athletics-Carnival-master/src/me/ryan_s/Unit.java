package me.ryan_s;


import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputControl;

import java.util.function.Supplier;

public enum Unit {//represents a unit an event could be measured by. Currently only Time and Length
    TIME("seconds", Unit::genericNumberField),LENGTH("meters",Unit::genericNumberField);

    private String unit;
    private Supplier<Node> input;

    Unit(String unit, Supplier<Node> input) {//each unit needs a name and a gui element creator that is able to represent this unit

        this.unit = unit;
        this.input = input;
    }

    public String getUnitName() {
        return unit;
    }

    public Supplier<Node> getInput() {
        return input;
    }

    private static TextField genericNumberField() {//creates a number field gui element
        TextField textField=new TextField();
        textField.textProperty().addListener((observable, oldValue, newValue) -> {//whenever something is typed inside
            if (!newValue.matches("\\d{0,7}([\\.]\\d{0,4})?")) {//we use regex to make sure its only 7 numbers long on one side, 4 on the other other side, only has numbers and only has one period
                textField.setText(oldValue);//if the regex above wasn't matched, the input is illegal, so we revert it to what it was before
            }
        });
        textField.setAlignment(Pos.CENTER_RIGHT);//this makes the text start from the right to make it more familiar to numbers

        return textField;
    }
//TODO just pretends its a textfield since we don't have anything else here. Will break if new fancy unit comes
    public String read(Node node) {
        return ((TextInputControl)node).getText();
    }
}
