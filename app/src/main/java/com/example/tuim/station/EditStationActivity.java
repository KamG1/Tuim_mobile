package com.example.tuim.station;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.tuim.ClientAPI;
import com.example.tuim.MainMenuActivity;
import com.example.tuim.R;
import com.example.tuim.RetrofitClient;
import com.example.tuim.user.UserData;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.messaging.FirebaseMessaging;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditStationActivity extends AppCompatActivity {
    private static final Integer ADDING_POINTS = 10;
    public static final Integer NOTIFICATION_ID = 1;
    public static final String USER = "USER";
    public static final String CHANNEL_ID = "CHANNEL_ID";
    public static final String CHANNEL_NAME = "CHANNEL_NAME";

    private TextView nameStationTextView;
    private TextInputLayout p95TextView;
    private EditText p95EditText;
    private TextInputLayout oNTextView;
    private EditText oNEditText;
    private TextInputLayout lPGTextView;
    private EditText lPGEditText;
    private Button confirmButton;
    private StationRecord record;
    private UserData user;
    private StationRecord stationData;
    private String token;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(task -> {
                    if (!task.isSuccessful()) {
                        Log.w("TAG", "Fetching FCM registration token failed", task.getException());
                        return;
                    }
                    token = task.getResult();
                    Log.d("TAG", "token: " + token);
                });
        stationData = (StationRecord) getIntent().getExtras().getSerializable(StationAdapter.STATION_RECORD);
        user = (UserData) getIntent().getExtras().getSerializable(StationAdapter.USER);
        setContentView(R.layout.edit_station_layout);
        initView();
    }

    private void initView() {
        nameStationTextView = findViewById(R.id.editFuelcostsTextView);
        p95TextView = findViewById(R.id.p95TextView);
        p95EditText = findViewById(R.id.p95EditText);
        oNTextView = findViewById(R.id.oNTextView);
        oNEditText = findViewById(R.id.oNEditText);
        lPGTextView = findViewById(R.id.lPGTextView);
        lPGEditText = findViewById(R.id.lPGEditText);
        confirmButton = findViewById(R.id.change_fuel_costs_button);
        nameStationTextView.setText(stationData.getName());
        p95EditText.setText(stationData.getCostBenz().toString());
        oNEditText.setText(stationData.getCostON().toString());
        lPGEditText.setText(stationData.getCostLPG().toString());


        confirmButton.setOnClickListener(v -> {
            if (validateP95() && validateON() && validateLPG()) {
                record = new StationRecord(stationData.getId(), stationData.getName(), stationData.getLan(), stationData.getLat(),
                        getON(), getP95(), getLPG());
                alterStation();

            }
        });

        p95EditText.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) {
                validateP95();
            }
        });

        oNEditText.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) {
                validateON();
            }
        });

        lPGEditText.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) {
                validateLPG();
            }
        });
    }

    private void alterStation() {
        ClientAPI clientAPI = RetrofitClient.getRetrofitClient().create(ClientAPI.class);
        Call<Void> call = clientAPI.alterStation(record);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (!response.isSuccessful()) {
                    Toast.makeText(getApplicationContext(), R.string.error, Toast.LENGTH_SHORT).show();
                }
                user.setPoints(user.getPoints() + ADDING_POINTS);
                addPoints();
                Intent intent = new Intent(EditStationActivity.this, MainMenuActivity.class);
                intent.putExtra(USER, user);
                startActivity(intent);
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(getApplicationContext(), R.string.error_connection_with_server, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void addPoints() {
        ClientAPI clientAPI = RetrofitClient.getRetrofitClient().create(ClientAPI.class);
        Call<Void> call = clientAPI.addPoints(user);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (!response.isSuccessful()) {
                    Toast.makeText(getApplicationContext(), R.string.error, Toast.LENGTH_SHORT).show();
                }
                notifying();
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(getApplicationContext(), R.string.error_connection_with_server, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private boolean validateP95() {
        if (TextUtils.isEmpty(p95EditText.getText().toString())) {
            p95TextView.setHint(R.string.enter_p95);
            p95TextView.setHintTextColor(ColorStateList.valueOf(getResources().getColor(R.color.red)));
            return false;
        } else if (Double.valueOf(p95EditText.getText().toString()) <= 0) {
            p95TextView.setHint(R.string.p95_not_minus);
            p95TextView.setHintTextColor(ColorStateList.valueOf(getResources().getColor(R.color.red)));
            return false;
        } else {
            p95TextView.setHint(getResources().getString(R.string.new_price_p95));
            p95TextView.setHintTextColor(ColorStateList.valueOf(getResources().getColor(R.color.black)));
            return true;
        }
    }

    private boolean validateON() {
        if (TextUtils.isEmpty(oNEditText.getText().toString())) {
            oNTextView.setHint(R.string.enter_on);
            oNTextView.setHintTextColor(ColorStateList.valueOf(getResources().getColor(R.color.red)));
            return false;
        } else if (Double.valueOf(oNEditText.getText().toString()) <= 0) {
            oNTextView.setHint(R.string.on_not_minus);
            oNTextView.setHintTextColor(ColorStateList.valueOf(getResources().getColor(R.color.red)));
            return false;
        } else {
            oNTextView.setHint(getResources().getString(R.string.new_price_on));
            oNTextView.setHintTextColor(ColorStateList.valueOf(getResources().getColor(R.color.black)));
            return true;
        }
    }

    private boolean validateLPG() {
        if (TextUtils.isEmpty(lPGEditText.getText().toString())) {
            lPGTextView.setHint(R.string.enter_lpg);
            lPGTextView.setHintTextColor(ColorStateList.valueOf(getResources().getColor(R.color.red)));
            return false;
        } else if (Double.valueOf(lPGEditText.getText().toString()) <= 0) {
            lPGTextView.setHint(R.string.lpg_not_minus);
            lPGTextView.setHintTextColor(ColorStateList.valueOf(getResources().getColor(R.color.red)));
            return false;
        } else {
            lPGTextView.setHint(getResources().getString(R.string.new_price_lpg));
            lPGTextView.setHintTextColor(ColorStateList.valueOf(getResources().getColor(R.color.black)));
            return true;
        }
    }

    private Double getP95() {
        return Double.valueOf(p95EditText.getText().toString());
    }

    private Double getON() {
        return Double.valueOf(oNEditText.getText().toString());
    }

    private Double getLPG() {
        return Double.valueOf(lPGEditText.getText().toString());
    }

    public void notifying() {
        createNotificationChannel();
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(getString(R.string.app_name))
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.wheel))
                .setContentText(getString(R.string.you_get_ten_points))
                .setAutoCancel(true)
                .setColor(0xffff7700)
                .setVibrate(new long[]{100, 100, 100, 100})
                .setPriority(Notification.PRIORITY_MAX);
        Notification notification = notificationBuilder.build();
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        notificationManager.notify(NOTIFICATION_ID, notification);
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, CHANNEL_NAME,
                    NotificationManager.IMPORTANCE_DEFAULT);
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }
}
