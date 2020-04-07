package com.example.instagram.Login;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;

import com.example.instagram.R;

public class LoginActivity extends AppCompatActivity {

    private EditText etUsernameOrTelOrEmail;
    private EditText password;
    private Button btnLogin;
    private TextWatcher watcher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        init();

        etUsernameOrTelOrEmail.addTextChangedListener(watcher);
        password.addTextChangedListener(watcher);
    }

    private void init() {
        btnLogin = findViewById(R.id.btnLoginActivity);
        etUsernameOrTelOrEmail = findViewById(R.id.usernameOrTelOrPhoneLoginActivity);
        password = findViewById(R.id.passwordLoginActivity);
        watcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (etUsernameOrTelOrEmail.getText().toString().length() > 5 && password.getText().toString().length() > 6) {
                    btnLogin.setEnabled(true);
                    btnLogin.setBackgroundResource(R.color.blue);
                    btnLogin.setTextColor(getResources().getColor(R.color.white, null));
                } else {
                    btnLogin.setEnabled(false);
                    btnLogin.setBackgroundResource(R.color.blue);
                    btnLogin.setTextColor(getResources().getColor(R.color.white, null));
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        };
    }
}
