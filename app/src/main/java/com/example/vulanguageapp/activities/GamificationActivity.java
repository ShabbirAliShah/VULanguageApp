package com.example.vulanguageapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.GravityCompat;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.vulanguageapp.R;
import com.google.android.material.navigation.NavigationView;

public class GamificationActivity extends AppCompatActivity {


    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private ActionBarDrawerToggle actionBarDrawerToggle;

    //Fragments for gamification activity.
    //Pronunciation, leaderboard, course progress, achievements.


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_gamification);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        setupNavigationDrawer();

    }

    public boolean setupNavigationDrawer() {
        drawerLayout = findViewById(R.id.drawer); // Find DrawerLayout by ID
        navigationView = findViewById(R.id.navView); // Find NavigationView by ID

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