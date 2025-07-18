package org.example.Controller;

import org.example.Database.DatabaseConnection;
import org.example.Model.Item;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ItemController {
    public void AddItem(List<Item> items) throws SQLException, ClassNotFoundException {

        DatabaseConnection db = new DatabaseConnection();
        Connection connection = db.connect();
        PreparedStatement statement = null;

        // Loop through the list of items and insert each one into the database

        for (Item item : items) {
            statement = connection.prepareStatement("insert into items(code, name, price) values(?,?,?)");
            statement.setString(1, item.getCode());
            statement.setString(2, item.getName());
            statement.setDouble(3, item.getPrice());

            int rowsInserted = statement.executeUpdate();

            if (rowsInserted < 0) {
                System.out.println("Failed to add item " + item.getName() + ".");
            }
        }
        if (statement != null) {
            statement.close();
        }
        if (connection != null) {
            db.closeConnection(connection);
        }
    }


    public Item getItem_from_Code(String Code) throws SQLException, ClassNotFoundException {

        DatabaseConnection db = new DatabaseConnection();
        Connection connection = db.connect();
        PreparedStatement statement = null;
        Item item = null;

        statement = connection.prepareStatement("select * from items where code = ?");
        statement.setString(1, Code);
        ResultSet databaseResponse = statement.executeQuery();

        if (databaseResponse.next()) {
            item = new Item(databaseResponse.getString("code"), databaseResponse.getString("name"), databaseResponse.getDouble("price"));
            item.setId(databaseResponse.getInt("id"));
        } else {
            System.out.println("Item with code " + Code + " not found.");
        }

        return item;
    }

    public Item getItem_from_Id(int id) throws SQLException, ClassNotFoundException {

        DatabaseConnection db = new DatabaseConnection();
        Connection connection = db.connect();
        PreparedStatement statement = null;
        Item item = null;


        statement = connection.prepareStatement("select * from items where id = ?");
        statement.setInt(1, id);
        ResultSet databaseResponse = statement.executeQuery();

        if (databaseResponse.next()) {
            item = new Item(databaseResponse.getString("code"), databaseResponse.getString("name"), databaseResponse.getDouble("price"));
            item.setId(databaseResponse.getInt("id"));
        } else {
            System.out.println("Item with id " + id + " not found.");
        }

        return item;
    }

    public boolean UpdateItem(Item item) throws SQLException, ClassNotFoundException {
        DatabaseConnection db = new DatabaseConnection();
        Connection connection = db.connect();
        PreparedStatement statement = null;
        boolean updated = false;

        try {
            String sql = "UPDATE items SET name = ?, price = ? WHERE code = ?";
            statement = connection.prepareStatement(sql);
            statement.setString(1, item.getName());
            statement.setDouble(2, item.getPrice());
            statement.setString(3, item.getCode());


            int rowsUpdated = statement.executeUpdate();
            updated = rowsUpdated > 0;
        } finally {
            if (statement != null) statement.close();
            if (connection != null) db.closeConnection(connection);
        }
        return updated;
    }

    public List<Item> getAllItems() {
        DatabaseConnection db = new DatabaseConnection();
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        List<Item> items = new ArrayList<>();

        try {
            connection = db.connect();
            String sql = "SELECT * FROM items";
            statement = connection.prepareStatement(sql);
            resultSet = statement.executeQuery();

            while (resultSet.next()) {
                Item item = new Item(resultSet.getString("code"), resultSet.getString("name"), resultSet.getDouble("price"));
                item.setId(resultSet.getInt("id"));
                items.add(item);
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return items;
    }
}



