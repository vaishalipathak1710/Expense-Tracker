package com.expensetracker.activities;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.expensetracker.R;
import com.expensetracker.adapters.ExpenseAdapter;
import com.expensetracker.api.ApiClient;
import com.expensetracker.api.ApiService;
import com.expensetracker.models.Expense;
import com.expensetracker.utils.TokenManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.charts.PieChart;
import android.os.Handler;

public class DashboardActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    Button addExpenseButton;
    ApiService apiService;
    PieChart pieChart;
    Handler handler = new Handler();
    Runnable refreshRunnable;
    Button logoutButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_dashboard);
        recyclerView = findViewById(R.id.recyclerView);

        addExpenseButton = findViewById(R.id.addExpenseButton);
        logoutButton = findViewById(R.id.logoutButton);

        // RecyclerView LayoutManager
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        recyclerView.setAdapter(
                new ExpenseAdapter(new ArrayList<>(), expenseId -> {})
        );

        apiService = ApiClient.getClient().create(ApiService.class);

        addExpenseButton.setOnClickListener(v -> {

            Intent intent = new Intent(
                    DashboardActivity.this,
                    AddExpenseActivity.class
            );

            startActivity(intent);
        });

        fetchExpenses();

        pieChart = findViewById(R.id.pieChart);

        logoutButton.setOnClickListener(v -> {

            // Remove JWT token
            TokenManager.clearToken(this);

            Toast.makeText(this,
                    "Logged Out",
                    Toast.LENGTH_SHORT).show();

            // Navigate to login screen
            Intent intent = new Intent(
                    DashboardActivity.this,
                    LoginActivity.class
            );

            startActivity(intent);

            finish();
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        System.out.println("onResume called successfully.");
        fetchExpenses();
    }

    private void fetchExpenses() {
        System.out.println("Fetching expenses...");

        String token = "Bearer " + TokenManager.getToken(this);
        System.out.println("Retrieved token: " + TokenManager.getToken(this));

        Call<List<Expense>> call = apiService.getExpenses(token);

        call.enqueue(new Callback<List<Expense>>() {

            @Override
            public void onResponse(Call<List<Expense>> call,
                                   Response<List<Expense>> response) {

                if(response.isSuccessful() && response.body() != null) {

                    List<Expense> expenses = response.body();

                    ExpenseAdapter adapter =
                            new ExpenseAdapter(expenses, expenseId -> {
                                deleteExpense(expenseId);
                            });

                    recyclerView.setAdapter(adapter);
                    setupPieChart(expenses);

                } else {

                    Toast.makeText(DashboardActivity.this,
                            "Failed to load expenses",
                            Toast.LENGTH_SHORT).show();
                    System.out.println("Failed to load expenses");
                }
            }

            @Override
            public void onFailure(Call<List<Expense>> call, Throwable t) {

                t.printStackTrace();

                System.out.println("REAL ERROR: " + t.getMessage());

                Toast.makeText(DashboardActivity.this,
                        "Error: " + t.getMessage(),
                        Toast.LENGTH_LONG).show();

            }
        });
    }

    private void setupPieChart(List<Expense> expenses) {

        ArrayList<PieEntry> entries = new ArrayList<>();

        HashMap<String, Float> categoryMap = new HashMap<>();

        // Calculate total amount per category
        for (Expense expense : expenses) {

            String category = expense.getCategory();

            float amount = (float) expense.getAmount();

            if(categoryMap.containsKey(category)) {

                float current = categoryMap.get(category);

                categoryMap.put(category, current + amount);

            } else {

                categoryMap.put(category, amount);
            }
        }

        // Convert map into PieEntries
        for(String category : categoryMap.keySet()) {

            entries.add(
                    new PieEntry(
                            categoryMap.get(category),
                            category
                    )
            );
        }

        PieDataSet dataSet = new PieDataSet(entries, "Expenses");
        dataSet.setColors(
                Color.RED,
                Color.BLUE,
                Color.GREEN,
                Color.MAGENTA,
                Color.CYAN,
                Color.YELLOW
        );

        pieChart.setUsePercentValues(true);

        pieChart.setCenterText("Expenses");

        pieChart.setCenterTextSize(18f);

        pieChart.getDescription().setEnabled(false);

        pieChart.animateY(1000);

        PieData data = new PieData(dataSet);

        pieChart.setData(data);

        pieChart.invalidate(); // refresh chart
    }

    private void deleteExpense(Long expenseId) {

        System.out.println("DELETE API CALLED FOR ID: " + expenseId);
        String token = "Bearer " + TokenManager.getToken(this);
        System.out.println("DELETE TOKEN: " + token);

        Call<Map<String, String>> call =
                apiService.deleteExpense(token, expenseId);

        call.enqueue(new Callback<Map<String, String>>(){

            @Override
            public void onResponse(Call<Map<String, String>> call,
                                   Response<Map<String, String>> response) {

                if(response.isSuccessful()) {

                    System.out.println("DELETE SUCCESS");

                    Toast.makeText(DashboardActivity.this,
                            "Expense Deleted",
                            Toast.LENGTH_SHORT).show();

                    fetchExpenses();

                } else {

                    System.out.println("DELETE FAILED CODE: "
                            + response.code());

                    Toast.makeText(DashboardActivity.this,
                            "Delete Failed",
                            Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Map<String, String>> call, Throwable t) {

                t.printStackTrace();

                System.out.println("DELETE ERROR: "
                        + t.getMessage());

                Toast.makeText(DashboardActivity.this,
                        t.getMessage(),
                        Toast.LENGTH_LONG).show();
            }
        });
    }
}