package com.example.tuim.station;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class StationRecord implements Serializable {
    @SerializedName("id_gas_station")
    private Long id;
    @SerializedName("gasStationName")
    private String name;
    @SerializedName("firstCo")
    private Double lan;
    @SerializedName("secondCo")
    private Double lat;
    @SerializedName("costOn")
    private Double costON;
    @SerializedName("costBenz")
    private Double costBenz;
    @SerializedName("costLpg")
    private Double costLPG;

    public StationRecord(Long id, String name, Double lan, Double lat, Double costON, Double costBenz, Double costLPG) {
        this.id = id;
        this.name = name;
        this.lan = lan;
        this.lat = lat;
        this.costON = costON;
        this.costBenz = costBenz;
        this.costLPG = costLPG;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getLan() {
        return lan;
    }

    public void setLan(Double lan) {
        this.lan = lan;
    }

    public Double getLat() {
        return lat;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }

    public Double getCostON() {
        return costON;
    }

    public void setCostON(Double costON) {
        this.costON = costON;
    }

    public Double getCostBenz() {
        return costBenz;
    }

    public void setCostBenz(Double costBenz) {
        this.costBenz = costBenz;
    }

    public Double getCostLPG() {
        return costLPG;
    }

    public void setCostLPG(Double costLPG) {
        this.costLPG = costLPG;
    }
}
