package com.example.tuim;


import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tuim.car.AddCarActivity;
import com.example.tuim.car.AutoData;
import com.example.tuim.station.AddStationActivity;
import com.example.tuim.station.EditStationActivity;
import com.example.tuim.station.GpsActivity;
import com.example.tuim.tanks.TankHistoryAdapter;
import com.example.tuim.tanks.TankUpActivity;
import com.example.tuim.tanks.TankUpRecord;
import com.example.tuim.user.UserData;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;

public class MainMenuActivity  extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{
    public static final String SPECIAL_DATA = "SPECIAL_DATA";
    public static final int REQUEST_CODE = 12345;
    private static final int TANK_REQUEST_CODE = 17788;
    public static final String USER_DATA = "USER_DATA";
    public static String login;

    private Spinner autoChooseSpinner;
    private ArrayAdapter<AutoData> arrayAdapter;

    private TextView isLoggedText;
    private RecyclerView historyRecyclerView;
    private RecyclerView.Adapter historyAdapter;
    private RecyclerView.LayoutManager historyLayoutManager;
    private UserData theUser;
    private TextView pointsText;

    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private Toolbar toolbar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_menu_layout);

        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        toolbar = findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);

        navigationView.bringToFront();
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.nav_open, R.string.nav_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);

        initView();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        return true;
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (getIntent().getExtras().getSerializable(EditStationActivity.USER) != null) {
            theUser = (UserData) getIntent().getExtras().getSerializable(EditStationActivity.USER);
            pointsText.setText(getString(R.string.amount_of_your_points) + theUser.getPoints());
        }
    }

    private void initView() {
        isLoggedText = findViewById(R.id.textViewIsLoggedInMenu);
        pointsText = findViewById(R.id.textViewYourPoints);

        autoChooseSpinner = findViewById(R.id.auto_choose_spinner);
        historyRecyclerView = findViewById(R.id.history_recycler_view);

        theUser = (UserData) getIntent().getExtras().getSerializable(StartActivity.USER_DATA);
        if (getIntent().getExtras().getSerializable(EditStationActivity.USER) != null) {
            theUser = (UserData) getIntent().getExtras().getSerializable(EditStationActivity.USER);
            pointsText.setText(getString(R.string.amount_of_your_points) + theUser.getPoints());
        }
        login = theUser.getLogin();
        isLoggedText.setText(getString(R.string.Hello) + " " + login + getString(R.string.have_fun));
        pointsText.setText(getString(R.string.amount_of_your_points)+" " + theUser.getPoints());

        initAutoList();
        initArrayAdapter();

        autoChooseSpinner.setAdapter(arrayAdapter);
        initRecyclerView();

        autoChooseSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                initRecyclerView();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        if (theUser.getAutoList().isEmpty()) {
            Intent intent = new Intent(MainMenuActivity.this, AddCarActivity.class);
            intent.putExtra(USER_DATA, theUser);
            startActivityForResult(intent, REQUEST_CODE);
        }
    }


    private void initRecyclerView() {
        historyLayoutManager = new LinearLayoutManager(this);
        historyRecyclerView.setLayoutManager(historyLayoutManager);
        historyRecyclerView.setHasFixedSize(true);
        historyAdapter = new TankHistoryAdapter(this, getCurrentAuto() != null ? getCurrentAuto().getTankUpRecord() : new ArrayList<TankUpRecord>());
        historyRecyclerView.setAdapter(historyAdapter);
    }

    private void goToNewCarActivity() {
        Intent intent = new Intent(MainMenuActivity.this, AddCarActivity.class);
        intent.putExtra(USER_DATA, theUser);
        startActivityForResult(intent, REQUEST_CODE);
    }

    private void goToGpsActivity() {
        requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1234);
        Intent intent = new Intent(MainMenuActivity.this, GpsActivity.class);
        intent.putExtra(USER_DATA, theUser);
        startActivity(intent);

    }
    private void goToNewStationActivity() {
        Intent intent = new Intent(MainMenuActivity.this, AddStationActivity.class);
        startActivity(intent);

    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            Intent intent = new Intent(this, GpsActivity.class);
            intent.putExtra(USER_DATA, theUser);
            startActivity(intent);
        }
    }


    private void goToTankUpActivity() {
        Intent intent = new Intent(MainMenuActivity.this, TankUpActivity.class);
        intent.putExtra(SPECIAL_DATA, getCurrentAuto());
        startActivityForResult(intent, TANK_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                if (data != null) {
                    AutoData newAutoData = (AutoData) data.getExtras().get(AddCarActivity.AUTO_DATA_NEW_CAR);
                    Boolean isNewCarMasterCar = (Boolean) data.getExtras().get(AddCarActivity.IS_NEW_CAR_MASTER_CAR);
                    if (isNewCarMasterCar != null && isNewCarMasterCar) {
                        theUser.addFirstAuto(newAutoData);
                        autoChooseSpinner.setAdapter(arrayAdapter);
                        autoChooseSpinner.setSelection(0, false);
                    } else {
                        theUser.addAuto(newAutoData);
                        autoChooseSpinner.setAdapter(arrayAdapter);
                        autoChooseSpinner.setSelection(theUser.getAutoList().size() - 1, false);
                    }
                }
            }
        }
        if (requestCode == TANK_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                if (data != null) {
                    TankUpRecord newTankUpRecord = (TankUpRecord) data.getExtras().get(TankUpActivity.NEW_TANKUP_RECORD);
                    if (newTankUpRecord != null) {
                        getCurrentAuto().getTankUpRecord().add(0, newTankUpRecord);
                        historyAdapter.notifyDataSetChanged();
                    }
                }
            }
        }
    }


    private void initArrayAdapter() {
        arrayAdapter = new ArrayAdapter<AutoData>(this, android.R.layout.simple_spinner_item, theUser.getAutoList());
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    }

    private void initAutoList() {
        if (theUser.getAutoList() == null) {
            theUser.setAutoArray(new ArrayList<AutoData>());
        }
    }

    private AutoData getCurrentAuto() {
        return (AutoData) autoChooseSpinner.getSelectedItem();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.go_to_gps:
                goToGpsActivity();
                break;
            case R.id.go_to_tank_form:
                goToTankUpActivity();
                break;
            case R.id.new_car:
                goToNewCarActivity();
                break;
            case R.id.go_to_repair:
                goToNewStationActivity();
                break;
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }
}
