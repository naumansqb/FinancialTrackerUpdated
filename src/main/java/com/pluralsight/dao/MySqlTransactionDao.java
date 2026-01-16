package com.pluralsight.dao;

import com.pluralsight.model.Transaction;
import javax.sql.DataSource;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class MySqlTransactionDao implements TransactionDao {
    private DataSource dataSource;

    public MySqlTransactionDao(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public List<Transaction> getAll() {
        List<Transaction> transactions = new ArrayList<>();
        String sql = "SELECT date, time, description, vendor, amount FROM transactions";

        try (Connection connection = dataSource.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql);
                ResultSet results = statement.executeQuery()) {

            while (results.next()) {
                transactions.add(mapRowToTransaction(results));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return transactions;
    }

    @Override
    public void add(Transaction transaction) {
        String sql = "INSERT INTO transactions (date, time, description, vendor, amount) VALUES (?, ?, ?, ?, ?)";
        try (Connection connection = dataSource.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setDate(1, Date.valueOf(transaction.getDate()));
            statement.setTime(2, Time.valueOf(transaction.getTime()));
            statement.setString(3, transaction.getDescription());
            statement.setString(4, transaction.getVendor());
            statement.setDouble(5, transaction.getAmount());

            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<Transaction> search(LocalDate startDate, LocalDate endDate, String vendor, String description) {
        List<Transaction> transactions = new ArrayList<>();

        // Build dynamic query
        StringBuilder sql = new StringBuilder(
                "SELECT date, time, description, vendor, amount FROM transactions WHERE 1=1");
        List<Object> params = new ArrayList<>();

        if (startDate != null) {
            sql.append(" AND date >= ?");
            params.add(Date.valueOf(startDate));
        }
        if (endDate != null) {
            sql.append(" AND date <= ?");
            params.add(Date.valueOf(endDate));
        }
        if (vendor != null && !vendor.isEmpty()) {
            sql.append(" AND vendor LIKE ?");
            params.add("%" + vendor + "%");
        }
        if (description != null && !description.isEmpty()) {
            sql.append(" AND description LIKE ?");
            params.add("%" + description + "%");
        }

        try (Connection connection = dataSource.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql.toString())) {

            for (int i = 0; i < params.size(); i++) {
                statement.setObject(i + 1, params.get(i));
            }

            try (ResultSet results = statement.executeQuery()) {
                while (results.next()) {
                    transactions.add(mapRowToTransaction(results));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return transactions;
    }

    private Transaction mapRowToTransaction(ResultSet rs) throws SQLException {
        LocalDate date = rs.getDate("date").toLocalDate();
        LocalTime time = rs.getTime("time").toLocalTime();
        String description = rs.getString("description");
        String vendor = rs.getString("vendor");
        double amount = rs.getDouble("amount");

        return new Transaction(date, time, description, vendor, amount);
    }
}
