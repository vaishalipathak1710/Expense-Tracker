package com.expense.expense_tracker.dto;
import java.time.LocalDate;

import lombok.Data;

@Data
public class ExpenseResponseDTO {

    private Long id;
    private String title;
    private String category;
    private double amount;
    private LocalDate date;
}