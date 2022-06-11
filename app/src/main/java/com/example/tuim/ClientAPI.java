package com.example.tuim;

import com.example.tuim.car.AutoData;
import com.example.tuim.station.StationRecord;
import com.example.tuim.tanks.TankRecordToRequest;
import com.example.tuim.user.UserData;


import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface ClientAPI {
    @GET("/getUserByLogin/{login}&{password}")
    Call<UserData> getUserByLogin(@Path("login") String login, @Path("password") String password);

    @POST("/addUser")
    Call<Void> addUser(@Body UserData user);

    @POST("/addCar")
    Call<Void> addCar(@Body AutoData auto);

    @POST("/deleteCar")
    Call<Void> deleteCar(@Body AutoData auto);

    @POST("/addTank")
    Call<Void> addTank(@Body TankRecordToRequest tank);

    @POST("/deleteTank")
    Call<Void> deleteTank(@Body TankRecordToRequest tank);

    @GET("/getStation")
    Call<ArrayList<StationRecord>> getStation();

    @PUT("/alterGasStation/")
    Call<Void> alterStation(@Body StationRecord station);

    @POST("/addStation")
    Call<Void> addStation(@Body StationRecord station);

    @POST("/deleteStation")
    Call<Void> deleteStation(@Body StationRecord station);

    @PUT("/addPoints/")
    Call<Void> addPoints(@Body UserData user);
}
