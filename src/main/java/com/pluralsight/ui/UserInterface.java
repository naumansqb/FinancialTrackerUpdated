package com.pluralsight.ui;

import com.pluralsight.dao.TransactionDao;
import com.pluralsight.model.Transaction;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

public class UserInterface {
    private TransactionDao transactionDao;
    private Scanner scanner;

    private static final String DATE_PATTERN = "yyyy-MM-dd";
    private static final String TIME_PATTERN = "HH:mm:ss";
    private static final DateTimeFormatter DATE_FMT = DateTimeFormatter.ofPattern(DATE_PATTERN);
    private static final DateTimeFormatter TIME_FMT = DateTimeFormatter.ofPattern(TIME_PATTERN);

    // ANSI Colors
    private static final String RESET = "\u001B[0m";
    private static final String RED = "\u001B[31m";
    private static final String GREEN = "\u001B[32m";
    private static final String CYAN = "\u001B[36m";
    private static final String BLUE = "\u001B[34m";
    private static final String PURPLE = "\u001B[35m";
    private static final String YELLOW = "\u001B[33m";

    public UserInterface(TransactionDao transactionDao) {
        this.transactionDao = transactionDao;
        this.scanner = new Scanner(System.in);
    }

    public void start() {
        boolean running = true;
        while (running) {
            System.out.println(CYAN + "Welcome to TransactionApp" + RESET);
            System.out.println("Choose an option:");
            System.out.println(GREEN + "D) Add Deposit" + RESET);
            System.out.println(RED + "P) Make Payment (Debit)" + RESET);
            System.out.println(PURPLE + "L) Ledger" + RESET);
            System.out.println(YELLOW + "X) Exit" + RESET);

            String input = scanner.nextLine().trim();

            switch (input.toUpperCase()) {
                case "D" -> addDeposit();
                case "P" -> addPayment();
                case "L" -> ledgerMenu();
                case "X" -> {
                    System.out.println("Exiting Application. Have a great day<3");
                    running = false;
                }
                default -> System.out.println(RED + "Invalid option" + RESET);
            }
        }
    }

    private void addDeposit() {
        System.out.println(CYAN + "All values must be filled in to save your deposit." + RESET);
        LocalDate parsedDate = promptForDate();
        LocalTime parsedTime = promptForTime();
        String description = promptForString("Enter Description:");
        String vendor = promptForString("Enter Vendor:");
        double amount = promptForDouble("Enter amount: (Must be greater than 0)");

        Transaction t = new Transaction(parsedDate, parsedTime, description, vendor, amount);
        transactionDao.add(t);
        System.out.println(GREEN + "Successfully Saved Deposit" + RESET);
    }

    private void addPayment() {
        System.out.println(CYAN + "All values must be filled in to save your payment." + RESET);
        LocalDate parsedDate = promptForDate();
        LocalTime parsedTime = promptForTime();
        String description = promptForString("Enter Description:");
        String vendor = promptForString("Enter Vendor:");
        double amount = promptForDouble("Enter amount: (Must be greater than 0)");

        Transaction t = new Transaction(parsedDate, parsedTime, description, vendor, -amount);
        transactionDao.add(t);
        System.out.println(GREEN + "Successfully Saved Payment" + RESET);
    }

    private void ledgerMenu() {
        boolean running = true;
        while (running) {
            System.out.println(PURPLE + "\nLedger Menu" + RESET);
            System.out.println("Choose an option:");
            System.out.println("A) All");
            System.out.println("D) Deposits");
            System.out.println("P) Payments");
            System.out.println("R) Reports");
            System.out.println("H) Home");
            String input = scanner.nextLine().trim();

            switch (input.toUpperCase()) {
                case "A" -> displayLedger();
                case "D" -> displayDeposits();
                case "P" -> displayPayments();
                case "R" -> reportsMenu();
                case "H" -> running = false;
                default -> System.out.println(RED + "Invalid option" + RESET);
            }
        }
    }

    private void displayLedger() {
        List<Transaction> transactions = transactionDao.getAll();

        if (transactions.isEmpty()) {
            System.out.println("No Entries To Display.");
            return;
        }
        System.out.println(CYAN + "Displaying All Entries" + RESET);
        displayHeader();
        for (Transaction transaction : transactions) {
            displayTransaction(transaction);
        }
    }

    private void displayDeposits() {
        List<Transaction> transactions = transactionDao.getAll();
        boolean found = false;
        displayHeader();
        for (Transaction t : transactions) {
            if (t.getAmount() > 0) {
                displayTransaction(t);
                found = true;
            }
        }
        if (!found)
            System.out.println("No Deposits Found");
    }

    private void displayPayments() {
        List<Transaction> transactions = transactionDao.getAll();
        boolean found = false;
        displayHeader();
        for (Transaction t : transactions) {
            if (t.getAmount() < 0) {
                displayTransaction(t);
                found = true;
            }
        }
        if (!found)
            System.out.println("No Payments Found");
    }

