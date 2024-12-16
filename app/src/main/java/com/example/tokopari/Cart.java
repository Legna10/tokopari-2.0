package com.example.tokopari;

import android.content.DialogInterface;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.tokopari.adapter.CartAdapter;
import com.example.tokopari.model.Product;
import com.example.tokopari.storage.CartManager;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;

public class Cart extends Fragment implements CartAdapter.OnQuantityChangeListener {

    private RecyclerView recyclerView;
    private CartAdapter cartAdapter;
    private CartManager cartManager;
    private TextView totalPriceTextView;
    private TextView originalPriceTextView;
    private TextView discountTextView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cart, container, false);

        // Menginisialisasi RecyclerView dan CartManager
        recyclerView = view.findViewById(R.id.recyclerViewCart);
        totalPriceTextView = view.findViewById(R.id.totalPriceTextView);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        cartManager = new CartManager(getContext());

        // Mengambil item cart dari CartManager
        List<Product> cartItems = cartManager.getCartItems();

        // Menginisialisasi CartAdapter dengan listener untuk perubahan kuantitas
        cartAdapter = new CartAdapter(cartItems, this);
        recyclerView.setAdapter(cartAdapter);

        // Menghitung total harga
        updateTotalPrice(cartItems);

        // Handle Checkout Button Click
        Button checkoutButton = view.findViewById(R.id.checkoutButton);
        checkoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create an AlertDialog to confirm the checkout action
                new android.app.AlertDialog.Builder(getActivity())
                        .setTitle("Confirm Checkout")
                        .setMessage("Are you sure you want to proceed with checkout?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                saveCheckoutToFirestore(cartManager.getCartItems());
                                cartManager.clearCart();

                                cartAdapter.setCartItems(cartManager.getCartItems()); // Update the adapter
                                updateTotalPrice(cartManager.getCartItems());
                                Toast.makeText(getActivity(), "Checkout confirmed!", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .setNegativeButton("No", null) // Do nothing on "No"
                        .show();
            }
        });
        return view;
    }
    private void saveCheckoutToFirestore(List<Product> cartItems) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        Map<String, Object> checkoutData = new HashMap<>();

        // Menghitung harga total tanpa diskon
        float totalPrice = 0;
        for (Product product : cartItems) {
            totalPrice += product.getTotalPrice();
        }

        // Menghitung diskon dan harga setelah diskon
        float discountPercentage = getDiscountPercentage(cartItems);
        float discount = totalPrice * discountPercentage;
        float discountedPrice = totalPrice - discount;

        // Menyimpan data checkout
        checkoutData.put("products", cartItems);
        checkoutData.put("totalPrice", discountedPrice); // Simpan harga setelah diskon
        checkoutData.put("timestamp", System.currentTimeMillis()); // Simpan timestamp checkout

        // Simpan data checkout ke Firestore
        CollectionReference checkoutCollection = db.collection("checkouts");
        checkoutCollection.add(checkoutData)
                .addOnSuccessListener(documentReference -> {
                    Log.d("Cart", "Checkout data saved successfully!");
                })
                .addOnFailureListener(e -> {
                    Log.w("Cart", "Error saving checkout data", e);
                });
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Inisialisasi TextViews setelah view dibuat
        originalPriceTextView = view.findViewById(R.id.productOriginalPrice);
        discountTextView = view.findViewById(R.id.discountTextView);

        // Check if views are properly initialized
        if (originalPriceTextView == null || discountTextView == null) {
            Log.e("Cart", "TextView initialization failed");
        }
    }

    @Override
    public void onQuantityChanged(Product product, int newQuantity) {
        // Memperbarui jumlah produk di CartManager
        cartManager.updateProductQuantity(product, newQuantity);

        // Mengambil daftar produk yang sudah diperbarui
        List<Product> cartItems = cartManager.getCartItems();

        // Mengupdate total harga
        updateTotalPrice(cartItems);

        // Menemukan posisi produk yang diubah dalam daftar
        int position = cartItems.indexOf(product);
        if (position != -1) {
            // Memperbarui produk di adapter secara realtime
            cartAdapter.updateProductQuantity(position, product);
        }

        // Menyegarkan seluruh daftar cart
        cartAdapter.setCartItems(cartItems);  // Memastikan data terbaru diterapkan
    }

    @Override
    public void onProductRemoved(Product product) {
        // Hapus produk dari CartManager
        cartManager.removeFromCart(product);

        // Mengupdate daftar produk yang tersisa dan menghitung total harga
        List<Product> cartItems = cartManager.getCartItems();
        updateTotalPrice(cartItems);

        // Mengupdate adapter untuk menghapus item dari RecyclerView
        int position = cartItems.indexOf(product);
        if (position != -1) {
            cartAdapter.notifyItemRemoved(position);
        }

        // Perbarui data adapter
        cartAdapter.setCartItems(cartItems);  // Set data yang diperbarui
    }

    private void updateTotalPrice(List<Product> cartItems) {
        float totalPrice = 0;

        // Calculate total price of all products in the cart
        for (Product product : cartItems) {
            totalPrice += product.getTotalPrice(); // Ensure getTotalPrice returns correct value
        }

        // Get the discount percentage based on the number of products
        float discountPercentage = getDiscountPercentage(cartItems);

        // Calculate the discount amount
        float discount = totalPrice * discountPercentage;

        // Calculate the price after discount
        float discountedPrice = totalPrice - discount;

        // Format the prices for display
        String formattedTotalPrice = "Rp " + formatPrice(totalPrice);
        String formattedDiscountedPrice = "Total: Rp " + formatPrice(discountedPrice);
        String formattedDiscount = "You saved: Rp " + formatPrice(discount) ;

        // Display the discounted price
        totalPriceTextView.setText(formattedDiscountedPrice);

        // Add a TextView for the total price before the discount (strike-through)
        if (originalPriceTextView != null) {
            originalPriceTextView.setText(formattedTotalPrice);
            originalPriceTextView.setPaintFlags(originalPriceTextView.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        }

        // Show the discount if desired
        if (discountTextView != null) {
            discountTextView.setText(formattedDiscount);
        }
    }

    // Memformat harga menjadi format yang lebih baik
    private String formatPrice(float price) {
        java.text.DecimalFormat decimalFormat = new java.text.DecimalFormat("#,###");
        return decimalFormat.format(price).replace(",", ".");
    }

    private float getDiscountPercentage(List<Product> cartItems) {
        int productCount = cartItems.size();
        if (productCount >= 6) {
            return 0.15f; // 15% diskon
        } else if (productCount >= 4) {
            return 0.10f; // 10% diskon
        } else if (productCount >= 1) {
            return 0.05f; // 5% diskon
        }
        return 0; // Tidak ada diskon
    }
}
