/*
 * Author: Fayaad Williams
 * Student Number: ST10471235
 * Project: Quick chat app
 *
 * Note:
 * Portions of this code were developed and debugged with the assistance of ChatGPT (OpenAI, 2025)
 * for code explanation, error resolution and structure guidance.
 * URL: https://chat.openai.com
 */

package messenger;

import org.json.JSONArray;
import org.json.JSONObject;
import javax.swing.*;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * The Message class handles all message functions including:
 * Creating and storing messages
 * Generating reports
 * Searching and deleting messages
 * storing messages to JSON file
 */
public class Message {
    //arrays for message details
    private static List<Message> sentMessages = new ArrayList<>();
    private static List<Message> disregardedMessages = new ArrayList<>();
    private static List<Message> storedMessages = new ArrayList<>();
    private static List<String> messageHashes = new ArrayList<>();
    private static List<String> messageIds = new ArrayList<>();
    private static int totalMessagesSent = 0;
    
    //private variables
    private String messageId; // Unique ID for the message
    private int numSent; // Sequence number of message
    private String recipient; // Recipient's phone number
    private String messageContent; // The message text
    private String messageHash; // Unique hash for the message
    private String status; // Message status (Sent/Stored/Discarded)

     /**
     * Constructor creates a new message with automatic ID and hash generation.
     * String recipient - The recipient's phone number
     * String messageContent - The message text
     */
    public Message(String recipient, String messageContent) { //for message report
        this.messageId = generateMessageId(); // Generate unique ID
        this.numSent = ++totalMessagesSent;  // Increment and assign sequence number
        this.recipient = recipient;
        this.messageContent = messageContent;
        this.messageHash = createMessageHash(); //later used for deleting messages
        
        //tracking lists
        messageIds.add(this.messageId);
        messageHashes.add(this.messageHash);
    }

    //Generates a random 10 digit message ID.
  
    private String generateMessageId() {
        long randomNumber = (long)(Math.random() * 900000000);
        return String.valueOf(1000000000L + randomNumber); //generates a message id with the use of a random number
    }

     /**
     * Creates a unique hash for the message made with:
     * First 2 digits of message ID
     * Sequence number
     * First and last words of message (uppercase)
     */
    public String createMessageHash() {
    //for empty message content
    if (messageContent == null || messageContent.trim().isEmpty()) {
        return messageId.substring(0, 2) + ":" + numSent + ":EMPTY";
    }
    
    String[] words = messageContent.trim().split("\\s+"); //for handling whitespace(ChatGPT reccomendation)
    // Get first and last words
    String firstWord = words.length > 0 ? words[0] : "";
    String lastWord = words.length > 1 ? words[words.length-1] : firstWord;
     // Convert to uppercase
    firstWord = firstWord != null ? firstWord.toUpperCase() : "";
    lastWord = lastWord != null ? lastWord.toUpperCase() : "";
     // Combine components to create hash
    return messageId.substring(0, 2) + ":" + numSent + ":" + firstWord + lastWord;
}

    //Presents options for handling a composed message.
    public String sentMessage() {
        // Options for message handling
    String[] options = {"Send Message", "Disregard Message", "Store Message to send later"};
    int choice = JOptionPane.showOptionDialog(null, "What would you like to do with the message?", "Message Options", JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);

    if (choice == 0) { //If choice is 0 then the message will be sent
        status = "Sent";
        sentMessages.add(this);
        return "Message sent successfully.";
    } 
    else if (choice == 1) { //If choice is 1 then the message will be discarded
        status = "Discarded";
        disregardedMessages.add(this);
        totalMessagesSent--;
        return "Message discarded.";
    } 
    else if (choice == 2) { //if choice is 2 then the message will be stored
        status = "Stored";
        storedMessages.add(this);
        storeMessage();
        return "Message stored successfully.";
    }
    else { //for any other choice
 return "No action taken.";
    }
}

