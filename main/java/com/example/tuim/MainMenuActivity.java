package com.example.tuim;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
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
import com.example.tuim.tanks.TankUpRecord;
import com.example.tuim.user.UserData;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;

public class MainMenuActivity  extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{
    public static final String USER_DATA = "USER_DATA";
    public static final String SPECIAL_DATA = "SPECIAL_DATA";
    public static String login;
    public static final int REQUEST_CODE = 12345;

    private TextView isLoggedText;
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

        initView();
    }

    private void initView() {
        isLoggedText = findViewById(R.id.textViewIsLoggedInMenu);
        pointsText = findViewById(R.id.textViewYourPoints);

        theUser = (UserData) getIntent().getExtras().getSerializable(StartActivity.USER_DATA);
        login = theUser.getLogin();
        isLoggedText.setText(getString(R.string.Hello) + " " + login + getString(R.string.have_fun));
        pointsText.setText(getString(R.string.amount_of_your_points)+" " + theUser.getPoints());

        if (theUser.getAutoList().isEmpty()) {
            Intent intent = new Intent(MainMenuActivity.this, AddCarActivity.class);
            intent.putExtra(USER_DATA, theUser);
            startActivityForResult(intent, REQUEST_CODE);
        }
    }


    private void goToNewCarActivity() {
        Intent intent = new Intent(MainMenuActivity.this, AddCarActivity.class);
        intent.putExtra(USER_DATA, theUser);
        startActivityForResult(intent, REQUEST_CODE);
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
                    } else {
                        theUser.addAuto(newAutoData);
                    }
                }
            }
        }


    }
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.go_to_gps:
                //mapa
                break;
            case R.id.go_to_tank_form:
                //tankowanie
                break;
            case R.id.new_car:
                goToNewCarActivity();
                break;
            case R.id.go_to_repair:
                //ustawienia
                break;
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }
}
