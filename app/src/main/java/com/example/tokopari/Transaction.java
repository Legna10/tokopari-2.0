package com.example.tokopari;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import androidx.fragment.app.Fragment;

public class Transaction extends Fragment {

    private Spinner statusSpinner, dateSpinner;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_transaction, container, false);

        // Find the Spinner views from the layout
        statusSpinner = view.findViewById(R.id.Status);
        dateSpinner = view.findViewById(R.id.Date);

        // Create arrays for the Spinner data
        String[] statusOptions = {"Pending", "Completed", "Cancelled"};
        String[] dateOptions = {"Today", "This Week", "This Month"};

        // ArrayAdapter for status spinner using resources
        ArrayAdapter<CharSequence> statusAdapter = ArrayAdapter.createFromResource(getContext(),
                R.array.status_options, android.R.layout.simple_spinner_item);
        statusAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        statusSpinner.setAdapter(statusAdapter);

        // ArrayAdapter for date spinner using resources
        ArrayAdapter<CharSequence> dateAdapter = ArrayAdapter.createFromResource(getContext(),
                R.array.date_options, android.R.layout.simple_spinner_item);
        dateAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        dateSpinner.setAdapter(dateAdapter);



        return view;
    }
}
