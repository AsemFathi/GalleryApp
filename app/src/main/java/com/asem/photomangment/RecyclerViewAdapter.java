package com.asem.photomangment;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerVieHolder> implements RecyclerViewInterface {
    private final Context context;
    private final ArrayList<String> imagePathArrayList;

    public RecyclerViewAdapter(Context context, ArrayList<String> imagePathArrayList) {
        this.context = context;
        this.imagePathArrayList = imagePathArrayList;
    }

    @NonNull
    @Override
    public RecyclerVieHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_layout , parent , false);
        return new RecyclerVieHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerVieHolder holder, int position) {

        File imgFile = new File(imagePathArrayList.get(position));
        if (imgFile.exists())
        {
            Picasso.get().load(imgFile)
                    .placeholder(R.drawable.ic_launcher_background)
                    .into(holder.imageView);
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context , ImageDetailActivity.class);
                    intent.putExtra("imagePath", imagePathArrayList.get(position));
                    context.startActivity(intent);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return imagePathArrayList.size();
    }

    @Override
    public void onItemClick(int pos) {

    }
}
