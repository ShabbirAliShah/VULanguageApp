package com.example.vulanguageapp.adminControlls.fragments;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class FragmentAdapter extends FragmentStateAdapter {

    private final String[] fragmentTitles = {"Dashboard", "Add Lessons", "Create Course"};
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
