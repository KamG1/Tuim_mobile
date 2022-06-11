package com.example.tuim.tanks;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Build;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.example.tuim.ClientAPI;
import com.example.tuim.MainMenuActivity;
import com.example.tuim.R;
import com.example.tuim.RetrofitClient;
import com.example.tuim.car.AutoData;
import com.google.android.material.textfield.TextInputLayout;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TankUpActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener{
    public static final String NEW_TANKUP_RECORD = "NEW_TANK_UP_RECORD";
    private static final String AUTO_DATA_OBJ = "AUTO_DATA_OBJ";
    private EditText dateEditText;
    private EditText mileageEditText;
    private EditText litersEditText;
    private EditText costEditText;
    private AutoData autoData;
    private DateFormat dateFormat;
    private TextInputLayout mileageEditTextLabel;

    private Button confirmButton;
    private TextInputLayout litersEditTextLabel;
    private TextInputLayout costEditTextLabel;

    private TankUpRecord tank;
    private TankRecordToRequest tankRecordToRequest;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            autoData = (AutoData) savedInstanceState.get(AUTO_DATA_OBJ);
        }
        setContentView(R.layout.tank_up_layout);
        viewInit();
        autoData = (AutoData) getIntent().getExtras().getSerializable(MainMenuActivity.SPECIAL_DATA);
        if (savedInstanceState != null) {
            autoData = (AutoData) savedInstanceState.get(AUTO_DATA_OBJ);
        }
        setTitle(getResources().getString(R.string.newTankUp));
    }

    private void viewInit() {
        dateEditText = findViewById(R.id.data);
        mileageEditText = findViewById(R.id.mileage);
        mileageEditTextLabel = findViewById(R.id.mileage_label);
        litersEditText = findViewById(R.id.liters);
        litersEditTextLabel = findViewById(R.id.liters_label);
        costEditText = findViewById(R.id.cost);
        costEditTextLabel = findViewById(R.id.cost_label);

        dateEditText.setText(getCurrentDate());
        dateEditText.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int day = calendar.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog datePickerDialog = new DatePickerDialog(TankUpActivity.this, TankUpActivity.this, year, month, day);
                datePickerDialog.show();
            }
        });

        confirmButton = findViewById(R.id.confirm);
        confirmButton.setOnClickListener(v -> {
            if (validateMileage() && validateCost() && validateLiters()) {
                tank = new TankUpRecord(null, getDateEditTextDate(), getMillageInteger(), getLitersInt(), getCostInteger(), autoData.getId());
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS", Locale.GERMANY);
                tankRecordToRequest = new TankRecordToRequest(null, sdf.format(tank.getTankUpDate()), tank.getMillage(), tank.getLiters(), tank.getCostInPLN(), tank.getFkAuto());
                System.out.println("Data : " + tank.getTankUpDate());
                addTank();
            }
        });

        mileageEditText.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) {
                validateMileage();
            }
        });

        costEditText.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) {
                validateCost();
            }
        });

        litersEditText.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) {
                validateLiters();
            }
        });
    }

    private void addTank() {
        ClientAPI clientAPI = RetrofitClient.getRetrofitClient().create(ClientAPI.class);
        Call<Void> call = clientAPI.addTank(tankRecordToRequest);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (!response.isSuccessful()) {
                    Toast.makeText(getApplicationContext(), R.string.error, Toast.LENGTH_SHORT);
                }
                Intent intent = new Intent();
                intent.putExtra(NEW_TANKUP_RECORD, tank);
                setResult(Activity.RESULT_OK, intent);
                finish();
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(getApplicationContext(), R.string.error_connection_with_server, Toast.LENGTH_SHORT);
            }
        });
    }

    private boolean validateLiters() {
        if (TextUtils.isEmpty(litersEditText.getText().toString())) {
            litersEditTextLabel.setHint(R.string.warning_no_liters);
            litersEditTextLabel.setHintTextColor(ColorStateList.valueOf(getResources().getColor(R.color.red)));
            return false;
        } else if (Integer.valueOf(litersEditText.getText().toString()) <= 0) {
            litersEditTextLabel.setHint(R.string.warning_liters_minus);
            litersEditTextLabel.setHintTextColor(ColorStateList.valueOf(getResources().getColor(R.color.red)));
            return false;
        } else {
            litersEditTextLabel.setHint(getResources().getString(R.string.Amount_of_liters));
            litersEditTextLabel.setHintTextColor(ColorStateList.valueOf(getResources().getColor(R.color.black)));
            return true;
        }
    }

    private boolean validateCost() {
        if (TextUtils.isEmpty(costEditText.getText().toString())) {
            costEditTextLabel.setHint(R.string.warning_no_cost);
            costEditTextLabel.setHintTextColor(ColorStateList.valueOf(getResources().getColor(R.color.red)));
            return false;
        } else if (Integer.valueOf(costEditText.getText().toString()) <= 0) {
            costEditTextLabel.setHint(R.string.warning_cost_minus);
            costEditTextLabel.setHintTextColor(ColorStateList.valueOf(getResources().getColor(R.color.red)));
            return false;
        } else {
            costEditTextLabel.setHint(getResources().getString(R.string.Fuel_cost));
            costEditTextLabel.setHintTextColor(ColorStateList.valueOf(getResources().getColor(R.color.black)));
            return true;
        }

    }

    private boolean validateMileage() {
        if (TextUtils.isEmpty(mileageEditText.getText().toString())) {
            mileageEditTextLabel.setHint(R.string.warning_no_millage);
            mileageEditTextLabel.setHintTextColor(ColorStateList.valueOf(getResources().getColor(R.color.red)));
            return false;
        } else if (Integer.valueOf(mileageEditText.getText().toString()) <= 0) {
            mileageEditTextLabel.setHint(R.string.warning_millage_zero);
            mileageEditTextLabel.setHintTextColor(ColorStateList.valueOf(getResources().getColor(R.color.red)));
            return false;
        } else {
            mileageEditTextLabel.setHint(getResources().getString(R.string.przebieg));
            mileageEditTextLabel.setHintTextColor(ColorStateList.valueOf(getResources().getColor(R.color.black)));
            int size = autoData.getTankUpRecord().size();
            if (autoData.getTankUpRecord().size() != 0) {
                Double newMileage = Double.valueOf(mileageEditText.getText().toString());
                Double oldMileage = autoData.getTankUpRecord().get((size - 1)).getMillage();
                if (newMileage <= oldMileage) {
                    mileageEditTextLabel.setHint(R.string.warning_millage_too_small);
                    mileageEditTextLabel.setHintTextColor(ColorStateList.valueOf(getResources().getColor(R.color.red)));
                    return false;
                } else {
                    mileageEditTextLabel.setHint(getResources().getString(R.string.przebieg));
                    mileageEditTextLabel.setHintTextColor(ColorStateList.valueOf(getResources().getColor(R.color.black)));
                    return true;
                }
            }
            return true;
        }
    }


    private Date getDateEditTextDate() {
        try {
            dateFormat = DateFormat.getDateInstance();
            return dateFormat.parse(dateEditText.getText().toString());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        dateFormat = DateFormat.getDateInstance();
        Date date = new Date();
        return date;
    }

    private Double getCostInteger() {
        return Double.valueOf(costEditText.getText().toString());
    }

    private Double getLitersInt() {
        return Double.valueOf(litersEditText.getText().toString());
    }

    private Double getMillageInteger() {
        return Double.valueOf(mileageEditText.getText().toString());
    }

    private String getCurrentDate() {
        dateFormat = DateFormat.getDateInstance();
        Date date = new Date();
        return dateFormat.format(date);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState, @NonNull PersistableBundle outPersistentState) {
        outState.putSerializable(AUTO_DATA_OBJ, autoData);
        super.onSaveInstanceState(outState, outPersistentState);
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        Calendar calendar = new GregorianCalendar(year, month, dayOfMonth);
        dateEditText.setText(dateFormat.format(calendar.getTime()));
    }
}
