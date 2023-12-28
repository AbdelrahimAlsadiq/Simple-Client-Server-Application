# Simple Client-Server Application
## Overview
This project is for completing the Network Programming subject provided in the 4th year of the Faculty of Computers & Information, Luxor University. The application comprises two primary components:

### Client Side

The client-side component handles user authentication and data submission to the server.

#### - Technologies Used:
- Java Swing for Graphical User Interface (GUI)
- Socket for communication with the server
- File I/O for user credentials validation and data submission

#### - How to Run:
1. Compile and execute the `Client.java` file.
2. Input valid credentials to log in.
3. Upon successful authentication, a submission form will be displayed.
4. Enter submission details and click "Submit" to send the data to the server.

### Server Side

The server-side component manages incoming client connections and stores submitted data.

#### - Technologies Used:
- Socket for communication with clients
- File I/O for storing submitted data persistently

#### - How to Run:
1. Compile and run the `Server.java` file.
2. The server will start listening for incoming client connections on port 5000.
3. Once a client connects, it will transmit and store the data submitted by clients in the `database.txt` file.

---

## File Structure

### Client Side
- `Client.java`: Contains the client-side code responsible for the GUI, user authentication, and data submission.

### Server Side
- `Server.java`: Holds the server-side code managing incoming client connections, receiving data, and persistently storing it in the `database.txt` file.

---

## Instructions for Running the Application

1. Compile the client and server files separately using a Java compiler.
2. Launch the server by executing the `Server.java` file.
3. Run the `Client.java` file to initiate the client-side graphical interface.
4. Enter valid credentials to log in and submit data to the server.

**Note:** Ensure that both client and server applications are running simultaneously for the expected functionality.

---

## Known Issues

- The current implementation doesn't support concurrent multiple client connections. It processes one client at a time.
