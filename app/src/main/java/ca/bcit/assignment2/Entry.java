package ca.bcit.assignment2;

import java.io.Serializable;
import java.util.Date;

//entry object
public class Entry implements Serializable {
    String _serialNum;
    Date _date;
    String _sys;
    String _dia;
    String _condition;
    String _id;

    public  Entry(){

    }

    public void set_serialNum(String _serialNum) {
        this._serialNum = _serialNum;
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

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public void set_condition(String _condition) {
        this._condition = _condition;
    }

    public Entry(String id, String serialNum, Date date, String sys, String dia, String condition) {
        _serialNum = serialNum;
        _date = date;
        _sys = sys;
        _dia = dia;
        _condition = condition;
        _id = id;

    }

    public String get_serialNum() {
        return _serialNum;
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
