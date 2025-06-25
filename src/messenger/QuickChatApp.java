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

import javax.swing.*;

/**
 * This class provides the main chat application interface after login.
 * It offers a menu system for sending messages, viewing reports and other features.
 */
public class QuickChatApp {
    private Login userSession;

    /**
     * Constructor initialises the chat application.
     * Login loginSession - The authenticated user session
     */
    public QuickChatApp(Login loginSession) {
        this.userSession = loginSession;
        Message.loadStoredMessages();//loads previously stored messages
        showMainMenu(); // Display the main menu
    }

    //Displays and handles the main menu options.
     
    private void showMainMenu() {
        boolean running = true;
        // Main menu options
        while (running) {
            String[] options = {"Send Messages", "Show Recent Messages","Message Reports","Quit"};
            // Show menu dialog
            int choice = JOptionPane.showOptionDialog(null,"Welcome to QuickChat\nPlease select an option:","QuickChat Menu",JOptionPane.DEFAULT_OPTION,JOptionPane.PLAIN_MESSAGE,null,options,options[0]);
            
            // Handle menu choice
            if (choice==0) {
            handleSendMessages(); // Send new messages
            }else if(choice==1){ //This option shows recent messages
                    JOptionPane.showMessageDialog(null,Message.generateReport());
}                else if(choice==2){ //This option displays message reports
                    showReportsMenu();
}                else if(choice==3){running = false; // Quit application
                    JOptionPane.showMessageDialog(null, "Thank you for using QuickChat!");} //This option exits the program
else if(choice==-1){ //What happens after window is closed.
                    running = false;
                    JOptionPane.showMessageDialog(null, 
                        "Thank you for using QuickChat!");
            }
        }
    }
    
   //Displays and handles the reports menu options.
    private void showReportsMenu() {
        // Reports menu options
        String[] options = {"Display Senders and Recipients","Find Longest Message","Search by Message ID","Search by Recipient","Delete by Message Hash","Generate Full Report","Back to Main Menu"};
        
        int choice = JOptionPane.showOptionDialog(null,"Message Reports\nSelect an option:","Reports Menu",JOptionPane.DEFAULT_OPTION,JOptionPane.PLAIN_MESSAGE,null,options,options[0]);
         // Show reports menu dialog
        if (choice==0) {
                JOptionPane.showMessageDialog(null, Message.displaySendersAndRecipients());              //senders and recipients
        } else if (choice==1) {//display longest message
                JOptionPane.showMessageDialog(null, "Longest message:\n" + Message.findLongestMessage());
}            else if(choice==2){ //search by message Id
                String id = JOptionPane.showInputDialog("Enter Message ID:");
                if (id != null) {
                    JOptionPane.showMessageDialog(null, Message.searchByMessageId(id));
                }
}            else if(choice==3){ //search by recipient number
                String recipient = JOptionPane.showInputDialog("Enter The Recipient's Number:");
                if (recipient != null) {
                    JOptionPane.showMessageDialog(null, Message.searchByRecipient(recipient));
                }
}            else if(choice==4){ //delete by message hash
                String hash = JOptionPane.showInputDialog("Enter Message Hash:");
                if (hash != null) {
        JOptionPane.showMessageDialog(null, Message.deleteByHash(hash));
                }
}
else if(choice==5){ //to generate a full report of messages
                JOptionPane.showMessageDialog(null, Message.generateReport());
        } 
    }

      //Handles the process of sending multiple messages.
    private void handleSendMessages() {
        try {
            // Get number of messages to send
            String numStr = JOptionPane.showInputDialog("Enter amount of messages you want to send.");
            if (numStr == null) return;
            
            int numMessages = Integer.parseInt(numStr); //converts the string to an integer
            
            // Process each message
            for (int i = 0; i < numMessages; i++) {
                // Get recipient number
                String recipient = JOptionPane.showInputDialog("Enter recieving number(+27):");
                if (recipient == null) break;
                
                // Get message content
                String content = JOptionPane.showInputDialog("Enter your message (max 250 characters):");
                if (content == null) break;
                
                // Validate message length
                if (content.length() > 250) {
                    JOptionPane.showMessageDialog(null, "Message too long! Longer than 250 characters.");
                    i--; //look to retry message
                    continue;
                }
                
                // Create and send message
                Message msg = new Message(recipient, content);
                String result = msg.sentMessage();
                JOptionPane.showMessageDialog(null, result);
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Please enter a valid number");
        }
    }
}