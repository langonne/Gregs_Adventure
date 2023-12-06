package org.gregsquad.gregserver;

import org.gregsquad.gregsadventure.card.*;

import java.io.*;
import java.net.*;

public class Client {
    private String serverIp; // Server IP address
    private int serverPort; // Server port number
    private String name; // Client name
    private Socket echoSocket; // Socket for communication
    private ObjectOutputStream out; // Output stream
    private ObjectInputStream in; // Input stream

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
            System.out.println("[INFO] Connected to " + serverIp + ":" + serverPort);

            // Create input and output streams
            System.out.println("[INFO] Creating streams");
            out = new ObjectOutputStream(echoSocket.getOutputStream());
            in = new ObjectInputStream(echoSocket.getInputStream());
            System.out.println("[INFO] Streams created");

            // Send the client name to the server
            System.out.println("[INFO] Sending name: " + name);
            out.writeObject(new Message<String>(name, "CONNEXION", "","has joined the chat"));

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
                        System.out.println("Sending message: " + userInput);
                        out.writeObject(new Message<String>(name, "CHAT", "",userInput)); // Send the message to the server
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
                Object inputObject;
                while ((inputObject = in.readObject()) != null) {
                    if (inputObject instanceof Message) {
                        Message<String> inputMessage = (Message<String>) inputObject;
                        System.out.println(inputMessage.getSender() + ": " + inputMessage.getContent()); // Print the received message
                    } else {
                        // handle the case where inputObject is not a Message
                    }
                }
            } catch (IOException e) {
                System.err.println("IOException: " + e.getMessage());
            } catch (ClassNotFoundException e) {
                System.err.println("ClassNotFoundException: " + e.getMessage());
            }
        }
    }

    // REQUESTS SECTION

    // Generic method to send a request
    public <T extends Serializable> void sendRequest(Message<T> request) {
        try {
            out.writeObject(request);
        } catch (IOException e) {
            System.err.println("Error sending message: " + e.getMessage());
        }
    }
    // Generic method to receive an answer
    public <T extends Serializable> Message<T> receiveAnswer(String type, String purpose) {
        try {
                Object inputObject;
                while ((inputObject = in.readObject()) != null) {
                    if (inputObject instanceof Message) {
                        Message<T> inputMessage = (Message<T>) inputObject;
                        if (inputMessage.getType().equals(type) && inputMessage.getPurpose().equals(purpose)) {
                            return inputMessage;
                        }
                    }
                }
        } catch (IOException e) {
            System.err.println("IOException: " + e.getMessage());
        } catch (ClassNotFoundException e) {
            System.err.println("ClassNotFoundException: " + e.getMessage());
        }
        return null;
    }

    public Card drawDonjonCard() {
        Message<String> request = new Message<String>(name, "REQUEST", "DRAW_DONJON_CARD","");
        sendRequest(request);
        Message<Card> answer = receiveAnswer("ANSWER", "DRAW_DONJON_CARD");
        return answer.getContent();
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
