package com.expense.expense_tracker.controller;

import java.security.Principal;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.expense.expense_tracker.dto.ExpenseRequestDTO;
import com.expense.expense_tracker.dto.ExpenseResponseDTO;
import com.expense.expense_tracker.model.Expense;
import com.expense.expense_tracker.service.ExpenseService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/expenses")
public class ExpenseController {

    private final ExpenseService expenseService;

    public ExpenseController(ExpenseService expenseService) {
        this.expenseService = expenseService;
    }

    @PostMapping
    public ExpenseResponseDTO addExpense(@Valid @RequestBody ExpenseRequestDTO request,
        Principal principal){

    return expenseService.addExpense(request, principal.getName());
    }


   @GetMapping
    public List<Expense> getAllExpenses(Principal principal) {

    return expenseService.getAllExpenses(principal.getName());
    }

    @GetMapping("/filter")
    public List<Expense> filterExpenses(
            @RequestParam String start,
            @RequestParam String end) {

        return expenseService.filterExpenses(
                LocalDate.parse(start),
                LocalDate.parse(end)
        );
    }

    @GetMapping("/category")
    public List<Expense> getByCategory(@RequestParam String category) {
        return expenseService.getExpensesByCategory(category);
    }

    @GetMapping("/{id}")
    public Expense getExpenseById(@PathVariable Long id) {
        return expenseService.getExpenseById(id);
    }

    @GetMapping("/categories")
    public List<String> getCategorySuggestions(@RequestParam String keyword) {
        return expenseService.getCategorySuggestions(keyword);
    }
    
    @GetMapping("/summary")
    public Map<String, Double> getExpenseSummary(Principal principal) {
        return expenseService.getExpenseSummary(principal.getName());
    }

    
    // @DeleteMapping("/{id}")
    // public String deleteExpense(@PathVariable Long id) {

    //     expenseService.deleteExpense(id);

    //     return "Expense deleted successfully";
    // }

    @DeleteMapping("/{id}")
    public Map<String, String> deleteExpense(@PathVariable Long id) {

        expenseService.deleteExpense(id);

        Map<String, String> response = new HashMap<>();

        response.put("message", "Expense deleted successfully");

        return response;
    }

    @PutMapping("/{id}")
    public Expense updateExpense(
        @PathVariable Long id,
        @RequestBody Expense updatedExpense) {

    return expenseService.updateExpense(id, updatedExpense);
    }
}
