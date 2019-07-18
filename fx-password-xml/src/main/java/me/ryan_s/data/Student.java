package me.ryan_s.data;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

/**
 * Created by SimplyBallistic on 16/07/2019
 *
 * @author SimplyBallistic
 **/
public class Student {
    //things we want to save to XML
    @JacksonXmlProperty(localName = "Student_ID")
    private int id;
    @JacksonXmlProperty(localName = "Student_FamilyName")
    private String familyName;
    @JacksonXmlProperty(localName = "Student_FirstName")
    private String firstName;
    @JacksonXmlProperty(localName = "Student_House")
    private House house;
    @JacksonXmlProperty(localName = "Student_ExamStatus")
    private ExamResult examStatus;


    public Student(int id, String familyName, String firstName, House house, ExamResult examStatus) {
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

    public void setId(int id) {
        this.id = id;
    }

    public String getFamilyName() {
        return familyName;
    }

    public void setFamilyName(String familyName) {
        this.familyName = familyName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public House getHouse() {
        return house;
    }

    public void setHouse(House house) {
        this.house = house;
    }

    public ExamResult getExamStatus() {
        return examStatus;
    }

    public void setExamStatus(ExamResult examStatus) {
        this.examStatus = examStatus;
    }
}

