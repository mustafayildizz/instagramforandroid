package com.example.instagram.Login;


import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.instagram.R;
import com.example.instagram.utils.EventbusDataEvents;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * A simple {@link Fragment} subclass.
 */
public class CodeVerificationFragment extends Fragment {

    private String credentialsPhoneNo;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;
    private String verificationID = "", sentCode = "";
    private Button btnVerify;
    private ProgressBar progressBar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_code_verification, container, false);
        TextView tvUserPhoneNo = view.findViewById(R.id.tvUserPhoneNo);
        final EditText etVerificationCode = view.findViewById(R.id.etVerificationCode);
        btnVerify = view.findViewById(R.id.btnNextVerification);
        progressBar = view.findViewById(R.id.progressBarVerification);
        btnVerify.setEnabled(true);
        tvUserPhoneNo.setText(credentialsPhoneNo);

        setupCallbacks();

        btnVerify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(sentCode.equals(etVerificationCode.getText().toString())) {
                    EventBus.getDefault().postSticky(new EventbusDataEvents.SendCredentials(credentialsPhoneNo, null, verificationID, sentCode, false));
                    FragmentManager manager = getActivity().getSupportFragmentManager();
                    FragmentTransaction transaction = manager.beginTransaction();
                    transaction.replace(R.id.loginContainer, new RegisterFragment())
                            .addToBackStack(null)
                            .commit();


                } else {
                    EventBus.getDefault().postSticky(new EventbusDataEvents.SendCredentials(credentialsPhoneNo, null, verificationID, sentCode, false));
                    FragmentManager manager = getActivity().getSupportFragmentManager();
                    FragmentTransaction transaction = manager.beginTransaction();
                    transaction.replace(R.id.loginContainer, new RegisterFragment())
                            .addToBackStack(null)
                            .commit();
                    Toast.makeText(getActivity(), "Hatalı Kod", Toast.LENGTH_LONG).show();
                }
            }
        });


        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                credentialsPhoneNo,        // Phone number to verify
                60,                 // Timeout duration
                TimeUnit.SECONDS,   // Unit of timeout
                Objects.requireNonNull(this.getActivity()),               // Activity (for callback binding)
                mCallbacks);        // OnVerificationStateChangedCallbacks

        return view;
    }

    private void setupCallbacks() {
        mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            @Override
            public void onVerificationCompleted(@NonNull PhoneAuthCredential credential) {
                sentCode = credential.getSmsCode();
                progressBar.setVisibility(View.INVISIBLE);
                btnVerify.setTextColor(getResources().getColor(R.color.white, null));
                btnVerify.setBackgroundResource(R.color.blue);
                btnVerify.setEnabled(true);
            }

            @Override
            public void onVerificationFailed(@NonNull FirebaseException e) {

            }

            @Override
            public void onCodeSent(@NonNull String verificationId, @NonNull PhoneAuthProvider.ForceResendingToken token) {
                verificationID = verificationId;
                progressBar.setVisibility(View.VISIBLE);
            }
        };

    }

    @Subscribe (sticky = true)
    public void onPhoneNoEvent(EventbusDataEvents.SendCredentials number) {
        credentialsPhoneNo = number.getNumber();
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
}
