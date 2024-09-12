package com.example.vulanguageapp.adapters;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.navigation.NavController;
import androidx.recyclerview.widget.RecyclerView;

import com.example.vulanguageapp.R;
import com.example.vulanguageapp.models.UserAccountModel;

import java.util.List;

public class LearnersAdapter extends RecyclerView.Adapter<LearnersAdapter.ViewHolder> {


    private List<UserAccountModel> dataList;
    private final NavController navController;

    public LearnersAdapter(List<UserAccountModel> dataList, NavController navController) {
        this.dataList = dataList;
        this.navController = navController;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Log.d("Adapter", "onCreateViewHolder called");

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_view_learners, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        UserAccountModel userAccountModel = dataList.get(position);
        holder.userNames.setText(userAccountModel.getUserName());

        holder.user.setOnClickListener(v -> {

            Bundle dataBundle = new Bundle();
            dataBundle.putString("user_Id", userAccountModel.getUserId());

            navController.navigate(R.id.action_learnersOnlineFragment_to_chatConversationFragment, dataBundle);
        });
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }


    public void updateData(List<UserAccountModel> newData) {
        this.dataList = newData;
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView userNames;
        LinearLayout user;

        public ViewHolder(View itemView) {
            super(itemView);

            userNames = itemView.findViewById(R.id.usersDisplayName);
            user = itemView.findViewById(R.id.userID);
        }
    }
}