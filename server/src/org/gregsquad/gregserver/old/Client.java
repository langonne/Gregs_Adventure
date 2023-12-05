package org.gregsquad.gregsadventure.server;

import java.io.*;
import java.net.*;

public class Client {
    private String serverIp; // Server IP address
    private int serverPort; // Server port number
    private String name; // Client name
    private Socket echoSocket; // Socket for communication
    private PrintWriter out; // Output stream
    private BufferedReader in; // Input stream

    // Constructor
    public Client(String serverIp, int serverPort, String name) {
        this.serverIp = serverIp;
        this.serverPort = serverPort;
        this.name = name;
    }

    // Method to start the client
    public void run() {
        try {
            // Connect to the server
            echoSocket = new Socket(serverIp, serverPort);
            System.out.println("Connected to " + serverIp + ":" + serverPort);

            // Create input and output streams
            out = new PrintWriter(echoSocket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(echoSocket.getInputStream()));

            // Send the client name to the server
            out.println(name);

            // Create threads for sending and receiving messages
            Thread sendThread = new Thread(new SendThread());
            Thread receiveThread = new Thread(new ReceiveThread());
            sendThread.start();
            receiveThread.start();

            // Wait for the send and receive threads to finish
            sendThread.join();
            receiveThread.join();

            // Close the streams and the connection
            out.close();
            in.close();
            echoSocket.close();
        } catch (IOException e) {
            System.err.println("IOException: " + e.getMessage());
        } catch (InterruptedException e) {
            System.err.println("InterruptedException: " + e.getMessage());
        }
    }

    // Thread for sending messages
    class SendThread implements Runnable {
        public void run() {
            try {
                BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in));
                String userInput;
                while ((userInput = stdIn.readLine()) != null) {
                    if (!userInput.isEmpty()) {
                        out.println(userInput); // Send the message to the server
                    } else {
                        System.out.println("Error: message cannot be empty. Please enter a new message.");
                    }
                }
            } catch (IOException e) {
                System.err.println("IOException: " + e.getMessage());
            }
        }
    }

    // Thread for receiving messages
    class ReceiveThread implements Runnable {
        public void run() {
            try {
                String inputLine;
                while ((inputLine = in.readLine()) != null) {
                    System.out.println(inputLine); // Print the received message
                }
            } catch (IOException e) {
                System.err.println("IOException: " + e.getMessage());
            }
        }
    }

    // Main method
    public static void main(String[] args) {
        // Check the arguments
        if (args.length != 3) {
            System.err.println("Need 3 arguments: server IP, server port, player name");
            System.exit(1);
        }
        // Create the client
        Client client = new Client(args[0], Integer.parseInt(args[1]), args[2]);
        // Start the client
        client.run();
    }
}
