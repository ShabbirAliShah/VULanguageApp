package com.example.vulanguageapp.adminControlls.adminAdapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.vulanguageapp.adminControlls.fragments.AddLessons;
import com.example.vulanguageapp.adminControlls.fragments.Create_Course;
import com.example.vulanguageapp.adminControlls.fragments.DashBoard;
import com.example.vulanguageapp.adminControlls.fragments.Manage;

public class FragmentAdapter extends FragmentStateAdapter {

    private final String[] fragmentTitles = {"Dashboard", "Add Lessons", "Create Course", "Manage"};
    public FragmentAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
                return new DashBoard();
            case 1:
                return new AddLessons();
            case 2:
                return new Create_Course();
            case 3:
                return new Manage();
            default:
                return new DashBoard();
        }
    }

    @Override
    public int getItemCount() {
        return fragmentTitles.length;
    }

    public String getPageTitle(int position) {
        return fragmentTitles[position];
    }
}