package com.expensetracker.activities;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.expensetracker.R;
import com.expensetracker.api.ApiClient;
import com.expensetracker.api.ApiService;
import com.expensetracker.models.Expense;
import com.expensetracker.utils.TokenManager;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UpdateExpenseActivity extends AppCompatActivity {

    EditText titleInput, categoryInput, amountInput, dateInput;

    Button updateButton;
    ApiService apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_update_expense);
        apiService = ApiClient.getClient().create(ApiService.class);

        titleInput = findViewById(R.id.titleInput);
        categoryInput = findViewById(R.id.categoryInput);
        amountInput = findViewById(R.id.amountInput);
        dateInput = findViewById(R.id.dateInput);

        updateButton = findViewById(R.id.updateButton);

        // Receive data from Dashboard
        String title = getIntent().getStringExtra("title");
        String category = getIntent().getStringExtra("category");
        double amount = getIntent().getDoubleExtra("amount", 0);
        String date = getIntent().getStringExtra("date");
        Long expenseId = getIntent().getLongExtra("id", 0);

        // Pre-fill fields
        titleInput.setText(title);
        categoryInput.setText(category);
        amountInput.setText(String.valueOf(amount));
        dateInput.setText(date);

        updateButton.setOnClickListener(v -> {

            String updatedTitle =
                    titleInput.getText().toString();

            String updatedCategory =
                    categoryInput.getText().toString();

            double updatedAmount =
                    Double.parseDouble(
                            amountInput.getText().toString()
                    );

            String updatedDate =
                    dateInput.getText().toString();

            Expense updatedExpense = new Expense();

            updatedExpense.setTitle(updatedTitle);
            updatedExpense.setCategory(updatedCategory);
            updatedExpense.setAmount(updatedAmount);
            updatedExpense.setDate(updatedDate);

            String token =
                    "Bearer " + TokenManager.getToken(this);

            Call<Expense> call =
                    apiService.updateExpense(
                            token,
                            expenseId,
                            updatedExpense
                    );

            call.enqueue(new Callback<Expense>() {

                @Override
                public void onResponse(Call<Expense> call,
                                       Response<Expense> response) {

                    if(response.isSuccessful()) {

                        Toast.makeText(
                                UpdateExpenseActivity.this,
                                "Expense Updated",
                                Toast.LENGTH_SHORT
                        ).show();

                        finish(); // 🔥 GO BACK TO DASHBOARD

                    } else {

                        Toast.makeText(
                                UpdateExpenseActivity.this,
                                "Update Failed",
                                Toast.LENGTH_SHORT
                        ).show();
                    }
                }

                @Override
                public void onFailure(Call<Expense> call,
                                      Throwable t) {

                    Toast.makeText(
                            UpdateExpenseActivity.this,
                            t.getMessage(),
                            Toast.LENGTH_LONG
                    ).show();
                }
            });
        });
    }
}