        //Loads stored messages from JSON file into memory.
     
    public static void loadStoredMessages() {
        try {
            if (!Files.exists(Paths.get("messages.json"))) {
                return; //This output will be displayed if json file doesnt exists yet
            }

            String content = new String(Files.readAllBytes(Paths.get("messages.json")));
            if (content.trim().isEmpty()) {
                return; //this output will happen after reading an empty json file
            }

            JSONArray jsonArray = new JSONArray(content); //for message storing
                for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject obj = jsonArray.getJSONObject(i);
                Message msg = new Message(obj.getString("recipient"), obj.getString("messageContent"));
                msg.messageId = obj.getString("messageId");
                msg.messageHash = obj.getString("messageHash");
                msg.status = obj.getString("status");
                
                if ("Stored".equals(msg.status)) {
                    storedMessages.add(msg);//collects all information for message to be stored
                }
            }
        } catch (Exception e) {
            System.err.println("Error loading messages: " + e.getMessage());
        }
    }

     //Stores a message in JSON file for persistence.
    
public void storeMessage() {
        try {
            // Create JSON object for message
            JSONObject jsonMsg = new JSONObject();
            jsonMsg.put("messageId", messageId);
            jsonMsg.put("recipient", recipient);
            jsonMsg.put("messageContent", messageContent);
            jsonMsg.put("messageHash", messageHash);
            jsonMsg.put("status", status); //collects array info to store

            // Load existing messages or create new array
            JSONArray jsonArray;
            try {
                String content = new String(Files.readAllBytes(Paths.get("messages.json")));
                jsonArray = new JSONArray(content);
            } catch (IOException e) {
                jsonArray = new JSONArray();
                
                // Add new message and write to file
            } jsonArray.put(jsonMsg);

            try (FileWriter writer = new FileWriter("messages.json")) {
                writer.write(jsonArray.toString(4)); //stores messages in messages.json
            }
        } catch (Exception e) {
            System.err.println("Error storing message: " + e.getMessage());
        }
    }

//Generates a report of all senders and recipients.

    public static String displaySendersAndRecipients() {
        StringBuilder sb = new StringBuilder();
        sb.append(" Senders and Recipients ");
        
        // Add sent messages
        for(Message msg : sentMessages) {
            sb.append("To: ").append(msg.getRecipient())
              .append(" | Message: ").append(msg.getMessageContent())
              .append("\n"); //displays all messages you have sent
        }
          // Add stored messages
        for (Message msg : storedMessages) {
            sb.append("[STORED] To: ").append(msg.getRecipient())
              .append(" | Message: ").append(msg.getMessageContent())
              .append("\n"); // displayed all messages you have stored
        }return sb.toString();
    }
    
       /**
     * Searches for a message by its ID.
     * String messageId - The ID to search for
     */
    public static String searchByMessageId(String messageId) {
            //search sent messages by the message ID shows whether its a sent message or stored message.
            for (Message msg : sentMessages) {
                if (msg.getMessageId().equals(messageId)) {
                    return "Found in sent message:\n" +
                            "To: " + msg.getRecipient() + "\n" + 
                            "Message: " + msg.getMessageContent();
                }
            }
            // Search stored messages
            for (Message msg : storedMessages) {
                if (msg.getMessageId().equals(messageId)) {
                    return "Found in stored messages:\n" +
                            "To: " + msg.getRecipient() + "\n" +
                            "Message: " + msg.getMessageContent();
                }
            }return "No message found with ID: " + messageId;
    }
    
       /**
     * Deletes a message by its hash.
     * String hash - The hash of message to delete
     */
    public static String deleteByHash(String hash) { //the actual method for deleting messages both sent and stored 
        for(Message msg : sentMessages) {
            if (msg.getMessageHash().equals(hash)) {//retrieving the message hash
                sentMessages.remove(msg);//removal of message
                return "Deleted sent message: " + msg.getMessageContent();
            }
        }
        // Search and delete from stored messages
        for (Message msg : storedMessages) {
            if (msg.getMessageHash().equals(hash)) {
                storedMessages.remove(msg);
                removeFromJsonFile(msg);
                return "Deleted stored message: " + msg.getMessageContent();
            }
        }return "No message found with hash: " + hash;
    }
    
    /**
     * Removes a message from the JSON storage file.
     * Message msgToRemove - The message to remove
     */
