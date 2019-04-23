package me.ryan_s.login;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

import java.util.Arrays;
import java.util.List;

/**
 * Created by SimplyBallistic on 23/04/2019
 *
 * @author SimplyBallistic
 **/
@JacksonXmlRootElement
public class Persons {

    @JacksonXmlElementWrapper(localName = "people")
    @JacksonXmlProperty(localName = "Person")
    private List<Person> persons;

    public Persons(Person... persons) {
        this.persons = Arrays.asList(persons);
    }
}
