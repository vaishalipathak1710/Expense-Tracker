package com.expensetracker.api;

import com.expensetracker.models.*;

import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.http.*;
import retrofit2.http.DELETE;
import retrofit2.http.Header;
import retrofit2.http.Path;
import retrofit2.http.PUT;
import retrofit2.http.Body;

public interface ApiService {

    @POST("auth/login")
    Call<LoginResponse> login(@Body LoginRequest request);

    @GET("api/expenses")
    Call<List<Expense>> getExpenses(
            @Header("Authorization") String token
    );

    @POST("api/expenses")
    Call<Expense> addExpense(
            @Header("Authorization") String token,
            @Body Expense expense
    );

//    @DELETE("api/expenses/{id}")
//    Call<String> deleteExpense(
//            @Header("Authorization") String token,
//            @Path("id") Long id
//    );

    @DELETE("api/expenses/{id}")
    Call<Map<String, String>> deleteExpense(
            @Header("Authorization") String token,
            @Path("id") Long id
    );

    @PUT("api/expenses/{id}")
    Call<Expense> updateExpense(
            @Header("Authorization") String token,
            @Path("id") Long id,
            @Body Expense expense
    );
}
