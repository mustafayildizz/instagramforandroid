package com.example.instagram.Login;


import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.instagram.Models.Users;
import com.example.instagram.R;
import com.example.instagram.utils.EventbusDataEvents;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.EmailAuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

/**
 * A simple {@link Fragment} subclass.
 */
public class RegisterFragment extends Fragment {

    private String verificationID = "", sentCode, phoneNo, email;
    private EditText etNameAndSurname, etPassword, etUsername;
    private Button btnNextRegister;
    private FirebaseAuth mAuth;
    private DatabaseReference myRef;
    private String user_id;
    private boolean isEmail = true;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_register, container, false);
        etNameAndSurname = view.findViewById(R.id.etNameRegisterFragment);
        etPassword = view.findViewById(R.id.etPasswordRegisterFragment);
        etUsername = view.findViewById(R.id.etUsernameRegisterFragment);
        btnNextRegister = view.findViewById(R.id.btnNextRegister);

        mAuth = FirebaseAuth.getInstance();
        if (mAuth.getCurrentUser() != null)
            mAuth.signOut();


        myRef = FirebaseDatabase.getInstance().getReference();

        etNameAndSurname.addTextChangedListener(watcher);
        etPassword.addTextChangedListener(watcher);
        etUsername.addTextChangedListener(watcher);


        btnNextRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // if user want to register with email
                if (isEmail) {
                    final String password = etPassword.getText().toString();
                    mAuth.createUserWithEmailAndPassword(email, password)
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        user_id = mAuth.getCurrentUser().getUid();
                                        Users user = new Users(email, password, etUsername.getText().toString(), "", "", etNameAndSurname.getText().toString(), user_id);
                                        myRef.child("users").child(user_id).setValue(user)
                                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        if (task.isSuccessful()) {
                                                            Toast.makeText(getContext(), "Kullanıcı kaydedildi", Toast.LENGTH_LONG).show();
                                                        } else {
                                                            mAuth.getCurrentUser().delete()
                                                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                        @Override
                                                                        public void onComplete(@NonNull Task<Void> task) {
                                                                            if (task.isSuccessful()) {
                                                                                Toast.makeText(getContext(), "Kullanıcı kaydedilirken hata", Toast.LENGTH_LONG).show();
                                                                            }
                                                                        }
                                                                    });
                                                        }
                                                    }
                                                });

                                    } else {
                                        Toast.makeText(getContext(), "Kullanıcı oluşturulurken Hata!", Toast.LENGTH_LONG).show();
                                    }
                                }
                            });
                }
                // if user want to register with phone number
                else {
                    final String fakeEmail = phoneNo + "@gmail.com";
                    final String password = etPassword.getText().toString();
                    mAuth.createUserWithEmailAndPassword(fakeEmail, password)
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        user_id = mAuth.getCurrentUser().getUid();
                                        Users user = new Users("", password, etUsername.getText().toString(), phoneNo, fakeEmail, etNameAndSurname.getText().toString(), user_id);
                                        myRef.child("users").child(user_id).setValue(user)
                                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        if (task.isSuccessful()) {
                                                            Toast.makeText(getContext(), "Kullanıcı kaydedildi", Toast.LENGTH_LONG).show();
                                                        } else {
                                                            mAuth.getCurrentUser().delete();
                                                            Toast.makeText(getContext(), "Kullanıcı kaydedilirken hata", Toast.LENGTH_LONG).show();
                                                        }
                                                    }
                                                });
                                    } else {
                                        Toast.makeText(getContext(), "Kullanıcı oluşturulurken Hata!", Toast.LENGTH_LONG).show();
                                    }
                                }
                            });
                }


            }
        });


        return view;
    }

    TextWatcher watcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (s.length() > 5) {

                if (etNameAndSurname.getText().toString().length() > 5 && etPassword.getText().toString().length() > 5 && etUsername.getText().toString().length() > 5) {
                    btnNextRegister.setEnabled(true);
                    btnNextRegister.setBackgroundResource(R.color.blue);
                    btnNextRegister.setTextColor(getResources().getColor(R.color.white, null));
                } else {
                    btnNextRegister.setEnabled(false);
                    btnNextRegister.setBackgroundResource(R.color.white);
                    btnNextRegister.setTextColor(getResources().getColor(R.color.lightBlue, null));
                }

            } else {
                btnNextRegister.setEnabled(false);
                btnNextRegister.setBackgroundResource(R.color.white);
                btnNextRegister.setTextColor(getResources().getColor(R.color.lightBlue, null));
            }
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };

    /////////////////////////////////// EVENTBUS ////////////////////////////////////////
    @Subscribe(sticky = true)
    public void onCredentialEvent(EventbusDataEvents.SendCredentials credentials) {

        if (credentials.isEmail()) {
            isEmail = true;
            email = credentials.getEmail();
        } else {
            isEmail = false;
            verificationID = credentials.getVerificationID();
            sentCode = credentials.getCode();
            phoneNo = credentials.getNumber();
        }

    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        EventBus.getDefault().register(this);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        EventBus.getDefault().unregister(this);
    }
    /////////////////////////////////// EVENTBUS ////////////////////////////////////////

}
