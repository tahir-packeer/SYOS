package Views;

import org.example.Controller.ItemController;
import org.example.Controller.ShelfController;
import org.example.Controller.StockController;
import org.example.Model.Item;
import org.example.Model.Shelf;
import org.example.Model.User;

import java.sql.SQLException;
import java.util.Scanner;

public class ShelfManagement {

    Scanner scanner = new Scanner(System.in);

    public void shelf_management_Interface(User user) throws SQLException, ClassNotFoundException {
        System.out.println("Shelf Management Interface");
        System.out.println("1. Add Shelf");

        int choice = scanner.nextInt();
        scanner.nextLine();

        switch(choice){
            case 1:
                addShelf_Interface(user);
                break;
            default:
                System.out.println("Invalid choice, please try again.");
                shelf_management_Interface(user);
        }
    }

    public void addShelf_Interface(User user) throws SQLException, ClassNotFoundException {
        System.out.println("Adding Shelf Interface");

        System.out.println("Enter the Item Code:");
        String itemCode = scanner.nextLine();

        Item selectedItem = new ItemController().getItem_from_Code(itemCode);
        int totalQuantity = new StockController().get_Stock_quantity_by_item(selectedItem);
        if (selectedItem != null) {
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
            String shelfType = scanner.nextLine();

            if (shelfType.equalsIgnoreCase("Website") || shelfType.equalsIgnoreCase("Store")) {

                Shelf shelf = new Shelf(
                        selectedItem,
                        50,
                        shelfType
                );

                new ShelfController().add_items_to_shelf(shelf);

            } else {
                System.out.println("Invalid Shelf Type. Please enter 'Website' or 'Store'.");
                addShelf_Interface(user);
            }
        }else {
            System.out.println("Invalid Item Code Please try again.");
        }
    }
}