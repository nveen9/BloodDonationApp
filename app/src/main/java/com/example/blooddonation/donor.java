package com.example.blooddonation;

public class donor {

    private String user_id,blood_type,locality,number,name;
    private double points;

    public donor() {
    }

    public donor(String user_id, String blood_type, String locality, String number, String name, double points) {
        this.user_id = user_id;
        this.blood_type = blood_type;
        this.locality = locality;
        this.number = number;
        this.name = name;
        this.points= points;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getBlood_type() {
        return blood_type;
    }

    public void setBlood_type(String blood_type) {
        this.blood_type = blood_type;
    }

    public String getLocality() {
        return locality;
    }

    public void setLocality(String locality) {
        this.locality = locality;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPoints() {
        return points;
    }

    public void setPoints(double points) {
        this.points = points;
    }
}
