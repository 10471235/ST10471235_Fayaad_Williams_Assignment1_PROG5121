package messenger;

public class Login {

    private String username;
    private String password;
    private String cellphone;
    private String firstName;
    private String lastName;

    public Login(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public boolean checkUserName(String username) {
        this.username = username;
        return username.contains("_") && username.length() <= 5;
    }


    public boolean checkPasswordComplexity(String password) {
        this.password = password;

        boolean hasUpper = false;
        boolean hasDigit = false;
        boolean hasSpecial = false;

        if (password.length() < 8) return false;

        for (char ch : password.toCharArray()) {
            if (Character.isUpperCase(ch)) hasUpper = true;
            else if (Character.isDigit(ch)) hasDigit = true;
            else if (!Character.isLetterOrDigit(ch)) hasSpecial = true;
        }

        return hasUpper && hasDigit && hasSpecial;
    }

    public boolean checkCellPhoneNumber(String cellphone) {
        this.cellphone = cellphone;

        if (!cellphone.startsWith("+27")) {
            return false;
        }

        String digits = cellphone.substring(3); // after +27

        if (digits.length() != 9) {
            return false;
        }

        for (char ch : digits.toCharArray()) {
            if (!Character.isDigit(ch)) {
                return false;
            }
        }

        return true;
    }

    public String registerUser(String username, String password, String cellphone) {
        boolean validUsername = checkUserName(username);
        boolean validPassword = checkPasswordComplexity(password);
        boolean validCell = checkCellPhoneNumber(cellphone);

        if (!validUsername) {
            this.username = null;
            this.password = null;
            return "Username is not correctly formatted, please ensure that your username is more than five characters in length.";
        }

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

    public boolean loginUser(String inputUsername, String inputPassword) {
        return username != null && password != null &&
               username.equals(inputUsername) && password.equals(inputPassword);
    }

    public String returnLoginStatus(boolean success) {
        if (success) {
            return "Welcome " + firstName + ", " + lastName + " it is great to see you again.";
        } else {
            return "Username or password incorrect, please try again.";
        }
    }
}
