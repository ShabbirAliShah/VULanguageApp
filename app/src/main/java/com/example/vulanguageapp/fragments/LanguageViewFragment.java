package com.example.vulanguageapp.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.vulanguageapp.R;
import com.example.vulanguageapp.adapters.LanguageAdapter;
import com.example.vulanguageapp.databinding.FragmentLanguageViewBinding;
import com.example.vulanguageapp.interfaces.LanguageActivityFragmentClickListener;
import com.example.vulanguageapp.models.Language_Data_Model;

import java.util.ArrayList;

public class LanguageViewFragment extends Fragment implements LanguageActivityFragmentClickListener {

    private ArrayList<Language_Data_Model> dataList = new ArrayList<>();
    private FragmentLanguageViewBinding binding;

    private Bundle languageDataBundle(String languageName, String description) {
        Bundle bundle = new Bundle();
        bundle.putString("language_name", languageName);
        bundle.putString("description", description);
        return bundle;
    }

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {

        binding = FragmentLanguageViewBinding.inflate(inflater, container, false);
        return binding.getRoot();

    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        dataList = new ArrayList<>();
        dataList.add(new Language_Data_Model( "Urdu", "National Language of Pakistan"));
        dataList.add(new Language_Data_Model( "Pashto", "Khyber Pakhtunkhwa, Balochistan"));
        dataList.add(new Language_Data_Model( "Punjabi", "Spoken in Punjab"));
        dataList.add(new Language_Data_Model( "Sindhi", "Spoken in Sindh"));
        dataList.add(new Language_Data_Model( "Balochi", "Spoken in Balochistan"));
        dataList.add(new Language_Data_Model( "Balti", "Spoken in Gilgit Baltistan"));

        RecyclerView recyclerView = view.findViewById(R.id.languageRecyclerView);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));

        LanguageAdapter langAdapter = new LanguageAdapter(dataList, this);
        recyclerView.setAdapter(langAdapter);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onLanguageCardClick(View view) {


        String languageName = "Urdu";
        String description = "Basic Urdu Course...";
        Bundle dataBundle = languageDataBundle(languageName, description);

        NavHostFragment.findNavController(LanguageViewFragment.this)
                .navigate(R.id.action_languageViewFragment_to_languageDetailFragment, dataBundle);

    }
}