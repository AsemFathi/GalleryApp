package com.asem.photomangment;

import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class RecyclerVieHolder extends RecyclerView.ViewHolder {
    ImageView imageView;
    public RecyclerVieHolder(@NonNull View itemView) {
        super(itemView);

        imageView = itemView.findViewById(R.id.idIVImage);
    }
}
