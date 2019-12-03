package com.example.socialnapp;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

class CommnetsHolder extends RecyclerView.ViewHolder {


    private TextView comments;

    public CommnetsHolder(@NonNull View itemView) {
        super(itemView);

        comments = itemView.findViewById(R.id.CommentsNameID);
    }


    public void setcommentsset(String set){
        comments.setText(set);
    }
}
