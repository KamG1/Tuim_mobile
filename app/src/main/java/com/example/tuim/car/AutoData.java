package com.example.tuim.car;

import androidx.annotation.NonNull;

import com.example.tuim.tanks.TankUpRecord;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

public class AutoData implements Serializable {
    @SerializedName("idcar")
    private Long id;
    private String model;
    private String make;
    @SerializedName("color")
    private String color;
    @SerializedName("fklogin")
    private String fkLogin;
    @SerializedName("ismain")
    private boolean ismain;
    @SerializedName("tankrecords")
    private ArrayList<TankUpRecord> tankUpRecord;

    public AutoData(Long id, String make, String model, String color, String fkLogin, boolean ismain) {
        this.id = id;
        this.model = model;
        this.make = make;
        this.color = color;
        this.fkLogin = fkLogin;
        this.tankUpRecord = new ArrayList<>();
        this.ismain = ismain;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getModel() {
        return model;
    }

    public String getMake() {
        return make;
    }

    public String getColor() {
        return color;
    }

    public String getFkLogin() {
        return fkLogin;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public void setMake(String make) {
        this.make = make;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public void setFkLogin(String fkLogin) {
        this.fkLogin = fkLogin;
    }

    public ArrayList<TankUpRecord> getTankUpRecord() {
        return tankUpRecord;
    }

    public void setTankUpRecord(ArrayList<TankUpRecord> tankUpRecord) {
        this.tankUpRecord = tankUpRecord;
    }

    @NonNull
    @Override
    public String toString() {
        return make + " " + model + " " + color;
    }

    public boolean isIsmain() {
        return ismain;
    }

    public void setIsmain(boolean ismain) {
        this.ismain = ismain;
    }
}
