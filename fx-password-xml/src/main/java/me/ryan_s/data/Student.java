package me.ryan_s.data;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

/**
 * Created by SimplyBallistic on 16/07/2019
 *
 * @author SimplyBallistic
 **/
public class Student {
    @JacksonXmlProperty(localName = "Student_ID")
    private int id;
    @JacksonXmlProperty(localName = "Student_FamilyName")
    private String familyName;
    @JacksonXmlProperty(localName = "Student_FirstName")
    private String firstName;
    @JacksonXmlProperty(localName = "Student_House")
    private String house;
    @JacksonXmlProperty(localName = "Student_ExamStatus")
    private ExamResult examStatus;

    public Student(int id, String familyName, String firstName, String house, ExamResult examStatus) {
        this.id = id;
        this.familyName = familyName;
        this.firstName = firstName;
        this.house = house;
        this.examStatus = examStatus;
    }

    public Student() {
    }

    public int getId() {
        return id;
    }

    public String getFamilyName() {
        return familyName;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getHouse() {
        return house;
    }

    public char getExamStatus() {
        return examStatus.getCharacter();
    }

    @Override
    public String toString() {
        return "Student{" +
                "id=" + id +
                ", familyName='" + familyName + '\'' +
                ", firstName='" + firstName + '\'' +
                ", house='" + house + '\'' +
                ", examStatus=" + examStatus +
                '}';
    }
}
