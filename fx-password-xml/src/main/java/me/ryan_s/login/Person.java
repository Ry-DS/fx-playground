package me.ryan_s.login;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

/**
 * Created by SimplyBallistic on 23/04/2019
 *
 * @author SimplyBallistic
 **/
public class Person {
    @JacksonXmlProperty(isAttribute = true)

    private String name;
    @JacksonXmlProperty(isAttribute = true)

    private int age;
    @JacksonXmlProperty(isAttribute = true)

    private String profession;
    @JacksonXmlProperty(isAttribute = true)
    private int wage;

    public Person(String name, int age, String profession, int wage) {
        this.name = name;
        this.age = age;
        this.profession = profession;
        this.wage = wage;
    }

    public String getName() {
        return name;
    }

    public int getAge() {
        return age;
    }

    public String getProfession() {
        return profession;
    }

    public int getWage() {
        return wage;
    }
}
