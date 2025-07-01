package org.example.Controller;

import org.example.Database.DatabaseConnection;

import java.sql.Connection;
import java.sql.SQLException;

public class ReportController {

    public String generate_sales_report(String date) throws SQLException, ClassNotFoundException {

        DatabaseConnection db = new DatabaseConnection();
        Connection connection = db.connect();

        String query = "SELECT i.code, i.name, SUM(bi.quantity) as total_quantity, SUM(bi.totalPrice) as total_revenue " +
                "FROM billItem bi " +
                "JOIN items i ON bi.item_id = i.id " +
                "JOIN bill b ON bi.bill_id = b.id " +
                "WHERE b.billDate = ? " +
                "GROUP BY i.code, i.name " +
                "ORDER BY total_revenue DESC";

        try (var statement = connection.prepareStatement(query)) {
            statement.setString(1, date);
            var resultSet = statement.executeQuery();

            System.out.println("Sales Report for " + date + ":");
            System.out.printf("%-10s %-20s %-15s %-15s%n", "Code", "Item Name", "Total Quantity", "Total Revenue");
            System.out.println("------------------------------------------------------------");

            while (resultSet.next()) {
                String code = resultSet.getString("code");
                String name = resultSet.getString("name");
                int totalQuantity = resultSet.getInt("total_quantity");
                double totalRevenue = resultSet.getDouble("total_revenue");

                System.out.printf("%-10s %-20s %-15d $%-14.2f%n", code, name, totalQuantity, totalRevenue);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return query;
    }

    public void generate_items_need_shelving_report() throws SQLException, ClassNotFoundException {
        DatabaseConnection db = new DatabaseConnection();
        Connection connection = db.connect();

        String query = """
            SELECT s.id AS shelf_id, i.code AS item_code, i.name AS item_name, s.quantity AS shelf_quantity, s.type,
                   COALESCE(SUM(st.quantity), 0) AS total_stock_quantity
            FROM shelf s
            JOIN items i ON s.item_id = i.id
            LEFT JOIN stock st ON s.item_id = st.item_id
            GROUP BY s.id, s.item_id, i.name, i.code, s.quantity, s.type
            HAVING shelf_quantity < 50 AND total_stock_quantity > 0
            ORDER BY shelf_quantity ASC;
        """;

        try (var statement = connection.prepareStatement(query);
             var resultSet = statement.executeQuery()) {

            System.out.println("Items that need shelving:");
            System.out.printf("%-10s %-20s %-15s %-15s %-15s%n", "Shelf ID", "Item Code", "Item Name", "Shelf Quantity", "Type");
            System.out.println("------------------------------------------------------------");

            while (resultSet.next()) {
                int shelfId = resultSet.getInt("shelf_id");
                String itemCode = resultSet.getString("item_code");
                String itemName = resultSet.getString("item_name");
                int shelfQuantity = resultSet.getInt("shelf_quantity");
                String type = resultSet.getString("type");

                System.out.printf("%-10d %-20s %-15s %-15d %-15s%n", shelfId, itemCode, itemName, shelfQuantity, type);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void generate_reorder_stock_report() throws SQLException, ClassNotFoundException {
        DatabaseConnection db = new DatabaseConnection();
        Connection connection = db.connect();

        String query = """
            SELECT i.code AS item_code, i.name AS item_name, SUM(s.quantity) AS total_stock_quantity
            FROM stock s
            JOIN items i ON s.item_id = i.id
            GROUP BY i.code, i.name
            ORDER BY total_stock_quantity DESC;
        """;

        try (var statement = connection.prepareStatement(query);
             var resultSet = statement.executeQuery()) {

            System.out.println("Stock Report:");
            System.out.printf("%-10s %-20s %-15s%n", "Item Code", "Item Name", "Total Stock Quantity");
            System.out.println("------------------------------------------------------------");

            while (resultSet.next()) {
                String itemCode = resultSet.getString("item_code");
                String itemName = resultSet.getString("item_name");
                int totalStockQuantity = resultSet.getInt("total_stock_quantity");

                System.out.printf("%-10s %-20s %-15d%n", itemCode, itemName, totalStockQuantity);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void generate_stock_batch_report() throws SQLException, ClassNotFoundException {
        DatabaseConnection db = new DatabaseConnection();
        Connection connection = db.connect();

        String query = """
            SELECT s.id AS stock_id, i.code AS item_code, i.name AS item_name, s.quantity AS stock_quantity
            FROM stock s
            JOIN items i ON s.item_id = i.id
            ORDER BY s.id ASC;
        """;

        try (var statement = connection.prepareStatement(query);
             var resultSet = statement.executeQuery()) {

            System.out.println("Stock Batch Report:");
            System.out.printf("%-10s %-20s %-25s %-15s%n", "Stock ID", "Item Code", "Item Name", "Stock Quantity");
            System.out.println("------------------------------------------------------------");

            while (resultSet.next()) {
                int stockId = resultSet.getInt("stock_id");
                String itemCode = resultSet.getString("item_code");
                String itemName = resultSet.getString("item_name");
                int stockQuantity = resultSet.getInt("stock_quantity");


                System.out.printf("%-10d %-20s %-25s %-15s%n", stockId, itemCode, itemName, stockQuantity);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void generate_bill_transaction_report(String startDate, String endDate) throws SQLException, ClassNotFoundException {
        DatabaseConnection db = new DatabaseConnection();
        Connection connection = db.connect();

        String query = """
            SELECT b.id AS bill_id, b.billDate, SUM(bi.totalPrice) AS total_amount
            FROM bill b
            JOIN billItem bi ON b.id = bi.bill_id
            WHERE b.billDate BETWEEN ? AND ?
            GROUP BY b.id, b.billDate
            ORDER BY b.billDate ASC;
        """;

        try (var statement = connection.prepareStatement(query)) {
            statement.setString(1, startDate);
            statement.setString(2, endDate);
            var resultSet = statement.executeQuery();

            System.out.println("Bill Transaction Report from " + startDate + " to " + endDate + ":");
            System.out.printf("%-10s %-20s %-15s%n", "Bill ID", "Bill Date", "Total Amount");
            System.out.println("------------------------------------------------------------");

            while (resultSet.next()) {
                int billId = resultSet.getInt("bill_id");
                String billDate = resultSet.getString("billDate");
                double totalAmount = resultSet.getDouble("total_amount");

                System.out.printf("%-10d %-20s Rs%-14.2f%n", billId, billDate, totalAmount);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
