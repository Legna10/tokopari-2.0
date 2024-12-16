package com.example.tokopari.api;

import com.example.tokopari.model.Product;
import java.util.List;
import retrofit2.Call;
import retrofit2.http.GET;

public interface ApiService {
    @GET("/get_products.php")
    Call<List<Product>> getProducts();
}
