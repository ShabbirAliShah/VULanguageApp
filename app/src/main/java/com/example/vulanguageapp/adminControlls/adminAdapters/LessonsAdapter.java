package com.example.vulanguageapp.adminControlls.adminAdapters;

import static androidx.test.InstrumentationRegistry.getContext;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.vulanguageapp.R;
import com.example.vulanguageapp.adminControlls.fragments.Manage;
import com.example.vulanguageapp.models.LessonsModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LessonsAdapter extends RecyclerView.Adapter<LessonsAdapter.LessonViewHolder> {

    private final List<LessonsModel> lessonsList;
    private final Context context;

    public LessonsAdapter(List<LessonsModel> lessonsList, Context context) {
        this.lessonsList = lessonsList;
        this.context = context;
    }

    @NonNull
    @Override
    public LessonViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_view_for_manage_content, parent, false);
        return new LessonViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LessonViewHolder holder, int position) {
        LessonsModel lesson = lessonsList.get(position);

        // Bind lesson data to views
        holder.lessonTitleInAdmin.setText(lesson.getTitle());
        //holder.languageOfTheLesson.setText(lesson.getLanguage()); // Assuming getLanguage() is implemented

        // Handle delete click
        holder.delete.setOnClickListener(v -> {
            new AlertDialog.Builder(context)
                    .setTitle("Delete Lesson")
                    .setMessage("Are you sure you want to delete this lesson?")
                    .setPositiveButton("Delete", (dialog, which) -> deleteLesson(lesson.getLessonId(), position))
                    .setNegativeButton("Cancel", null)
                    .show();
        });

        holder.edit.setOnClickListener(v -> {
            // Open dialog to edit lesson
            openEditDialog(lesson, holder.itemView.getContext());
        });
    }

    @Override
    public int getItemCount() {
        return lessonsList.size();
    }

    private void deleteLesson(String lessonId, int position) {
        DatabaseReference lessonRef = FirebaseDatabase.getInstance().getReference("lessons").child(lessonId);

        lessonRef.removeValue().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Toast.makeText(context, "Lesson deleted successfully", Toast.LENGTH_SHORT).show();
                lessonsList.remove(position);
                notifyItemRemoved(position);
            } else {
                Toast.makeText(context, "Failed to delete lesson", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public static class LessonViewHolder extends RecyclerView.ViewHolder {
        TextView lessonTitleInAdmin, languageOfTheLesson;
        ImageView delete, edit;

        public LessonViewHolder(@NonNull View itemView) {
            super(itemView);

            lessonTitleInAdmin = itemView.findViewById(R.id.lesson_title_in_admin);
            languageOfTheLesson = itemView.findViewById(R.id.language_of_the_lesson);
            delete = itemView.findViewById(R.id.delete_lesson);
            edit = itemView.findViewById(R.id.edit_lesson);
        }
    }

    private void openEditDialog(LessonsModel lesson, Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        View dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_edit_lesson, null);
        builder.setView(dialogView);

        EditText titleEdit = dialogView.findViewById(R.id.edit_lesson_title);
        EditText audioLinkEdit = dialogView.findViewById(R.id.edit_audio_link);
        EditText videoLinkEdit = dialogView.findViewById(R.id.edit_video_link);
        @SuppressLint({"MissingInflatedId", "LocalSuppress"}) EditText imageLinkEdit = dialogView.findViewById(R.id.editIMageLink);

        // Set current values
        titleEdit.setText(lesson.getTitle());
        audioLinkEdit.setText(lesson.getAudioLink());
        videoLinkEdit.setText(lesson.getVideoLink());
        imageLinkEdit.setText(lesson.getImageLink());

        builder.setTitle("Edit Lesson")
                .setPositiveButton("Update", (dialog, which) -> {
                    String newTitle = titleEdit.getText().toString().trim();
                    String newAudioLink = audioLinkEdit.getText().toString().trim();
                    String newVideoLink = videoLinkEdit.getText().toString().trim();
                    String newImageLink = imageLinkEdit.getText().toString().trim();

                    if (!newTitle.isEmpty()) {
                        // Update Firebase
                        updateLessonInFirebase(lesson.getLessonId(), newTitle, newAudioLink, newVideoLink, newImageLink);
                    } else {
                        Toast.makeText(context, "Title cannot be empty", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());

        builder.create().show();
    }

    private void updateLessonInFirebase(String lessonId, String newTitle, String newAudioLink, String newVideoLink, String newImageLink) {

        DatabaseReference lessonRef = FirebaseDatabase.getInstance().getReference("lessons").child(lessonId);

        // Prepare data for update
        Map<String, Object> updates = new HashMap<>();
        updates.put("title", newTitle);
        updates.put("audioLink", newAudioLink);
        updates.put("videoLink", newVideoLink);
        updates.put("imageLink", newImageLink);

        lessonRef.updateChildren(updates).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                //Toast.makeText(getContext(), "Lesson updated successfully", Toast.LENGTH_SHORT).show();
                Log.d("update lesson", "Lesso updated successfully");
                // Optionally refresh data in RecyclerView
            } else {
                //Toast.makeText(getContext(), "Failed to update lesson", Toast.LENGTH_SHORT).show();
                Log.d("update lesson", "Failed to update lesson");
            }
        });
    }
}
