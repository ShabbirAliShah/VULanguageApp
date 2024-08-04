package com.example.vulanguageapp.activities;


import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.vulanguageapp.R;
import com.google.android.material.navigation.NavigationView;

public class BaseActivity extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private ActionBarDrawerToggle actionBarDrawerToggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    public boolean setupNavigationDrawer() {

        drawerLayout = findViewById(R.id.drawer);
        navigationView = findViewById(R.id.navView);

        // Setup action bar toggle (optional, for opening drawer from action bar)
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        navigationView.setNavigationItemSelectedListener(item -> {
            int itemId = item.getItemId();

            if(itemId == R.id.navHome) {

                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
            }

            if(itemId == R.id.languages){
                Intent intent = new Intent(this, LanguageViewsActivity.class);
                startActivity(intent);
            }

            if(itemId == R.id.quiz){
                Intent intent = new Intent(this, QuizActivity.class);
                startActivity(intent);
            }

            if(itemId == R.id.progress){
                Intent intent = new Intent(this, GamificationActivity.class);
                startActivity(intent);
            }

            if(itemId == R.id.lecture){
                Intent intent = new Intent(this, ViewLectureActivity.class);
                startActivity(intent);
            }

            if(itemId == R.id.testActivity){
                Intent intent = new Intent(this, TestActivity.class);
                startActivity(intent);
            }

            drawerLayout.closeDrawer(GravityCompat.START); // Close drawer after handling selection
            return true;
        });
        return false;
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        // Handle actions from navigation drawer (if up button is pressed)
        if (actionBarDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}

