package com.example.vulanguageapp.activities;

import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ExpandableListView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;

import com.example.vulanguageapp.R;
import com.example.vulanguageapp.adapters.CustomExpandableListAdapter;
import com.example.vulanguageapp.fragments.SettingsGeneralFragment;
import com.example.vulanguageapp.fragments.UserLoginFragment;
import com.example.vulanguageapp.interfaces.UserIdProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

//Fragments contained in this activity;
//UserLoginFragment, UserRegistrationFragment, SettingsGeneralFragment;
public class UserAccountActivity extends AppCompatActivity implements UserIdProvider {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_account);


        Intent intent = getIntent();
        if (intent != null) {
            String fragmentToOpen = intent.getStringExtra("openFragment");
            boolean isNavigation = intent.getBooleanExtra("isNavigation", false);

            if ("SettingsMenuList".equals(fragmentToOpen) && isNavigation) {
                Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.userAccountFragmentContainerView);

                if (fragment instanceof NavHostFragment) {
                    NavHostFragment navHostFragment = (NavHostFragment) fragment;
                    NavController navController = navHostFragment.getNavController();
                    navController.navigate(R.id.settingsMenuList);
                } else {
                    Log.e("Navigation Error", "Fragment is not a NavHostFragment.");
                }
            }
        }


    }


    @Override
    public String getUserId() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            return user.getUid();  // Return the Firebase User ID (UID)
        } else {
            return null;  // Return null if the user is not logged in
        }
    }
}