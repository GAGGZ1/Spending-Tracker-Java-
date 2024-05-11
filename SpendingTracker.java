import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;

public class SpendingTracker {
    private static double balance = 0;
    private static Map<String, Double> expenses = new HashMap<>();
    private static Map<String, Double> budgetGoals = new HashMap<>();
    private static final String EXPENSES_FILE = "expenses.txt";

    public static void main(String[] args) {
        loadExpensesFromFile();
        Scanner scanner = new Scanner(System.in);
        int choice;
        do {
            System.out.println("\nSpending Tracker Menu:");
            System.out.println("1. Add Expense");
            System.out.println("2. View Balance");
            System.out.println("3. View Expenses");
            System.out.println("4. Save Expenses to File");
            System.out.println("5. Set Budget Goal");
            System.out.println("6. Generate Expense Report");
            System.out.println("7. Exit");
            System.out.print("Enter your choice: ");
            choice = scanner.nextInt();
            switch (choice) {
                case 1:
                    addExpense(scanner);
                    break;
                case 2:
                    viewBalance();
                    break;
                case 3:
                    viewExpenses();
                    break;
                case 4:
                    saveExpensesToFile();
                    break;
                case 5:
                    setBudgetGoal(scanner);
                    break;
                case 6:
                    generateExpenseReport();
                    break;
                case 7:
                    System.out.println("Exiting the Spending Tracker. Goodbye!");
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
                    break;
            }
        } while (choice != 7);
        scanner.close();
    }

    private static void addExpense(Scanner scanner) {
        System.out.print("Enter the amount of the expense: ");
        double expenseAmount = scanner.nextDouble();
        scanner.nextLine(); // Consume newline
        System.out.print("Enter the category of the expense: ");
        String category = scanner.nextLine();
        balance -= expenseAmount;
        expenses.put(category, expenses.getOrDefault(category, 0.0) + expenseAmount);
        System.out.println("Expense added successfully.");
    }

    private static void viewBalance() {
        System.out.println("Current Balance: $" + balance);
    }

    private static void viewExpenses() {
        if (expenses.isEmpty()) {
            System.out.println("No expenses recorded yet.");
        } else {
            System.out.println("Expenses:");
            for (Map.Entry<String, Double> entry : expenses.entrySet()) {
                System.out.println(entry.getKey() + ": $" + entry.getValue());
            }
        }
    }

    private static void saveExpensesToFile() {
        try (PrintWriter writer = new PrintWriter(new FileWriter(EXPENSES_FILE))) {
            for (Map.Entry<String, Double> entry : expenses.entrySet()) {
                writer.println(entry.getKey() + "," + entry.getValue());
            }
            writer.println("Current Balance," + balance);
            System.out.println("Expenses saved to file 'expenses.txt' successfully.");
        } catch (IOException e) {
            System.out.println("Error occurred while saving expenses to file: " + e.getMessage());
        }
    }

    private static void loadExpensesFromFile() {
        try (BufferedReader reader = new BufferedReader(new FileReader(EXPENSES_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 2) {
                    if (parts[0].equals("Current Balance")) {
                        balance = Double.parseDouble(parts[1]);
                    } else {
                        expenses.put(parts[0], Double.parseDouble(parts[1]));
                    }
                }
            }
            System.out.println("Expenses loaded from file 'expenses.txt' successfully.");
        } catch (IOException e) {
            System.out.println("Error occurred while loading expenses from file: " + e.getMessage());
        }
    }

    private static void setBudgetGoal(Scanner scanner) {
        System.out.print("Enter the category for which you want to set a budget goal: ");
        String category = scanner.next();
        System.out.print("Enter the budget goal amount: ");
        double goalAmount = scanner.nextDouble();
        budgetGoals.put(category, goalAmount);
        System.out.println("Budget goal set successfully for category: " + category);
    }

    private static void generateExpenseReport() {
        if (expenses.isEmpty()) {
            System.out.println("No expenses recorded yet.");
            return;
        }
        System.out.println("Expense Report:");
        System.out.println("Date: " + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
        System.out.println("Category\t\tExpense\t\tBudget Goal\t\tStatus");
        for (Map.Entry<String, Double> entry : expenses.entrySet()) {
            String category = entry.getKey();
            double expenseAmount = entry.getValue();
            double budgetGoal = budgetGoals.getOrDefault(category, 0.0);
            String status = (expenseAmount <= budgetGoal) ? "Under Budget" : "Over Budget";
            System.out.printf("%-15s\t$%.2f\t\t$%.2f\t\t%s\n", category, expenseAmount, budgetGoal, status);
        }
    }
}
