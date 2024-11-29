import javax.swing.*;
import javax.xml.crypto.Data;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class UserLogin extends JFrame {
    private JTextField txtUsername, txtPassword;
    private JButton btnLogin, btnAddUser, btnCancel;
    private JLabel lblTitle, lblUsername, lblPassword;
    //private JPanel loginPanel;
    ArrayList<UserAuthentication> loglist;

    // Static map simulating a user database
    private static Map<String, String> userDatabase = new HashMap<>();

    // Constructor
    public UserLogin() {
        setTitle("User Login");
        setLayout(new BorderLayout());
        setSize(400, 300);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        JPanel loginPanel = new JPanel();
        loginPanel.setLayout(new BoxLayout(loginPanel, BoxLayout.Y_AXIS));
        loginPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Title label
        lblTitle = new JLabel("MRC LOGIN", SwingConstants.CENTER);
        lblTitle.setFont(new Font("Arial", Font.BOLD, 24));
        lblTitle.setOpaque(true);
        lblTitle.setBackground(new Color(0, 70, 140));
        lblTitle.setForeground(Color.WHITE);
        lblTitle.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        loginPanel.add(lblTitle);

        // Username field
        JPanel usernamePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        lblUsername = new JLabel("Username:");
        txtUsername = new JTextField(20);
        usernamePanel.add(lblUsername);
        usernamePanel.add(txtUsername);
        loginPanel.add(usernamePanel);

        // Password field
        JPanel passwordPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        lblPassword = new JLabel("Password:");
        txtPassword = new JPasswordField(20); // Using JPasswordField for password input
        passwordPanel.add(lblPassword);
        passwordPanel.add(txtPassword);
        loginPanel.add(passwordPanel);

        // Button Panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        btnLogin = new JButton("Login");
        btnAddUser = new JButton("Add User");
        btnCancel = new JButton("Cancel");

        // Adding buttons to panel
        buttonPanel.add(btnLogin);
        buttonPanel.add(btnAddUser);
        buttonPanel.add(btnCancel);
        loginPanel.add(buttonPanel);

        // Adding action listeners for buttons
        btnLogin.addActionListener(e -> login());
        btnAddUser.addActionListener(e -> addUser());
        btnCancel.addActionListener(e -> dispose());

        add(loginPanel, BorderLayout.CENTER);

        loglist = loadlogin("Logindetails.txt");
    }

    // Method to add a user (for demonstration purposes)
    public static void addUser(String staffID, String password) {
        userDatabase.put(staffID, password);
    }

    // Public getter for userDatabase
    public static Map<String, String> getUserDatabase() {
        return userDatabase;
    }

    // Login method
    private void login() {
        String username = txtUsername.getText().trim();
        String password = txtPassword.getText().trim();
       
            if (loglist.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No users in the database. Please add users first.", "Database Empty", JOptionPane.ERROR_MESSAGE);
            return;
            }

            if (Loginsearch(username,password)){
            mainmenu main = new mainmenu();
            UserLogin user = new UserLogin();
            main.setVisible(true);
            user.setVisible(false);
            } else {
            JOptionPane.showMessageDialog(this, "Login failed. Invalid username or password.", "Login Failed", JOptionPane.ERROR_MESSAGE);
            }
    }




    private boolean Loginsearch(String user, String password){
        for(UserAuthentication s: loglist){
            if(s.getstaffID().equals(user) && s.getPassword().equals(password)){
                return true;
            }
        }
        return false;
    }

    // Method to add a new user (called from the button)
    private void addUser() {
        String username = txtUsername.getText().trim();
        String password = txtPassword.getText().trim();

        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter both username and password.", "Validation Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        UserAuthentication User = new UserAuthentication(username, password);
        addtofile(User);
        addtolist(User);

        JOptionPane.showMessageDialog(this, "User added successfully.", "User Added", JOptionPane.INFORMATION_MESSAGE);
    }

    public ArrayList<UserAuthentication> getLogin(){
        return loglist;
    }
     
    private void addtofile(UserAuthentication u)
    {
        String [] Data = {u.getstaffID(),""+u.getPassword()};
        try
        {
            BufferedWriter writer = new BufferedWriter(new FileWriter("Logindetails.txt", true));
            for (int i=0; i<Data.length; i++){
                writer.write(Data[i] + " ");
            }
            
            writer.close();
        }
        catch (IOException ioe)
        {}
    }

    private void addtolist(UserAuthentication u){
        loglist.add(u);
    }

    private ArrayList<UserAuthentication> loadlogin(String vfile)
    {
        Scanner vscan = null;
        ArrayList<UserAuthentication> loglist = new ArrayList<UserAuthentication>();
        try
        {
            vscan  = new Scanner(new File(vfile));
            while(vscan.hasNext())
            {
                String [] nextLine = vscan.nextLine().split(" ");
                String User = nextLine[0];
                String password = nextLine[1];
 
                UserAuthentication v = new UserAuthentication(User, password);
                loglist.add(v);
            }

            vscan.close();
        }
        catch(IOException e)
        {}
        return loglist;
    }

    // Main method to run the application
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            UserLogin loginFrame = new UserLogin();
            loginFrame.setVisible(true);
        });
    }
}


