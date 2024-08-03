package com.example.vulanguageapp.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.vulanguageapp.R;
import com.example.vulanguageapp.adapters.EnrollmentTopicListAdapter;
import com.example.vulanguageapp.models.Language_Data_Model;

import java.util.ArrayList;

public class TopicToEnrollFragment extends Fragment {

    private ArrayList<Language_Data_Model> dataList = new ArrayList<>();

    public TopicToEnrollFragment(){

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        dataList = new ArrayList<>();
        dataList.add(new Language_Data_Model( "Lecture 1: This is the first lecture of Urdu","Just test",""));

    }
    RecyclerView recyclerView;

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {


        View view = inflater.inflate(R.layout.fragment_topic_to_enroll, container, false);
        recyclerView = view.findViewById(R.id.topic_list_to_enroll);
        //recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        EnrollmentTopicListAdapter langAdapter = new EnrollmentTopicListAdapter(dataList, getContext());
        recyclerView.setAdapter(langAdapter);

        return view;

    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        recyclerView = null;
    }
}