package me.ryan_s;

import java.text.NumberFormat;

/**
 * Created by SimplyBallistic on 26/02/2019
 *
 * @author SimplyBallistic
 **/
public class Car {
    private String name;
    private double costMultiplier;


    public Car(String name, double costMultiplier) {
        this.name = name;
        this.costMultiplier = costMultiplier;
    }

    public String getName() {
        return name;
    }

    public double getCostMultiplier() {
        return costMultiplier;
    }

    static Car parse(String name, String costPerDay) {
        return new Car(name,Double.parseDouble(costPerDay));

    }

    @Override
    public String toString() {
        return getName();
    }
}
