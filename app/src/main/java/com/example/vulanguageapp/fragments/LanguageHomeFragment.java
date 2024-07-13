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
import com.example.vulanguageapp.adapters.ActivityAdapter;
import com.example.vulanguageapp.databinding.FragmentLanguageHomeBinding;
import com.example.vulanguageapp.interfaces.MainActivityFragmentClickListener;
import com.example.vulanguageapp.models.Language_Data_Model;

import java.util.ArrayList;

public class LanguageHomeFragment extends Fragment implements MainActivityFragmentClickListener {

    private ArrayList<Language_Data_Model> dataList = new ArrayList<>();
    private FragmentLanguageHomeBinding binding;

    public LanguageHomeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    RecyclerView recyclerView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentLanguageHomeBinding.inflate(inflater, container, false);
        return binding.getRoot();

    }
        public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
            super.onViewCreated(view, savedInstanceState);

            dataList = new ArrayList<>();
            dataList.add(new Language_Data_Model("1","Urdu", "Pakistan"));
            dataList.add(new Language_Data_Model("2","Pashto", "Afghanistan"));
            dataList.add(new Language_Data_Model("3","Hindi", "India"));
            dataList.add(new Language_Data_Model("4","Farsi", "Iran"));
            dataList.add(new Language_Data_Model("5","Chinese", "China"));

            recyclerView = view.findViewById(R.id.langList);
            recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));

            ActivityAdapter langAdapter = new ActivityAdapter(dataList, this);
            recyclerView.setAdapter(langAdapter);
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

    @Override
    public void onLangCardClick(View view) {
        String languageName = "Urdu";
        String description = "Basic Urdu Course...";
        Bundle dataBundle = languageDataBundle(languageName, description);

        NavHostFragment.findNavController(LanguageHomeFragment.this)
                .navigate(R.id.action_languageHomeFragment_to_languageDetailFragment, dataBundle);
    }
}