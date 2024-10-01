package com.example.tokopari;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tokopari.adapter.TransactionAdapter;
import com.example.tokopari.model.TransactionItem;

import java.util.ArrayList;
import java.util.List;

public class Transaction extends Fragment {

    private Spinner statusSpinner, dateSpinner;
    private RecyclerView recyclerView;
    private TransactionAdapter transactionAdapter;
    private List<TransactionItem> transactionItems;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_transaction, container, false);

        // Find the Spinner views from the layout
        statusSpinner = view.findViewById(R.id.Status);
        dateSpinner = view.findViewById(R.id.Date);

        // Create arrays for the Spinner data
        String[] statusOptions = {"Pending", "Completed", "Cancelled"};
        String[] dateOptions = {"Today", "This Week", "This Month"};

        // ArrayAdapter for status spinner
        ArrayAdapter<String> statusAdapter = new ArrayAdapter<>(getContext(),
                android.R.layout.simple_spinner_item, statusOptions);
        statusAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        statusSpinner.setAdapter(statusAdapter);

        // ArrayAdapter for date spinner
        ArrayAdapter<String> dateAdapter = new ArrayAdapter<>(getContext(),
                android.R.layout.simple_spinner_item, dateOptions);
        dateAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        dateSpinner.setAdapter(dateAdapter);

        // RecyclerView setup
        recyclerView = view.findViewById(R.id.recyclerViewTransaction);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Initialize the transaction list
        transactionItems = new ArrayList<>();
        transactionItems.add(new TransactionItem("Produk 1", "Pending", "Today"));
        transactionItems.add(new TransactionItem("Produk 2", "Completed", "This Week"));
        // Add more transactions as needed

        transactionAdapter = new TransactionAdapter(transactionItems);
        recyclerView.setAdapter(transactionAdapter);

        return view;
    }
}
