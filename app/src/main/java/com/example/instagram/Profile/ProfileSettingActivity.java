package com.example.instagram.Profile;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.instagram.R;
import com.example.instagram.utils.BottomNavigationViewHelper;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;

public class ProfileSettingActivity extends AppCompatActivity {

    ConstraintLayout profileSettingRoot;
    ImageView imgBackButton;
    TextView tvProfileEditSettings, tvSignOutSettings;
    BottomNavigationViewEx bottomNavigationViewEx;
    private final int ACTIVITY_NO = 4;
    private final String TAG = "ProfileActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_setting);

        init();
        setupToolbar();
        setUpNavigationView();
        fragmentNavigations();
    }

    private void fragmentNavigations() {

        tvProfileEditSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                profileSettingRoot.setVisibility(View.GONE);
                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                transaction.replace(R.id.profileSettingContainer, new ProfileEditFragment(), "profileEditFragment");
                transaction.addToBackStack("addedProfileEditFragment");
                transaction.commit();
            }
        });

        tvSignOutSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment dialog = new SignOutFragment();
                dialog.show(getSupportFragmentManager(), "EXITDIALOG");
            }
        });

    }


    @Override
    public void onBackPressed() {
        profileSettingRoot.setVisibility(View.VISIBLE);
        super.onBackPressed();
    }


    private void init() {
        profileSettingRoot = findViewById(R.id.profileSettingRoot);
        bottomNavigationViewEx = findViewById(R.id.bottomNavigationViewProfileSettings);
        imgBackButton = findViewById(R.id.imgBackButton);
        tvProfileEditSettings = findViewById(R.id.tvProfileEditSettings);
        tvSignOutSettings = findViewById(R.id.tvSignOutSettings);
    }

    private void setupToolbar() {
        imgBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    public void setUpNavigationView(){
        BottomNavigationViewHelper.setupBottomNavigationView(bottomNavigationViewEx);
        BottomNavigationViewHelper.setupNavigation(this, bottomNavigationViewEx);
        Menu menu = bottomNavigationViewEx.getMenu();
        MenuItem menuItem = menu.getItem(ACTIVITY_NO);
        menuItem.setChecked(true);

    }
}
