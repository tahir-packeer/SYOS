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
        System.out.println("5. Logout");

        int choice = scanner.nextInt();
        scanner.nextLine(); // Consume newline

        switch (choice) {
            case 1:
                getItemById_Interface(user);
                break;

            case 2:
                    addItem_Interface(user);
                break;
            case 3:
                updateItem_Interface(user);
                break;
            case 4:

            case 5:
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

    public void getItemById_Interface(User user) throws SQLException, ClassNotFoundException {
        System.out.println("Enter the CODE of the item you want to retrieve:");
        String code = scanner.nextLine();

        Item item = new ItemController().getItemById(code);
        if (item != null) {
            System.out.println("Item found:");
            System.out.println("ID: " + item.getId());
            System.out.println("Code: " + item.getCode());
            System.out.println("Name: " + item.getName());
            System.out.println("Price: " + item.getPrice());
        } else {
            System.out.println("Item not found with code: " + code);
        }
        storeManagerDashboard(user);
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
                System.out.println("Item updated successfully ✅");
            } else {
                System.out.println("Item update failed. Item code may not exist.");
            }
        } catch (SQLException | ClassNotFoundException e) {
            System.out.println("Error updating item: " + e.getMessage());
        }

        storeManagerDashboard(user);
    }

}
