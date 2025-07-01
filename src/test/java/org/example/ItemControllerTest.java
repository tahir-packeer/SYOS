package org.example;

import org.example.Controller.ItemController;
import org.example.Database.DatabaseConnection;
import org.example.Model.Item;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ItemControllerTest {
    DatabaseConnection dbConnection = new DatabaseConnection();
    private ItemController itemController;
    private Connection testConnection;

    @BeforeEach
    void setUp() throws SQLException, ClassNotFoundException {
        testConnection = dbConnection.connect();

        itemController = new ItemController();
    }

    @Test
    void addItem_Success() throws SQLException, ClassNotFoundException {
        // Arrange
        Item item1 = new Item("0024", "Wheat Flour", 259);
        Item item2 = new Item("00478", "Coconut Oil", 780);
        List<Item> items = Arrays.asList(item1, item2);

        itemController.AddItem(items);

        // Assert - verify items were added by retrieving them
        Item retrieved1 = itemController.getItem_from_Code("0024");
        assertNotNull(retrieved1);
        assertEquals("Wheat Flour", retrieved1.getName());
        assertEquals(259, retrieved1.getPrice());

        Item retrieved2 = itemController.getItem_from_Code("00478");
        assertNotNull(retrieved2);
        assertEquals("Coconut Oil", retrieved2.getName());
        assertEquals(780, retrieved2.getPrice());
    }

    @Test
    void getItemFromCode_Success() throws SQLException, ClassNotFoundException {
        // Arrange - insert test data directly
        try (Statement stmt = testConnection.createStatement()) {
            stmt.execute("INSERT INTO items(code, name, price) VALUES('00897', 'Chocolate Fingers', 250)");
        }

        // Act
        Item result = itemController.getItem_from_Code("00897");

        // Assert
        assertNotNull(result);
        assertEquals("00897", result.getCode());
        assertEquals("Chocolate Fingers", result.getName());
        assertEquals(250, result.getPrice());
        assertTrue(result.getId() > 0);
    }

    @Test
    void getItemFromCode_NotFound() throws SQLException, ClassNotFoundException {
        // Act
        Item result = itemController.getItem_from_Code("NONEXISTENT");

        // Assert
        assertNull(result);
    }

    @Test
    void updateItem_Success() throws SQLException, ClassNotFoundException {
        // Arrange - insert test data directly
        try (Statement stmt = testConnection.createStatement()) {
            stmt.execute("INSERT INTO items(code, name, price) VALUES('01254', 'Pepsi', 180)");
        }

        // Get the item to update
        Item itemToUpdate = itemController.getItem_from_Code("01254");
        assertNotNull(itemToUpdate);

        // Modify the item
        itemToUpdate.setName("Pepsi 1l");
        itemToUpdate.setPrice(280);

        // Act
        itemController.UpdateItem(itemToUpdate);

        // Assert - retrieve the item and verify changes
        Item updatedItem = itemController.getItem_from_Code("01254");
        assertNotNull(updatedItem);
        assertEquals("Pepsi 1l", updatedItem.getName());
        assertEquals(280, updatedItem.getPrice());
    }

    @Test
    void getItemFromId_Success() throws SQLException, ClassNotFoundException {
        // Arrange - insert test data directly
        try (Statement stmt = testConnection.createStatement()) {
            stmt.execute("INSERT INTO items(code, name, price) VALUES('03698', 'Dilmah Tea', 500)");
            // Get the generated ID
            ResultSet rs = stmt.executeQuery("SELECT id FROM items WHERE code = '03698'");
            assertTrue(rs.next());
            int id = rs.getInt(1);

            // Act
            Item result = itemController.getItem_from_Id(id);

            // Assert
            assertNotNull(result);
            assertEquals("03698", result.getCode());
            assertEquals("Dilmah Tea", result.getName());
            assertEquals(500, result.getPrice());
            assertEquals(id, result.getId());
        }
    }

    @Test
    void getItemFromId_NotFound() throws SQLException, ClassNotFoundException {
        // Act
        Item result = itemController.getItem_from_Id(999);

        // Assert
        assertNull(result);
    }
}