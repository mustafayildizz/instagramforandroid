package com.example.instagram.Profile;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.instagram.Login.LoginActivity;
import com.example.instagram.R;
import com.example.instagram.utils.BottomNavigationViewHelper;
import com.example.instagram.utils.UniversalImageLoader;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;

public class ProfileActivity extends AppCompatActivity {


    BottomNavigationViewEx bottomNavigationViewEx;
    ImageView imgSettingProfile;
    ConstraintLayout profileRoot;
    FrameLayout profileContainer;
    TextView tvProfileEdit;
    ImageView imgProfile;
    ProgressBar mProgressBar;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseUser user;

    private final int ACTIVITY_NO = 4;
    private final String TAG = "ProfileActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        mAuth = FirebaseAuth.getInstance();

        init();
        setupToolbar();
        setUpNavigationView();
        fragmentNavigations();
        setupProfilePicture();
        SetUpAuthListener();
    }

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

    private void SetUpAuthListener() {
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                user = FirebaseAuth.getInstance().getCurrentUser();

                if (user == null) {
                    Intent intent = new Intent(getApplicationContext(), LoginActivity.class).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    finish();
                } else {

                }
            }
        };
    }

    private void fragmentNavigations() {
        tvProfileEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                profileRoot.setVisibility(View.GONE);
                FragmentManager manager = getSupportFragmentManager();
                FragmentTransaction transaction = manager.beginTransaction();
                transaction.replace(R.id.profileContainer, new ProfileEditFragment()).addToBackStack(null);
                transaction.commit();
            }
        });
    }

    private void init() {
        bottomNavigationViewEx = findViewById(R.id.bottomNavigationViewProfile);
        imgSettingProfile = findViewById(R.id.imgProfileSettings);
        profileRoot = findViewById(R.id.profileRoot);
        profileContainer = findViewById(R.id.profileContainer);
        tvProfileEdit = findViewById(R.id.tvProfileEdit);
        imgProfile = findViewById(R.id.circleProfileImage);
        mProgressBar = findViewById(R.id.progressBarProfileActivity);
    }

    private void setupToolbar() {
        imgSettingProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ProfileSettingActivity.class).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(intent);
            }
        });
    }

    private void setupProfilePicture() {
        String URL = "wallpaperaccess.com/full/25640.jpg";
        UniversalImageLoader.setImage(URL, imgProfile, mProgressBar, "https://");
    }

    public void setUpNavigationView(){

        BottomNavigationViewHelper.setupBottomNavigationView(bottomNavigationViewEx);
        BottomNavigationViewHelper.setupNavigation(this, bottomNavigationViewEx);
        Menu menu = bottomNavigationViewEx.getMenu();
        MenuItem menuItem = menu.getItem(ACTIVITY_NO);
        menuItem.setChecked(true);

    }

    @Override
    public void onBackPressed() {
        profileRoot.setVisibility(View.VISIBLE);
        super.onBackPressed();
    }

}
