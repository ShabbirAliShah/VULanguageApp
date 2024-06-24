package com.example.vulanguageapp.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.vulanguageapp.R;
import com.example.vulanguageapp.adapters.LanguageAdapter;
import com.example.vulanguageapp.models.Language_Data_Model;

import java.util.ArrayList;


public class LanguageViewFragment extends Fragment {
    private ArrayList<Language_Data_Model> dataList = new ArrayList<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        dataList = new ArrayList<>();
        dataList.add(new Language_Data_Model( "Urdu", "Pakistan"));
        dataList.add(new Language_Data_Model( "Pashto", "Afghanistan"));
        dataList.add(new Language_Data_Model( "Hindi", "India"));
        dataList.add(new Language_Data_Model( "Arabic", "KSA"));

    }
    RecyclerView recyclerView;

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {

//        binding = FragmentFirstBinding.inflate(inflater, container, false);
//        return binding.getRoot();


        View view = inflater.inflate(R.layout.fragment_language_view, container, false);
        recyclerView = view.findViewById(R.id.languageRecyclerView);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));

        LanguageAdapter langAdapter = new LanguageAdapter(dataList, getContext());
        recyclerView.setAdapter(langAdapter);

        return view;

    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Button buttonFirst = view.findViewById(R.id.button_first);

        buttonFirst.setOnClickListener(v ->
                NavHostFragment.findNavController(LanguageViewFragment.this)
                        .navigate(R.id.action_languageViewFragment_to_languageDetailFragment)
        );
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        recyclerView = null;
    }
}