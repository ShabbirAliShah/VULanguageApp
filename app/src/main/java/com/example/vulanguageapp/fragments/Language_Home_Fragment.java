package com.example.vulanguageapp.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.vulanguageapp.R;
import com.example.vulanguageapp.adapters.ActivityAdapter;
import com.example.vulanguageapp.models.Language_Data_Model;

import java.util.ArrayList;

public class Language_Home_Fragment extends Fragment {

    private ArrayList<Language_Data_Model> dataList = new ArrayList<>();

    public Language_Home_Fragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        dataList = new ArrayList<>();
        dataList.add(new Language_Data_Model("1", "Urdu", "Pakistan"));
        dataList.add(new Language_Data_Model("2", "Urdu", "Pakistan"));
        dataList.add(new Language_Data_Model("3", "Urdu", "Pakistan"));
        dataList.add(new Language_Data_Model("4", "Urdu", "Pakistan"));
        dataList.add(new Language_Data_Model("5", "Urdu", "Pakistan"));
        dataList.add(new Language_Data_Model("6", "Urdu", "Pakistan"));
        dataList.add(new Language_Data_Model("1", "Urdu", "Pakistan"));
        dataList.add(new Language_Data_Model("2", "Urdu", "Pakistan"));
        dataList.add(new Language_Data_Model("3", "Urdu", "Pakistan"));
        dataList.add(new Language_Data_Model("4", "Urdu", "Pakistan"));
        dataList.add(new Language_Data_Model("5", "Urdu", "Pakistan"));
        dataList.add(new Language_Data_Model("6", "Urdu", "Pakistan"));
        dataList.add(new Language_Data_Model("1", "Urdu", "Pakistan"));
        dataList.add(new Language_Data_Model("2", "Urdu", "Pakistan"));
        dataList.add(new Language_Data_Model("3", "Urdu", "Pakistan"));
        dataList.add(new Language_Data_Model("4", "Urdu", "Pakistan"));
        dataList.add(new Language_Data_Model("5", "Urdu", "Pakistan"));
        dataList.add(new Language_Data_Model("6", "Urdu", "Pakistan"));

    }

    RecyclerView recyclerView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_language__home_, container, false);
        recyclerView = view.findViewById(R.id.langList);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));

        ActivityAdapter langAdapter = new ActivityAdapter(dataList, getContext());
        recyclerView.setAdapter(langAdapter);

        return view;

    }
}