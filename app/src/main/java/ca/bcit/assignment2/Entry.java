package ca.bcit.assignment2;

import java.io.Serializable;
import java.util.Date;

//entry object
public class Entry implements Serializable {
    String _familyMember;
    Date _date;
    String _sys;
    String _dia;
    String _condition;
    String _id;

    //constructor
    public Entry(String id, String familyMember, Date date, String sys, String dia, String condition) {
        _familyMember = familyMember;
        _date = date;
        _sys = sys;
        _dia = dia;
        _condition = condition;
        _id = id;

    }

    //setters
    public void set_familyMember(String _familyMember) {
        this._familyMember = _familyMember;
    }

    public void set_date(Date _date) {
        this._date = _date;
    }

    public void set_sys(String _sys) {
        this._sys = _sys;
    }

    public void set_dia(String _dia) {
        this._dia = _dia;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public void set_condition(String _condition) {
        this._condition = _condition;
    }

    //getters
    public String get_id() {
        return _id;
    }

    public String get_familyMember() {
        return _familyMember;
    }

    public Date get_date() {
        return _date;
    }

    public String get_sys() {
        return _sys;
    }

    public String get_dia() {
        return _dia;
    }

    public String get_condition() {
        return _condition;
    }
}
