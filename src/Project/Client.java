package Project;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.*;
import java.util.HashMap;
import java.util.Scanner;

public class Client extends JFrame {

    // JFrame Objects (For the GUI):
    private JLabel usernameLabel, passwordLabel;
    private JTextField usernameField, passwordField, fullNameField, emailField, phoneField;
    private JButton loginButton, submitButton;
    private JPanel loginPanel, submissionPanel;

    // HashMap for User Authentication:
    private HashMap<String, String> userCredentials;
    
    // Important objects for Client/Server connection:
    private Socket socket;
    private ObjectOutputStream out;
    private DataOutputStream clientOut;

    // Main Constructor
    public Client() {
        userCredentials = loadUserCredentials("users.txt");
        createAndShowLoginGUI();        
    }

    // A function that shows the Login GUI (to check if user is authenticated):
    private void createAndShowLoginGUI() {
        // Creating the main GUI attributes:
        setTitle("Client Login");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        setPreferredSize(new Dimension(400, 300));
        setResizable(false);
        

        JPanel girdPanel = new JPanel();
        girdPanel.setLayout(new GridLayout(0,1));
        girdPanel.setBackground(new Color(50,45,74));

        loginPanel = new JPanel();
        loginPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        loginPanel.setBackground(new Color(50,45,74));

        // Adding Empty Labels (for design)
        JLabel e3 = new JLabel(" ");

        // Creating the "Username" GUI Part:
        usernameLabel = new JLabel("Username:");
        usernameLabel.setFont(new Font("Tajwal", Font.BOLD, 15));
        usernameLabel.setForeground(Color.WHITE);      
        usernameField = new JTextField();
        usernameField.setHorizontalAlignment(SwingConstants.CENTER);
        usernameField.setFont(new Font("Tajwal", Font.PLAIN, 15)); 
        usernameField.setPreferredSize(new Dimension(25, 35));
        usernameField.setBackground(new Color(122,119,139));
       
        girdPanel.add(usernameLabel);
        girdPanel.add(usernameField);

        // Creating the "Password" GUI Part:
        passwordLabel = new JLabel("Password:");
        passwordLabel.setFont(new Font("Tajwal", Font.BOLD, 15)); 
        passwordLabel.setForeground(Color.WHITE);             
        passwordField = new JPasswordField(25);
        passwordField.setHorizontalAlignment(SwingConstants.CENTER);
        passwordField.setFont(new Font("Tajwal", Font.PLAIN, 15));
        passwordField.setBackground(new Color(122,119,139));

        girdPanel.add(passwordLabel);
        girdPanel.add(passwordField);

        // Creating the "Login" Button:
        loginButton = new JButton("Login");
        loginButton.setPreferredSize(new Dimension(100, 40));
        
        // Creating an action listener for "Login" button:
        loginButton.addActionListener(new ActionListener() {

            // A function that checks if the user has entered correct credentials:
            public void actionPerformed(ActionEvent e) {
                String username = usernameField.getText();
                String password = passwordField.getText();

                // Verify username and password from loaded credentials:
                if (userCredentials.containsKey(username) && userCredentials.get(username).equals(password)) {
                    try {
                        socket = new Socket("localhost", 5000);
                        clientOut = new DataOutputStream(socket.getOutputStream());
                        clientOut.writeUTF(username);
                        out = new ObjectOutputStream(socket.getOutputStream());
                        JOptionPane.showMessageDialog(Client.this, "Welcome Back, " + username + "!", "Welcome!", JOptionPane.INFORMATION_MESSAGE);
                        openSubmissionForm();

                    } 
                    catch (IOException ex) {
                        JOptionPane.showMessageDialog(Client.this, "Server is not responding.", "Server Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
                else {
                    JOptionPane.showMessageDialog(Client.this, "Invalid username or password", "Login Failed", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        girdPanel.add(e3);
        loginPanel.add(girdPanel);
        loginPanel.add(loginButton);
        add(loginPanel);
        pack();

        setLocationRelativeTo(null);

        // Shows the window in the screen:
        setVisible(true);
    }

    // A function that loads the valid user credentials from stored database file:
    private HashMap<String, String> loadUserCredentials(String fileName) {
        HashMap<String, String> credentials = new HashMap<>();
        try (Scanner scanner = new Scanner(new File(fileName))) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] parts = line.split(",");
                if (parts.length == 2) {
                    String username = parts[0].trim();
                    String password = parts[1].trim();
                    credentials.put(username, password);
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return credentials;
    }

    // A function that creates the Submission form (shows after the user successfully logged in):
    private void openSubmissionForm() {
        
        // Creating the main GUI attributes:
        setTitle("Submission Form");
        remove(loginPanel);

        JPanel gridPanel = new JPanel();
        JPanel tempPanel = new JPanel();
        submissionPanel = new JPanel();

        gridPanel.setBackground(new Color(198,226,255));
        tempPanel.setBackground(new Color(198,226,255));
        submissionPanel.setBackground(new Color(198,226,255));


        tempPanel.setLayout(new BorderLayout());
        gridPanel.setPreferredSize(new Dimension(100,100));
        gridPanel.setLayout(new GridLayout(4,2));
        submissionPanel.setLayout(new GridLayout(2,1));

        gridPanel.add(new JLabel("Username:"));
        gridPanel.add(new JLabel(usernameField.getText()));

        gridPanel.add(new JLabel("Full Name:"));
        fullNameField = new JTextField();
        gridPanel.add(fullNameField);

        gridPanel.add(new JLabel("Email:"));
        emailField = new JTextField();
        gridPanel.add(emailField);

        gridPanel.add(new JLabel("Phone Number:"));
        phoneField = new JTextField();
        gridPanel.add(phoneField);

        submitButton = new JButton("Submit");
        submitButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (fullNameField.getText().isEmpty() || emailField.getText().isEmpty() || emailField.getText().isEmpty() || phoneField.getText().isEmpty()) {
                    JOptionPane.showMessageDialog(Client.this, "Please Enter valid inputs.", "Error", JOptionPane.ERROR_MESSAGE);
                }

                else {
                    sendDataToServer(fullNameField.getText(), emailField.getText(), phoneField.getText());
                    JOptionPane.showMessageDialog(Client.this, "Data stored successfully.", "Result", JOptionPane.INFORMATION_MESSAGE);
                }
            }
        });
        submissionPanel.add(gridPanel); 
        tempPanel.add(submitButton, BorderLayout.SOUTH);
        submissionPanel.add(tempPanel);
        add(submissionPanel, BorderLayout.CENTER);
        revalidate();
    }

    // A function that sends tne client's inputs to the server:
    private void sendDataToServer(String fullName, String email, String phoneNumber) {
        String username = usernameField.getText();
        String[] userData = {username, fullName, email, phoneNumber};

        try {
            out.writeObject(userData);

        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new Client();
            }
        });
    }
}
