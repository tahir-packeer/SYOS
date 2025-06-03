package org.example;

import org.example.Controller.Authentication;
import org.example.Model.User;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
public class AuthenticationTest {

    @Test
    void getCashierDetails_when_loggedInAsCashier() {
        Authentication authentication = new Authentication();
        String name = "Tahir";
        String password = "password";
        User user = authentication.AuthenticateUser(name, password);
        assertNotNull(user, "User should not be null after successful authentication");
        assertEquals("Tahir", user.getName(), "Username should match");
        assertEquals("cashier", user.getType(), "User should have role cashier");
    }

    @Test
    void getManagerDetails_when_loggedInAsManager() {
        Authentication authentication = new Authentication();
        String name = "Susanthika";
        String password = "password";
        User user = authentication.AuthenticateUser(name, password);
        assertNotNull(user, "User should not be null after successful authentication");
        assertEquals("Susanthika", user.getName(), "Username should match");
        assertEquals("manager", user.getType(), "User should have role manager");
    }

    @Test
    void getStoreManagerDetails_when_loggedInAsStoreManager() {
        Authentication authentication = new Authentication();
        String name = "Susantha";
        String password = "password";
        User user = authentication.AuthenticateUser(name, password);
        assertNotNull(user, "User should not be null after successful authentication");
        assertEquals("Susantha", user.getName(), "Username should match");
        assertEquals("storemanager", user.getType(), "User should have role storemanager");
    }

    @Test
    void getInvalidUserDetails_when_loggedInWithInvalidCredentials() {
        Authentication authentication = new Authentication();
        String name = "invalidUser";
        String password = "wrongPassword";
        User user = authentication.AuthenticateUser(name, password);
        assertNull(user, "User should be null for invalid credentials");
    }
}
