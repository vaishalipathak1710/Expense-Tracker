package com.expensetracker.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.expensetracker.R;
import com.expensetracker.api.ApiClient;
import com.expensetracker.api.ApiService;
import com.expensetracker.models.LoginRequest;
import com.expensetracker.models.LoginResponse;
import com.expensetracker.utils.TokenManager;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    private EditText usernameInput, passwordInput;
    private Button loginButton;

    private ApiService apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        String token = TokenManager.getToken(this);

        if(token != null) {

            Intent intent = new Intent(
                    LoginActivity.this,
                    DashboardActivity.class
            );

            startActivity(intent);

            finish();
        }

        if(token != null) {

            Intent intent = new Intent(
                    LoginActivity.this,
                    DashboardActivity.class
            );

            startActivity(intent);

            finish();
        }

        // Initialize views
        usernameInput = findViewById(R.id.usernameInput);
        passwordInput = findViewById(R.id.passwordInput);
        loginButton = findViewById(R.id.loginButton);

        // Initialize API
        apiService = ApiClient.getClient().create(ApiService.class);

        // Button click
        loginButton.setOnClickListener(v -> loginUser());
    }

    private void loginUser() {

        String username = usernameInput.getText().toString().trim();
        String password = passwordInput.getText().toString().trim();

        // Basic validation
        if (username.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Enter username and password", Toast.LENGTH_SHORT).show();
            return;
        }

        LoginRequest request = new LoginRequest(username, password);

        Call<LoginResponse> call = apiService.login(request);

        call.enqueue(new Callback<LoginResponse>() {

            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {

                System.out.println("Response Code: " + response.code());

                if (response.isSuccessful() && response.body() != null) {

                    String token = response.body().getToken();

                    System.out.println("JWT Token: " + token);
                    System.out.println("Saved token: " + token);

                    // Save token
                    TokenManager.saveToken(LoginActivity.this, token);

                    Toast.makeText(LoginActivity.this,
                            "Login Successful",
                            Toast.LENGTH_SHORT).show();

                    // Navigate to Dashboard
                    Intent intent = new Intent(LoginActivity.this, DashboardActivity.class);
                    startActivity(intent);
                    finish();

                } else {
                    Toast.makeText(LoginActivity.this,
                            "Invalid Credentials",
                            Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {

                t.printStackTrace();

                Toast.makeText(LoginActivity.this,
                        "Error: " + t.getMessage(),
                        Toast.LENGTH_LONG).show();
            }
        });
    }
}