package Views;

import org.example.Controller.ItemController;
import org.example.Controller.ShelfController;
import org.example.Controller.StockController;
import org.example.Model.Item;
import org.example.Model.Shelf;
import org.example.Model.Shelf_Stock_Information;
import org.example.Model.User;

import java.sql.SQLException;
import java.text.ParseException;
import java.util.List;
import java.util.Scanner;

public class ShelfView {

    Scanner scanner = new Scanner(System.in);

    public void shelf_management_Interface
            (User user)
            throws SQLException, ClassNotFoundException, ParseException {
        System.out.println("Shelf Management Interface");
        System.out.println("1. Add Shelf");
        System.out.println("2. View Shelves");
        System.out.println("3. Restock Shelf");
        System.out.println("4. Back to Dashboard");

        int choice = scanner.nextInt();
        scanner.nextLine();

        switch(choice){
            case 1:
                addShelf_Interface(user);
                break;
            case 2:
                viewShelves_Interface(user);
                break;
            case 3:
                restock_Shelf_Interface(user);
                break;
            case 4:
                System.out.println("Returning to Dashboard...");
                new StoreManager().storeManagerDashboard(user);
                break;
            default:
                System.out.println("Invalid choice, please try again.");
                shelf_management_Interface(user);
        }
    }

    public void addShelf_Interface
            (User user)
            throws SQLException, ClassNotFoundException, ParseException {
        System.out.println("Adding Shelf Interface");

        System.out.println("Enter the Item Code:");
        String itemCode = scanner.nextLine();

        Item selectedItem = new ItemController().getItem_from_Code(itemCode);
        if (selectedItem == null) {
            System.out.println("Invalid Item Code. Please try again.");
            addShelf_Interface(user);
            return;
        }
        int totalQuantity = new StockController().get_Stock_quantity_by_item(selectedItem);
        if (selectedItem != null)
        {
            System.out.println(selectedItem.getCode() + "  |  " + selectedItem.getName() + "  |  " + selectedItem.getPrice() + "  |  " + totalQuantity);

            System.out.println("Enter the Quantity to add to the shelf:");
            int quantity = scanner.nextInt();

            if (quantity > totalQuantity) {
                System.out.println("Quantity exceeds available stock. Please enter a valid quantity.");
                addShelf_Interface(user);
                return;
            } else if (quantity <= 0) {
                System.out.println("Invalid quantity. Please enter a positive number.");
                addShelf_Interface(user);
                return;
            }

            System.out.println("Select the Shelf Type (Website/Store):");
            String shelfType = null;
            while (shelfType == null || shelfType.isEmpty()) {
                shelfType = scanner.nextLine();
            }


            if (shelfType.equalsIgnoreCase("Website") || shelfType.equalsIgnoreCase("Store")) {

                Shelf shelf = new Shelf(
                        selectedItem,
                        quantity,
                        shelfType
                );

                new ShelfController().add_items_to_shelf(shelf);

                shelf_management_Interface(user);

            } else {
                System.out.println("Invalid Shelf Type. Please enter 'Website' or 'Store'.");
                addShelf_Interface(user);
            }


        }else {
            System.out.println("Invalid Item Code Please try again.");
        }
    }

    public void viewShelves_Interface
            (User user)
            throws SQLException, ClassNotFoundException, ParseException {

        List<Shelf> shelves = new ShelfController().get_all_shelves();

        if (shelves.isEmpty()) {
            System.out.println("Shelves are empty.");
        } else {
            System.out.printf("%-5s %-20s %-10s %-10s%n", "ID", "Item Name", "Quantity", "Type");
            System.out.println("---------------------------------------------------------");

            for (Shelf shelf : shelves) {
                Item item = shelf.getItem();
                String itemName = (item != null) ? item.getName() : "Unknown";

                System.out.printf("%-5d %-20s %-10d %-10s%n",
                        shelf.getId(),
                        itemName,
                        shelf.getQuantity(),
                        shelf.getType());
            }
        }

        shelf_management_Interface(user);
    }

    public void restock_Shelf_Interface
            (User user)
            throws SQLException, ClassNotFoundException, ParseException
    {
        List<Shelf_Stock_Information> shelves = new ShelfController().get_Low_Shelf_With_Stock();

        System.out.printf("%-5s %-10s %-20s %-10s %-10s %-15s%n", "ID", "Item Code", "Item Name", "Shelf Qty", "Type", "Stock Qty");
        System.out.println("---------------------------------------------------------------");

        if (shelves.isEmpty()) {
            System.out.println("No matching shelves found.");
            return;
        }

        for (Shelf_Stock_Information info : shelves) {
            System.out.printf("%-5d %-10s %-20s %-10d %-10s %-15d%n",
                    info.getShelfId(),
                    info.getItemCode(),
                    info.getItemName(),
                    info.getShelfQuantity(),
                    info.getType(),
                    info.getTotalStockQuantity());
        }

        System.out.println("Enter the Shelf ID to restock:");
        int shelfId = scanner.nextInt();

        System.out.println("Enter the quantity to restock:");
        int quantity = scanner.nextInt();

        Shelf shelf = new ShelfController().get_Shelf_By_Id(shelfId);
        if (shelf == null) {
            System.out.println("Invalid Shelf ID. Please try again.");
            restock_Shelf_Interface(user);
            return;
        }

        if (quantity <= 0) {
            System.out.println("Invalid quantity. Please enter a positive number.");
            restock_Shelf_Interface(user);
            return;
        }

        int totalStockQuantity = new StockController().get_Stock_quantity_by_item(shelf.getItem());
        if (quantity > totalStockQuantity) {
            System.out.println("Quantity exceeds available stock. Please enter a valid quantity.");
            restock_Shelf_Interface(user);
            return;
        }

        new ShelfController().restock_Shelf(shelf, quantity);
        shelf_management_Interface(user);
    }
}

