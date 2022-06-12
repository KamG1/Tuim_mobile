package com.example.tuim.station;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.example.tuim.R;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapActivity extends AppCompatActivity implements LocationListener, OnMapReadyCallback{
    public static String MAPVIEV_BUNDLE_KEY = "MapViewBundleKey";

    private LocationManager locationManager;
    private MapView map;
    private GoogleMap googleMap;

    private Double firstCoordinatePerson;
    private Double secondCoordinatePerson;

    private StationRecord stationData;

    private MarkerOptions place1, place2;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.station_map_layout);
        map = findViewById(R.id.mapViewToStation);
        stationData = (StationRecord) getIntent().getExtras().getSerializable(StationAdapter.STATION_RECORD);
        firstCoordinatePerson = (Double) getIntent().getExtras().getSerializable(StationAdapter.FIRST_CO);
        secondCoordinatePerson = (Double) getIntent().getExtras().getSerializable(StationAdapter.SECOND_CO);
        place1 = new MarkerOptions().position(new LatLng(stationData.getLan(), stationData.getLat())).title(stationData.getName());
        place2 = new MarkerOptions().position(new LatLng(firstCoordinatePerson, secondCoordinatePerson)).title("MyPosition");
        Bundle mapViewBundle = null;
        if (savedInstanceState != null) {
            mapViewBundle = savedInstanceState.getBundle(MAPVIEV_BUNDLE_KEY);
        }
        map.onCreate(mapViewBundle);
        map.getMapAsync(this);
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 500L, 2.0f, this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        locationManager.removeUpdates(this);
    }


    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        Bundle mapViewBundle = outState.getBundle(MAPVIEV_BUNDLE_KEY);
        if (mapViewBundle == null) {
            mapViewBundle = new Bundle();
            outState.putBundle(MAPVIEV_BUNDLE_KEY, mapViewBundle);
        }
        map.onSaveInstanceState(mapViewBundle);
    }

    @Override
    public void onLocationChanged(@NonNull Location location) {

    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        googleMap.addMarker(place1);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        googleMap.setMyLocationEnabled(true);
        this.googleMap = googleMap;
    }

    @Override
    protected void onStart() {
        super.onStart();
        map.onStart();
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
    }
}
