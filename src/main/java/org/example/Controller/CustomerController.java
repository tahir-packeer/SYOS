package org.example.Controller;

import org.example.Database.DatabaseConnection;
import org.example.Model.Customer;

import java.sql.Connection;
import java.sql.SQLException;

public class CustomerController {

    public Customer get_Customer_from_contactNumber
            (String contactNumber)
            throws SQLException, ClassNotFoundException
    {
        DatabaseConnection databaseConnection = new DatabaseConnection();
        Customer customer = null;
        Connection connection = databaseConnection.connect();

        var statement = connection.prepareStatement("select * from customers where contactNumber = ?");
        statement.setString(1, contactNumber);
        var resultSet = statement.executeQuery();
        if (resultSet.next()) {

            String customerName = resultSet.getString("name");
            String customerContact = resultSet.getString("contactNumber");

            customer = new Customer(customerName, customerContact);
            customer.setId(resultSet.getInt("id"));

            return customer;
        } else {
            System.out.println("Customer with contact number " + contactNumber + " not found.");

            return customer;
        }
    }

    public void add_Customer(Customer customer)
            throws SQLException, ClassNotFoundException
    {
        DatabaseConnection databaseConnection = new DatabaseConnection();
        Connection connection = databaseConnection.connect();

        var statement = connection.prepareStatement("insert into customers(name, contactNumber) values(?, ?)");
        statement.setString(1, customer.getName());
        statement.setString(2, customer.getcontactNumber());

        int rowsInserted = statement.executeUpdate();
        if (rowsInserted > 0) {
            System.out.println("Customer added successfully: " + customer.getName());
        } else {
            System.out.println("Failed to add customer: " + customer.getName());
        }
    }
}