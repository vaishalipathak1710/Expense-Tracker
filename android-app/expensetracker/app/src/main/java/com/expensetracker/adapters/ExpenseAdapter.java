package com.expensetracker.adapters;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.expensetracker.R;
import com.expensetracker.activities.UpdateExpenseActivity;
import com.expensetracker.models.Expense;

import java.util.List;

public class ExpenseAdapter extends RecyclerView.Adapter<ExpenseAdapter.ExpenseViewHolder> {

    private List<Expense> expenseList;
    private OnDeleteClickListener listener;

    public interface OnDeleteClickListener {
        void onDeleteClick(Long expenseId);
    }

    public ExpenseAdapter(List<Expense> expenseList,
                          OnDeleteClickListener listener) {

        this.expenseList = expenseList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ExpenseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_expense, parent, false);

        return new ExpenseViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ExpenseViewHolder holder, int position) {

        Expense expense = expenseList.get(position);

        holder.titleText.setText(expense.getTitle());

        holder.categoryText.setText("Category: " + expense.getCategory());

        holder.amountText.setText("Amount: ₹" + expense.getAmount());

        holder.dateText.setText("Date: " + expense.getDate());

        // DELETE BUTTON
        holder.deleteButton.setOnClickListener(v -> {

            System.out.println("DELETE CLICKED ID: " + expense.getId());

            listener.onDeleteClick(expense.getId());
        });

        // EDIT BUTTON
        holder.editButton.setOnClickListener(v -> {

            Intent intent = new Intent(
                    v.getContext(),
                    UpdateExpenseActivity.class
            );

            intent.putExtra("id", expense.getId());
            intent.putExtra("title", expense.getTitle());
            intent.putExtra("category", expense.getCategory());
            intent.putExtra("amount", expense.getAmount());
            intent.putExtra("date", expense.getDate());

            v.getContext().startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return expenseList.size();
    }

    public static class ExpenseViewHolder extends RecyclerView.ViewHolder {

        TextView titleText, categoryText, amountText, dateText;

        Button editButton, deleteButton;

        public ExpenseViewHolder(@NonNull View itemView) {
            super(itemView);

            titleText = itemView.findViewById(R.id.titleText);
            categoryText = itemView.findViewById(R.id.categoryText);
            amountText = itemView.findViewById(R.id.amountText);
            dateText = itemView.findViewById(R.id.dateText);

            editButton = itemView.findViewById(R.id.editButton);

            deleteButton = itemView.findViewById(R.id.deleteButton);
        }
    }
}