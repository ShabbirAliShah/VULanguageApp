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
import com.example.vulanguageapp.interfaces.UserIdProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class UserAccountActivity extends AppCompatActivity implements UserIdProvider {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_account);


//        Intent intent = getIntent();
//        if (intent != null) {
//            String fragmentToOpen = intent.getStringExtra("openFragment");
//            boolean isNavigation = intent.getBooleanExtra("isNavigation", false); // Add this line
//            if ("SettingsMenuList".equals(fragmentToOpen) && isNavigation) { // Modify this line
//                // Navigate to SettingsFragment
//                NavHostFragment navHostFragment =
//                        (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.userAccountFragmentContainerView);
//                NavController navController = navHostFragment.getNavController();
//                navController.navigate(R.id.settingsMenuList);
//            }
//        }

        Intent intent = getIntent();
        if (intent != null) {
            String fragmentToOpen = intent.getStringExtra("openFragment");
            boolean isNavigation = intent.getBooleanExtra("isNavigation", false);

            if ("SettingsMenuList".equals(fragmentToOpen) && isNavigation) {
                // Ensure that the fragment retrieved is a NavHostFragment
                Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.userAccountFragmentContainerView);

                if (fragment instanceof NavHostFragment) {
                    NavHostFragment navHostFragment = (NavHostFragment) fragment;
                    NavController navController = navHostFragment.getNavController();
                    navController.navigate(R.id.settingsMenuList);
                } else {
                    // Handle case when it's not a NavHostFragment
                    Log.e("Navigation Error", "Fragment is not a NavHostFragment.");
                }
            }
        }


    }

    @Override
    public String getUserId() {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        return currentUser != null ? currentUser.getUid() : null;
    }
}