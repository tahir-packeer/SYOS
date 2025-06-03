package org.example;

import org.example.Database.DatabaseConnection;
import org.junit.jupiter.api.Test;
import java.sql.Connection;
import static org.junit.jupiter.api.Assertions.*;

public class DatabaseConnectionTest {

    @Test
    void testConnect() {
        DatabaseConnection db = new DatabaseConnection();
        try (Connection conn = db.connect()) {
            assertNotNull(conn, "Connection should not be null");
            assertFalse(conn.isClosed(), "Connection should be open");
        } catch (Exception e) {
            fail("Exception thrown during DB connection: " + e.getMessage());
        }
    }
} 