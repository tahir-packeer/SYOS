package org.example.Controller;

import org.example.Database.DatabaseConnection;
import org.example.Model.Item;
import org.example.Model.Stock;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class StockController {
    public void addStock(Stock stock) throws SQLException, ClassNotFoundException {
        DatabaseConnection db = new DatabaseConnection();
        Connection connection = db.connect();
        PreparedStatement statement = null;
        // This method adds a new stock entry to the database
        try {
            String sql = "INSERT INTO stock (item_id, quantity, date_of_purchase, date_of_expiry, availabiity) VALUES (?, ?, ?, ?, ?)";
            statement = connection.prepareStatement(sql);
            statement.setInt(1, stock.getItem().getId());
            statement.setInt(2, stock.getQuantity());
            statement.setDate(3, stock.getDateOfPurchase());
            statement.setDate(4, stock.getDateOfExpiry());
            statement.setBoolean(5, stock.isAvailability());
            statement.executeUpdate();
        } finally {
            if (statement != null) statement.close();
            if (connection != null) db.closeConnection(connection);
        }
    }
    public int get_Stock_quantity_by_item(Item item) throws SQLException, ClassNotFoundException {
        DatabaseConnection db = new DatabaseConnection();
        Connection connection = db.connect();
        PreparedStatement statement = null;

        statement = connection.prepareStatement("select sum(quantity) from stock where item_id = ?");
        statement.setInt(1, item.getId());

        ResultSet resultSet = statement.executeQuery();

        if (resultSet.next()) {
            int totalQuantity = resultSet.getInt(1);
            return totalQuantity;
        } else {
            System.out.println("No stock found for item: " + item.getName());
            return 0;
        }
    }

    public void reduce_stock_quantity_and_update_stock_shelf_table(Item item, int quantity, int shelf_id) throws SQLException, ClassNotFoundException {
        DatabaseConnection db = new DatabaseConnection();
        Connection connection = db.connect();

        try{

            // It reduces the stock quantity for a specific item and updates the shelf_stock table
            connection.setAutoCommit(false);

            int remainingQuantity = quantity;

            PreparedStatement getStockQuery = connection.prepareStatement(
                    "SELECT id, quantity FROM stock WHERE item_id = ? AND quantity > 0 ORDER BY date_of_expiry ASC"
            );
            getStockQuery.setInt(1, item.getId());
            ResultSet stockOfItem = getStockQuery.executeQuery();

            // Iterate through the stock of the item and reduce the quantity
            while (stockOfItem.next()) {
                int stockId = stockOfItem.getInt("id");
                int stockQuantity = stockOfItem.getInt("quantity");

                int quantityToReduce = Math.min(remainingQuantity, stockQuantity);
                PreparedStatement updateStockQuery = connection.prepareStatement(
                        "UPDATE stock SET quantity = quantity - ? WHERE id = ?"
                );
                updateStockQuery.setInt(1, quantityToReduce);
                updateStockQuery.setInt(2, stockId);
                updateStockQuery.executeUpdate();
                updateStockQuery.close();

                PreparedStatement updateStockShelfQuery = connection.prepareStatement(
                        "INSERT INTO shelf_stock (stock_id, shelf_id) VALUES (?, ?)"
                );
                updateStockShelfQuery.setInt(1, stockId);
                updateStockShelfQuery.setInt(2, shelf_id);
                updateStockShelfQuery.executeUpdate();
                updateStockShelfQuery.close();

                remainingQuantity -= quantityToReduce;
            }

            stockOfItem.close();
            getStockQuery.close();

            if (remainingQuantity == 0) {
                connection.commit();
                System.out.println("Stock transferred to shelf for item: " + item.getName());
            } else {
                connection.rollback();
                System.out.println("Insufficient stock to transfer to shelf for item: " + item.getName());
            }
        }
        catch (SQLException e)
        {
            connection.rollback();
            throw e;
        }
        finally {
            connection.setAutoCommit(true);
            connection.close();
        }
    }
}