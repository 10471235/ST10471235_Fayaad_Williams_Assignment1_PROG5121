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
import java.util.Random;

public class Message {
    // Static arrays for message management
    private static List<Message> sentMessages = new ArrayList<>();
    private static List<Message> disregardedMessages = new ArrayList<>();
    private static List<Message> storedMessages = new ArrayList<>();
    private static List<String> messageHashes = new ArrayList<>();
    private static List<String> messageIds = new ArrayList<>();
    private static int totalMessagesSent = 0;
    
    // Instance variables
    private String messageId;
    private int numSent;
    private String recipient;
    private String messageContent;
    private String messageHash;
    private String status;

    public Message(String recipient, String messageContent) {
        this.messageId = generateMessageId();
        this.numSent = ++totalMessagesSent;
        this.recipient = recipient;
        this.messageContent = messageContent;
        this.messageHash = createMessageHash();
        
        // Add to tracking lists
        messageIds.add(this.messageId);
        messageHashes.add(this.messageHash);
    }

    private String generateMessageId() {
        Random random = new Random();
        return String.valueOf(1000000000L + random.nextInt(900000000));
    }

    public String createMessageHash() {
    // Handle empty or null message content
    if (messageContent == null || messageContent.trim().isEmpty()) {
        return messageId.substring(0, 2) + ":" + numSent + ":EMPTY";
    }
    
    String[] words = messageContent.trim().split("\\s+"); // Split by whitespace
    
    String firstWord = words.length > 0 ? words[0] : "";
    String lastWord = words.length > 1 ? words[words.length-1] : firstWord;
    
    // Handle empty words
    firstWord = firstWord != null ? firstWord.toUpperCase() : "";
    lastWord = lastWord != null ? lastWord.toUpperCase() : "";
    
    return messageId.substring(0, 2) + ":" + numSent + ":" + firstWord + lastWord;
}

    public String sentMessage() {
        String[] options = {"Send", "Discard", "Store"};
        int choice = JOptionPane.showOptionDialog(null,
                "What would you like to do with this message?",
                "Message Options",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null, options, options[0]);

        if (choice==0){ {
                status = "Sent";
                sentMessages.add(this);
                return "Message sent successfully.";}
         if (choice==1){ // Discard
                status = "Discarded";
                disregardedMessages.add(this);
                totalMessagesSent=totalMessagesSent-1;
                return "Message discarded.";}
        else if(choice==2){ // Store
                status = "Stored";
                storedMessages.add(this);
                storeMessage();
                return "Message stored successfully.";}
        else if(choice==-1){
                return "No action taken.";}
        }
        return 
    }

