package org.example.Database;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    private final String url = "jdbc:mysql://localhost:3306/syos";
    private final String username = "root";
    private final String password = "9900@tahir";

    public Connection connect() throws ClassNotFoundException, SQLException
    {
        Class.forName("com.mysql.cj.jdbc.Driver");

        return DriverManager.getConnection(url, username, password);
    }

    public void closeConnection(Connection connection) {
        try
        {
            if (connection != null && !connection.isClosed())
            {
                connection.close();
            }
        } catch (SQLException e)
        {
            System.out.println("Failed to close connection: " + e.getMessage());
        }
    }
}