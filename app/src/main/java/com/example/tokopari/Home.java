package com.example.tokopari;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;
import android.widget.Toast;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import com.example.tokopari.api.ApiClient;
import com.example.tokopari.api.ApiService;
import com.example.tokopari.model.Product;
import com.example.tokopari.adapter.ProductAdapter;
import com.example.tokopari.storage.CartManager;
import java.util.Collections;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Home extends Fragment implements SensorEventListener, ProductAdapter.OnAddToCartListener {

    private SensorManager sensorManager;
    private Sensor accelerometer;
    private static final float SHAKE_THRESHOLD = 15.0f;
    private RecyclerView recyclerView;
    private ProductAdapter productAdapter;
    private long lastShakeTime = 0;
    private int shakeCount = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        // Inisialisasi RecyclerView
        recyclerView = view.findViewById(R.id.recyclerViewRecommendation);
        int numberOfColumns = 2;
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), numberOfColumns));

        // Fetch Products from API
        fetchProducts();

        // Inisialisasi SearchView
        SearchView searchView = view.findViewById(R.id.search);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                // Filter produk berdasarkan teks pencarian
                if (productAdapter != null) {
                    productAdapter.filter(newText);
                }
                return true;
            }
        });

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        sensorManager = (SensorManager) getActivity().getSystemService(Context.SENSOR_SERVICE);
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        float x = event.values[0];
        float y = event.values[1];
        float z = event.values[2];

        // Hitung akselerasi
        float acceleration = (float) Math.sqrt(x * x + y * y + z * z);
        if (acceleration > SHAKE_THRESHOLD) {
            long currentTime = System.currentTimeMillis();
            if (currentTime - lastShakeTime > 1000) { // 1 detik delay
                shakeCount++;
                lastShakeTime = currentTime;

                if (shakeCount == 5) {
                    Toast.makeText(getActivity(), "wink wink shake shake", Toast.LENGTH_SHORT).show();
                    shakeCount = 0; // Reset shake count after pop-up
                    // Acak produk
                    if (productAdapter != null) {
                        productAdapter.shuffleProducts();
                    }
                }
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // Tidak ada tindakan yang perlu diambil di sini
    }

    @Override
    public void onResume() {
        super.onResume();
        sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    public void onPause() {
        super.onPause();
        sensorManager.unregisterListener(this);
    }

    private void fetchProducts() {
        ApiService apiService = ApiClient.getClient().create(ApiService.class);
        Call<List<Product>> call = apiService.getProducts();

        call.enqueue(new Callback<List<Product>>() {
            @Override
            public void onResponse(Call<List<Product>> call, Response<List<Product>> response) {
                if (response.isSuccessful()) {
                    List<Product> productList = response.body();
                    // Acak produk
                    Collections.shuffle(productList);
                    productAdapter = new ProductAdapter(productList, Home.this);
                    recyclerView.setAdapter(productAdapter);
                } else {
                    Log.e("Home", "Failed to get products, status code: " + response.code());
                    Toast.makeText(getActivity(), "Failed to load products", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Product>> call, Throwable t) {
                Log.e("Home", "Error: " + t.getMessage());
                Toast.makeText(getActivity(), "Failed to connect to API", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onAddToCart(Product product) {
        Toast.makeText(getActivity(), product.getTitle() + " added to cart", Toast.LENGTH_SHORT).show();

        CartManager cartManager = new CartManager(getActivity());
        cartManager.addToCart(product);
    }
}
