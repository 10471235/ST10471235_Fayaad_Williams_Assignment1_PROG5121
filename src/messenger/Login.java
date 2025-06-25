/*
 * Author: Fayaad Williams
 * Student Number: ST10471235
 * Project: Quick Chat App
 *
 * Note:
 * Portions of this code were developed and debugged with the assistance of ChatGPT (OpenAI, 2025)
 * for code explanation, error resolution and structure guidance.
 * URL: https://chat.openai.com
 */

package messenger;
/**
 * The Login class handles user authentication including registration and login
 * functionality.
 * It validates user credentials (username, password, cellphone number) and manages
 * user sessions.
 */
public class Login {
// private fields to store user credentials
    private String username;
    private String password;
    private String cellphone;
    private String firstName;
    private String lastName;
    
/**
     * Constructor initializes a user with first and last name.
     * String firstName - User's first name
     * String lastName - User's last name
     */
    public Login(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
    }
    
    /**
     * this validates the username requirements.
     * that the Username must contain an underscore and be 5 characters or less.
     * String username - The username to validate
     * returns true if valid, false if not
     */
    public boolean checkUserName(String username) {
        this.username = username;
        return username.contains("_") && username.length() <= 5;
    }

    /**
     * validates password rules.
     * Password must be at least 8 characters long
     * at least one uppercase letter
     * at least one digit
     * at least one special character
     * String password - The password to validate
     * returns true if valid, false if not
     */
    public boolean checkPasswordComplexity(String password) {
        this.password = password;

        boolean hasUpper = false; // checks uppercase letters
        boolean hasDigit = false; // checks digits
        boolean hasSpecial = false;  // checks special characters


        if (password.length() < 8) return false; // Minimum length check
        
        // Checks each character in the password separately
        for (char ch : password.toCharArray()) {
            if (Character.isUpperCase(ch)) hasUpper = true;
            else if (Character.isDigit(ch)) hasDigit = true;
            else if (!Character.isLetterOrDigit(ch)) hasSpecial = true;
        }
        
        // All complexity requirements must be met
        return hasUpper && hasDigit && hasSpecial;
    }
    
/**
     * Validates South African cellphone number format.
     * Must start with +27 and then 9 digits.
     * String cellphone - The cellphone number to validate
     * returns true if valid, false if not
     */
    public boolean checkCellPhoneNumber(String cellphone) {
        this.cellphone = cellphone;

        // Must start with South African country code
        if (!cellphone.startsWith("+27")) {
            return false;
        }

        // Extract digits after country code
        String digits = cellphone.substring(3); // after +27

        // Must have exactly 9 digits
        if (digits.length() != 9) {
            return false;
        }

        // All characters must be digits
        for (char ch : digits.toCharArray()) {
            if (!Character.isDigit(ch)) {
                return false;
            }
        }

        return true;
    }

    /**
     * Registers a new user by validating credentials.
     * String username - The username to register
     * String password - The password to register
     * String cellphone - The cellphone number to register
     * @return Registration status message
     */
    public String registerUser(String username, String password, String cellphone) {
        boolean validUsername = checkUserName(username);
        boolean validPassword = checkPasswordComplexity(password);
        boolean validCell = checkCellPhoneNumber(cellphone);

        // Validate each credential
        if (!validUsername) {
            this.username = null;
            this.password = null;
            return "Username is not correctly formatted, please ensure that your username is less than five characters in length and contains an underscore.";
        }

        // Check each validation result and return appropriate message
        if (!validPassword) {
            this.username = null;
            this.password = null;
            return "Password is not correctly formatted, please ensure that the password contains at least eight characters, a capital letter, a number and a special character.";
        }

        if (!validCell) {
            this.username = null;
            this.password = null;
            return "Cell phone number incorrectly formatted or does not contain South African international code.";
        }

        return "User registered successfully.";
    }

    /**
     * Authenticates a user by comparing input credentials with stored ones.
     * String inputUsername - The username to check
     * String inputPassword - The password to check
     * Returns true if credentials match, false if not
     */
    public boolean loginUser(String inputUsername, String inputPassword) {
        return username != null && password != null &&
               username.equals(inputUsername) && password.equals(inputPassword);
    }

     /**
     * Generates a login status message.
     * boolean success - Whether login was successful
     * returns welcome message if successful else an error
     */
    public String returnLoginStatus(boolean success) {
        if (success) {
            return "Welcome " + firstName + ", " + lastName + " it is great to see you again.";
        } else {
            return "Username or password incorrect, please try again.";
        }
    }
}
