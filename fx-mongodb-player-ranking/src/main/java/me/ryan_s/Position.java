package me.ryan_s;

/**
 * Created by SimplyBallistic on 5/03/2019
 *
 * @author SimplyBallistic
 **/
public enum Position {
    FALL_BACK("Fall Back"),CENTER("Center"),FALL_FORWARD("Fall Forward");
    String name;

    Position(String name) {
        this.name = name;
    }

    @Override
    public String toString() {

        return name;
    }}
