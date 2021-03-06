package com.tartouslab.hasanlab.nensyria;

/**
 * Created by hasanzgh on 22/11/2017.
 */

public class Course {

    //private variables
    private int _id;
    private String _name;
    private String _hrs;
    private String _hrsPd;
    private String _dPw;
    private String _days;
    private String _startDate;
    private String _hotORnot;
    private String _description;
    private String _certificate;
    private String _icon;

    // Empty constructor
    public Course(){

    }
    // constructor
    public Course(int id, String name, String hrs, String hrsPd, String dPw, String days,
                  String startDate, String hotORnot, String description, String _certificate ,String icon){
        this._id = id;
        this._name = name;
        this._hrs = hrs;
        this._hrsPd = hrsPd;
        this._dPw = dPw;
        this._days = days;
        this._startDate = startDate;
        this._hotORnot = hotORnot;
        this._description = description;
        this._certificate = _certificate;
        this._icon = icon;
    }

    // constructor
    public Course(String name, String hrs, String hrsPd, String dPw, String days, String startDate,
                  String hotORnot, String description, String _certificate ,String icon){
        this._name = name;
        this._hrs = hrs;
        this._hrsPd = hrsPd;
        this._dPw = dPw;
        this._days = days;
        this._startDate = startDate;
        this._hotORnot = hotORnot;
        this._description = description;
        this._certificate = _certificate;
        this._icon = icon;
    }
    // getting ID
    public int getID(){
        return this._id;
    }

    // setting id
    public void setID(int id){
        this._id = id;
    }

    // getting name
    public String getName(){
        return this._name;
    }

    // setting name
    public void setName(String name){
        this._name = name;
    }

    // getting hrs
    public String gethrs(){
        return this._hrs;
    }

    // setting hrs
    public void sethrs(String hrs){
        this._hrs = hrs;
    }


    public String get_hrsPd() {
        return _hrsPd;
    }

    public void set_hrsPd(String _hrsPd) {
        this._hrsPd = _hrsPd;
    }

    public String get_dPw() {
        return _dPw;
    }

    public void set_dPw(String _dPw) {
        this._dPw = _dPw;
    }

    public String get_days() {
        return _days;
    }

    public void set_days(String _days) {
        this._days = _days;
    }

    public String get_startDate() {
        return _startDate;
    }

    public void set_startDate(String _startDate) {
        this._startDate = _startDate;
    }

    public String get_hotORnot() {
        return _hotORnot;
    }

    public void set_hotORnot(String _hotORnot) {
        this._hotORnot = _hotORnot;
    }

    public String get_description() {
        return _description;
    }

    public void set_description(String _description) {
        this._description = _description;
    }

    public String get_certificate() {
        return _certificate;
    }

    public void set_certificate(String _certificate) {
        this._certificate = _certificate;
    }

    public String get_icon() {
        return _icon;
    }

    public void set_icon(String _icon) {
        this._icon = _icon;
    }
}