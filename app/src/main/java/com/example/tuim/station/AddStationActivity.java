package com.example.tuim.station;

import android.content.res.ColorStateList;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.tuim.ClientAPI;
import com.example.tuim.R;
import com.example.tuim.RetrofitClient;
import com.google.android.material.textfield.TextInputLayout;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddStationActivity extends AppCompatActivity {

    private EditText nameEditText;
    private EditText lanEditText;
    private EditText latEditText;
    private EditText onEditText;
    private EditText benzEditText;
    private EditText lpgEditText;

    private TextInputLayout nameEditTextLabel;
    private TextInputLayout lanEditTextLabel;
    private TextInputLayout latEditTextLabel;
    private TextInputLayout onEditTextLabel;
    private TextInputLayout benzEditTextLabel;
    private TextInputLayout lpgEditTextLabel;

    private Button confirmButton;

    private StationRecord stationRecord;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_station_layout);
        viewInit();
        setTitle(getString(R.string.station_add_form));
    }

    private void viewInit() {
        nameEditText = findViewById(R.id.name_station_edittext);
        lanEditText = findViewById(R.id.lan_edittext);
        latEditText = findViewById(R.id.lat_edittext);
        onEditText = findViewById(R.id.on_edittext);
        benzEditText = findViewById(R.id.benz_edittext);
        lpgEditText = findViewById(R.id.lpg_edittext);

        nameEditTextLabel = findViewById(R.id.textView_name_station);
        lanEditTextLabel = findViewById(R.id.textView_lan_station);
        latEditTextLabel = findViewById(R.id.textView_add_lat);
        onEditTextLabel = findViewById(R.id.textView_add_on);
        benzEditTextLabel = findViewById(R.id.textView_add_benz);
        lpgEditTextLabel = findViewById(R.id.textView_add_lpg);

        confirmButton = findViewById(R.id.add_station_button);

        confirmButton.setOnClickListener(v -> {
            if (validateName() && validateLan() && validateLat() && validateOn() && validateBenz() && validateLpg()) {
                stationRecord = new StationRecord(null,nameEditText.getText().toString(),Double.valueOf(lanEditText.getText().toString()), Double.valueOf(latEditText.getText().toString()),
                        Double.valueOf(onEditText.getText().toString()), Double.valueOf(benzEditText.getText().toString()),Double.valueOf(lpgEditText.getText().toString()));
                addStation();
            }
        });

        nameEditText.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) {
                validateName();
            }
        });

        lanEditText.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) {
                validateLan();
            }
        });

        latEditText.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) {
                validateLat();
            }
        });

        onEditText.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) {
                validateOn();
            }
        });

        benzEditText.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) {
                validateBenz();
            }
        });
        lpgEditText.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) {
                validateLpg();
            }
        });
    }

    private void addStation() {
        ClientAPI clientAPI = RetrofitClient.getRetrofitClient().create(ClientAPI.class);
        Call<Void> call = clientAPI.addStation(stationRecord);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (!response.isSuccessful()) {
                    Toast.makeText(getApplicationContext(), R.string.error, Toast.LENGTH_SHORT).show();
                } else {
                    finish();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(getApplicationContext(), R.string.error_connection_with_server, Toast.LENGTH_SHORT).show();
            }
        });
    }


    private boolean validateName() {
        if (TextUtils.isEmpty(nameEditText.getText().toString())) {
            nameEditTextLabel.setError(getResources().getString(R.string.name_is_necessary));
            nameEditTextLabel.setErrorTextColor(ColorStateList.valueOf(getResources().getColor(R.color.red)));
            return false;
        } else if ((nameEditText.getText().toString().length()) <= 2) {
            nameEditTextLabel.setError(getResources().getString(R.string.name_must_more_than_one_sign));
            nameEditTextLabel.setErrorTextColor(ColorStateList.valueOf(getResources().getColor(R.color.red)));
            return false;
        } else {
            nameEditTextLabel.setError(null);
            nameEditTextLabel.setHint(getResources().getString(R.string.give_name));
            nameEditTextLabel.setHintTextColor(ColorStateList.valueOf(getResources().getColor(R.color.black)));
            return true;
        }
    }

    private boolean validateLan() {
        if (TextUtils.isEmpty(lanEditText.getText().toString())) {
            lanEditTextLabel.setHint(R.string.enter_lan);
            lanEditTextLabel.setHintTextColor(ColorStateList.valueOf(getResources().getColor(R.color.red)));
            return false;
        } else {
            lanEditTextLabel.setHint(getResources().getString(R.string.lan));
            lanEditTextLabel.setHintTextColor(ColorStateList.valueOf(getResources().getColor(R.color.black)));
            return true;
        }
    }

    private boolean validateLat() {
        if (TextUtils.isEmpty(latEditText.getText().toString())) {
            latEditTextLabel.setHint(R.string.enter_lat);
            latEditTextLabel.setHintTextColor(ColorStateList.valueOf(getResources().getColor(R.color.red)));
            return false;
        } else {
            latEditTextLabel.setHint(getResources().getString(R.string.lat));
            latEditTextLabel.setHintTextColor(ColorStateList.valueOf(getResources().getColor(R.color.black)));
            return true;
        }
    }

    private boolean validateOn() {
        if (TextUtils.isEmpty(onEditText.getText().toString())) {
            onEditTextLabel.setHint(R.string.enter_on);
            onEditTextLabel.setHintTextColor(ColorStateList.valueOf(getResources().getColor(R.color.red)));
            return false;
        } else if (Double.valueOf(onEditText.getText().toString()) <= 0) {
            onEditTextLabel.setHint(R.string.on_not_minus);
            onEditTextLabel.setHintTextColor(ColorStateList.valueOf(getResources().getColor(R.color.red)));
            return false;
        } else {
            onEditTextLabel.setHint(getResources().getString(R.string.new_price_on));
            onEditTextLabel.setHintTextColor(ColorStateList.valueOf(getResources().getColor(R.color.black)));
            return true;
        }
    }

    private boolean validateBenz() {
        if (TextUtils.isEmpty(benzEditText.getText().toString())) {
            benzEditTextLabel.setHint(R.string.enter_p95);
            benzEditTextLabel.setHintTextColor(ColorStateList.valueOf(getResources().getColor(R.color.red)));
            return false;
        } else if (Double.valueOf(benzEditText.getText().toString()) <= 0) {
            benzEditTextLabel.setHint(R.string.p95_not_minus);
            benzEditTextLabel.setHintTextColor(ColorStateList.valueOf(getResources().getColor(R.color.red)));
            return false;
        } else {
            benzEditTextLabel.setHint(getResources().getString(R.string.new_price_p95));
            benzEditTextLabel.setHintTextColor(ColorStateList.valueOf(getResources().getColor(R.color.black)));
            return true;
        }
    }
    private boolean validateLpg() {
       if (TextUtils.isEmpty(lpgEditText.getText().toString())) {
            lpgEditTextLabel.setHint(R.string.enter_lpg);
           lpgEditTextLabel.setHintTextColor(ColorStateList.valueOf(getResources().getColor(R.color.red)));
            return false;
        } else if (Double.valueOf(lpgEditText.getText().toString()) <= 0) {
           lpgEditTextLabel.setHint(R.string.lpg_not_minus);
           lpgEditTextLabel.setHintTextColor(ColorStateList.valueOf(getResources().getColor(R.color.red)));
            return false;
        } else {
           lpgEditTextLabel.setHint(getResources().getString(R.string.new_price_lpg));
           lpgEditTextLabel.setHintTextColor(ColorStateList.valueOf(getResources().getColor(R.color.black)));
            return true;
        }
    }
}
