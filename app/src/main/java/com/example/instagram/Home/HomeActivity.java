package com.example.instagram.Home;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.example.instagram.R;
import com.example.instagram.utils.BottomNavigationViewHelper;
import com.example.instagram.utils.HomePagerAdapter;
import com.example.instagram.utils.UniversalImageLoader;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;
import com.nostra13.universalimageloader.core.ImageLoader;

public class HomeActivity extends AppCompatActivity {

    BottomNavigationViewEx bottomNavigationViewEx;
    private final int ACTIVITY_NO = 0;
    private final String TAG = "HomeActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setUpNavigationView();
        setupHomeViewPager();
        initImageLoader();
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
