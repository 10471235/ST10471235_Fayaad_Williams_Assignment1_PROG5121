package messenger;

import junit.framework.TestCase;

public class LoginTest extends TestCase {
    private Login login;

    protected void setUp() throws Exception {
        login = new Login("Kyle", "Smith");
        login.registerUser("kyl_1", "Ch&&sec@ke99!", "+27838968976");
    }

    // Username tests
    public void testUsernameCorrectlyFormatted() {
        assertTrue(login.checkUserName("kyl_1"));
    }

    public void testUsernameWithoutUnderscore() {
        assertFalse(login.checkUserName("kyle1"));
    }

    // Password tests
    public void testPasswordMeetsComplexity() {
        assertTrue(login.checkPasswordComplexity("Ch&&sec@ke99!"));
    }

    public void testPasswordTooSimple() {
        assertFalse(login.checkPasswordComplexity("password"));
    }
}
