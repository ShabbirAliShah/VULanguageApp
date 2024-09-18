package com.example.vulanguageapp.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.vulanguageapp.R;
import com.example.vulanguageapp.adminControlls.activities.AdminDashboard;
import com.example.vulanguageapp.adminControlls.activities.PracticeActivity;
import com.example.vulanguageapp.interfaces.UserIdProvider;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public abstract class BaseActivity extends AppCompatActivity implements UserIdProvider {

    protected DrawerLayout drawerLayout;
    protected NavigationView navigationView;
    protected ActionBarDrawerToggle actionBarDrawerToggle;
    protected FirebaseAuth mAuth;
    protected FirebaseUser currentUsr;

    public static final String PREFS_NAME = "MyPrefs";
    public static final String KEY_LANGUAGE = "language";
    public static final String KEY_USER_ID = "user_id";
    private String userId;
    private String key;


    @Override
    public void onStart() {
        super.onStart();
        mAuth = FirebaseAuth.getInstance();
        currentUsr = mAuth.getCurrentUser();
        if (currentUsr == null) {

            startActivity(new Intent(getApplicationContext(), UserAccountActivity.class));
            finish();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.base_activity);

        FirebaseApp.initializeApp(this);
        String currentCourse = "hindi";
        userId = getUserId();
        //checkInternetConnection();
        setNavigationDrawer();
        applyTheme();

        SharedPreferences sharedPreferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString(KEY_LANGUAGE, currentCourse);
        editor.putString(KEY_USER_ID, userId);
        editor.apply();
}

    public boolean setNavigationDrawer(){
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);

        View headerView = navigationView.getHeaderView(0);

        TextView user = headerView.findViewById(R.id.currentUser);

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
                }  else if (itemId == R.id.testActivity) {
                    startActivity(new Intent(BaseActivity.this, PracticeActivity.class));
                } else if (itemId == R.id.lecture) {
                    startActivity(new Intent(BaseActivity.this, StudyActivity.class));
                } else if (itemId == R.id.admin) {
                    startActivity(new Intent(BaseActivity.this, AdminDashboard.class));
                } else if (itemId == R.id.chat) {
                    startActivity(new Intent(BaseActivity.this, SocialActivity.class));
                } else if (itemId == R.id.settings) {

                    Intent intent = new Intent(BaseActivity.this, UserAccountActivity.class);
                    intent.putExtra("openFragment", "SettingsMenuList");
                    intent.putExtra("isNavigation", true);
                    startActivity(intent);

                } else if (itemId == R.id.logut) {
                    mAuth.signOut();
                    startActivity(new Intent(BaseActivity.this, UserAccountActivity.class));
                } else if (itemId == R.id.profile) {
                    startActivity(new Intent(BaseActivity.this, UserAccountActivity.class));
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

    // Method to check internet connectivity
    private void checkInternetConnection() {
        // Avoid checking internet in StudyActivity to prevent a loop
        if (!getCurrentActivityName().equals(StudyActivity.class.getSimpleName())) {
            if (!isInternetAvailable()) {
                // Navigate to StudyActivity if no internet
                Intent intent = new Intent(BaseActivity.this, StudyActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
            }
        }
    }

    // Helper method to check network status
    private boolean isInternetAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();
    }

    // Method to get the name of the current activity
    private String getCurrentActivityName() {
        return this.getClass().getSimpleName();
    }

    public void applyTheme(){
        // Load the saved theme from SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("AppPreferences", Context.MODE_PRIVATE);
        String theme = sharedPreferences.getString("theme", "system");

        if ("light".equals(theme)) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        } else if ("dark".equals(theme)) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);
        }
    }

    @Override
    public String getUserId() {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        return currentUser != null ? currentUser.getUid() : null;
    }
}
