package com.example.vulanguageapp.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.vulanguageapp.R;
import com.example.vulanguageapp.adapters.StudyTopicListAdapter;
import com.example.vulanguageapp.databinding.FragmentTopicsToStudyBinding;
import com.example.vulanguageapp.interfaces.LessonActivityFragmentClickListener;
import com.example.vulanguageapp.models.Language_Data_Model;

import java.util.ArrayList;

public class TopicsToStudyFragment extends Fragment implements LessonActivityFragmentClickListener {

    private FragmentTopicsToStudyBinding binding;
    private ArrayList<Language_Data_Model> studyTopicListData = new ArrayList<>();
    private Context context;

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
        studyTopicListData.add(new Language_Data_Model( "Language name as a topic 1"));
        studyTopicListData.add(new Language_Data_Model( "Card view of language is used"));
        studyTopicListData.add(new Language_Data_Model( "Adapter and card view is used from other fragment"));
        studyTopicListData.add(new Language_Data_Model( "There will be some topics to study"));
        studyTopicListData.add(new Language_Data_Model( "There will be some topics to study"));
        studyTopicListData.add(new Language_Data_Model( "There will be some topics to study"));
        studyTopicListData.add(new Language_Data_Model( "There will be some topics to study"));
        studyTopicListData.add(new Language_Data_Model( "There will be some topics to study"));
        studyTopicListData.add(new Language_Data_Model( "There will be some topics to study"));
        studyTopicListData.add(new Language_Data_Model( "There will be some topics to study"));
        studyTopicListData.add(new Language_Data_Model( "There will be some topics to study"));
        studyTopicListData.add(new Language_Data_Model( "There will be some topics to study"));


        RecyclerView  recyclerView = view.findViewById(R.id.topic_list_to_study);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        StudyTopicListAdapter stdTopicListAdapter = new StudyTopicListAdapter(studyTopicListData, this);
        recyclerView.setAdapter(stdTopicListAdapter);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onCourseContentClick(View view) {

                NavHostFragment.findNavController(TopicsToStudyFragment.this)
                        .navigate(R.id.action_topicsToStudy_to_lectureViewFragment);
    }
}