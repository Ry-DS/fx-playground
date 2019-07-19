package me.ryan_s.data;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

/**
 * Created by SimplyBallistic on 19/07/2019
 *
 * @author SimplyBallistic
 **/
public class Member {
    //jackson annotations to let the XML api know what to save and how
    @JacksonXmlProperty(localName = "Member_ID")
    private int id;
    @JacksonXmlProperty(localName = "Member_FirstName")
    private String firstName;
    @JacksonXmlProperty(localName = "Member_FamilyName")
    private String familyName;
    @JacksonXmlProperty(localName = "Member_Grade")
    private PlayGrade playGrade;
    @JacksonXmlProperty(localName = "Member_FinancialStatus")
    private FinancialStatus financialStatus;

    public Member(int id, String firstName, String familyName, PlayGrade playGrade, FinancialStatus financialStatus) {
        this.id = id;
        this.firstName = firstName;
        this.familyName = familyName;
        this.playGrade = playGrade;
        this.financialStatus = financialStatus;
    }

    public Member() {
    }

    public int getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    //getters and setters
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getFamilyName() {
        return familyName;
    }

    public void setFamilyName(String familyName) {
        this.familyName = familyName;
    }

    public PlayGrade getPlayGrade() {
        return playGrade;
    }

    public void setPlayGrade(PlayGrade playGrade) {
        this.playGrade = playGrade;
    }

    public FinancialStatus getFinancialStatus() {
        return financialStatus;
    }

    public void setFinancialStatus(FinancialStatus financialStatus) {
        this.financialStatus = financialStatus;
    }
}
