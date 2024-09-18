package com.example.vulanguageapp.adminControlls.fragments;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.vulanguageapp.R;
import com.example.vulanguageapp.adapters.EnrollmentTopicListAdapter;
import com.example.vulanguageapp.adminControlls.adminAdapters.LessonsAdapter;
import com.example.vulanguageapp.databinding.FragmentManageBinding;
import com.example.vulanguageapp.models.LessonsModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class Manage extends Fragment {

    private RecyclerView lessonsRecyclerView;
    private FragmentManageBinding binding;
    private List<LessonsModel> dataList = new ArrayList<>();
    private LessonsAdapter lessonsAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentManageBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Initialize RecyclerView
        lessonsRecyclerView = binding.recyclerforlessons;
        lessonsRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));

        // Set up adapter and attach it to RecyclerView
        lessonsAdapter = new LessonsAdapter(dataList, requireContext());
        lessonsRecyclerView.setAdapter(lessonsAdapter);

        // Fetch lessons
        fetchCourseLesson();
    }

    public void fetchCourseLesson() {
        Query query = FirebaseDatabase.getInstance().getReference("lessons");

        query.addValueEventListener(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                dataList.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    LessonsModel lessonsModel = dataSnapshot.getValue(LessonsModel.class);
                    if (lessonsModel != null) {
                        lessonsModel.setLessonId(dataSnapshot.getKey()); // Set the Firebase key as the lesson ID
                        dataList.add(lessonsModel);
                    }
                }
                lessonsAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("FirebaseDebug", "DatabaseError: " + error.getMessage());
            }
        });
    }
}
