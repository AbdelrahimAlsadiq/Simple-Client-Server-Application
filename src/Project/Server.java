package Project;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.*;
import java.util.Scanner;

// Class for handling incoming client connections
class Handler extends Thread {
    Socket c;

    Handler(Socket c) {
        this.c = c;
    }

    @Override
    public void run() {
        try {
            ObjectInputStream input = new ObjectInputStream(c.getInputStream());
            while (true) {
                String[] data = (String[]) input.readObject();

                // Process the received data
                String textToAppend = data[0] + "," + data[1] + "," + data[2] + "," + data[3];

                // FileWriter with append mode (true)
                try (FileWriter fileWriter = new FileWriter("database.txt", true)) {
                    fileWriter.write(textToAppend);
                    fileWriter.write(System.lineSeparator()); // Add a new line after appending text
                    System.out.println("NEW DATA STORED:");
                    System.out.println("[CLIENT-USERNAME]:\t" + data[0] + "\n[FULLNAME]:\t\t" + data[1] + "\n[EMAIL]:\t\t" + data[2] + "\n[PASSWORD]:\t\t" + data[3]);
                    System.out.println("---------------------------------------------");
                } catch (IOException e) {
                    System.err.println("Error appending text: " + e.getMessage());
                }
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}

public class Server extends JFrame {
    private JTextArea displayArea;

    private void showDataGUI() {
        setTitle("Data Display");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600, 400);
        setLocationRelativeTo(null);

        // Creating components
        JButton showDataButton = new JButton("Show Data");
        displayArea = new JTextArea(15, 30);
        displayArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(displayArea);

        // Adding components to the frame
        setLayout(new BorderLayout());
        add(showDataButton, BorderLayout.SOUTH);
        add(scrollPane, BorderLayout.CENTER);

        // Button action listener
        showDataButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                displayData();
            }
        });
    }

    private void displayData() {
        displayArea.setText("");
        try (Scanner scanner = new Scanner(new File("database.txt"))) {
            int counter = 0;
            while (scanner.hasNextLine()) {
                counter++;
                String data = "Entry: " + counter + ":\n";
                String line = scanner.nextLine();
                String[] parts = line.split(",");
                String[] titles = { "Username", "Full Name", "Email", "Phone Number" };

                for (int i = 0; i < 4; i++) {
                    data += titles[i] + ": " + parts[i] + "\n";
                }

                data += "--------------------\n";
                displayArea.append(data);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

    }

    public static void main(String[] args) throws Exception {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                Server serverGUI = new Server();
                serverGUI.showDataGUI();
                serverGUI.setVisible(true);
            }
        });

        try {
            ServerSocket server = new ServerSocket(5000);
            System.out.println("Server Listening on Port: 5000");
            while (true) {
                Socket client = server.accept();
                DataInputStream clientIn = new DataInputStream(client.getInputStream());
                System.out.println("---------------------------------------------");
                System.out.println("New Client Connected: " + clientIn.readUTF());
                System.out.println("---------------------------------------------");
                Handler t1 = new Handler(client);
                t1.start();
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
}
