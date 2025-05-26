package messenger;

import junit.framework.TestCase;
import java.io.File;

public class MessageTest extends TestCase {
    private Message message;

    protected void setUp() throws Exception {
        message = new Message("+27718693002", "Hi Mike, can you join us for dinner tonight");
        new File("messages.json").delete(); // Clear test file
    }

    public void testMessageWithinLengthLimit() {
        assertTrue(message.getMessageContent().length() <= 250);
    }

    public void testRecipientNumberCorrectFormat() {
        try {
            new Message("+27718693002", "Test");
            assertTrue(true);
        } catch (IllegalArgumentException e) {
            fail("Valid number threw exception");
        }
    }

    public void testMessageStorage() {
        message.storeMessage();
        assertTrue(new File("messages.json").exists());
    }
}
