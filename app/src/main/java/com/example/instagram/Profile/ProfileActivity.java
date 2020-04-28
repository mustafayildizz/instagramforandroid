package com.example.instagram.Profile;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Context;
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
import com.example.instagram.Models.UserDetails;
import com.example.instagram.Models.Users;
import com.example.instagram.R;
import com.example.instagram.utils.BottomNavigationViewHelper;
import com.example.instagram.utils.EventbusDataEvents;
import com.example.instagram.utils.UniversalImageLoader;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;

import org.greenrobot.eventbus.EventBus;

import java.util.Objects;

public class ProfileActivity extends AppCompatActivity {


    BottomNavigationViewEx bottomNavigationViewEx;
    ImageView imgSettingProfile;
    ConstraintLayout profileRoot;
    FrameLayout profileContainer;
    TextView tvProfileEdit, tvToolbarUserName, tvPostCount, tvFollowingCount, tvFollowerCount, tvProfileRealName, tvBiographi;
    ImageView imgProfile;
    ProgressBar mProgressBar;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseUser user;
    private DatabaseReference mRef;
    private String user_id;

    private final int ACTIVITY_NO = 4;
    private final String TAG = "ProfileActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        mAuth = FirebaseAuth.getInstance();
        mRef = FirebaseDatabase.getInstance().getReference();

        user_id = mAuth.getCurrentUser().getUid();

        init();
        setupToolbar();
        setUpNavigationView();
        fragmentNavigations();
        SetUpAuthListener();
        GetUserDetails();
    }

    private void init() {
        bottomNavigationViewEx = findViewById(R.id.bottomNavigationViewProfile);
        imgSettingProfile = findViewById(R.id.imgProfileSettings);
        profileRoot = findViewById(R.id.profileRoot);
        profileContainer = findViewById(R.id.profileContainer);
        tvProfileEdit = findViewById(R.id.tvProfileEdit);
        imgProfile = findViewById(R.id.circleProfileImage);
        mProgressBar = findViewById(R.id.progressBarProfileActivity);
        tvToolbarUserName = findViewById(R.id.tvToolbarUserName);
        tvPostCount = findViewById(R.id.tvPostCount);
        tvFollowerCount = findViewById(R.id.tvFollowerCount);
        tvFollowingCount = findViewById(R.id.tvFollowingCount);
        tvProfileRealName = findViewById(R.id.tvProfileRealName);
        tvBiographi = findViewById(R.id.tvBiographi);
    }

    private void GetUserDetails() {

        mRef.child("users").child(user_id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Users users = dataSnapshot.getValue(Users.class);
                EventBus.getDefault().postSticky(new EventbusDataEvents.SendUserInfo(users));
                tvToolbarUserName.setText(users.getUser_name());
                tvFollowerCount.setText(users.getUserDetails().getFollower());
                tvFollowingCount.setText(users.getUserDetails().getFollowing());
                tvPostCount.setText(users.getUserDetails().getPost());
                String imgUrl = users.getUserDetails().getProfile_pic();
                UniversalImageLoader.setImage(imgUrl, imgProfile, mProgressBar, "");
                tvProfileRealName.setText(users.getName_surname());
                if (users.getUserDetails().getBiography() != null) {
                    tvBiographi.setText(users.getUserDetails().getBiography());
                    tvBiographi.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
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
                    user = mAuth.getCurrentUser();
                }
            }
        };
    }

    private void fragmentNavigations() {
        tvProfileEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                profileRoot.setVisibility(View.GONE);
                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                transaction.replace(R.id.profileContainer, new ProfileEditFragment(), "profileEditFragment");
                transaction.addToBackStack("addedProfileEditFragment");
                transaction.commit();
            }
        });
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
