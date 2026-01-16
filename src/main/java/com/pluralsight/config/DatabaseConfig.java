package com.pluralsight.config;

import org.apache.commons.dbcp2.BasicDataSource;
import javax.sql.DataSource;

public class DatabaseConfig {

    public static DataSource getDataSource() {
        BasicDataSource dataSource = new BasicDataSource();
        dataSource.setUrl("jdbc:mysql://localhost:3306/financial_tracker");
        dataSource.setUsername("root");
        dataSource.setPassword("yearup");
        return dataSource;
    }
}
