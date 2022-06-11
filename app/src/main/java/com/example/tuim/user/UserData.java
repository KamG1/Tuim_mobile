package com.example.tuim.user;


import com.example.tuim.car.AutoData;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

public class UserData implements Serializable {
    @SerializedName("login")
    private String login;
    @SerializedName("password")
    private String password;
    @SerializedName("email")
    private String email;
    @SerializedName("phone_number")
    private String phone;
    private Integer points;
    private ArrayList<AutoData> autoList;

    public UserData(String login, String password, String email, String phone, Integer points) {
        this.login = login;
        this.password = password;
        this.email = email;
        this.phone = phone;
        this.points = points;
        this.autoList = new ArrayList<AutoData>();
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Integer getPoints() {
        return points;
    }

    public void setPoints(Integer points) {
        this.points = points;
    }

    public ArrayList<AutoData> getAutoList() {
        return autoList;
    }

    public ArrayList<AutoData> addAuto(AutoData auto) {
        autoList.add(auto);
        return autoList;
    }

    public ArrayList<AutoData> addFirstAuto(AutoData auto) {
        autoList.add(0, auto);
        return autoList;
    }

    public void setAutoArray(ArrayList<AutoData> autoArray) {
        autoList = autoArray;
    }
}