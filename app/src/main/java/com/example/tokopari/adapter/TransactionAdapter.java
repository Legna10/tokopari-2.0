package com.example.tokopari.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tokopari.R;
import com.example.tokopari.model.TransactionItem;

import java.util.List;

public class TransactionAdapter extends RecyclerView.Adapter<TransactionAdapter.TransactionViewHolder> {

    private List<TransactionItem> transactionItems;

    public TransactionAdapter(List<TransactionItem> transactionItems) {
        this.transactionItems = transactionItems;
    }

    @NonNull
    @Override
    public TransactionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_transaction, parent, false);
        return new TransactionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TransactionViewHolder holder, int position) {
        TransactionItem item = transactionItems.get(position);
        holder.productName.setText(item.getProductName());
        holder.status.setText(item.getStatus());
        holder.date.setText(item.getDate());
    }

    @Override
    public int getItemCount() {
        return transactionItems.size();
    }

    public static class TransactionViewHolder extends RecyclerView.ViewHolder {
        TextView productName;
        TextView status;
        TextView date;

        public TransactionViewHolder(@NonNull View itemView) {
            super(itemView);
            productName = itemView.findViewById(R.id.product_name);
            status = itemView.findViewById(R.id.transaction_status);
            date = itemView.findViewById(R.id.transaction_date);
        }
    }
}
