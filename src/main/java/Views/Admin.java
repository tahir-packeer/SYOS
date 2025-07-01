package Views;

import org.example.Controller.ReportController;
import org.example.Model.User;

import java.util.Scanner;

public class Admin {

    Scanner scanner = new Scanner(System.in);


    public void adminInterface(User user) {
        System.out.println("Welcome to SYOS Admin Panel");

        while (true) {
            System.out.println("\nMenu:");
            System.out.println("1. Daily Sales Report");
            System.out.println("2. Items Need Shelving");
            System.out.println("3. Reorder Stock Report");
            System.out.println("4. Stock Batch Report");
            System.out.println("5. Bill Transaction Report");
            System.out.println("6. Exit");
            System.out.print("Enter your choice: ");

            int choice = scanner.nextInt();
            scanner.nextLine(); // consume newline

            switch (choice) {
                case 1:
                    generateDailySalesReport(user);
                    break;
                case 2:
                    generateItemsNeedShelving(user);
                    break;
                case 3:
                    generateReorderStockReport(user);
                    break;
                case 4:
                    generateStockBatchReport(user);
                    break;
                case 5:
                    generateBillTransactionReport(user);
                    break;
                case 6:
                    System.out.println("Exiting...");
                    return;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private void generateDailySalesReport(User user) {
        System.out.print("Enter date (YYYY-MM-DD): ");
        String date = scanner.nextLine();

        ReportController reportController = new ReportController();
        try {
            String report = reportController.generate_sales_report(date);
        } catch (Exception e) {
            System.out.println("Error generating daily sales report: " + e.getMessage());
        }


        adminInterface(user);

    }

    private void generateItemsNeedShelving(User user) {
        ReportController reportController = new ReportController();
        try {
            reportController.generate_items_need_shelving_report();
        } catch (Exception e) {
            System.out.println("Error generating items need shelving report: " + e.getMessage());
        }

        adminInterface(user);
    }

    public void generateReorderStockReport(User user) {
        ReportController reportController = new ReportController();
        try {
            reportController.generate_reorder_stock_report();
        } catch (Exception e) {
            System.out.println("Error generating reorder stock report: " + e.getMessage());
        }

        adminInterface(user);
    }

    private void generateStockBatchReport(User user) {
        ReportController reportController = new ReportController();
        try {
            reportController.generate_stock_batch_report();
        } catch (Exception e) {
            System.out.println("Error generating stock batch report: " + e.getMessage());
        }

        adminInterface(user);
    }

    private void generateBillTransactionReport(User user) {
        ReportController reportController = new ReportController();
        System.out.println("Enter the start date for the bill transaction report (YYYY-MM-DD):");
        String startDate = scanner.nextLine();

        System.out.println("Enter the end date for the bill transaction report (YYYY-MM-DD):");
        String endDate = scanner.nextLine();
        try {
            reportController.generate_bill_transaction_report(startDate, endDate);
        } catch (Exception e) {
            System.out.println("Error generating bill transaction report: " + e.getMessage());
        }

        adminInterface(user);
    }
}
