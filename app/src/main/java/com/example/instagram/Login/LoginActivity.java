package com.example.instagram.Login;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.instagram.Home.HomeActivity;
import com.example.instagram.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends AppCompatActivity {

    private TextView tvRegister;
    private EditText etUsernameOrTelOrEmail;
    private EditText password;
    private Button btnLogin;
    private TextWatcher watcher;
    private FirebaseAuth mAuth;
    private DatabaseReference mRef;
    private String email_phone_no, e_mail, phone;
    private boolean isAvailableUser;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        FirebaseUserAndDatabase();
        Init();
        Login();
        SetUpAuthListener();
        SignUp();
        etUsernameOrTelOrEmail.addTextChangedListener(watcher);
        password.addTextChangedListener(watcher);
    }

    private void SignUp() {
        tvRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), RegisterActivity.class).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(intent);
            }
        });
    }

    private void CheckUserCredential(String email, final String password) {
        isAvailableUser = false;
        mRef.child("users").orderByChild("email").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    e_mail = ds.child("email").getValue(String.class);
                    String userName = ds.child("user_name").getValue(String.class);
                    phone = ds.child("phone_no").getValue(String.class);
                    email_phone_no = ds.child("email_phone_no").getValue(String.class);

                    if (e_mail != null && e_mail.equals(etUsernameOrTelOrEmail.getText().toString())) {
                        SignInSuccessfully(e_mail, password, false);
                        isAvailableUser = true;
                        break;
                    } else if (userName.equals(etUsernameOrTelOrEmail.getText().toString())) {
                        SignInSuccessfully(e_mail, password, false);
                        isAvailableUser = true;
                        break;
                    }
                    else if (phone != null && phone.equals(etUsernameOrTelOrEmail.getText().toString())) {
                        SignInSuccessfully(email_phone_no, password, true);
                        isAvailableUser = true;
                        break;
                    }
                }
//                if(phone == null || e_mail == null && !isAvailableUser) {
//                    Toast.makeText(getApplicationContext(), "Oturum açılamadı. Boş Alan", Toast.LENGTH_LONG).show();
//                }
                if (!isAvailableUser) {
                    Toast.makeText(getApplicationContext(), "Kullanıcı Bulunamadı", Toast.LENGTH_LONG).show();
                }
            }

            private void SignInSuccessfully(String email, String password, boolean signInWithPhone) {
                //                String willBeEnterEmail = "";
//                if (signInWithPhone) {
//                    willBeEnterEmail = email_phone_no;
//                } else {
//                    willBeEnterEmail = e_mail;
//                }

                mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(getApplicationContext(), mAuth.getCurrentUser().getUid(), Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(getApplicationContext(), "Oturum açılamadı", Toast.LENGTH_LONG).show();
                        }
                    }
                });

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void FirebaseUserAndDatabase() {
        mAuth = FirebaseAuth.getInstance();
        mRef = FirebaseDatabase.getInstance().getReference();
    }

    private void Init() {
        tvRegister = findViewById(R.id.tvLoginActivityRegister);
        btnLogin = findViewById(R.id.btnLoginActivity);
        etUsernameOrTelOrEmail = findViewById(R.id.usernameOrTelOrPhoneLoginActivity);
        password = findViewById(R.id.passwordLoginActivity);
        watcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (etUsernameOrTelOrEmail.getText().toString().length() > 3 && password.getText().toString().length() > 4) {
                    btnLogin.setEnabled(true);
                    btnLogin.setBackgroundResource(R.color.blue);
                    btnLogin.setTextColor(getResources().getColor(R.color.white, null));
                } else {
                    btnLogin.setEnabled(false);
                    btnLogin.setBackgroundResource(R.drawable.register_button);
                    btnLogin.setTextColor(getResources().getColor(R.color.lightBlue, null));
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        };
    }

    private void Login() {
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CheckUserCredential(etUsernameOrTelOrEmail.getText().toString(), password.getText().toString());
            }
        });
    }

    private void SetUpAuthListener() {
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                user = FirebaseAuth.getInstance().getCurrentUser();

                if (user != null) {
                    Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                    startActivity(intent);
                    finish();
                } else {

                }
            }
        };
    }

    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }
}
