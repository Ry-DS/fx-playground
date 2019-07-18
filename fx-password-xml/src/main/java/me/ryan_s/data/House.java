package me.ryan_s.data;

/**
 * Created by SimplyBallistic on 18/07/2019
 *
 * @author SimplyBallistic
 **/
public enum House {
    BLACKWOOD("Blackwood", "primary"), KOROROIT("Kororoit", "danger"), ROTHWELL("Rothwell", "warning"), COTTRELL("Cottrell", "success");

    private String name, style;

    House(String name, String style) {

        this.name = name;
        this.style = style;
    }

    public String getStyle() {
        return style;
    }

    @Override
    public String toString() {
        return name;
    }
}
