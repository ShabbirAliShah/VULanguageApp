package com.example.vulanguageapp.adminControlls.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.vulanguageapp.R;
import com.example.vulanguageapp.databinding.FragmentAddLessonsBinding;
import com.example.vulanguageapp.databinding.FragmentDashBoardBinding;
import com.example.vulanguageapp.fragments.ChatConversationFragment;

public class DashBoard extends Fragment {

    private FragmentDashBoardBinding binding;


    public DashBoard() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

         binding = FragmentDashBoardBinding.inflate(inflater, container, false);


        // Inflate the layout for this fragment
        return binding.getRoot();
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }
}