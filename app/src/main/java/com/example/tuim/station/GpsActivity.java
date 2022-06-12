package com.example.tuim.station;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tuim.ClientAPI;
import com.example.tuim.MainMenuActivity;
import com.example.tuim.R;
import com.example.tuim.RetrofitClient;
import com.example.tuim.user.UserData;

import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GpsActivity extends AppCompatActivity implements LocationListener {
    private static final Double MAX_DISTANCE = 5.0;
    private TextView bestSpeed;
    private TextView currentSpeed;
    private Button startButton;

    private Location loc;
    private LocationManager locationManager;
    private Date startTime;
    private boolean wasCounted;

    private ArrayList<StationRecord> stationList;

    private RecyclerView stationRecyclerView;
    private RecyclerView.Adapter stationAdapter;
    private RecyclerView.LayoutManager stationLayoutManager;

    private Double firstCoordinatePerson = 0.0;
    private Double secondCoordinatePerson = 0.0;

    private ArrayList<Double> distances;
    private ArrayList<StationRecord> newStationList;
    private UserData theUser;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.gps_layout);
        distances = new ArrayList<>();
        newStationList = new ArrayList<>();

        bestSpeed = findViewById(R.id.best_speed);
        currentSpeed = findViewById(R.id.current_speed);
        startButton = findViewById(R.id.start_measurement);

        theUser = (UserData) getIntent().getExtras().getSerializable(MainMenuActivity.USER_DATA);

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        stationRecyclerView = findViewById(R.id.station_recycler_view);
        setLocation();

        initStationList();

    }

    private void afterGetData() {
        initRecyclerView();
        startButton.setOnClickListener(v -> {
            startTime = new Date();
            wasCounted = false;
        });
    }

    private void setLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        try {

            boolean isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
            Log.v("isGPSEnabled", "=" + isGPSEnabled);
                if (isGPSEnabled) {
                    loc = null;
                    if (loc == null) {
                        locationManager.requestLocationUpdates(
                                LocationManager.GPS_PROVIDER, 500L, 2.0f, this);
                        Log.d("GPS Enabled", "GPS Enabled");
                        if (locationManager != null) {
                            loc = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                            if (loc != null) {
                                firstCoordinatePerson = loc.getLatitude();
                                Log.d("FCoo", String.valueOf(firstCoordinatePerson));
                                secondCoordinatePerson = loc.getLongitude();
                                Log.d("SCoo", String.valueOf(secondCoordinatePerson));
                            }
                        }
                    }
                }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initStationList() {
        stationList = new ArrayList<StationRecord>();
        ClientAPI clientAPI = RetrofitClient.getRetrofitClient().create(ClientAPI.class);
        Call<ArrayList<StationRecord>> call = clientAPI.getStation();
        call.enqueue(new Callback<ArrayList<StationRecord>>() {
            @Override
            public void onResponse(Call<ArrayList<StationRecord>> call, Response<ArrayList<StationRecord>> response) {
                stationList = response.body();
                afterGetData();
            }

            @Override
            public void onFailure(Call<ArrayList<StationRecord>> call, Throwable t) {
            }
        });
    }

    private void initRecyclerView() {
        stationLayoutManager = new LinearLayoutManager(this);
        stationRecyclerView.setLayoutManager(stationLayoutManager);
        stationRecyclerView.setHasFixedSize(true);
        for (int i = 0; i < stationList.size(); i++) {
            Double d = calculateDistance(stationList.get(i));
            if (d <= MAX_DISTANCE) {
                newStationList.add(stationList.get(i));
                distances.add(d);
            }
        }
        stationAdapter = new StationAdapter(this, newStationList,firstCoordinatePerson,secondCoordinatePerson,distances,theUser);
        stationRecyclerView.setAdapter(stationAdapter);
    }

    @SuppressLint("MissingPermission")
    @Override
    protected void onResume() {
        super.onResume();
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
    }

    @Override
    public void onLocationChanged(@NonNull Location location) {
        setLocation();
        float speed = location.getSpeed();
        float kmhspeed = speed * 3600 / 1000;
        currentSpeed.setText(getString(R.string.current_speed) + kmhspeed + getString(R.string.km_per_h));
        if (kmhspeed >= 100 && !wasCounted) {
            long diffinMs = new Date().getTime();
            long diffInS = TimeUnit.MILLISECONDS.toSeconds(diffinMs);
            bestSpeed.setText(getString(R.string.last_best) + diffInS + getString(R.string.seconds));
            wasCounted = true;
        }
    }


    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
    }

    public Double calculateDistance(StationRecord stationRecord) {
        Double distance;
        int earthRadius = 6371000;
        double firstCoordinateStation = stationRecord.getLan();
        double secondCoordinateStation = stationRecord.getLat();

        double dLat = Math.toRadians((firstCoordinatePerson - firstCoordinateStation));
        double dLong = Math.toRadians((secondCoordinatePerson - secondCoordinateStation));

        double startLat = Math.toRadians(firstCoordinateStation);
        double endLat = Math.toRadians(firstCoordinatePerson);

        double a = Math.pow(Math.sin(dLat / 2), 2) + Math.cos(startLat) * Math.cos(endLat) * Math.pow(Math.sin(dLong / 2), 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        double round = (int) Math.round(earthRadius * c);
        distance = (round / 1000);
        return distance;
    }
}
