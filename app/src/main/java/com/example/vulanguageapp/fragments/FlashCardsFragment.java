package com.example.vulanguageapp.fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.example.vulanguageapp.R;
import com.example.vulanguageapp.adapters.GamificationAdapter;
import com.example.vulanguageapp.adapters.LessonActivityAdapter;
import com.example.vulanguageapp.databinding.FragmentFlashCardsBinding;
import com.example.vulanguageapp.models.Enrollment;
import com.example.vulanguageapp.models.FlashCardModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class FlashCardsFragment extends Fragment {

    FragmentFlashCardsBinding binding;

    private ArrayList<FlashCardModel> cardDatalist = new ArrayList<>();
    private RecyclerView cardRecyclerView;
    private GamificationAdapter gamificationAdapter;

    public FlashCardsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentFlashCardsBinding.inflate(inflater, container, false);

        cardRecyclerView = binding.flashcardRecyclerView;

        if (getContext() != null) {
            cardRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 1));
        }

        // Inflate the layout for this fragment
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState){
        super.onViewCreated(view, savedInstanceState);

        Log.d("Falshcards fragment onViewCreated ", " just check  " );

        gamificationAdapter = new GamificationAdapter(cardDatalist, getContext());
        cardRecyclerView.setAdapter(gamificationAdapter);

        fetchFlashCards();
    }

    private void fetchFlashCards() {

        if (getArguments() != null) {
            String lessonId = getArguments().getString("lesson_id");

            Log.d("Falshcards  ", "Lesson id  " + lessonId);

            DatabaseReference flashcardsRef = FirebaseDatabase.getInstance().getReference("flashcards");
            Query cardWithLessonRef = flashcardsRef.orderByKey().equalTo(lessonId);

            cardWithLessonRef.addValueEventListener(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                cardDatalist.clear();
                for (DataSnapshot flashcardSnapShot : snapshot.getChildren()) {
                    FlashCardModel flashcard = flashcardSnapShot.getValue(FlashCardModel.class);
                    if (flashcard != null) {
                        cardDatalist.add(flashcard);
                    }
                }

                gamificationAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("EnrolledCoursesFragment", "Failed to read lesson data.", error.toException());
            }
        });
        }
    }
}