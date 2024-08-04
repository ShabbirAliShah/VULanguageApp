package com.example.vulanguageapp.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.vulanguageapp.adapters.LanguageAdapter;
import com.example.vulanguageapp.databinding.FragmentLanguageViewBinding;
import com.example.vulanguageapp.models.Language_Data_Model;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class LanguageViewFragment extends Fragment{

    private ArrayList<Language_Data_Model> dataList = new ArrayList<>();
    private FragmentLanguageViewBinding binding;
    private Context context;

    private Bundle languageDataBundle(String languageName, String description) {
        Bundle bundle = new Bundle();
        bundle.putString("language_name", languageName);
        bundle.putString("description", description);
        return bundle;
    }

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceStat) {

        binding = FragmentLanguageViewBinding.inflate(inflater, container, false);

        RecyclerView recyclerView = binding.langRecyclerView;
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));

        FirebaseDatabase.getInstance().getReference("languages")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        dataList.clear();

                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                            Language_Data_Model languageDataModel = dataSnapshot.getValue(Language_Data_Model.class);
                            dataList.add(languageDataModel);
                        }

                        LanguageAdapter langAdapter = new LanguageAdapter(dataList, context);
                        recyclerView.setAdapter(langAdapter);

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        String errorMessage = error.getMessage();
                        int errorCode = error.getCode();

                        // Handle the error (e.g., display a toast with more specific information)
                        Toast.makeText(getContext(), "Error fetching data: " + errorMessage, Toast.LENGTH_LONG).show();
                        Toast.makeText(getContext(), "Error fetching data: " + errorCode, Toast.LENGTH_LONG).show();

                    }

                });

        return binding.getRoot();
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

//    @Override
//    public void onClick(View v) {
//
//        String languageName = "Urdu";
//        String description = "Basic Urdu Course...";
//        Bundle dataBundle = languageDataBundle(languageName, description);
//
//        NavHostFragment.findNavController(LanguageViewFragment.this)
//                .navigate(R.id.action_languageViewFragment_to_languageDetailFragment, dataBundle);
//
//    }
}