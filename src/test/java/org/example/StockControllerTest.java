package org.example;

import org.example.Controller.ItemController;
import org.example.Controller.ShelfController;
import org.example.Controller.StockController;
import org.example.Database.DatabaseConnection;
import org.example.Model.Item;
import org.example.Model.Shelf;
import org.example.Model.Stock;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class StockControllerTest {

    private StockController stockController;
    private ItemController itemController;
    private Connection testConnection;
    private ShelfController shelfController;

    @BeforeEach
    void setUp() throws Exception {
        DatabaseConnection databaseConnection = new DatabaseConnection();
        testConnection = databaseConnection.connect();

        stockController = new StockController();
        itemController = new ItemController();
        shelfController = new ShelfController();

    }

    @Test
    void addItemsToStock_Success() throws Exception {
        // Arrange
        Item item = new Item("15789", "Kandos White Chocolate", 200);
        itemController.AddItem(List.of(item));
        Item savedItem = itemController.getItem_from_Code("15789");

        Stock stock = new Stock(savedItem,170, new Date(System.currentTimeMillis() + 86400000));
        stock.setAvailability(true);

        // Act
        stockController.addStock(stock);

        // Assert
        int stockQuantity = stockController.get_Stock_quantity_by_item(savedItem);
        assertEquals(170, stockQuantity);
    }

    @Test
    void getStockQuantityByItem_WithStock() throws Exception {

        Item savedItem = itemController.getItem_from_Code("15789");



        // Act
        int totalQuantity = stockController.get_Stock_quantity_by_item(savedItem);

        // Assert
        assertEquals(170, totalQuantity);
    }

    @Test
    void getStockQuantityByItem_NoStock() throws Exception {

        Item savedItem = itemController.getItem_from_Code("0003");

        // Act
        int totalQuantity = stockController.get_Stock_quantity_by_item(savedItem);

        // Assert
        assertEquals(0, totalQuantity);
    }

    @Test
    void reduceStockQuantityAndUpdateStockShelfTable_Success() throws Exception {
        // Arrange
        Item savedItem = itemController.getItem_from_Code("15789");

        Shelf shelf = new Shelf(savedItem,40 , "Store");

        // Create a shelf
        shelfController.add_items_to_shelf(shelf);

        int shelfId = shelfController.get_latest_added_shelf_id();

        // Act
        stockController.reduce_stock_quantity_and_update_stock_shelf_table(savedItem, 60, shelfId);

        // Assert
        // Verify stock quantities
        int remainingStock = stockController.get_Stock_quantity_by_item(savedItem);
        assertEquals(70, remainingStock); // 170 - (40 + 60) = 70
    }
}