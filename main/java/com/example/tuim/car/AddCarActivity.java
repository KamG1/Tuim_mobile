package com.example.tuim.car;

import android.app.Activity;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.tuim.ClientAPI;
import com.example.tuim.MainMenuActivity;
import com.example.tuim.R;
import com.example.tuim.RetrofitClient;
import com.example.tuim.user.UserData;
import com.google.android.material.textfield.TextInputLayout;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddCarActivity extends AppCompatActivity {
    public static final String AUTO_DATA_NEW_CAR = "AUTO_DATA_NEW_CAR";
    public static final String IS_NEW_CAR_MASTER_CAR = "IS_NEW_CAR_MASTER_CAR";
    private UserData theUser;
    private EditText makeEditText;
    private EditText modelEditText;
    private EditText colorEditText;
    private TextInputLayout makeText;
    private TextInputLayout modelText;
    private TextInputLayout colorText;
    private Switch isMasterCarSwitch;
    private Button confirmButton;
    private AutoData autoData;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_car_layout);
        theUser = (UserData) getIntent().getExtras().getSerializable(MainMenuActivity.USER_DATA);

        makeText = findViewById(R.id.textView);
        modelText = findViewById(R.id.textView2);
        colorText = findViewById(R.id.textView3);

        makeEditText = findViewById(R.id.make_edittext);
        modelEditText = findViewById(R.id.model_edittext);
        colorEditText = findViewById(R.id.color_edittext);

        isMasterCarSwitch = findViewById(R.id.mater_car_switch);
        confirmButton = findViewById(R.id.add_car_button);
        confirmButton.setOnClickListener(v -> {
            Boolean isMasterCar = isMasterCarSwitch.isChecked();
            if (validate()) {
                autoData = new AutoData(null, makeEditText.getText().toString(), modelEditText.getText().toString(), colorEditText.getText().toString(), theUser.getLogin(), isMasterCar);
                ClientAPI clientAPI = RetrofitClient.getRetrofitClient().create(ClientAPI.class);
                Call<Void> call = clientAPI.addCar(autoData);
                call.enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        if (!response.isSuccessful()) {
                            Toast.makeText(getApplicationContext(), R.string.error, Toast.LENGTH_SHORT).show();
                        }
                        if (response.code() == 200) {
                            Intent intent = new Intent();
                            intent.putExtra(AUTO_DATA_NEW_CAR, autoData);
                            intent.putExtra(IS_NEW_CAR_MASTER_CAR, isMasterCar);
                            setResult(Activity.RESULT_OK, intent);
                            finish();
                        }
                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {
                        Toast.makeText(getApplicationContext(), R.string.error_connection_with_server, Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    private boolean validate() {
        if (TextUtils.isEmpty(makeEditText.getText().toString())) {
            makeText.setError(getResources().getString(R.string.make));
            makeText.setErrorTextColor(ColorStateList.valueOf(getResources().getColor(R.color.red)));
            return false;
        } else if (TextUtils.isEmpty(modelEditText.getText().toString())) {
            modelText.setError(getResources().getString(R.string.model));
            modelText.setErrorTextColor(ColorStateList.valueOf(getResources().getColor(R.color.red)));
            return false;
        } else if (TextUtils.isEmpty(colorEditText.getText().toString())) {
            colorText.setError(getResources().getString(R.string.color));
            colorText.setErrorTextColor(ColorStateList.valueOf(getResources().getColor(R.color.red)));
            return false;
        } else
            modelText.setErrorTextColor(ColorStateList.valueOf(getResources().getColor(R.color.black)));
        makeText.setErrorTextColor(ColorStateList.valueOf(getResources().getColor(R.color.black)));
        colorText.setErrorTextColor(ColorStateList.valueOf(getResources().getColor(R.color.black)));
        return true;
    }
}
