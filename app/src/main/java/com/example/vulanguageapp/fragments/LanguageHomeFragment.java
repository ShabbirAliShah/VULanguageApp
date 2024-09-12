package com.example.vulanguageapp.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.vulanguageapp.R;
import com.example.vulanguageapp.adapters.ActivityAdapter;
import com.example.vulanguageapp.adapters.EnrollmentTopicListAdapter;
import com.example.vulanguageapp.models.CourseModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class LanguageHomeFragment extends Fragment {

    private ArrayList<CourseModel> dataList = new ArrayList<>();

    public LanguageHomeFragment() {
        // Required empty public constructor
    }

    RecyclerView recyclerView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_language__home_, container, false);

        recyclerView = view.findViewById(R.id.langList);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));

        return view;
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

       NavController navController = NavHostFragment.findNavController(this);

        FirebaseDatabase.getInstance().getReference("courses").addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                dataList.clear();

                ActivityAdapter activityAdapter = new ActivityAdapter(dataList, navController);
                recyclerView.setAdapter(activityAdapter);

                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {

                    CourseModel courseDataModel = dataSnapshot.getValue(CourseModel.class);

                    if (courseDataModel != null) {
                        String courseId = dataSnapshot.getKey();
                        Log.d("Language Home Fragment", "courseId" + courseId);
                        courseDataModel.setKey(courseId);

                        dataList.add(courseDataModel);
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle error
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}