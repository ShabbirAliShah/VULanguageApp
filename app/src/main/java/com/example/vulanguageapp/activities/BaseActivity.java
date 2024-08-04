package com.example.vulanguageapp.activities;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.vulanguageapp.R;
import com.google.android.material.navigation.NavigationView;

public abstract class BaseActivity extends AppCompatActivity {

    protected DrawerLayout drawerLayout;
    protected NavigationView navigationView;

    protected ActionBarDrawerToggle actionBarDrawerToggle;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.base_activity);

        setupNavigationDrawer();
    }

    private boolean setupNavigationDrawer() {

        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);

        if (drawerLayout == null || navigationView == null) {
            throw new NullPointerException("DrawerLayout or NavigationView is null. Check your layout file.");
        }

        // Setup action bar toggle (optional, for opening drawer from action bar)
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        navigationView.setNavigationItemSelectedListener(item -> {
            int itemId = item.getItemId();

            Log.d("BaseActivity", "Menu item clicked: " + itemId);

            if (itemId == R.id.navHome) {
                startActivity(new Intent(this, MainActivity.class));
            } else if (itemId == R.id.languages) {
                startActivity(new Intent(this, LanguageViewsActivity.class));
            } else if (itemId == R.id.quiz) {
                startActivity(new Intent(this, QuizActivity.class));
            } else if (itemId == R.id.progress) {
                startActivity(new Intent(this, GamificationActivity.class));
            } else if (itemId == R.id.lecture) {
                startActivity(new Intent(this, ViewLectureActivity.class));
            }

            drawerLayout.closeDrawer(GravityCompat.START); // Close drawer after handling selection
            return true;
        });

//        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
//            @Override
//            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
//                Log.d("BaseActivity", "Menu item clicked: " + item.getTitle()); // Check if this line gets triggered
//                Toast.makeText(BaseActivity.this, "Clicked: " + item.getTitle(), Toast.LENGTH_SHORT).show();
//                return true;
//            }
//        });

        return false;
    }

    protected void setContent(int layoutResID) {
        FrameLayout container = findViewById(R.id.container);
        getLayoutInflater().inflate(layoutResID, container);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        // Handle actions from navigation drawer (if up button is pressed)
        if (actionBarDrawerToggle != null && actionBarDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}

