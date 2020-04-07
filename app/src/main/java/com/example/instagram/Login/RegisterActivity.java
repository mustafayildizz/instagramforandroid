package com.example.instagram.Login;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.instagram.R;
import com.example.instagram.utils.EventbusDataEvents;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.greenrobot.eventbus.EventBus;

public class RegisterActivity extends AppCompatActivity {

    ConstraintLayout loginRoot;
    FrameLayout loginContainer;
    TextView tvPhone, tvEmail, tvLogin;
    EditText etLoginType;
    View viewPhone, viewEmail;
    Button buttonNext;
    DatabaseReference mRef;
    boolean isAvailableEmail, isAvailablePhone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mRef = FirebaseDatabase.getInstance().getReference();

        init();
        Email();
        Phone();
        textChanged();
        EmailOrPhone();
    }

    private void init() {
        tvEmail = findViewById(R.id.tvEmailRegisterActivity);
        tvLogin = findViewById(R.id.tvRegisterActivityLogin);
        tvPhone = findViewById(R.id.tvPhoneRegisterActivity);
        etLoginType = findViewById(R.id.etLoginTypeRegisterActivity);
        viewPhone = findViewById(R.id.phoneView);
        viewEmail = findViewById(R.id.emailView);
        buttonNext = findViewById(R.id.buttonNextRegisterActivity);
        loginRoot = findViewById(R.id.loginRoot);
        loginContainer = findViewById(R.id.loginContainer);
    }


    public void Email() {
        tvEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                etLoginType.setText("");
                etLoginType.setHint("E-mail");

                buttonNext.setEnabled(false);
                buttonNext.setTextColor(getResources().getColor(R.color.lightBlue, null));
                buttonNext.setBackgroundResource(R.drawable.register_button);

                etLoginType.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
                viewPhone.setBackgroundResource(R.color.shadows);
                viewEmail.setBackgroundResource(R.color.black);
            }
        });
    }

    public void Phone() {
        tvPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                etLoginType.setText("");
                etLoginType.setHint("Telefon");

                buttonNext.setEnabled(false);
                buttonNext.setTextColor(getResources().getColor(R.color.lightBlue, null));
                buttonNext.setBackgroundResource(R.drawable.register_button);

                etLoginType.setInputType(InputType.TYPE_CLASS_PHONE);
                viewEmail.setBackgroundResource(R.color.shadows);
                viewPhone.setBackgroundResource(R.color.black);
            }
        });
    }


    private void textChanged() {
        etLoginType.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (start+before+count >= 10) {
                    buttonNext.setEnabled(true);
                    buttonNext.setTextColor(getResources().getColor(R.color.white, null));
                    buttonNext.setBackgroundResource(R.color.blue);
                } else {
                    buttonNext.setEnabled(false);
                    buttonNext.setTextColor(getResources().getColor(R.color.lightBlue, null));
                    buttonNext.setBackgroundResource(R.drawable.register_button);

                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void EmailOrPhone() {
        isAvailableEmail = true;
        isAvailablePhone = true;
        buttonNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               if(etLoginType.getHint().toString().equals("Telefon")) {

                   mRef.child("users").addListenerForSingleValueEvent(new ValueEventListener() {
                       @Override
                       public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                           if(dataSnapshot.getValue() != null) {
                               for (DataSnapshot ds: dataSnapshot.getChildren()) {
                                   String phoneUser = ds.child("phone_no").getValue(String.class);
                                   if (phoneUser.equals(etLoginType.getText().toString())) {
                                       Toast.makeText(getApplicationContext(), "Telefon numarası zaten var", Toast.LENGTH_LONG).show();
                                       isAvailablePhone = false;
                                       break;
                                   }
                               }
                           }
                       }

                       @Override
                       public void onCancelled(@NonNull DatabaseError databaseError) {

                       }
                   });

                   if (isAvailablePhone) {
                       loginRoot.setVisibility(View.GONE);
                       FragmentManager manager = getSupportFragmentManager();
                       FragmentTransaction transaction = manager.beginTransaction();

                       transaction.replace(R.id.loginContainer, new CodeVerificationFragment())
                               .addToBackStack(null)
                               .commit();
                       EventBus.getDefault().postSticky(new EventbusDataEvents.SendCredentials(etLoginType.getText().toString(), null, null, null, false));

                   }

               } else {

                   if (isValidEmail(etLoginType.getText().toString())) {

                       mRef.child("users").addListenerForSingleValueEvent(new ValueEventListener() {
                           @Override
                           public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                               if (dataSnapshot.getValue() != null) {
                                   for (DataSnapshot ds: dataSnapshot.getChildren()) {
                                       String emailUser = ds.child("email").getValue(String.class);
                                       if (emailUser.equals(etLoginType.getText().toString())) {
                                           Toast.makeText(getApplicationContext(), "Email daha önce alınmış", Toast.LENGTH_LONG).show();
                                           isAvailableEmail = false;
                                           break;
                                       }
                                   }
                               }
                           }

                           @Override
                           public void onCancelled(@NonNull DatabaseError databaseError) {

                           }
                       });

                       if (isAvailableEmail) {
                           loginRoot.setVisibility(View.GONE);
                           FragmentManager manager = getSupportFragmentManager();
                           FragmentTransaction transaction = manager.beginTransaction();
                           transaction.replace(R.id.loginContainer, new RegisterFragment())
                                   .addToBackStack(null)
                                   .commit();
                           EventBus.getDefault().postSticky(new EventbusDataEvents.SendCredentials(null, etLoginType.getText().toString(), null, null, true));
                       }
                   } else {
                       Toast.makeText(getApplicationContext(), "Lütfen geçerli bir mail adresi giriniz", Toast.LENGTH_LONG).show();
                   }
               }
            }
        });
    }

    public boolean isValidEmail(String email) {
        if (email == null) {
            return false;
        }

        return Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    @Override
    public void onBackPressed() {
        int count = getSupportFragmentManager().getBackStackEntryCount();

        if (count == 1) {
            super.onBackPressed();
            loginRoot.setVisibility(View.VISIBLE);
        } else {
            getSupportFragmentManager().popBackStack();
        }

    }
}

