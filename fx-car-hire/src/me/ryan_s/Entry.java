package me.ryan_s;

import java.util.Calendar;

import static me.ryan_s.Controller.ADDITIONAL_CAR_PRICE_PER_DAY;
import static me.ryan_s.Controller.CAR_PRICE_PER_DAY;

/**
 * Created by SimplyBallistic on 26/02/2019
 *
 * @author SimplyBallistic
 **/
public class Entry {
    private String customerName;
    private Car carType;
    private int days;
    private boolean hasInsurance;
    private double cost;
    private Calendar returnDay;

    public Entry(String customerName, Car carType, int days, boolean hasInsurance) {
        this.customerName = customerName;
        this.carType = carType;
        this.days = days;
        this.hasInsurance = hasInsurance;
        this.cost=calculateCost();
        returnDay=Calendar.getInstance();
        returnDay.add(Calendar.DAY_OF_MONTH,days);


    }

    private double calculateCost() {
        double cost=0;
        int additionalDays=days-3;
        System.out.println(additionalDays);
        if(additionalDays<0){
            cost+=days*CAR_PRICE_PER_DAY;
        }else cost+=CAR_PRICE_PER_DAY*3+additionalDays*ADDITIONAL_CAR_PRICE_PER_DAY;
        cost*=carType.getCostMultiplier();
        if(hasInsurance)
            cost+=days*25;

        return cost;
    }

    public String getCustomerName() {
        return customerName;
    }

    public Car getCarType() {
        return carType;
    }

    public int getDays() {
        return days;
    }

    public boolean isHasInsurance() {
        return hasInsurance;
    }

    public double getCost() {
        return cost;
    }

    public Calendar getReturnDay() {
        return returnDay;
    }
}
