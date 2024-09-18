package com.example.vulanguageapp.fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
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
import com.example.vulanguageapp.activities.BaseActivity;
import com.example.vulanguageapp.adapters.EnrolledAdapter;
import com.example.vulanguageapp.databinding.FragmentEnrolledCourseBinding;
import com.example.vulanguageapp.interfaces.UserIdProvider;
import com.example.vulanguageapp.models.EnrollmentModel;
import com.example.vulanguageapp.roomDatabase.EnrollmentDatabase;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class EnrolledCourseFragment extends Fragment {

    private RecyclerView recyclerView;
    private NavController navController;
    private String userId;
    private UserIdProvider userIdProvider;
    private ArrayList<EnrollmentModel> enrollmentDataList = new ArrayList<>();
    private EnrolledAdapter enrolledAdapter;

    private EnrollmentDatabase enrollmentDatabase;

    public EnrolledCourseFragment() {}

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        if (context instanceof UserIdProvider) {
            userIdProvider = (UserIdProvider) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement UserIdProvider");
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (userIdProvider != null) {
            userId = userIdProvider.getUserId();
        }

        enrollmentDatabase = EnrollmentDatabase.getInstance(requireContext());

        Toast.makeText(getContext(), "User ID is " + userId, Toast.LENGTH_SHORT).show();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        FragmentEnrolledCourseBinding binding = FragmentEnrolledCourseBinding.inflate(inflater, container, false);

        recyclerView = binding.enrolledCoursesRv;
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Initialize NavController
        navController = NavHostFragment.findNavController(this);

        // Initialize adapter
        enrolledAdapter = new EnrolledAdapter(navController, enrollmentDataList);
        recyclerView.setAdapter(enrolledAdapter);

        // Button to sync data from Firebase to Room
        binding.syncButton.setOnClickListener(v -> fetchEnrolledCoursesFromFirebase());

        // Button to clear Room database
        binding.clearButton.setOnClickListener(v -> clearRoomDatabase());

        // Fetch data (check for internet first)
        if (isConnected()) {
            fetchEnrolledCoursesFromFirebase();
        } else {
            fetchEnrolledCoursesFromRoom();
        }

        return binding.getRoot();
    }

    private void fetchEnrolledCoursesFromFirebase() {
        DatabaseReference enrollmentNode = FirebaseDatabase.getInstance().getReference("enrollments");
        Query query = enrollmentNode.orderByChild("userId").equalTo(userId);

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                enrollmentDataList.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    EnrollmentModel enrollmentData = dataSnapshot.getValue(EnrollmentModel.class);
                    if (enrollmentData != null) {
                        enrollmentData.setEnrollmentId(dataSnapshot.getKey());
                        enrollmentDataList.add(enrollmentData);
                        // Save to Room database
                        saveEnrollmentToRoom(enrollmentData);
                    }
                }
                enrolledAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), "Failed to load data: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void saveEnrollmentToRoom(EnrollmentModel enrollment) {
        AsyncTask.execute(() -> enrollmentDatabase.enrollmentDao().insert(enrollment));
        Toast.makeText(getContext(), "successfully synced with firebase", Toast.LENGTH_SHORT).show();

    }

    private void fetchEnrolledCoursesFromRoom() {
        AsyncTask.execute(() -> {
            List<EnrollmentModel> enrollmentList = enrollmentDatabase.enrollmentDao().getAllEnrollments();
            getActivity().runOnUiThread(() -> {
                if (enrollmentList.isEmpty()) {
                    Toast.makeText(getContext(), "No data synced. Please sync with internet.", Toast.LENGTH_SHORT).show();
                } else {
                    enrollmentDataList.clear();
                    enrollmentDataList.addAll(enrollmentList);
                    enrolledAdapter.notifyDataSetChanged();
                }
            });
        });
    }

    private void clearRoomDatabase() {
        AsyncTask.execute(() -> enrollmentDatabase.enrollmentDao().clearTable());
        Toast.makeText(getContext(), "Room database cleared.", Toast.LENGTH_SHORT).show();
    }

    private boolean isConnected() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = connectivityManager.getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.isConnected();
    }
}


//public class EnrolledCourseFragment extends Fragment {
//
//    private RecyclerView recyclerView;
//    private NavController navController;
//    private String userId;
//    private UserIdProvider userIdProvider;
//    private ArrayList<EnrollmentModel> enrollmentDataList = new ArrayList<>();
//    private EnrolledAdapter enrolledAdapter;
//
//    public EnrolledCourseFragment() {
//        // Required empty public constructor
//    }
//
//    @Override
//    public void onAttach(@NonNull Context context) {
//        super.onAttach(context);
//
//        if (context instanceof UserIdProvider) {
//            userIdProvider = (UserIdProvider) context;
//        } else {
//            throw new RuntimeException(context.toString() + " must implement UserIdProvider");
//        }
//    }
//
//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//
//        if (userIdProvider != null) {
//            userId = userIdProvider.getUserId();
//        }
//
//        Toast.makeText(getContext(), "User ID is " + userId, Toast.LENGTH_SHORT).show();
//    }
//
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container,
//                             Bundle savedInstanceState) {
//        FragmentEnrolledCourseBinding binding = FragmentEnrolledCourseBinding.inflate(inflater, container, false);
//
//        recyclerView = binding.enrolledCoursesRv;
//        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
//
//        // Initialize NavController from FragmentManager
//        navController = NavHostFragment.findNavController(this);
//
//        // Initialize adapter
//        enrolledAdapter = new EnrolledAdapter(navController, enrollmentDataList);
//        recyclerView.setAdapter(enrolledAdapter);
//
//        // Fetch enrolled courses
//        fetchEnrolledCourses();
//
//        return binding.getRoot();
//    }
//
//    private void fetchEnrolledCourses() {
//        DatabaseReference enrollmentNode = FirebaseDatabase.getInstance().getReference("enrollments");
//        Query query = enrollmentNode.orderByChild("userId").equalTo(userId);
//
//        query.addListenerForSingleValueEvent(new ValueEventListener() {
//            @SuppressLint("NotifyDataSetChanged")
//
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                enrollmentDataList.clear(); // Clear the list to avoid duplication
//
//                String enrollmentKey = snapshot.getKey();
//
//                if (enrollmentKey != null) {  // Add a null check for enrollmentKey
//                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
//                        EnrollmentModel enrollmentData = dataSnapshot.getValue(EnrollmentModel.class);
//                        if (enrollmentData != null) {
//                            enrollmentData.setEnrollmentId(enrollmentKey); // Set enrollmentId only if enrollmentData is not null
//                            enrollmentDataList.add(enrollmentData);
//                        }
//                    }
//                } else {
//                    Log.e("onDataChange", "Enrollment key is null.");
//                }
//
//                enrolledAdapter.notifyDataSetChanged(); // Notify adapter about the data change
//            }
//
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//                // Handle potential errors here
//                Toast.makeText(getContext(), "Failed to load data: " + error.getMessage(), Toast.LENGTH_SHORT).show();
//            }
//        });
//    }
//}
