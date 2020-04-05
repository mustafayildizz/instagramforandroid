package com.example.instagram.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.instagram.Home.HomeActivity;
import com.example.instagram.News.NewsActivity;
import com.example.instagram.Profile.ProfileActivity;
import com.example.instagram.R;
import com.example.instagram.Search.SearchActivity;
import com.example.instagram.Share.ShareActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;

public class BottomNavigationViewHelper extends AppCompatActivity {
    public static void setupBottomNavigationView(BottomNavigationViewEx bottomNavigationViewEx) {
        bottomNavigationViewEx.enableAnimation(false);
        bottomNavigationViewEx.enableItemShiftingMode(false);
        bottomNavigationViewEx.enableShiftingMode(false);
        bottomNavigationViewEx.setTextVisibility(false);
    }

    public static void setupNavigation(final Context context, BottomNavigationViewEx bottomNavigationViewEx) {


        bottomNavigationViewEx.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.ic_home:
                        Intent intent = new Intent(context, HomeActivity.class).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                        context.startActivity(intent);
                        return true;

                    case R.id.ic_search:
                        Intent intentSearch = new Intent(context, SearchActivity.class).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                        context.startActivity(intentSearch);
                        return true;

                    case R.id.ic_news:
                        Intent intentNews = new Intent(context, NewsActivity.class).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                        context.startActivity(intentNews);
                        return true;

                    case R.id.ic_share:
                        Intent intentShare = new Intent(context, ShareActivity.class).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                        context.startActivity(intentShare);
                        return true;

                    case R.id.ic_profile:
                        Intent intentProfile = new Intent(context, ProfileActivity.class).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                        context.startActivity(intentProfile);
                        return true;
                }

                return false;
            }
        });
    }
}
