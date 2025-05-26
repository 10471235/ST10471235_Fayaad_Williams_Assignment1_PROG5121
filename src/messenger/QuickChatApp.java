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

public class QuickChatApp {
    private Login userSession;

    public QuickChatApp(Login loginSession) {
        this.userSession = loginSession;
        // Load any previously stored messages
        Message.loadStoredMessages();
        showMainMenu();
    }

    private void showMainMenu() {
        boolean running = true;
        
        while (running) {
            String[] options = {"Send Messages", "Show Recent Messages","Message Reports","Quit"};
            
            int choice = JOptionPane.showOptionDialog(null,"Welcome to QuickChat\nPlease select an option:","QuickChat Menu",JOptionPane.DEFAULT_OPTION,JOptionPane.PLAIN_MESSAGE,null,options,options[0]);
            
            if (choice==0) {
            handleSendMessages();
            }else if(choice==1){ //This option shows recent messages
                    JOptionPane.showMessageDialog(null,Message.generateReport());
}                else if(choice==2){ //This option displays message reports
                    showReportsMenu();
}                else if(choice==3){running = false;
                    JOptionPane.showMessageDialog(null, "Thank you for using QuickChat!");} //This option exits the program
else if(choice==-1){ // Window closed
                    running = false;
                    JOptionPane.showMessageDialog(null, 
                        "Thank you for using QuickChat!");
            }
        }
    }

    private void showReportsMenu() {
        String[] options = {"Display Senders and Recipients","Find Longest Message","Search by Message ID","Search by Recipient","Delete by Message Hash","Generate Full Report","Back to Main Menu"};
        
        int choice = JOptionPane.showOptionDialog(null,"Message Reports\nSelect an option:","Reports Menu",JOptionPane.DEFAULT_OPTION,JOptionPane.PLAIN_MESSAGE,null,options,options[0]);
        
        if (choice==0) {
                JOptionPane.showMessageDialog(null, Message.displaySendersAndRecipients());              //senders and recipients
        } else if (choice==1) {//display longest message
                JOptionPane.showMessageDialog(null, "Longest message:\n" + Message.findLongestMessage());
}            else if(choice==2){ //search by message Id
                String id = JOptionPane.showInputDialog("Enter Message ID:");
                if (id != null) {
                    JOptionPane.showMessageDialog(null, Message.searchByMessageId(id));
                }
}            else if(choice==3){ //search by recipient
                String recipient = JOptionPane.showInputDialog("Enter Recipient Number:");
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

    private void handleSendMessages() {
        try {
            String numStr = JOptionPane.showInputDialog("How many messages would you like to send?");
            if (numStr == null) return;
            
            int numMessages = Integer.parseInt(numStr);
            
            for (int i = 0; i < numMessages; i++) {
                String recipient = JOptionPane.showInputDialog("Enter recipient number (+27 format):");
                if (recipient == null) break;
                
                String content = JOptionPane.showInputDialog("Enter your message (max 250 chars):");
                if (content == null) break;
                
                if (content.length() > 250) {
                    JOptionPane.showMessageDialog(null, "Message too long! Max 250 characters.");
                    i--; //look to retry message
                    continue;
                }
                
                Message msg = new Message(recipient, content);
                String result = msg.sentMessage();
                JOptionPane.showMessageDialog(null, result);
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Please enter a valid number");
        }
    }
}