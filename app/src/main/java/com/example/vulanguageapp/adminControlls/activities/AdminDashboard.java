package com.example.vulanguageapp.adminControlls.activities;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.viewpager2.widget.ViewPager2;

import com.example.vulanguageapp.R;
import com.example.vulanguageapp.adminControlls.adminAdapters.FragmentAdapter;
import com.example.vulanguageapp.databinding.ActivityAdminDashboardBinding;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class AdminDashboard extends AppCompatActivity {

    private NavController navController;
    private AppBarConfiguration appBarConfiguration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);

        ActivityAdminDashboardBinding binding = ActivityAdminDashboardBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        getSupportActionBar().hide();
        ViewPager2 viewPager = findViewById(R.id.viewPager);
        TabLayout tabLayout = findViewById(R.id.tabLayout);

        FragmentAdapter adapter = new FragmentAdapter(this);
        viewPager.setAdapter(adapter);

        new TabLayoutMediator(tabLayout, viewPager, new TabLayoutMediator.TabConfigurationStrategy() {
            @Override
            public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                tab.setText(adapter.getPageTitle(position)); // Set tab title
            }
        }).attach();

//        NavHostFragment navHostFragment =
//                (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment_content_dashboard);
//        NavController navController = navHostFragment.getNavController();
//
//
//        binding.goToManage.setOnClickListener(v ->{
//                    navController.navigate(R.id.action_dashBoard_to_manage);
//        });

//        binding.createCourse.setOnClickListener(v->{
//            navController.navigate(R.id.action_dashBoard_to_create_Course);
//        });
    }


//    @Override
//    public boolean onSupportNavigateUp() {
//        return NavigationUI.navigateUp(navController, appBarConfiguration)
//                || super.onSupportNavigateUp();
//    }


}
