package com.expense.expense_tracker.service;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.expense.expense_tracker.dto.ExpenseRequestDTO;
import com.expense.expense_tracker.dto.ExpenseResponseDTO;
import com.expense.expense_tracker.exception.ResourceNotFoundException;
import com.expense.expense_tracker.model.Expense;
import com.expense.expense_tracker.model.User;
import com.expense.expense_tracker.repository.ExpenseRepository;
import com.expense.expense_tracker.repository.UserRepository;

@Service
public class ExpenseService {

    private final ExpenseRepository expenseRepository;

    private final UserRepository userRepository;

    public ExpenseService(ExpenseRepository expenseRepository, UserRepository userRepository) {
        this.expenseRepository = expenseRepository;
        this.userRepository = userRepository;
    }

    public ExpenseResponseDTO addExpense(ExpenseRequestDTO request, String username) {

    User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Expense expense = new Expense();

        expense.setTitle(request.getTitle());
        expense.setCategory(request.getCategory());
        expense.setAmount(request.getAmount());
        expense.setDate(request.getDate());
        expense.setUser(user);

        Expense savedExpense = expenseRepository.save(expense);

        ExpenseResponseDTO response = new ExpenseResponseDTO();
        response.setId(savedExpense.getId());
        response.setTitle(savedExpense.getTitle());
        response.setCategory(savedExpense.getCategory());
        response.setAmount(savedExpense.getAmount());
        response.setDate(savedExpense.getDate());

        return response;
}

    public List<Expense> getAllExpenses(String username) {

    return expenseRepository.findByUserUsername(username);
}

    public List<Expense> filterExpenses(LocalDate start, LocalDate end) {
        return expenseRepository.findByDateBetween(start, end);
    }

    public List<Expense> getExpensesByCategory(String category) {
    return expenseRepository.findByCategory(category);
    }

    public Expense getExpenseById(Long id) {

    return expenseRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Expense not found"));
    }

    public List<String> getCategorySuggestions(String keyword) {
    return expenseRepository.findCategorySuggestions(keyword);
    }

    public Map<String, Double> getExpenseSummary(String username) {

    List<Object[]> results = expenseRepository.getExpenseSummaryByCategory(username);

    Map<String, Double> summary = new HashMap<>();

    for (Object[] row : results) {
        String category = (String) row[0];
        Double total = (Double) row[1];
        summary.put(category, total);
    }

        return summary;
    }

    public void deleteExpense(Long id) {

        expenseRepository.deleteById(id);
    }

    public Expense updateExpense(Long id, Expense updatedExpense) {

    Expense expense = expenseRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Expense not found"));

    expense.setTitle(updatedExpense.getTitle());
    expense.setCategory(updatedExpense.getCategory());
    expense.setAmount(updatedExpense.getAmount());
    expense.setDate(updatedExpense.getDate());

    return expenseRepository.save(expense);
    }
}