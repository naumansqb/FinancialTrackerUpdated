# Transaction App

## Description
This application allows you to track all your financial transactions, including deposits and payments. It provides reports and filtering to help you manage your money effectively.

**Refactored Version**: This application has been upgraded to a professional **Layered Architecture** and is ready for **MySQL Database** integration.

## Architecture
The project is organized into the following packages in `src/main/java/com/pluralsight`:
- **`model`**: Contains data classes (e.g., `Transaction`).
- **`dao`**: Data Access Objects for database interaction.
    - `TransactionDao` (Interface)
    - `MySqlTransactionDao` (MySQL Implementation Skeleton)
- **`ui`**: User Interface logic (`UserInterface`).
- **`FinancialTrackerApplication`**: The main entry point.

## Database Setup
> [!NOTE]
> The database implementation is currently a **skeleton**. The application structure is ready, but the actual SQL queries need to be implemented by the DB team.

### Prerequisites
- MySQL Server compatible with `mysql-connector-java` 8.0.33.

### Configuration
1.  Open `src/main/java/com/pluralsight/FinancialTrackerApplication.java`.
2.  Update the database credentials:
    ```java
    dataSource.setUrl("jdbc:mysql://localhost:3306/financial_tracker");
    dataSource.setUsername("root");
    dataSource.setPassword("password");
    ```

## Running the Application
1.  Open the project in **IntelliJ IDEA**.
2.  Run `com.pluralsight.FinancialTrackerApplication.main()`.
3.  Follow the on-screen prompts to manage your transactions.

## Technologies Used
- **Java 17**
- **MySQL Connector/J**
- **Apache Commons DBCP2** (Connection Pooling)
- **Maven**

## Features
- **Add Transactions**: Record deposits and payments.
- **Ledger View**: View all transactions, or filter by deposits/payments.
- **Reports**: Generate reports (Month to Date, Previous Month, Year to Date, etc.).
- **Custom Search**: Filter transactions by date, vendor, description, or amount.

## Future Work
- **Database Implementation**: Complete the SQL logic in `MySqlTransactionDao`.
- **Graphical UI**: Replace the console UI with a JavaFX or Web frontend.
- **Authentication**: Add user login and security.
