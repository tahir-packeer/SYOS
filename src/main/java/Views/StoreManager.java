package Views;

import org.example.Controller.Authentication;
import org.example.Controller.ItemController;
import org.example.Controller.StockController;
import org.example.Model.Item;
import org.example.Model.Stock;
import org.example.Model.User;

import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

public class StoreManager {

    Scanner scanner = new Scanner(System.in);

    public void storeManagerDashboard(User user) throws SQLException, ClassNotFoundException, ParseException {
        System.out.println("Welcome Store Manager: " + user.getName());
        System.out.println("Store Manager Dashboard");
        System.out.println("1. View Items");
        System.out.println("2. Add Item");
        System.out.println("3. Update Item");
        System.out.println("4. Add Stock");
        System.out.println("5. Shelf Management");
        System.out.println("6. Logout");

        int choice = scanner.nextInt();
        scanner.nextLine();

        switch (choice) {
            case 1:
                viewItems();
                storeManagerDashboard(user);
                break;

            case 2:
                addItem_Interface(user);

            case 3:
                updateItem_Interface(user);
            case 4:
                addStock_Interface(user);
            case 5:
                new ShelfView().shelf_management_Interface(user);
            case 6:
                System.out.println("Logging out...");
                Authentication.ApplicationStartup();
                break;
            default:
                System.out.println("Invalid choice, please try again.");
                storeManagerDashboard(user);
        }
    }

    public void viewItems() {
        // Logic to view items
        System.out.println("Viewing items is not implemented yet.");List<Item> items = new ItemController().getAllItems();

        if (items.isEmpty()) {
            System.out.println("No items available.");
        } else {
            System.out.println("Available Items:");
            for (Item item : items) {
                System.out.println("Code: " + item.getCode() + ", Name: " + item.getName() + ", Price: " + item.getPrice());
            }
        }
    }

    public void addItem_Interface(User user) throws SQLException, ClassNotFoundException, ParseException {

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

        new ItemController().AddItem(itemList);

        storeManagerDashboard(user);
    }

    public void updateItem_Interface(User user) throws SQLException, ClassNotFoundException, ParseException {

        System.out.println("Enter the Item Code to update:");
        String itemCode = scanner.nextLine();

        Item searchedItem = new ItemController().getItem_from_Code(itemCode);

        if (searchedItem != null) {
            System.out.println("Current Item Details:");
            System.out.println("Code: " + searchedItem.getCode());
            System.out.println("Name: " + searchedItem.getName());
            System.out.println("Price: " + searchedItem.getPrice());

            System.out.println("Enter new name (or press Enter to keep current):");
            String newName = scanner.nextLine();
            if (!newName.isEmpty()) {
                searchedItem.setName(newName);
            }

            System.out.println("Enter new price (or press Enter to keep current):");
            String priceInput = scanner.nextLine();
            if (!priceInput.isEmpty()) {
                double newPrice = Double.parseDouble(priceInput);
                searchedItem.setPrice(newPrice);
            }

            new ItemController().UpdateItem(searchedItem);

            System.out.println("Item updated successfully.");
            storeManagerDashboard(user);

        } else {
            System.out.println("Item not found.");
        }
    }

    public void addStock_Interface(User user) throws SQLException, ClassNotFoundException, ParseException {
        System.out.println("Add Stock");

        System.out.println("Enter Item Code:");
        String itemCode = scanner.nextLine();

        Item item = new ItemController().getItem_from_Code(itemCode);
        if (item != null) {
            System.out.println("Current Stock for " + item.getName() + ": " + item.getPrice());

            System.out.println("Is this the correct item? (yes/no)");
            String confirmation = scanner.nextLine();

            if (!confirmation.equalsIgnoreCase("yes")) {

                System.out.println("Item not confirmed. Returning to dashboard.");
                addItem_Interface(user);
                return;
            }

            System.out.println("Enter new stock quantity:");
            int quantity = scanner.nextInt();
            scanner.nextLine();

            System.out.println("Enter the Date of Expiry (dd/mm/yyyy):");
            String expiryDate = scanner.nextLine();

            Date dateOfExpiry = new java.text.SimpleDateFormat("dd/MM/yyyy").parse(expiryDate);
            java.sql.Date sqlDateOfExpiry = new java.sql.Date(dateOfExpiry.getTime());

            Stock currentStock = new Stock(item, quantity, sqlDateOfExpiry, sqlDateOfExpiry, true);

            new StockController().addStock(currentStock);

            storeManagerDashboard(user);

        } else {
            System.out.println("Item not found.");
        }
    }




}

