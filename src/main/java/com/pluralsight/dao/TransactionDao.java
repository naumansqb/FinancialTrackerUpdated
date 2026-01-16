package com.pluralsight.dao;

import com.pluralsight.model.Transaction;
import java.util.List;

public interface TransactionDao {
    List<Transaction> getAll();

    void add(Transaction transaction);

    List<Transaction> search(java.time.LocalDate startDate, java.time.LocalDate endDate, String vendor,
            String description);
}
