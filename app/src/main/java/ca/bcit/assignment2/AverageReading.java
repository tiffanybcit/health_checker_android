package ca.bcit.assignment2;

import java.util.Date;

//an object created for the report
public class AverageReading {
    String familyMember;
    String sys;
    String dia;
    String condition;

    //getters and setters
    public String getFamilyMember() {
        return familyMember;
    }

    public void setFamilyMember(String familyMember) {
        this.familyMember = familyMember;
    }

    public String getSys() {
        return sys;
    }

    public void setSys(String sys) {
        this.sys = sys;
    }

    public String getDia() {
        return dia;
    }

    public void setDia(String dia) {
        this.dia = dia;
    }

    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }

    //constructor
    public AverageReading(String familyMember, String sys, String dia, String condition) {
        this.familyMember = familyMember;
        this.sys = sys;
        this.dia = dia;
        this.condition = condition;
    }
}
