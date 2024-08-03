package com.example.vulanguageapp.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.vulanguageapp.adapters.ActivityAdapter;
import com.example.vulanguageapp.databinding.FragmentLanguageHomeBinding;
import com.example.vulanguageapp.models.Language_Data_Model;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class LanguageHomeFragment extends Fragment {

    private ArrayList<Language_Data_Model> dataList = new ArrayList<>();
    private FragmentLanguageHomeBinding binding;
    private LinearLayout hm;

    public LanguageHomeFragment() {
        // Required empty public constructor
    }

    RecyclerView recyclerView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentLanguageHomeBinding.inflate(inflater, container, false);

        recyclerView = binding.langList;
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));


        return binding.getRoot();

    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        FirebaseDatabase.getInstance().getReference("languages")
                .addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                dataList.clear();

                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Language_Data_Model languageDataModel = dataSnapshot.getValue(Language_Data_Model.class);
                    dataList.add(languageDataModel);
                }

                ActivityAdapter langAdapter = new ActivityAdapter(dataList, requireActivity());
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

//       LinearLayout crHm = view.findViewById(R.id.cardHm);
//
//       crHm.setOnClickListener(new View.OnClickListener() {
//           @Override
//           public void onClick(View v) {
//
//               Toast.makeText(getActivity(), "you clicked", Toast.LENGTH_SHORT).show();
//
//           }
//       });

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private Bundle languageDataBundle(String languageName, String description) {
        Bundle bundle = new Bundle();
        bundle.putString("language_name", languageName);
        bundle.putString("description", description);
        return bundle;
    }

//    @Override
//    public void onLangCardClick(View view) {
//        String languageName = "Urdu";
//        String description = "Basic Urdu Course...";
//        Bundle dataBundle = languageDataBundle(languageName, description);
//
//        NavHostFragment.findNavController(LanguageHomeFragment.this).navigate(R.id.action_languageHomeFragment_to_languageDetailFragment, dataBundle);
//    }
}