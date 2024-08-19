package com.example.vulanguageapp.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.vulanguageapp.R;
import com.example.vulanguageapp.activities.BaseActivity;
import com.example.vulanguageapp.adapters.EnrollmentTopicListAdapter;
import com.example.vulanguageapp.databinding.FragmentLanguageDetailBinding;
import com.example.vulanguageapp.models.Enrollment;
import com.example.vulanguageapp.models.LessonsModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class LanguageDetailFragment extends Fragment {

    private FragmentLanguageDetailBinding binding;
    private RecyclerView lessonsRecyclerView;
    private List<LessonsModel> dataList = new ArrayList<>();

    public LanguageDetailFragment() {
    }

    private TextView languageName, courseTitle, courseLevel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentLanguageDetailBinding.inflate(inflater, container, false);

        lessonsRecyclerView = binding.lessonsRecyclerView;
        lessonsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        return binding.getRoot();

    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Button wishList = view.findViewById(R.id.wishlist);
        Button enrolll = view.findViewById(R.id.enroll);

        NavController navController = NavHostFragment.findNavController(this);

        wishList.setOnClickListener(v -> {
            navController.navigate(R.id.action_languageDetailFragment_to_wishListFragment);
        });

        courseTitle = view.findViewById(R.id.courseTitle);
        languageName = view.findViewById(R.id.languageCountry);
        courseLevel = view.findViewById(R.id.courseDescription);

        if (getArguments() != null) {
            String name = getArguments().getString("language_name");
            String title = getArguments().getString("course_title");
            String level = getArguments().getString("course_level");
            String courseId = getArguments().getString("course_id");


            Log.d("debug", "Course Id " + courseId);

            Query query = FirebaseDatabase.getInstance().getReference("lessons").orderByChild("courseId").equalTo(courseId);

            query.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {

                    dataList.clear();

                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {

                        LessonsModel lessonsModel = dataSnapshot.getValue(LessonsModel.class);
                        String key = dataSnapshot.getKey();
                        lessonsModel.setLessonId(key);
                        if (lessonsModel != null) {
                            dataList.add(lessonsModel);
                        }
                    }

                    EnrollmentTopicListAdapter enrollmentTopicListAdapter = new EnrollmentTopicListAdapter(dataList, getContext(), false);
                    lessonsRecyclerView.setAdapter(enrollmentTopicListAdapter);

                    enrollmentTopicListAdapter.notifyDataSetChanged();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    // Log the error if the query is cancelled or fails
                    Log.e("FirebaseDebug by shabeer", "DatabaseError: " + error.getMessage());
                }
            });

            enrolll.setOnClickListener(v -> {

                Bundle bundle = new Bundle();
                bundle.putSerializable("lessonsData", (Serializable) dataList);
                bundle.putString("courseId", courseId);
                bundle.putString("course_title", title);
                bundle.putString("userId", ((BaseActivity) getActivity()).getUserId());

                navController.navigate(R.id.action_languageDetailFragment_to_topicToEnrollFragment, bundle);
            });

            courseTitle.setText(title);
            courseLevel.setText(level);
            languageName.setText(name);

        } else {
            courseTitle.setText("No data found");
            courseLevel.setText("No data found");
            languageName.setText("No data found");
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}