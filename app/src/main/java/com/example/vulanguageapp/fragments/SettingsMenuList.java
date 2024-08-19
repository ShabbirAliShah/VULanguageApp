package com.example.vulanguageapp.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;

import com.example.vulanguageapp.R;
import com.example.vulanguageapp.adapters.CustomExpandableListAdapter;
import com.example.vulanguageapp.databinding.FragmentSettingsMenuBinding;

import java.util.ArrayList;
import java.util.List;

public class SettingsMenuList extends Fragment {

    FragmentSettingsMenuBinding binding;

    public SettingsMenuList() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentSettingsMenuBinding.inflate(inflater, container, false);

        List<String> groupList = new ArrayList<>();
        List<List<String>> childList = new ArrayList<>();

        // Add groups and children
        groupList.add("General Settings");
        groupList.add("Notifications");

        List<String> generalItems = new ArrayList<>();
        generalItems.add("Profile");
        generalItems.add("Language");
        generalItems.add("Theme");
        generalItems.add("Security");

        List<String> notificationItems = new ArrayList<>();
        notificationItems.add("Email Notifications");
        notificationItems.add("Push Notifications");

        childList.add(generalItems);
        childList.add(notificationItems);

        // Set up the ExpandableListView
        ExpandableListView expandableListView = binding.expandableListView;
        CustomExpandableListAdapter adapter = new CustomExpandableListAdapter(getContext(), groupList, childList);
        expandableListView.setAdapter(adapter);

        expandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                String selectedChild = (String) adapter.getChild(groupPosition, childPosition);

                // Navigate to another fragment and pass the selected child data
                Bundle bundle = new Bundle();
                bundle.putString("selected_child", selectedChild);

                Fragment fragment = new SettingsGeneralFragment(); // Replace with your target fragment
                fragment.setArguments(bundle);

                FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.userAccountFragmentContainerView, fragment); // Replace with your container id
                transaction.addToBackStack(null);
                transaction.commit();

                return true;
            }
        });


        // Inflate the layout for this fragment
        return binding.getRoot();
    }
}