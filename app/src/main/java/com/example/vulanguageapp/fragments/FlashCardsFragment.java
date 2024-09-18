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
import android.widget.Toast;

import com.example.vulanguageapp.adapters.GamificationAdapter;
import com.example.vulanguageapp.databinding.FragmentFlashCardsBinding;
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
    private String language;

    public FlashCardsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Get flashcard IDs from arguments
        ArrayList<String> flashCardIdArray = getArguments().getStringArrayList("flashcardIdArray");
        language = getArguments().getString("language");

        // Fetch flashcards from Firebase based on the flashcard IDs
        if (flashCardIdArray != null && !flashCardIdArray.isEmpty()) {
            fetchFlashcards(flashCardIdArray);  // Call fetchFlashcards method to load flashcards
        } else {
            Toast.makeText(getContext(), "No flashcards available", Toast.LENGTH_SHORT).show();
        }
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
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Log.d("Flashcards fragment onViewCreated", " just check");

        gamificationAdapter = new GamificationAdapter(cardDatalist, getContext(), language);
        cardRecyclerView.setAdapter(gamificationAdapter);
    }

    private void fetchFlashcards(ArrayList<String> flashCardIdArray) {
        DatabaseReference flashcardsRef = FirebaseDatabase.getInstance().getReference("flashcards");

        // Loop through each flashcard ID and fetch its data
        for (String flashCardId : flashCardIdArray) {
            flashcardsRef.child(flashCardId).get().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    DataSnapshot snapshot = task.getResult();
                    if (snapshot.exists()) {
                        FlashCardModel flashcard = snapshot.getValue(FlashCardModel.class);
                        if (flashcard != null) {
                            cardDatalist.add(flashcard);  // Add flashcard to list
                            gamificationAdapter.notifyDataSetChanged();  // Notify adapter
                        }
                    }
                } else {
                    Toast.makeText(getContext(), "Error fetching flashcards", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (gamificationAdapter != null) {
            gamificationAdapter.shutdownTTS();
        }
    }
}
