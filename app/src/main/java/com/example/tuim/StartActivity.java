package com.example.tuim;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.tuim.user.LogInActivity;
import com.example.tuim.user.RegisteringActivity;
import com.example.tuim.user.UserData;

import java.util.ArrayList;

public class StartActivity extends AppCompatActivity {

    private static final int REQUEST_CODE_REGISTER = 12125;
    private static final int REQUEST_CODE_LOGIN = 12123;
    public static final String USER_DATA = "USER_DATA";
    public static ArrayList<UserData> userList;

    private Button goToRegisterButton;
    private Button goToLogInButton;
    private TextView isLoggedText;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.start_layout);
        initView();
    }

    private void initView() {
        goToRegisterButton = findViewById(R.id.registering);
        goToLogInButton = findViewById(R.id.logging);

        isLoggedText = findViewById(R.id.textViewIsLoggedInMenu);
        initUserList();

        goToRegisterButton.setOnClickListener(goToRegisterActivity());
        goToLogInButton.setOnClickListener(goToLogInActivity());
    }

    private View.OnClickListener goToRegisterActivity() {
        return v -> {
            Intent intent = new Intent(StartActivity.this, RegisteringActivity.class);
            startActivityForResult(intent, REQUEST_CODE_REGISTER);
        };
    }

    private View.OnClickListener goToLogInActivity() {
        return v -> {
            Intent intent = new Intent(StartActivity.this, LogInActivity.class);
            startActivityForResult(intent, REQUEST_CODE_LOGIN);
        };
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_REGISTER) {
            if (resultCode == Activity.RESULT_OK) {
                if (data != null) {
                    //dołączyc RegisterActivity
                    UserData newUserData = (UserData) data.getExtras().get(RegisteringActivity.NEW_USER_DATA);
                }
            }
        }
        if (requestCode == REQUEST_CODE_LOGIN) {
            if (resultCode == Activity.RESULT_OK) {
                if (data != null) {
                    Intent intent = new Intent(StartActivity.this, MainMenuActivity.class);
                    //dołączyc LoginActivity
                    UserData user = (UserData) data.getExtras().get(LogInActivity.USER);
                    intent.putExtra(USER_DATA, user);
                    startActivity(intent);
                }
            }
        }
    }

    private void initUserList() {
        userList = new ArrayList<UserData>();
    }
}
