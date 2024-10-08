package com.example.tokopari.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.tokopari.R;
import com.example.tokopari.model.WishlistItem;

import java.util.List;

public class WishlistAdapter extends RecyclerView.Adapter<WishlistAdapter.WishlistViewHolder> {

    private List<WishlistItem> wishlistItems;

    public WishlistAdapter(List<WishlistItem> wishlistItems) {
        this.wishlistItems = wishlistItems;
    }

    @NonNull
    @Override
    public WishlistViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_wishlist, parent, false);
        return new WishlistViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull WishlistViewHolder holder, int position) {
        WishlistItem item = wishlistItems.get(position);
        holder.itemName.setText(item.getName());
        holder.itemPrice.setText(String.valueOf(item.getPrice()));

        Glide.with(holder.itemView.getContext())
                .load(item.getImageUrl())
                .into(holder.itemImage);
    }

    @Override
    public int getItemCount() {
        return wishlistItems.size();
    }

    public static class WishlistViewHolder extends RecyclerView.ViewHolder {
        TextView itemName;
        TextView itemPrice;
        ImageView itemImage;

        public WishlistViewHolder(@NonNull View itemView) {
            super(itemView);
            itemName = itemView.findViewById(R.id.item_name);
            itemPrice = itemView.findViewById(R.id.item_price);
            itemImage = itemView.findViewById(R.id.item_image);
        }
    }
}
