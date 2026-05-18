package com.expensetracker.activities;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.expensetracker.R;
import com.expensetracker.adapters.ExpenseAdapter;
import com.expensetracker.api.ApiClient;
import com.expensetracker.api.ApiService;
import com.expensetracker.models.Expense;
import com.expensetracker.utils.TokenManager;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddExpenseActivity extends AppCompatActivity {

    EditText titleInput, categoryInput, amountInput, dateInput;
    Button addExpenseButton;

    ApiService apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_expense);

        titleInput = findViewById(R.id.titleInput);
        categoryInput = findViewById(R.id.categoryInput);
        amountInput = findViewById(R.id.amountInput);
        dateInput = findViewById(R.id.dateInput);
        addExpenseButton = findViewById(R.id.addExpenseButton);

        apiService = ApiClient.getClient().create(ApiService.class);

        addExpenseButton.setOnClickListener(v -> addExpense());
    }

    private void addExpense() {

        String title = titleInput.getText().toString();
        String category = categoryInput.getText().toString();
        String date = dateInput.getText().toString();
        String amountText = amountInput.getText().toString().trim();

        if (amountText.isEmpty()) {
            Toast.makeText(this, "Enter amount", Toast.LENGTH_SHORT).show();
            return;
        }

        double amount = Double.parseDouble(amountText);

        Expense expense = new Expense();
        expense.setTitle(title);
        expense.setCategory(category);
        expense.setAmount(amount);
        expense.setDate(date);

        String token = "Bearer " + TokenManager.getToken(this);

        Call<Expense> call = apiService.addExpense(token, expense);

        call.enqueue(new Callback<Expense>() {

            @Override
            public void onResponse(Call<Expense> call, Response<Expense> response) {

                if(response.isSuccessful()) {

                    Toast.makeText(AddExpenseActivity.this,
                            "Expense Added successfully",
                            Toast.LENGTH_SHORT).show();

                    finish();
                }
            }

            @Override
            public void onFailure(Call<Expense> call, Throwable t) {

                Toast.makeText(AddExpenseActivity.this,
                        "Failed to add expense",
                        Toast.LENGTH_SHORT).show();
            }
        });
    }
}