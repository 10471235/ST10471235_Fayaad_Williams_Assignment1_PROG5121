package messenger;

//Created with help from ChatGPT – OpenAI

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LoginGUI extends JFrame {
    private JTextField firstNameField, lastNameField, usernameField, cellphoneField, loginUsernameField;
    private JPasswordField passwordField, loginPasswordField;
    private JTextArea outputArea;

    private Login login;

    public LoginGUI() {
        setTitle("User Registration and Login");
        setSize(500, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        //Input fields
        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new GridLayout(10, 2));

        inputPanel.add(new JLabel("First Name:"));
        firstNameField = new JTextField();
        inputPanel.add(firstNameField);

        inputPanel.add(new JLabel("Last Name:"));
        lastNameField = new JTextField();
        inputPanel.add(lastNameField);

        inputPanel.add(new JLabel("Username:"));
        usernameField = new JTextField();
        inputPanel.add(usernameField);

        inputPanel.add(new JLabel("Password:"));
        passwordField = new JPasswordField();
        inputPanel.add(passwordField);

        inputPanel.add(new JLabel("Cellphone (+27...):"));
        cellphoneField = new JTextField();
        inputPanel.add(cellphoneField);

        JButton registerButton = new JButton("Register");
        inputPanel.add(registerButton);

        inputPanel.add(new JLabel("")); 

        inputPanel.add(new JLabel("Login Username:"));
        loginUsernameField = new JTextField();
        inputPanel.add(loginUsernameField);

        inputPanel.add(new JLabel("Login Password:"));
        loginPasswordField = new JPasswordField();
        inputPanel.add(loginPasswordField);

        JButton loginButton = new JButton("Login");
        inputPanel.add(loginButton);

        add(inputPanel, BorderLayout.CENTER);

        //Output for errors and more
        outputArea = new JTextArea(6, 20);
        outputArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(outputArea);
        add(scrollPane, BorderLayout.SOUTH);

        //Register area
        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                login = new Login(firstNameField.getText(), lastNameField.getText());
                String username = usernameField.getText();
                String password = new String(passwordField.getPassword());
                String cellphone = cellphoneField.getText();

                String result = login.registerUser(username, password, cellphone);
                outputArea.setText(result);
            }
        });

        //Login area
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (login == null) {
                    outputArea.setText("Please register first.");
                    return;
                }

                String inputUsername = loginUsernameField.getText();
                String inputPassword = new String(loginPasswordField.getPassword());

                boolean success = login.loginUser(inputUsername, inputPassword);
                outputArea.setText(login.returnLoginStatus(success));
                
                if (success) {
                    dispose();
                    new QuickChatApp(login);
                }
            }
        });

        setVisible(true);
    }

public static void main(String[] args) {
    new LoginGUI();
}
}
// --- End of Code ---