private static void removeFromJsonFile(Message msgToRemove) { 
    try {
        // Check if file exists
        if (!Files.exists(Paths.get("messages.json"))) {
            return;
        }
        
        // Read and parse existing messages
        String content = new String(Files.readAllBytes(Paths.get("messages.json")));
        JSONArray jsonArray = new JSONArray(content);
        JSONArray newArray = new JSONArray();
        
          // Copy all messages except the one to remove
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject obj = jsonArray.getJSONObject(i);
            if (!obj.getString("messageHash").equals(msgToRemove.getMessageHash())) {
                newArray.put(obj); //once message is delete it must be removed from being stored in the json file
            }
            // Write updated array back to file
        }try (FileWriter writer = new FileWriter("messages.json")) {
            writer.write(newArray.toString(4));
        }
    } catch (Exception e) {
        System.err.println("Error updating JSON file: " + e.getMessage());
    }
    
    }
    //method to generate the message report
    public static String generateReport() {
        StringBuilder sb = new StringBuilder();
        sb.append("MESSAGE REPORT\n");
        sb.append("Total Sent: ").append(sentMessages.size()).append("\n");
        sb.append("Total Stored: ").append(storedMessages.size()).append("\n");
        sb.append("Total Discarded: ").append(disregardedMessages.size()).append("\n\n");
        sb.append("SENT MESSAGES:\n");
        for (Message msg : sentMessages) {
        sb.append("ID: ").append(msg.messageId).append("\n");
        sb.append("To: ").append(msg.recipient).append("\n");
        sb.append("Message: ").append(msg.messageContent).append("\n");
        sb.append("Hash: ").append(msg.messageHash).append("\n\n");
        }return sb.toString();
    }

    //Finds the longest message in sent messages.
    public static String findLongestMessage() {
        Message longest = null;
        // Find message with maximum length
        for (Message msg : sentMessages) {
            if (longest == null || msg.messageContent.length() > longest.messageContent.length()) {
            longest = msg;
            }
        }return longest != null ? longest.messageContent : "No messages found";
    }

    
    /**
     * Searches for messages by recipient number.
     * String number - The recipient's number to search for
     */
    public static String searchByRecipient(String number) { //returns messages sent to the number whether stored or sent
        StringBuilder sb = new StringBuilder();
        sb.append("Messages to ").append(number).append(":\n");
        
        // Search sent messages
        for (Message msg : sentMessages) {
            if (msg.recipient.equals(number)) {//checking phone number
                sb.append("[SENT] ").append(msg.messageContent).append("\n");
            }
        }
        
        // Search stored messages
        for (Message msg : storedMessages) {
            if (msg.recipient.equals(number)) {
            sb.append("[STORED] ").append(msg.messageContent).append("\n");
            }
        }return sb.length() > 0 ? sb.toString() : "No messages found for this recipient";
    }

    //getters for exposing private variables
  public static List<Message> getSentMessages() { return sentMessages; }
    public static List<Message> getStoredMessages() { return storedMessages; }
    public static List<Message> getDisregardedMessages() { return disregardedMessages; }
    public static List<String> getMessageHashes() { return messageHashes; }
    public static List<String> getMessageIds() { return messageIds; }
    public String getMessageId() { return messageId; }
    public String getRecipient() { return recipient; }
    public String getMessageContent() { return messageContent; }
    public String getMessageHash() { return messageHash; }
    public String getStatus() { return status; }
}