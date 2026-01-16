package com.pluralsight;

import com.pluralsight.config.DatabaseConfig;
import com.pluralsight.dao.MySqlTransactionDao;
import com.pluralsight.dao.TransactionDao;
import com.pluralsight.ui.UserInterface;
import javax.sql.DataSource;

public class FinancialTrackerApplication {
    public static void main(String[] args) {
        // Prepare DataSource
        DataSource dataSource = DatabaseConfig.getDataSource();

        // Initialize DAO
        TransactionDao transactionDao = new MySqlTransactionDao(dataSource);

        // Test Connection
        try (var conn = dataSource.getConnection()) {
            System.out.println("Database connected successfully!");
        } catch (Exception e) {
            System.err.println("Could not connect to database. Please check your credentials.");
            System.err.println(e.getMessage());
            System.exit(1);
        }

        // Run UI
        UserInterface ui = new UserInterface(transactionDao);
        ui.start();
    }
}
