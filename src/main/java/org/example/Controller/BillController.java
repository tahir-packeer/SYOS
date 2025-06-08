package org.example.Controller;

import org.example.Database.DatabaseConnection;
import org.example.Model.Bill;
import org.example.Model.BillItem;

import java.sql.*;
import java.util.List;

public class BillController {

    public String getInvoiceNumber() throws SQLException, ClassNotFoundException {
        DatabaseConnection db = new DatabaseConnection();
        Connection connection = db.connect();
        String query = "SELECT COUNT(*) AS total FROM bill";

        try (PreparedStatement stmt = connection.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {
            if (rs.next()) {
                int count = rs.getInt("total") + 1;
                return String.format("INV-%05d", count);
            }
        }
        return "INV00001";
    }



    public Bill Add_Bill(Bill bill) throws SQLException, ClassNotFoundException {
        DatabaseConnection db = new DatabaseConnection();
        Connection connection = db.connect();
        PreparedStatement statement = null;
        String query = "INSERT INTO bill (customer_id, invoiceNumber, fullPrice, discount, cashTendered,changeAmount,billDate) VALUES (?, ?, ?, ?, ?, ?,?)";
        statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
        if (bill.getCustomer() != null) {
            statement.setInt(1, bill.getCustomer().getId());
        } else {
            statement.setNull(1, Types.INTEGER);
        }
        statement.setString(2, bill.getInvoiceNumber());
        statement.setDouble(3, bill.getFullPrice());
        statement.setDouble(4, bill.getDiscount());
        statement.setDouble(5, bill.getCashTendered());
        statement.setDouble(6, bill.getChangeAmount());
        statement.setObject(7, bill.getBillDate());

        int rowsInserted = statement.executeUpdate();

        if (rowsInserted > 0) {
            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    int generatedId = generatedKeys.getInt(1);
                    bill.setId(generatedId);
                    System.out.println("Bill added successfully with ID: " + generatedId);
                }
            }
        } else {
            System.out.println("Failed to add bill.");
        }
        return bill;
    }

    public void add_Bill_items
            (List<BillItem> billItems, Bill bill)
            throws SQLException, ClassNotFoundException
    {
        DatabaseConnection db = new DatabaseConnection();
        Connection connection = db.connect();
        PreparedStatement Billstatement = null;
        PreparedStatement ShelfStatement = null;

        String BillItemquery = "INSERT INTO billItem ( item_id,bill_id, quantity, itemPrice, totalPrice) VALUES (?, ?, ?, ?,?)";
        String updateShelfQuery = "UPDATE shelf SET quantity = quantity - ? WHERE item_id = ? AND quantity >= ? LIMIT 1";

        Billstatement = connection.prepareStatement(BillItemquery);
        ShelfStatement = connection.prepareStatement(updateShelfQuery);

        for (BillItem billItem : billItems) {
            Billstatement.setInt(1, billItem.getItem().getId());
            Billstatement.setInt(2, bill.getId());
            Billstatement.setInt(3, billItem.getQuantity());
            Billstatement.setDouble(4, billItem.getItemPrice());
            Billstatement.setDouble(5, billItem.getTotalPrice());

            Billstatement.executeUpdate();

            ShelfStatement.setInt(1, billItem.getQuantity());
            ShelfStatement.setInt(2, billItem.getItem().getId());
            ShelfStatement.setInt(3, billItem.getQuantity());
            ShelfStatement.executeUpdate();

        }
        Billstatement.close();
        ShelfStatement.close();
    }

}