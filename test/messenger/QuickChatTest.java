package messenger;

import junit.framework.TestCase;

public class QuickChatTest extends TestCase {
    private QuickChatApp quickChat;
    private Login login;

    protected void setUp() throws Exception {
        login = new Login("Test", "User");
        login.registerUser("test_1", "Test123!", "+27831234567");
        quickChat = new QuickChatApp(login);
    }

    public void testSentMessagesArrayPopulated() {
        assertFalse(Message.getSentMessages().isEmpty());
    }

    public void testLongestMessageDetection() {
        String longest = quickChat.findLongestMessage();
        assertTrue(longest.contains("dinner tonight")); 
    }
}
