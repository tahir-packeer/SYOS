package org.example;

import org.example.Controller.Authentication;
import org.example.Model.User;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class AuthenticationTest {

    @Test
    void get_CashierDetails_when_loggedInAsCashier() {
       Authentication authentication = new Authentication();

       String name = "Tahir";
       String password = "tahir123";

       User user = authentication.AuthenticateUser(name, password);

       assertNotNull(user, "User should not be null after successful authentication");
       assertEquals("Tahir", user.getName(), "Username should match");
       assertEquals("cashier", user.getType(), "User should have role cashier");
    }

    @Test
    void get_StoreManagerDetails_when_loggedInAsAdmin() {
        Authentication authentication = new Authentication();

        String name = "Susantha";
        String password = "susantha123";

        User user = authentication.AuthenticateUser(name, password);

        assertNotNull(user, "User should not be null after successful authentication");
        assertEquals("Susantha", user.getName(), "Username should match");
        assertEquals("storemanager", user.getType(), "User should have role admin");
    }

    @Test
    void get_ManagerDetails_when_loggedInAsAdmin() {
        Authentication authentication = new Authentication();

        String name = "Susanthika";
        String password = "susanthika123";

        User user = authentication.AuthenticateUser(name, password);

        assertNotNull(user, "User should not be null after successful authentication");
        assertEquals("Susanthika", user.getName(), "Username should match");
        assertEquals("manager", user.getType(), "User should have role admin");
    }

    @Test
    void get_invalidUserDetails_when_loggedInWithInvalidCredentials() {
        Authentication authentication = new Authentication();

        String name = "invalidUser";
        String password = "wrongPassword";

        User user = authentication.AuthenticateUser(name, password);

        assertEquals(null, user, "User should be null for invalid credentials");
    }
}
