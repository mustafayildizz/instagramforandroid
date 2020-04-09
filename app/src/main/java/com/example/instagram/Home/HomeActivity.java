package com.example.instagram.Home;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.instagram.Login.LoginActivity;
import com.example.instagram.R;
import com.example.instagram.utils.BottomNavigationViewHelper;
import com.example.instagram.utils.HomePagerAdapter;
import com.example.instagram.utils.UniversalImageLoader;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;
import com.nostra13.universalimageloader.core.ImageLoader;

public class HomeActivity extends AppCompatActivity {

    private BottomNavigationViewEx bottomNavigationViewEx;
    private final int ACTIVITY_NO = 0;
    private final String TAG = "HomeActivity";
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();
        setUpNavigationView();
        setupHomeViewPager();
        initImageLoader();
        SetUpAuthListener();
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
                    Toast.makeText(getApplicationContext(), user.getEmail(), Toast.LENGTH_LONG).show();
                }
            }
        };
    }

    private void setupHomeViewPager() {

        HomePagerAdapter homePagerAdapter = new HomePagerAdapter(getSupportFragmentManager());

        homePagerAdapter.addFragment(new CameraFragment());
        homePagerAdapter.addFragment(new HomeFragment());
        homePagerAdapter.addFragment(new MessagesFragment());


        ViewPager homeViewPager = findViewById(R.id.homeViewPager);

        //home activity'de ki view pager ile bağlantısını sağlar
        homeViewPager.setAdapter(homePagerAdapter);

        //ana sayfanın home fragment ile başlamasını sağlar
        homeViewPager.setCurrentItem(1);

    }

    public void setUpNavigationView(){
        bottomNavigationViewEx = findViewById(R.id.navigationBottom);
        BottomNavigationViewHelper.setupBottomNavigationView(bottomNavigationViewEx);
        BottomNavigationViewHelper.setupNavigation(this, bottomNavigationViewEx);
        Menu menu = bottomNavigationViewEx.getMenu();
        MenuItem menuItem = menu.getItem(ACTIVITY_NO);
        menuItem.setChecked(true);

    }

    private void initImageLoader() {
        UniversalImageLoader universalImageLoader = new UniversalImageLoader(this);
        ImageLoader.getInstance().init(universalImageLoader.getConfig());
    }
}
