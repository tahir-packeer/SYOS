package Views;

import org.example.Controller.Authentication;
import org.example.Controller.ItemController;
import org.example.Model.Item;
import org.example.Model.User;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class StoreManager {

    Scanner scanner = new Scanner(System.in);

    public void storeManagerDashboard(User user) throws SQLException, ClassNotFoundException {

        System.out.println("Welcome Store Manager: " + user.getName());
        System.out.println("Store Manager Dashboard");
        System.out.println("1. View Items");
        System.out.println("2. Add Item");
        System.out.println("3. Update Item");
        System.out.println("4. Add Stock");
        System.out.println("5. Shelf Dashboard");
        System.out.println("6. Logout");

        int choice = scanner.nextInt();
        scanner.nextLine(); // Consume newline

        switch (choice) {
            case 1:

            case 2:
                    addItem_Interface(user);
                break;
            case 3:
                updateItem_Interface(user);
                break;
            case 4:
                addStock_Interface(user);
                break;
            case 5:
            case 6:
                System.out.println("Logging out...");
                Authentication.ApplicationStartup(); // Redirect to log in
                break;
            default:
                System.out.println("Invalid choice, please try again.");
                storeManagerDashboard(user);
        }
    }


    /**
     * Method to add items through the interface.
     * It collects item details from the user and adds them to the database.
     *
     * @param user The user who is adding the item.
     * @throws SQLException If there is an error with the database operation.
     * @throws ClassNotFoundException If the JDBC driver class is not found.
     */
    public void addItem_Interface(User user) throws SQLException, ClassNotFoundException {

        List<Item> itemList = new ArrayList<Item>();
        String response;
        do {
            System.out.println("Enter item code:");
            String code = scanner.nextLine();
            System.out.println("Enter item name:");
            String name = scanner.nextLine();

            System.out.println("Enter item price:");
            double price = scanner.nextDouble();
            scanner.nextLine(); // Consume newline

            Item item = new Item(code, name, price);

            itemList.add(item);

            System.out.println("Do you want to add another item? (yes/no)");
            response = scanner.nextLine();

        } while (response.equalsIgnoreCase("yes") || response.equalsIgnoreCase("y"));

        try {
            if (!itemList.isEmpty()) { // Ensure items are added only once
                new ItemController().AddItem(itemList);
                System.out.println("Items added successfully ✅");
            } else {
                System.out.println("No items to add.");
            }
        } catch (SQLException | ClassNotFoundException e) {
            System.out.println("Error adding items to the database: " + e.getMessage());
        }

        new StoreManager().storeManagerDashboard(user); // if StoreManager class exists

    }


    /**
     * Method to update an item through the interface.
     */
    public void updateItem_Interface(User user) throws SQLException, ClassNotFoundException {
        System.out.println("Enter the code of the item you want to update:");
        String code = scanner.nextLine();

        System.out.println("Enter new name for the item:");
        String name = scanner.nextLine();

        System.out.println("Enter new price for the item:");
        double price = scanner.nextDouble();
        scanner.nextLine(); // Consume newline

        Item updatedItem = new Item(code, name, price);

        try {
            boolean success = new ItemController().UpdateItem(updatedItem);
            if (success) {
                System.out.println("Item updated successfully ");
            } else {
                System.out.println("Item update failed. Item code may not exist.");
            }
        } catch (SQLException | ClassNotFoundException e) {
            System.out.println("Error updating item: " + e.getMessage());
        }

        storeManagerDashboard(user);
    }


    public void addStock_Interface(User user) throws SQLException, ClassNotFoundException {
        System.out.println("Enter Item Code:");
        String code = scanner.nextLine();

        Item item = new ItemController().getItem_from_Code(code);
        if (item == null) {
            System.out.println("Item not found with code: " + code);
            storeManagerDashboard(user);
            return;
        }

        System.out.println("Item found: " + item.getName() + " (Price: " + item.getPrice() + ")");
        System.out.println("Confirm item? (yes/no):");
        String confirm = scanner.nextLine();
        if (!confirm.equalsIgnoreCase("yes") && !confirm.equalsIgnoreCase("y")) {
            System.out.println("Cancelled.");
            storeManagerDashboard(user);
            return;
        }

        System.out.println("Enter stock quantity:");
        int quantity = scanner.nextInt();
        scanner.nextLine();

        System.out.println("Enter date of purchase (yyyy-mm-dd):");
        String dopStr = scanner.nextLine();
        java.sql.Date dateOfPurchase = java.sql.Date.valueOf(dopStr);

        System.out.println("Enter date of expiry (yyyy-mm-dd):");
        String doeStr = scanner.nextLine();
        java.sql.Date dateOfExpiry = java.sql.Date.valueOf(doeStr);

        System.out.println("Is the stock available? (true/false):");
        boolean availability = scanner.nextBoolean();
        scanner.nextLine();

        org.example.Model.Stock stock = new org.example.Model.Stock(item, quantity, dateOfPurchase, dateOfExpiry, availability);

        try {
            new org.example.Controller.StockController().addStock(stock);
            System.out.println("Stock added successfully ");
        } catch (SQLException | ClassNotFoundException e) {
            System.out.println("Error adding stock: " + e.getMessage());
        }
        storeManagerDashboard(user);
    }


    //shelf interface
    //view shelf stock
    //restock item
    //auto stock
    //manual stock



}
