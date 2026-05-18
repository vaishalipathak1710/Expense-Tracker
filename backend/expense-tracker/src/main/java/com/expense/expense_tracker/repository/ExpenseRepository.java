package com.expense.expense_tracker.repository;
import java.time.LocalDate;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.expense.expense_tracker.model.Expense;

public interface ExpenseRepository extends JpaRepository<Expense,Long> {

    List<Expense> findByDateBetween(LocalDate start, LocalDate end);

    List<Expense> findByCategory(String category);

    @Query("SELECT e.category, SUM(e.amount) FROM Expense e WHERE e.user.username = :username GROUP BY e.category")
    List<Object[]> getExpenseSummaryByCategory(String username);

    @Query("SELECT DISTINCT e.category FROM Expense e WHERE LOWER(e.category) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<String> findCategorySuggestions(String keyword);

    List<Expense> findByUserUsername(String username);

    Page<Expense> findByUserUsername(String username, Pageable pageable);

}
