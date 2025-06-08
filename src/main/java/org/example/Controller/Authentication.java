package org.example.Controller;

import Views.Admin;
import Views.Cashier;
import Views.StoreManager;
import org.example.Database.DatabaseConnection;
import org.example.Model.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.Scanner;

public class Authentication {

    public static void ApplicationStartup() throws SQLException, ClassNotFoundException, ParseException {

        Scanner input = new Scanner(System.in);

        System.out.println("Welcome to Synex Outlet Store");
        System.out.println("Please login to continue");

        System.out.println("Enter your Name:");
        String username = input.nextLine();

        System.out.println("Enter your Password:");
        String password = input.nextLine();

        Authentication auth = new Authentication();
        User user = auth.AuthenticateUser(username, password);

        if (user != null)
        {

            String userType = user.getType();
            switch (userType.toLowerCase()) {
                case "cashier":
                    Cashier cashier = new Cashier();
                    cashier.cashierInterface(user);
                    System.out.println("Welcome Cashier: " + user.getName());
                    break;
                case "storemanager":
                    StoreManager storeManager = new StoreManager();
                    storeManager.storeManagerDashboard(user);
                    break;
                case "manager":
                    //admin
                    Admin admin = new Admin();
                    admin.adminInterface(user);

                default:
                    System.out.println("Unknown user type: " + userType);
            }
        }
        else
        {
            ApplicationStartup();
        }
    }

    public User AuthenticateUser(String username, String password) {

        DatabaseConnection db = new DatabaseConnection();
        try {
            Connection connection = db.connect();
            PreparedStatement statement = null;
            ResultSet resultSet = null;
            User user = null;

            String query = "SELECT * FROM users WHERE name=? AND password=?";
            statement = connection.prepareStatement(query);
            statement.setString(1, username);
            statement.setString(2, password);
            resultSet = statement.executeQuery();

            if (resultSet.next())
            {
                String id = resultSet.getString("id");
                String name = resultSet.getString("name");
                String type = resultSet.getString("type");

                user = new User(id, name, password, type);

                return user;
            }
            else
            {
                System.out.println("Invalid username or password");
            }
        }
        catch (ClassNotFoundException e)
        {
            System.out.println("Driver not found");
        }
        catch (SQLException e)
        {
            System.out.println("Connection failed");
        }
        return null;
    }

}