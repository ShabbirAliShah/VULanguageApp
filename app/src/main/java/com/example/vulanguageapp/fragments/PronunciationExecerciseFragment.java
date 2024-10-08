package com.example.vulanguageapp.fragments;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.vulanguageapp.R;
import com.example.vulanguageapp.adapters.PronunciationAdapter;
import com.example.vulanguageapp.databinding.FragmentPronunciationExecerciseBinding;
import com.example.vulanguageapp.models.PronunciationModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class PronunciationExecerciseFragment extends Fragment {
    private List<PronunciationModel> vocabDataList = new ArrayList<>();
    private NavController navController;
    private RecyclerView recyclerView;
    private PronunciationAdapter pronunciationAdapter;
    private PronunciationModel pronunciationModel;
    private static final int REQUEST_RECORD_AUDIO_PERMISSION = 200;
    private boolean permissionToRecordAccepted = false;
    private String[] permissions = {Manifest.permission.RECORD_AUDIO};
    private Context context;


    public PronunciationExecerciseFragment(Context context) {
        this.context = context;
    }
    public PronunciationExecerciseFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityCompat.requestPermissions(requireActivity(), permissions, REQUEST_RECORD_AUDIO_PERMISSION);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        FragmentPronunciationExecerciseBinding binding = FragmentPronunciationExecerciseBinding.inflate(inflater, container, false);

        navController = NavHostFragment.findNavController(this);

        recyclerView = binding.vocabRecycler;
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (getArguments() != null) {

            String lessonId = getArguments().getString("lesson_id");

            Toast.makeText(getContext(), "Lesson Id is " + lessonId, Toast.LENGTH_SHORT).show();

            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("vocabulary");
            Query query = databaseReference.orderByChild("lessonId").equalTo(lessonId);

            query.addListenerForSingleValueEvent(new ValueEventListener() {

                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {

                    if (snapshot.exists()) {
                        Log.d("Pronunciation Exercise", "Snapshot exist");
                    }

                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {

                        pronunciationModel = dataSnapshot.getValue(PronunciationModel.class);

                        if (pronunciationModel != null) {
                            vocabDataList.add(pronunciationModel);
                        }
                    }

                    pronunciationAdapter = new PronunciationAdapter(vocabDataList, context, "Hindi");
                    recyclerView.setAdapter(pronunciationAdapter);

                    pronunciationAdapter.notifyDataSetChanged();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }else {
            Toast.makeText(getContext(), "No lesson Id found or you may came wrong way.", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_RECORD_AUDIO_PERMISSION) {
            permissionToRecordAccepted = grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED;
        }
    }
}