    public static void loadStoredMessages() {
        try {
            if (!Files.exists(Paths.get("messages.json"))) {
                return; // No file exists yet
            }

            String content = new String(Files.readAllBytes(Paths.get("messages.json")));
            if (content.trim().isEmpty()) {
                return; // Empty file
            }

            JSONArray jsonArray = new JSONArray(content);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject obj = jsonArray.getJSONObject(i);
                Message msg = new Message(obj.getString("recipient"), obj.getString("messageContent"));
                msg.messageId = obj.getString("messageId");
                msg.messageHash = obj.getString("messageHash");
                msg.status = obj.getString("status");
                
                if ("Stored".equals(msg.status)) {
                    storedMessages.add(msg);
                }
            }
        } catch (Exception e) {
            System.err.println("Error loading messages: " + e.getMessage());
        }
    }

    public void storeMessage() {
        try {
            JSONObject jsonMsg = new JSONObject();
            jsonMsg.put("messageId", messageId);
            jsonMsg.put("recipient", recipient);
            jsonMsg.put("messageContent", messageContent);
            jsonMsg.put("messageHash", messageHash);
            jsonMsg.put("status", status);

            JSONArray jsonArray;
            try {
                String content = new String(Files.readAllBytes(Paths.get("messages.json")));
                jsonArray = new JSONArray(content);
            } catch (IOException e) {
                jsonArray = new JSONArray();
            }

            jsonArray.put(jsonMsg);

            try (FileWriter writer = new FileWriter("messages.json")) {
                writer.write(jsonArray.toString(4));
            }
        } catch (Exception e) {
            System.err.println("Error storing message: " + e.getMessage());
        }
    }
    public static String displaySendersAndRecipients() {
        StringBuilder sb = new StringBuilder();
        sb.append(" Senders and Recipients ");
        
        for(Message msg : sentMessages) {
            sb.append("To: ").append(msg.getRecipient())
              .append(" | Message: ").append(msg.getMessageContent())
              .append("\n");
        }
        for (Message msg : storedMessages) {
            sb.append("[STORED] To: ").append(msg.getRecipient())
              .append(" | Message: ").append(msg.getMessageContent())
              .append("\n");
        }
        
        return sb.toString();
    }
    
    public static String searchByMessageId(String messageId) {
            // Seach in sent messages
            for (Message msg : sentMessages) {
                if (msg.getMessageId().equals(messageId)) {
                    return "Found in sent message:\n" +
                            "To: " + msg.getRecipient() + "\n" + 
                            "Message: " + msg.getMessageContent();
                }
            }
            for (Message msg : storedMessages) {
                if (msg.getMessageId().equals(messageId)) {
                    return "Found in stored messages:\n" +
                            "To: " + msg.getRecipient() + "\n" +
                            "Message: " + msg.getMessageContent();
                }
            }
            
            return "No message found with ID: " + messageId;
    }
    
    public static String deleteByHash(String hash) {
        for(Message msg : sentMessages) {
            if (msg.getMessageHash().equals(hash)) {
                sentMessages.remove(msg);
                return "Deleted sent message: " + msg.getMessageContent();
            }
        }
        for (Message msg : storedMessages) {
            if (msg.getMessageHash().equals(hash)) {
                storedMessages.remove(msg);
                removeFromJsonFile(msg);
                return "Deleted stored message: " + msg.getMessageContent();
            }
        }
        return "No message found with hash: " + hash;
    }
    
private static void removeFromJsonFile(Message msgToRemove) {
    try {
        if (!Files.exists(Paths.get("messages.json"))) {
            return;
        }
        
        String content = new String(Files.readAllBytes(Paths.get("messages.json")));
        JSONArray jsonArray = new JSONArray(content);
        JSONArray newArray = new JSONArray();
        
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject obj = jsonArray.getJSONObject(i);
            if (!obj.getString("messageHash").equals(msgToRemove.getMessageHash())) {
                newArray.put(obj);
            }
        }
        
        try (FileWriter writer = new FileWriter("messages.json")) {
            writer.write(newArray.toString(4));
        }
    } catch (Exception e) {
        System.err.println("Error updating JSON file: " + e.getMessage());
    }
    
    }
    // Reporting methods
    public static String generateReport() {
        StringBuilder sb = new StringBuilder();
        sb.append("=== MESSAGE REPORT ===\n");
        sb.append("Total Sent: ").append(sentMessages.size()).append("\n");
        sb.append("Total Stored: ").append(storedMessages.size()).append("\n");
        sb.append("Total Discarded: ").append(disregardedMessages.size()).append("\n\n");
        
        sb.append("SENT MESSAGES:\n");
        for (Message msg : sentMessages) {
            sb.append("ID: ").append(msg.messageId).append("\n");
            sb.append("To: ").append(msg.recipient).append("\n");
            sb.append("Message: ").append(msg.messageContent).append("\n");
            sb.append("Hash: ").append(msg.messageHash).append("\n\n");
        }
        return sb.toString();
    }

    public static String findLongestMessage() {
        Message longest = null;
        for (Message msg : sentMessages) {
            if (longest == null || msg.messageContent.length() > longest.messageContent.length()) {
                longest = msg;
            }
        }
        return longest != null ? longest.messageContent : "No messages found";
    }

    
    
    public static String searchByRecipient(String number) {
        StringBuilder sb = new StringBuilder();
        sb.append("Messages to ").append(number).append(":\n");
        
        for (Message msg : sentMessages) {
            if (msg.recipient.equals(number)) {
                sb.append("[SENT] ").append(msg.messageContent).append("\n");
            }
        }
        
        for (Message msg : storedMessages) {
            if (msg.recipient.equals(number)) {
                sb.append("[STORED] ").append(msg.messageContent).append("\n");
            }
        }
        
        return sb.length() > 0 ? sb.toString() : "No messages found for this recipient";
    }

    // Getters
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