    private void reportsMenu() {
        boolean running = true;
        while (running) {
            System.out.println(BLUE + "\nReports Menu" + RESET);
            System.out.println("1) Month To Date");
            System.out.println("2) Previous Month");
            System.out.println("3) Year To Date");
            System.out.println("4) Previous Year");
            System.out.println("5) Search by Vendor");
            System.out.println("6) Custom Search");
            System.out.println("0) Back");

            String input = scanner.nextLine().trim();

            switch (input) {
                case "1" -> {
                    LocalDate start = LocalDate.now().withDayOfMonth(1);
                    LocalDate end = LocalDate.now();
                    displaySearchResults(transactionDao.search(start, end, null, null));
                }
                case "2" -> {
                    LocalDate end = LocalDate.now().withDayOfMonth(1).minusDays(1);
                    LocalDate start = LocalDate.now().minusMonths(1).withDayOfMonth(1);
                    displaySearchResults(transactionDao.search(start, end, null, null));
                }
                case "3" -> {
                    LocalDate start = LocalDate.now().withDayOfYear(1);
                    LocalDate end = LocalDate.now();
                    displaySearchResults(transactionDao.search(start, end, null, null));
                }
                case "4" -> {
                    LocalDate start = LocalDate.now().minusYears(1).withDayOfYear(1);
                    LocalDate end = LocalDate.now().withDayOfYear(1).minusDays(1);
                    displaySearchResults(transactionDao.search(start, end, null, null));
                }
                case "5" -> {
                    String vendor = promptForString("Enter Vendor Name:");
                    displaySearchResults(transactionDao.search(null, null, vendor, null));
                }
                case "6" -> customSearch();
                case "0" -> running = false;
                default -> System.out.println(RED + "Invalid option" + RESET);
            }
        }
    }

    private void customSearch() {
        System.out.println("Enter values for filters (press Enter to skip):");

        LocalDate startDate = promptForDateOptional("Start Date (" + DATE_PATTERN + "): ");
        LocalDate endDate = promptForDateOptional("End Date (" + DATE_PATTERN + "): ");

        System.out.println("Description: ");
        String description = scanner.nextLine().trim();

        System.out.println("Vendor: ");
        String vendor = scanner.nextLine().trim();

        displaySearchResults(transactionDao.search(startDate, endDate, vendor, description));
    }

    private void displaySearchResults(List<Transaction> results) {
        if (results.isEmpty()) {
            System.out.println(YELLOW + "No transactions found matching criteria." + RESET);
        } else {
            System.out.println(CYAN + "Found " + results.size() + " transactions:" + RESET);
            displayHeader();
            for (Transaction t : results) {
                displayTransaction(t);
            }
        }
    }

    private void displayHeader() {
        System.out.println(BLUE + String.format(
                "\t%-12s | %-10s | %-30s | %-25s | %s",
                "Date", "Time", "Description", "Vendor", "Amount") + RESET);
        System.out.println(
                "\t------------------------------------------------------------------------------------------------------");
    }

    private void displayTransaction(Transaction t) {
        String color = t.getAmount() >= 0 ? GREEN : RED;
        // Re-coloring standard toString logic manually or we can update Transaction
        // model.
        // Doing it here to preserve model purity.
        System.out.printf(
                "\t%-12s | %-10s | %-30s | %-25s | %s$%,.2f%s\n",
                t.getDate(), t.getTime(), t.getDescription(), t.getVendor(), color, t.getAmount(), RESET);
    }

    // Input Helpers
    private LocalDate promptForDate() {
        while (true) {
            try {
                System.out.println("Please enter the date (" + DATE_PATTERN + "):");
                return LocalDate.parse(scanner.nextLine(), DATE_FMT);
            } catch (DateTimeParseException e) {
                System.out.println(RED + "Incorrect Input For Date!" + RESET);
            }
        }
    }

    private LocalDate promptForDateOptional(String prompt) {
        while (true) {
            System.out.println(prompt);
            String input = scanner.nextLine().trim();
            if (input.isEmpty())
                return null;
            try {
                return LocalDate.parse(input, DATE_FMT);
            } catch (DateTimeParseException e) {
                System.out.println(RED + "Invalid date format." + RESET);
            }
        }
    }

    private LocalTime promptForTime() {
        while (true) {
            try {
                System.out.println("Please enter the time (" + TIME_PATTERN + "):");
                return LocalTime.parse(scanner.nextLine(), TIME_FMT);
            } catch (DateTimeParseException e) {
                System.out.println(RED + "Incorrect Input For Time!" + RESET);
            }
        }
    }

    private String promptForString(String prompt) {
        String input;
        do {
            System.out.println(prompt);
            input = scanner.nextLine().trim();
        } while (input.isEmpty());
        return input;
    }

    private double promptForDouble(String prompt) {
        while (true) {
            try {
                System.out.println(prompt);
                double val = scanner.nextDouble();
                scanner.nextLine();
                if (val > 0)
                    return val;
            } catch (InputMismatchException e) {
                System.out.println(RED + "Invalid entry. Please enter a numerical input" + RESET);
                scanner.nextLine();
            }
        }
    }
}
