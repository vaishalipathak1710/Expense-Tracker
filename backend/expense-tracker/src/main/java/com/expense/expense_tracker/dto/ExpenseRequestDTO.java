package com.expense.expense_tracker.dto;

import java.time.LocalDate;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class ExpenseRequestDTO {

    @NotBlank(message = "Title cannot be empty")
    private String title;

    @NotBlank(message = "Category cannot be empty")
    private String category;

    @Positive(message = "Amount must be greater than 0")
    private double amount;

    private LocalDate date;
}
