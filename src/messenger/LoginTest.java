package messenger;

import org.junit.Test;
import static org.junit.Assert.*;

public class LoginTest {

    //Test username validation
    @Test
    public void testValidUsername() {
        Login user = new Login("John", "Doe");
        assertTrue(user.checkUserName("jd_1"));
    }

    @Test
    public void testInvalidUsernameNoUnderscore() {
        Login user = new Login("John", "Doe");
        assertFalse(user.checkUserName("john"));
    }

    @Test
    public void testInvalidUsernameTooLong() {
        Login user = new Login("John", "Doe");
        assertFalse(user.checkUserName("john_doe"));
    }

    // Test password validation
    @Test
    public void testValidPassword() {
        Login user = new Login("John", "Doe");
        assertTrue(user.checkPasswordComplexity("Passw0rd!"));
    }

    @Test
    public void testInvalidPasswordTooShort() {
        Login user = new Login("John", "Doe");
        assertFalse(user.checkPasswordComplexity("Pw0!"));
    }

    @Test
    public void testInvalidPasswordNoSpecialChar() {
        Login user = new Login("John", "Doe");
        assertFalse(user.checkPasswordComplexity("Password1"));
    }

    @Test
    public void testInvalidPasswordNoCapital() {
        Login user = new Login("John", "Doe");
        assertFalse(user.checkPasswordComplexity("password1!"));
    }

    @Test
    public void testInvalidPasswordNoDigit() {
        Login user = new Login("John", "Doe");
        assertFalse(user.checkPasswordComplexity("Password!"));
    }

    // Test cellphone number validation
    @Test
    public void testValidCellNumber() {
        Login user = new Login("John", "Doe");
        assertTrue(user.checkCellPhoneNumber("+27812345678"));
    }

    @Test
    public void testInvalidCellNoPlus27() {
        Login user = new Login("John", "Doe");
        assertFalse(user.checkCellPhoneNumber("0812345678"));
    }

    @Test
    public void testInvalidCellTooShort() {
        Login user = new Login("John", "Doe");
        assertFalse(user.checkCellPhoneNumber("+2781234"));
    }

    @Test
    public void testInvalidCellWithLetters() {
        Login user = new Login("John", "Doe");
        assertFalse(user.checkCellPhoneNumber("+27ABC45678"));
    }

    // Test registration and login logic
    @Test
    public void testSuccessfulRegistration() {
        Login user = new Login("John", "Doe");
        String result = user.registerUser("jd_1", "Passw0rd!", "+27812345678");
        assertEquals("User registered successfully.", result);
    }

    @Test
    public void testLoginSuccess() {
        Login user = new Login("John", "Doe");
        user.registerUser("jd_1", "Passw0rd!", "+27812345678");
        assertTrue(user.loginUser("jd_1", "Passw0rd!"));
    }

    @Test
    public void testLoginFail() {
        Login user = new Login("John", "Doe");
        user.registerUser("jd_1", "Passw0rd!", "+27812345678");
        assertFalse(user.loginUser("jd_1", "WrongPass!"));
    }

    @Test
    public void testLoginStatusMessageSuccess() {
        Login user = new Login("Jane", "Smith");
        user.registerUser("js_1", "Abc123!@", "+27876543210");
        boolean loginResult = user.loginUser("js_1", "Abc123!@");
        String message = user.returnLoginStatus(loginResult);
        assertEquals("Welcome Jane, Smith it is great to see you again.", message);
    }

    @Test
    public void testLoginStatusMessageFail() {
        Login user = new Login("Jane", "Smith");
        user.registerUser("js_1", "Abc123!@", "+27876543210");
        boolean loginResult = user.loginUser("js_1", "wrong123");
        String message = user.returnLoginStatus(loginResult);
        assertEquals("Username or password incorrect, please try again.", message);
    }
}


