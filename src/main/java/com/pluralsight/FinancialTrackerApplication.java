package com.pluralsight;

import com.pluralsight.dao.MySqlTransactionDao;
import com.pluralsight.dao.TransactionDao;
import com.pluralsight.ui.UserInterface;
import org.apache.commons.dbcp2.BasicDataSource;

import javax.sql.DataSource;

public class FinancialTrackerApplication {
    public static void main(String[] args) {
        // Prepare DataSource
        BasicDataSource dataSource = new BasicDataSource();
        dataSource.setUrl("jdbc:mysql://localhost:3306/financial_tracker");
        dataSource.setUsername("root");
        dataSource.setPassword("password");

        // Initialize DAO
        TransactionDao transactionDao = new MySqlTransactionDao(dataSource);

        // Run UI
        UserInterface ui = new UserInterface(transactionDao);
        ui.start();
    }
}
