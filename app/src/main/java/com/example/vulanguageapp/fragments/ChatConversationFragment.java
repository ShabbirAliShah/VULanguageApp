package com.example.vulanguageapp.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.vulanguageapp.R;
import com.example.vulanguageapp.adapters.ChatAdapter;
import com.example.vulanguageapp.databinding.FragmentChatConversationBinding;
import com.example.vulanguageapp.models.ChatModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ChatConversationFragment extends Fragment {

    private FragmentChatConversationBinding binding;
    private RecyclerView recyclerView;
    private EditText messageInput;
    private ImageButton sendButton;
    private List<ChatModel> messageList = new ArrayList<>();
    private String userId;
    private String currentUserId;
    private DatabaseReference chatRef;
    private ChatAdapter chatAdapter;
    private String chatId;

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        binding = FragmentChatConversationBinding.inflate(inflater, container, false);
        recyclerView = binding.chatRecyclerView;
        messageInput = binding.messageInput;
        sendButton = binding.sendButton;

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        userId = getArguments().getString("user_Id"); // Get the userId of the other person
        currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid(); // Current logged-in user

        if (currentUserId.compareTo(userId) < 0) {
            chatId = currentUserId + "_" + userId;
        } else {
            chatId = userId + "_" + currentUserId;
        }

        // Firebase reference to the chat node
        chatRef = FirebaseDatabase.getInstance().getReference("chats");

        loadMessages();

        sendButton.setOnClickListener(v -> {
            String messageText = messageInput.getText().toString().trim();

            if (!messageText.isEmpty()) {
                sendMessage(messageText);
                messageInput.setText("");  // Clear the input field after sending the message
            } else {
                messageInput.setError("Cannot send empty message");
            }
        });




        chatAdapter = new ChatAdapter(messageList, currentUserId);
        recyclerView.setAdapter(chatAdapter);

        loadMessages();  // Call the method to load messages

        return binding.getRoot();
    }

    // Method to load messages between the two users
    public void loadMessages() {
        DatabaseReference chatRef = FirebaseDatabase.getInstance().getReference("chats");

        // Query messages for the specific chatId
        chatRef.child(chatId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                messageList.clear();

                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    ChatModel message = dataSnapshot.getValue(ChatModel.class);
                    if (message != null) {
                        messageList.add(message);  // Add the message to the list
                    }
                }

                chatAdapter.notifyDataSetChanged();  // Notify the adapter about data change
                recyclerView.scrollToPosition(messageList.size() - 1);  // Scroll to the latest message
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("FirebaseError", "Failed to fetch messages: " + error.getMessage());
            }
        });
    }



    // Method to send a message
    public void sendMessage(String messageText) {
        String senderId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        String receiverId = getArguments().getString("user_Id");

        DatabaseReference chatRef = FirebaseDatabase.getInstance().getReference("chats").child(chatId);

        String messageId = chatRef.push().getKey();  // Generate unique message ID
        ChatModel message = new ChatModel(senderId, receiverId, messageText, System.currentTimeMillis());

        chatRef.child(messageId).setValue(message);  // Save message under chatId
    }

}