package com.android.booksapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ViewHolder>{

    private final ArrayList<ItemData> values;
    private final LayoutInflater inflater;

    public ItemAdapter(Context context, ArrayList<ItemData> values){
        this.values = values;
        this.inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_list, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ItemData data = values.get(position);
        holder.titleText.setText(data.itemTitle);
        holder.authorText.setText(data.itemAuthor);
        holder.descriptionText.setText(data.itemDescription);
        if (data.itemImage.isEmpty()){
            holder.imageBook.setImageResource(R.drawable.ic_baseline_broken_image_24);
        }else {
            Picasso.get()
                    .load(data.itemImage)
                    .resize(120, 200)
                    .error(R.drawable.ic_baseline_broken_image_24)
                    .placeholder(R.drawable.ic_baseline_broken_image_24)
                    .centerCrop()// resizes the image to these dimensions (in pixel)
                    .into(holder.imageBook);
        }

    }

    @Override
    public int getItemCount() {
        return values.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView titleText;
        TextView authorText;
        ImageView imageBook;
        TextView descriptionText;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            titleText = itemView.findViewById(R.id.text1);
            authorText = itemView.findViewById(R.id.text2);
            descriptionText = itemView.findViewById(R.id.textDescription);
            imageBook = itemView.findViewById(R.id.imageBook);
        }
    }
}