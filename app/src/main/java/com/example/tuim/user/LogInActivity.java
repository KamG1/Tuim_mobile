package com.example.tuim.user;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.tuim.ClientAPI;
import com.example.tuim.R;
import com.example.tuim.RetrofitClient;
import com.example.tuim.car.AutoData;

import java.net.URLEncoder;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LogInActivity extends AppCompatActivity {
    public static final String USER = "User";
    private EditText loginEditText;
    private EditText passwordEditText;
    private Button confirmButton;
    private TextView logInAndConfirmLabel;
    private UserData theUser;
    private ArrayList<UserData> userList;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_layout);
        userList = new ArrayList<>();
        viewInit();
        setTitle(getString(R.string.log_in_form));
    }

    private void viewInit() {
        logInAndConfirmLabel = findViewById(R.id.textViewLoggingGood);
        loginEditText = findViewById(R.id.loginInLoggingEditText);
        passwordEditText = findViewById(R.id.passwordInLoggingEditText);
        confirmButton = findViewById(R.id.confirm_logging_button);
        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validateLogIn()) {
                    if (loginExists()) {
                        Intent intent = new Intent();
                        intent.putExtra(USER, theUser);
                        setResult(Activity.RESULT_OK, intent);
                        finish();
                    }
                }
            }
        });
    }

    private boolean validateLogIn() {
        if (TextUtils.isEmpty(loginEditText.getText().toString())) {
            logInAndConfirmLabel.setText(R.string.enter_login);
            logInAndConfirmLabel.setTextColor(getResources().getColor(R.color.red));
            return false;
        } else if (TextUtils.isEmpty(passwordEditText.getText().toString())) {
            logInAndConfirmLabel.setText(R.string.enter_password);
            logInAndConfirmLabel.setTextColor(getResources().getColor(R.color.red));
            return false;
        } else {
            logInAndConfirmLabel.setText(R.string.enter_login);
            logInAndConfirmLabel.setTextColor(getResources().getColor(R.color.black));
            return true;
        }
    }

    private boolean loginExists() {
        ClientAPI clientAPI = RetrofitClient.getRetrofitClient().create(ClientAPI.class);
        Call<UserData> call = clientAPI.getUserByLogin(URLEncoder.encode(loginEditText.getText().toString()), URLEncoder.encode(passwordEditText.getText().toString()));
        call.enqueue(new Callback<UserData>() {
            @Override
            public void onResponse(Call<UserData> call, Response<UserData> response) {
                theUser = response.body();
                if (theUser == null) {
                    logInAndConfirmLabel.setText(R.string.login_or_passworrd_not_correct);
                    logInAndConfirmLabel.setTextColor(getResources().getColor(R.color.red));
                } else {
                    Intent intent = new Intent();
                    setMainAutoFirst();
                    intent.putExtra(USER, theUser);
                    setResult(Activity.RESULT_OK, intent);
                    finish();
                }
            }

            @Override
            public void onFailure(Call<UserData> call, Throwable t) {
                Toast.makeText(getApplicationContext(), R.string.error_connection_with_server, Toast.LENGTH_SHORT).show();
            }
        });
        return theUser != null && passwordEditText.getText().toString().equals(theUser.getPassword());
    }

    private void setMainAutoFirst() {
        AutoData auto = null;
        for (AutoData i : theUser.getAutoList()) {
            if (i.isIsmain()) {
                auto = i;
            }
        }
        if (auto != null) {
            theUser.getAutoList().remove(auto);
            theUser.getAutoList().add(0, auto);
        }
    }
}
