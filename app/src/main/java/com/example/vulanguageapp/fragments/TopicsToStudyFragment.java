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
import com.example.vulanguageapp.adapters.StudyTopicListAdapter;
import com.example.vulanguageapp.databinding.FragmentTopicsToStudyBinding;
import com.example.vulanguageapp.models.Language_Data_Model;

import java.util.ArrayList;

public class TopicsToStudyFragment extends Fragment {

    private FragmentTopicsToStudyBinding binding;

    private ArrayList<Language_Data_Model> dataList = new ArrayList<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



        dataList = new ArrayList<>();
        dataList.add(new Language_Data_Model( "Language name as a topic 1"));
        dataList.add(new Language_Data_Model( "Card view of language is used"));
        dataList.add(new Language_Data_Model( "Adapter and card view is used from other fragment"));
        dataList.add(new Language_Data_Model( "There will be some topics to study"));

    }

    RecyclerView recyclerView;

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {

        binding = FragmentTopicsToStudyBinding.inflate(inflater, container, false);


        //View view = inflater.inflate(R.layout.fragment_topics_to_study, container, false);

        recyclerView = binding.topicListToStudy;
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 1));

        StudyTopicListAdapter stdTopicListAdapter = new StudyTopicListAdapter(dataList, getContext());
        recyclerView.setAdapter(stdTopicListAdapter);

        return binding.getRoot();

    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.viewLecture.setOnClickListener(v ->
                NavHostFragment.findNavController(TopicsToStudyFragment.this)
                        .navigate(R.id.action_chatConversationFragment_to_learnersOnlineFragment)
        );
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        recyclerView = null;
    }

}