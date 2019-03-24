package me.ryan_s;

import java.util.Date;

/**
 * Created by SimplyBallistic on 13/02/2019
 *
 * @author SimplyBallistic
 **/
public class Entry {//a very simple object to just help store everything in the gui the user entered
    private String name;
    private String gender;
    private Event event;
    private String house;
    private String yearLevel;
    private String result;
    private Date timeStamp;

    public Entry(String name, String gender, Event event, String house, String yearLevel, String result) {
        this.name = name;
        this.gender = gender;
        this.event = event;
        this.house = house;
        this.yearLevel = yearLevel;
        this.result = result;
        timeStamp = new Date();
    }

    public String getName() {
        return name;
    }

    public String getGender() {
        return gender;
    }

    public Event getEvent() {
        return event;
    }

    public String getYearLevel() {
        return yearLevel;
    }

    public String getResult() {
        return result;
    }

    public Date getTimeStamp() {
        return timeStamp;
    }

    public String getHouse() {
        return house;
    }
}
