package com.example.instagram.News;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.example.instagram.R;
import com.example.instagram.utils.BottomNavigationViewHelper;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;

public class NewsActivity extends AppCompatActivity {


    BottomNavigationViewEx bottomNavigationViewEx;
    private final int ACTIVITY_NO = 3;
    private final String TAG = "NewsActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setUpNavigationView();
    }

    public void setUpNavigationView(){
        bottomNavigationViewEx = findViewById(R.id.navigationBottom);
        BottomNavigationViewHelper.setupBottomNavigationView(bottomNavigationViewEx);
        BottomNavigationViewHelper.setupNavigation(this, bottomNavigationViewEx);
        Menu menu = bottomNavigationViewEx.getMenu();
        MenuItem menuItem = menu.getItem(ACTIVITY_NO);
        menuItem.setChecked(true);

    }
}
