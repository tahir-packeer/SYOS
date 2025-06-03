// In src/main/java/org/example/Controller/StockController.java

package org.example.Controller;

import org.example.Database.DatabaseConnection;
import org.example.Model.Stock;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class StockController {
    public void addStock(Stock stock) throws SQLException, ClassNotFoundException {
        DatabaseConnection db = new DatabaseConnection();
        Connection connection = db.connect();
        PreparedStatement statement = null;
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
}