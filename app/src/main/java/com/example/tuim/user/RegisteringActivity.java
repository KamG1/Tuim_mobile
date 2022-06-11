package com.example.tuim.user;

import android.app.Activity;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.tuim.ClientAPI;
import com.example.tuim.R;

import com.example.tuim.RetrofitClient;
import com.google.android.material.textfield.TextInputLayout;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisteringActivity extends AppCompatActivity {
    public static final String NEW_USER_DATA = "NEW_USER_DATA";

    private EditText loginEditText;
    private EditText passwordEditText;
    private EditText repeatPasswordEditText;
    private EditText emailEditText;
    private EditText phoneEditText;

    private TextInputLayout loginEditTextLabel;
    private TextInputLayout passwordEditTextLabel;
    private TextInputLayout repeatPasswordEditTextLabel;
    private TextInputLayout emailEditTextLabel;
    private TextInputLayout phoneEditTextLabel;

    private Button confirmButton;
    private CheckBox regulationCheckBox;
    private ProgressBar progressBar;

    private UserData userData;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_layout);
        viewInit();
        setTitle(getString(R.string.register_form));
    }

    private void viewInit() {
        loginEditText = findViewById(R.id.editTextLogin);
        passwordEditText = findViewById(R.id.editTextPassword);
        repeatPasswordEditText = findViewById(R.id.editTextRepeatPassword);
        emailEditText = findViewById(R.id.editTextEmail);
        phoneEditText = findViewById(R.id.editTextPhone);

        loginEditTextLabel = findViewById(R.id.textViewLogin);
        passwordEditTextLabel = findViewById(R.id.textViewPassword);
        repeatPasswordEditTextLabel = findViewById(R.id.textViewRepeatPassword);
        emailEditTextLabel = findViewById(R.id.textViewEmail);
        phoneEditTextLabel = findViewById(R.id.textViewPhone);

        progressBar = findViewById(R.id.progressBarRegister);
        progressBar.setVisibility(View.GONE);

        regulationCheckBox = findViewById(R.id.checkBoxRegulations);

        confirmButton = findViewById(R.id.buttonRegister);

        confirmButton.setOnClickListener(v -> {
            if (validateLogin() && validatePassword() && validateRepeatPassword() && validateEmail() && validatePhone() && validateCheckBox()) {
                userData = new UserData(loginEditText.getText().toString(), passwordEditText.getText().toString(), emailEditText.getText().toString(),
                        phoneEditText.getText().toString(), 0);
                addUser();
            }
        });

        loginEditText.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) {
                validateLogin();
            }
        });

        passwordEditText.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) {
                validatePassword();
            }
        });

        repeatPasswordEditText.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) {
                validateRepeatPassword();
            }
        });

        emailEditText.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) {
                validateEmail();
            }
        });

        phoneEditText.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) {
                validatePhone();
            }
        });
        regulationCheckBox.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) {
                validateCheckBox();
            }
        });
    }

    private void addUser() {
        progressBar.setVisibility(View.VISIBLE);
        ClientAPI clientAPI = RetrofitClient.getRetrofitClient().create(ClientAPI.class);
        Call<Void> call = clientAPI.addUser(userData);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                progressBar.setVisibility(View.GONE);
                if (!response.isSuccessful()) {
                    Toast.makeText(getApplicationContext(), R.string.error, Toast.LENGTH_SHORT).show();
                } else {
                    Intent intent = new Intent();
                    intent.putExtra(NEW_USER_DATA, userData);
                    setResult(Activity.RESULT_OK, intent);
                    finish();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(getApplicationContext(), R.string.error_connection_with_server, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private boolean validateCheckBox() {
        if (!regulationCheckBox.isChecked()) {
            regulationCheckBox.setText(R.string.you_have_to_accept_regulations);
            regulationCheckBox.setTextColor(getResources().getColor(R.color.red));
            return false;
        } else {
            regulationCheckBox.setText(getResources().getString(R.string.checkboxRegulations));
            regulationCheckBox.setTextColor(getResources().getColor(R.color.black));
            return true;
        }
    }

    private boolean validatePhone() {
        if (TextUtils.isEmpty(phoneEditText.getText().toString())) {
            phoneEditTextLabel.setError(getResources().getString(R.string.phone_number_is_necessary));
            phoneEditTextLabel.setErrorTextColor(ColorStateList.valueOf(getResources().getColor(R.color.red)));
            return false;
        } else if (!(phoneEditText.getText().toString().matches("[0-9][0-9][0-9][0-9][0-9][0-9][0-9][0-9][0-9]"))
                || (phoneEditText.getText().toString().length()) != 9) {
            phoneEditTextLabel.setError(getResources().getString(R.string.phone_number_only_nine_numbers));
            phoneEditTextLabel.setErrorTextColor(ColorStateList.valueOf(getResources().getColor(R.color.red)));
            return false;
        } else {
            phoneEditTextLabel.setError(null);
            phoneEditTextLabel.setHint(getResources().getString(R.string.give_phone_number));
            phoneEditTextLabel.setHintTextColor(ColorStateList.valueOf(getResources().getColor(R.color.black)));
            return true;
        }
    }

    private boolean validateEmail() {
        if (TextUtils.isEmpty(emailEditText.getText().toString())) {
            emailEditTextLabel.setError(getResources().getString(R.string.email_is_necessary));
            emailEditTextLabel.setErrorTextColor(ColorStateList.valueOf(getResources().getColor(R.color.red)));
            return false;
        } else if (!emailEditText.getText().toString().matches("(.+)@(.+)")) {
            emailEditTextLabel.setError(getResources().getString(R.string.false_email));
            emailEditTextLabel.setErrorTextColor(ColorStateList.valueOf(getResources().getColor(R.color.red)));
            return false;
        } else {
            emailEditTextLabel.setError(null);
            emailEditTextLabel.setHint(getResources().getString(R.string.give_email));
            emailEditTextLabel.setHintTextColor(ColorStateList.valueOf(getResources().getColor(R.color.black)));
            return true;
        }
    }

    private boolean validateRepeatPassword() {
        if (TextUtils.isEmpty(repeatPasswordEditText.getText().toString())) {
            repeatPasswordEditTextLabel.setError(getResources().getString(R.string.must_repeat_password));
            repeatPasswordEditTextLabel.setErrorTextColor(ColorStateList.valueOf(getResources().getColor(R.color.red)));
            return false;
        } else if (!repeatPasswordEditText.getText().toString().equals(passwordEditText.getText().toString())) {
            repeatPasswordEditTextLabel.setError(getResources().getString(R.string.password_dont_match));
            repeatPasswordEditTextLabel.setErrorTextColor(ColorStateList.valueOf(getResources().getColor(R.color.red)));
            return false;
        } else {
            repeatPasswordEditTextLabel.setError(null);
            repeatPasswordEditTextLabel.setHint(getResources().getString(R.string.repeat_password));
            repeatPasswordEditTextLabel.setHintTextColor(ColorStateList.valueOf(getResources().getColor(R.color.black)));
            return true;
        }
    }

    private boolean validatePassword() {
        Pattern letter = Pattern.compile("[a-zA-z]");
        Pattern digit = Pattern.compile("[0-9]");
        Pattern special = Pattern.compile("[!@#$%&*()_+=|<>?{}\\[\\]~-]");
        Matcher hasLetter = letter.matcher((passwordEditText.getText().toString()));
        Matcher hasDigit = digit.matcher((passwordEditText.getText().toString()));
        Matcher hasSpecial = special.matcher((passwordEditText.getText().toString()));

        if (TextUtils.isEmpty(passwordEditText.getText().toString())) {
            passwordEditTextLabel.setError(getResources().getString(R.string.password_is_necessary));
            passwordEditTextLabel.setErrorTextColor(ColorStateList.valueOf(getResources().getColor(R.color.red)));
            return false;
        } else if ((passwordEditText.getText().toString().length()) < 8 || !hasLetter.find() || !hasDigit.find() || !hasSpecial.find()) {
            passwordEditTextLabel.setError(getResources().getString(R.string.password_eight_signs_leter_number_special));
            passwordEditTextLabel.setErrorTextColor(ColorStateList.valueOf(getResources().getColor(R.color.red)));
            return false;
        } else {
            passwordEditTextLabel.setError(null);
            passwordEditTextLabel.setHint(getResources().getString(R.string.give_password));
            passwordEditTextLabel.setHintTextColor(ColorStateList.valueOf(getResources().getColor(R.color.black)));
            return true;
        }
    }

    private boolean validateLogin() {
        if (TextUtils.isEmpty(loginEditText.getText().toString())) {
            loginEditTextLabel.setError(getResources().getString(R.string.login_is_necessary));
            loginEditTextLabel.setErrorTextColor(ColorStateList.valueOf(getResources().getColor(R.color.red)));
            return false;
        } else if ((loginEditText.getText().toString().length()) <= 3) {
            loginEditTextLabel.setError(getResources().getString(R.string.login_must_more_than_three_signs));
            loginEditTextLabel.setErrorTextColor(ColorStateList.valueOf(getResources().getColor(R.color.red)));
            return false;
        } else {
            loginEditTextLabel.setError(null);
            loginEditTextLabel.setHint(getResources().getString(R.string.give_login));
            loginEditTextLabel.setHintTextColor(ColorStateList.valueOf(getResources().getColor(R.color.black)));
            return true;
        }
    }
}
