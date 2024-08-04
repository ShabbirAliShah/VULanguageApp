package com.example.vulanguageapp.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.vulanguageapp.R;
import com.example.vulanguageapp.adapters.StudyTopicListAdapter;
import com.example.vulanguageapp.databinding.FragmentTopicsToStudyBinding;
import com.example.vulanguageapp.models.Language_Data_Model;

import java.util.ArrayList;

public class TopicsToStudyFragment extends Fragment{

    private FragmentTopicsToStudyBinding binding;
    private ArrayList<Language_Data_Model> studyTopicListData = new ArrayList<>();
    private Context context;
    private CardView cardViewClk;

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {

        binding = FragmentTopicsToStudyBinding.inflate(inflater, container, false);


        return binding.getRoot();
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        studyTopicListData = new ArrayList<>();
        studyTopicListData.add(new Language_Data_Model( "Pakistan", "Urdu", "Urdu topic one,"));

        RecyclerView  recyclerView = view.findViewById(R.id.topic_list_to_study);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        StudyTopicListAdapter stdTopicListAdapter = new StudyTopicListAdapter(studyTopicListData, requireContext());
        recyclerView.setAdapter(stdTopicListAdapter);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}