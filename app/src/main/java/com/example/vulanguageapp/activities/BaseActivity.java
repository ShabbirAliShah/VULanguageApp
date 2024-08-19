package com.example.vulanguageapp.activities;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.vulanguageapp.R;
import com.example.vulanguageapp.adminControlls.activities.AdminDashboard;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public abstract class BaseActivity extends AppCompatActivity {

    protected DrawerLayout drawerLayout;
    protected NavigationView navigationView;
    protected ActionBarDrawerToggle actionBarDrawerToggle;
    protected FirebaseAuth mAuth;

    @Override
    public void onStart(){
        super.onStart();
        FirebaseUser currentUsr = mAuth.getCurrentUser();
        if(currentUsr == null){

            startActivity( new Intent(getApplicationContext(), login.class));
            finish();
        }
    }

    public String getUserId() {
        FirebaseUser currentUsr = mAuth.getCurrentUser();
        if (currentUsr != null) {
            return currentUsr.getUid();
        }
        return null;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.base_activity);

        FirebaseApp.initializeApp(this);

        setNavigationDrawer();

    }

    public boolean setNavigationDrawer(){
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);

        mAuth = FirebaseAuth.getInstance();

        View headerView = navigationView.getHeaderView(0);

        TextView user = headerView.findViewById(R.id.currentUser);
        FirebaseUser currentUsr = mAuth.getCurrentUser();

        if (currentUsr != null) {
            // Set the user's display name or email to the TextView
            user.setText(currentUsr.getEmail());
        }

        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        // Handle navigation item clicks
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                int itemId = item.getItemId();

                if (itemId == R.id.navHome) {
                    startActivity(new Intent(BaseActivity.this, MainActivity.class));
                } else if (itemId == R.id.quiz) {
                    startActivity(new Intent(BaseActivity.this, QuizActivity.class));
                }
                else if (itemId == R.id.progress) {
                    startActivity(new Intent(BaseActivity.this, GamificationActivity.class));
                }
                else if (itemId == R.id.lecture) {
                    startActivity(new Intent(BaseActivity.this, ViewLectureActivity.class));
                } else if (itemId == R.id.admin) {
                    startActivity(new Intent(BaseActivity.this, AdminDashboard.class));

                }

                drawerLayout.closeDrawer(GravityCompat.START);
                return true;
            }
        });
        return false;
    }

    // Method to set content in the FrameLayout container
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
