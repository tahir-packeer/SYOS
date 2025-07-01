package org.example;

import org.example.Controller.BillController;
import org.example.Controller.CustomerController;
import org.example.Controller.ItemController;
import org.example.Database.DatabaseConnection;
import org.example.Model.Bill;
import org.example.Model.BillItem;
import org.example.Model.Customer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class BillControllerTest {
    private BillController billController;
    private ItemController itemController;
    private DatabaseConnection databaseConnection;
    private CustomerController customerController;

    @BeforeEach
    void setUp() throws Exception {
        billController = new BillController();
        itemController = new ItemController();
        databaseConnection = new DatabaseConnection();
        customerController = new CustomerController();

        databaseConnection.connect();
    }

    @Test
    void getInvoiceNumber_Success() throws Exception {

        // Act
        String bill = billController.getInvoiceNumber();

        // Assert
        assertNotNull(bill, "Bill Invoice Number should not be null");

        assertEquals(9, bill.length(), "Bill Invoice Number should be 10 characters long");
        assertEquals("INV-", bill.substring(0, 4), "Bill Invoice Number should start with 'INV-'");

        System.out.println("Bill Invoice Number : " + bill);
    }

    @Test
    void AddBill_Success() throws Exception {
        // Arrange
        String invoiceNumber = billController.getInvoiceNumber();
        Customer customer = customerController.get_Customer_by_contactNumber("1234567890");

        Bill bill = new Bill(customer, invoiceNumber, 9000, 0,10000,1000);

        // Act
       Bill retrievedBill =  billController.Add_Bill(bill);

        // Assert - verify bill was added by retrieving it

        assertNotNull(retrievedBill, "Retrieved Bill should not be null");
        assertEquals(invoiceNumber, retrievedBill.getInvoiceNumber(), "Invoice Number should match");
        assertEquals("1234567890", retrievedBill.getCustomer().getcontactNumber(), "Contact Number should match");
        assertEquals(9000, retrievedBill.getFullPrice(), "Total Amount should match");
    }

    @Test
    void AddBillItems_Success() throws Exception {
        // Arrange
        String invoiceNumber = billController.getInvoiceNumber();
        Customer customer = customerController.get_Customer_by_contactNumber("1234567890");

        Bill bill = new Bill(customer, invoiceNumber, 3517, 0,3600,83);

        List<BillItem> billItems = new ArrayList<>();

        // Add items to the bill
        BillItem item1 = new BillItem(itemController.getItem_from_Code("15789"), 2);
        BillItem item2 = new BillItem(itemController.getItem_from_Code("0024"), 3);
        BillItem item3 = new BillItem(itemController.getItem_from_Code("00478"), 3);
        billItems.add(item1);
        billItems.add(item2);
        billItems.add(item3);


        // Act
        Bill retrievedBill = billController.Add_Bill(bill);
        billController.add_Bill_items(billItems, retrievedBill);




        // Assert - verify bill items were added by retrieving it
        assertNotNull(retrievedBill, "Retrieved Bill should not be null");
        assertEquals(invoiceNumber, retrievedBill.getInvoiceNumber(), "Invoice Number should match");
    }


}
