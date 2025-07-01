package org.example;

import org.example.Controller.CustomerController;
import org.example.Database.DatabaseConnection;
import org.example.Model.Customer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class CustomerControllerTest {
    private CustomerController controller;
    private DatabaseConnection connection;

    @BeforeEach
    void setUp() throws Exception {
        connection = new DatabaseConnection();
        controller = new CustomerController();

        // Initialize the database connection
        connection.connect();
    }

    @Test
    void addCustomer_Success() throws Exception {
        // Arrange
        String contactNumber = "1234567890";
        String name = "John Doe";

        Customer customer = new Customer(name,contactNumber);

        // Act
        controller.add_Customer(customer);

        // Assert - verify customer was added by retrieving it
        Customer addedCustomer = controller.get_Customer_by_contactNumber(contactNumber);
        assertNotNull(addedCustomer, "Customer should not be null");
        assertEquals(name, addedCustomer.getName(), "Customer name should match");
        assertEquals(contactNumber, addedCustomer.getcontactNumber(), "Customer address should match");
    }

    @Test
    void get_customer_from_contactNumber_Success() throws Exception {
        // Arrange
        String contactNumber = "1234567890";

        // Act
        var customer = controller.get_Customer_by_contactNumber(contactNumber);

        // Assert
        assertNotNull(customer, "Customer should not be null");
        assertEquals(contactNumber, customer.getcontactNumber(), "Contact number should match");
    }

    @Test
    void get_customer_from_contactNumber_NotFound() throws Exception {
        // Arrange
        String contactNumber = "0000000000"; // Non-existent contact number

        // Act
        Customer customer = controller.get_Customer_by_contactNumber(contactNumber);

        // Assert
        assertNull(customer, "Customer should be null for non-existent contact number");

    }
}
