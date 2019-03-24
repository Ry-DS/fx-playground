package me.ryan_s;

/**
 * Created by SimplyBallistic on 13/02/2019
 *
 * @author SimplyBallistic
 **/
public class Event {//represents an event. In this case, the name and the unit a result would use in this event
    private String name;
    private Unit unit;

    public Event(String name, Unit unit) {
        this.name = name;
        this.unit = unit;
    }

    public static Event parse(String string) {//in the events.txt, an event is represented as <Name>,<Time/Length> so we parse it as follows
        try{
            String[] split = string.split(",");//we try split into two halves.
            return new Event(split[0], Unit.valueOf(split[1].toUpperCase()));//and make the first the name and the second a unit
        } catch (Exception e) {//if we failed
            e.printStackTrace();
            throw new IllegalArgumentException("Failed to parse String " + string + " into event");//we print an error and let the developer know that the string given is wrong
        }
    }

    @Override
    public String toString() {
        return name;
    }

    public String getName() {
        return name;
    }

    public Unit getUnit() {
        return unit;
    }
}
