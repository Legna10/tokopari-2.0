package com.example.tokopari;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.tokopari.adapter.WishlistAdapter;
import com.example.tokopari.model.WishlistItem;

import java.util.ArrayList;
import java.util.List;

public class Wishlist extends Fragment {

    private RecyclerView recyclerView;
    private WishlistAdapter wishlistAdapter;
    private List<WishlistItem> wishlistItems;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_wishlist, container, false);

        recyclerView = view.findViewById(R.id.recyclerViewWishlist);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Inisialisasi daftar wishlist
        wishlistItems = new ArrayList<>();
        wishlistItems.add(new WishlistItem("Felix", "https://www.google.com/url?sa=i&url=https%3A%2F%2Fwww.wattpad.com%2F905733677-lee-felix&psig=AOvVaw0kuh9RWM_EhV3m9y0ZYXEV&ust=1727793712070000&source=images&cd=vfe&opi=89978449&ved=0CBAQjRxqFwoTCOjM36Tz6ogDFQAAAAAdAAAAABAE", 1000000));
        wishlistItems.add(new WishlistItem("Hyunjin", "https://www.google.com/url?sa=i&url=https%3A%2F%2Fkstationtv.com%2F2024%2F04%2F04%2Fstray-kids-a-new-donation-from-hyunjin%2F%3Flang%3Den&psig=AOvVaw1q1vFDLLuZYTV3abgea2HY&ust=1727793776418000&source=images&cd=vfe&opi=89978449&ved=0CBQQjRxqFwoTCNipuMjz6ogDFQAAAAAdAAAAABAE", 2000000));
        // Tambahkan produk lainnya

        wishlistAdapter = new WishlistAdapter(wishlistItems);
        recyclerView.setAdapter(wishlistAdapter);

        return view;
    }
}
