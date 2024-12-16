package com.example.tokopari.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.example.tokopari.R;
import com.example.tokopari.model.Product;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {

    private List<Product> productList;
    private OnAddToCartListener listener;
    private Context context;

    public ProductAdapter(List<Product> productList, OnAddToCartListener listener) {
        this.productList = productList;
        this.listener = listener;
    }

    @Override
    public ProductViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_product, parent, false);
        context = parent.getContext();
        return new ProductViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ProductViewHolder holder, int position) {
        Product product = productList.get(position);
        holder.productTitle.setText(product.getTitle());

        // Format harga menggunakan DecimalFormat
        String formattedPrice = formatPrice(product.getPrice());
        holder.productPrice.setText("Rp " + formattedPrice);

        Glide.with(context).load(product.getImage()).into(holder.productImage);

        holder.buttonAddToCart.setOnClickListener(v -> listener.onAddToCart(product));
    }

    // Tambahkan metode untuk memformat harga
    private String formatPrice(float price) {
        java.text.DecimalFormat decimalFormat = new java.text.DecimalFormat("#,###");
        return decimalFormat.format(price).replace(",", ".");
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    public void filter(String query) {
        List<Product> filteredList = new ArrayList<>();
        for (Product product : productList) {
            if (product.getTitle().toLowerCase().contains(query.toLowerCase())) {
                filteredList.add(product);
            }
        }
        productList.clear();
        productList.addAll(filteredList);
        notifyDataSetChanged();
    }

    public void shuffleProducts() {
        if (productList != null) {
            Collections.shuffle(productList);
            notifyDataSetChanged(); // Membuat perubahan langsung terlihat di RecyclerView
        }
    }

    public interface OnAddToCartListener {
        void onAddToCart(Product product);
    }

    public class ProductViewHolder extends RecyclerView.ViewHolder {
        public TextView productTitle, productPrice;
        public ImageView productImage;
        public Button buttonAddToCart;

        public ProductViewHolder(View itemView) {
            super(itemView);
            productTitle = itemView.findViewById(R.id.productTitle);
            productPrice = itemView.findViewById(R.id.productPrice);
            productImage = itemView.findViewById(R.id.productImage);
            buttonAddToCart = itemView.findViewById(R.id.buttonAddToCart);
        }
    }
}
