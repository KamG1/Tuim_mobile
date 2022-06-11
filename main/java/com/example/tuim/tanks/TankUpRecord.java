package com.example.tuim.tanks;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.Date;

public class TankUpRecord implements Serializable {
    private Long idTankRecords;
    @SerializedName("date")
    private Date tankUpDate;
    private Double millage;
    private Double liters;
    @SerializedName("costInPln")
    private Double costInPLN;
    @SerializedName("fkauto")
    private Long fkAuto;

    public TankUpRecord(Long idTankRecords, Date tankUpDate, Double millage, Double liters, Double costInPLN, Long fkAuto) {
        this.idTankRecords = idTankRecords;
        this.tankUpDate = tankUpDate;
        this.millage = millage;
        this.liters = liters;
        this.costInPLN = costInPLN;
        this.fkAuto = fkAuto;
    }

    public Long getIdTankRecords() {
        return idTankRecords;
    }

    public void setIdTankRecords(Long idTankRecords) {
        this.idTankRecords = idTankRecords;
    }

    public Date getTankUpDate() {
        return tankUpDate;
    }

    public void setTankUpDate(Date tankUpDate) {
        this.tankUpDate = tankUpDate;
    }

    public Double getMillage() {
        return millage;
    }

    public void setMillage(Double millage) {
        this.millage = millage;
    }

    public Double getLiters() {
        return liters;
    }

    public void setLiters(Double liters) {
        this.liters = liters;
    }

    public Double getCostInPLN() {
        return costInPLN;
    }

    public void setCostInPLN(Double costInPLN) {
        this.costInPLN = costInPLN;
    }

    public Long getFkAuto() {
        return fkAuto;
    }

    public void setFkAuto(Long fkAuto) {
        this.fkAuto = fkAuto;
    }
}
