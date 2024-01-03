package org.gregsquad.gregserver;

import org.gregsquad.gregsadventure.card.*;
import org.gregsquad.gregsadventure.game.*;

import java.io.*;
import java.net.*;
import java.util.*;

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
            System.out.println("["+name+"] " + "Connected to " + serverIp + ":" + serverPort);

            // Create input and output streams
            System.out.println("["+name+"] " + "Creating streams");
            out = new ObjectOutputStream(echoSocket.getOutputStream());
            in = new ObjectInputStream(echoSocket.getInputStream());
            System.out.println("["+name+"] " + "Streams created");

            // Send the client name to the server
            System.out.println("["+name+"] " + "Sending name: " + name);
            //out.writeObject(new Message<String>(name, "CONNEXION", "NAME","has joined the chat"));
            //sendInformation("CONNEXION", "NAME", "has joined the chat");
            
            while(true){
                Message<String> answer = request("CONNEXION", "NAME");
                if(answer.getContent().equals("OK")){
                    break;
                }
                System.out.println("["+name+"] " +"Name already taken. Please enter a new name.");
                
            }
            
            System.out.println("");
            System.out.println("["+name+"] " + name + " is correctly connected");
            System.out.println("");

            // Create threads for sending and receiving messages
            Thread sendThread = new Thread(new SendThread());
            Thread receiveThread = new Thread(new ReceiveThread());

            //Debug
            Thread debugSendThread = new Thread(new DebugSendThread());

            sendThread.start();
            receiveThread.start();

            //Debug
            debugSendThread.start();

            // Wait for the send and receive threads to finish
            sendThread.join();
            receiveThread.join();

            //Debug
            debugSendThread.join();

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
                        out.writeObject(new Message<String>(name, "CHAT", "",userInput,String.class)); // Send the message to the server
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

    // Debug thread for sending requests every 5 seconds
    class DebugSendThread implements Runnable {
        public void run() {
            try {
                while (true) {
                    Thread.sleep(5000);
                    System.out.println("[DebugSendThread] Sending request");
                    drawTreasureCard();
                    System.out.println("[DebugSendThread] Request sent");
                }
            } catch (InterruptedException e) {
                System.err.println("InterruptedException: " + e.getMessage());
            }
        }
    }
    
    // REQUESTS SECTION

    // Generic method to send a request
    public <T extends Serializable> void sendRequest(Message<T> request) {
        try {
            System.out.println("["+name+"] " + "Sending request: " + request.getType() + " " + request.getPurpose());
            out.writeObject(request);
        } catch (IOException e) {
            System.err.println("["+name+"] " + "Error sending message: " + e.getMessage());
        }
    }
    // Generic method to receive an answer
    public <T extends Serializable> Message<T> receiveAnswer(UUID id, String type, String purpose) {
        try {   
                echoSocket.setSoTimeout(5000);
                Object inputObject;
                while ((inputObject = in.readObject()) != null) {
                    if (inputObject instanceof Message) {
                        Message<T> inputMessage = (Message<T>) inputObject;
                        if (inputMessage.getId().equals(id) && inputMessage.getType().equals(type) && inputMessage.getPurpose().equals(purpose)) {
                            System.out.println("["+name+"] "+"Received answer: " + inputMessage.getType() + " " + inputMessage.getPurpose());
                            return inputMessage;
                        }
                    }
                }
        } catch (IOException e) {
            System.err.println("IOException: " + e.getMessage());
        } catch (ClassNotFoundException e) {
            System.err.println("ClassNotFoundException: " + e.getMessage());
        }
        System.out.println("["+name+"] "+"Error: no answer received");
        return null;
    }

    // Generic method to send a request and receive an answer
    public <T extends Serializable> Message<T> request(String type, String purpose) {
        Message<String> request = new Message<String>(name, type, purpose,"",String.class);
        System.out.println("["+name+"] " + "Sending request. Name: " + request.getSender() + " Type: " + request.getType() + " Purpose: " + request.getPurpose());
        sendRequest(request);
        Message<T> answer = receiveAnswer(request.getId(), type, purpose);
        System.out.println("["+name+"] " + "Received answer. Name: " + answer.getSender() + " Type: " + answer.getType() + " Purpose: " + answer.getPurpose());
        return answer;
    } 
    // Generic method to send information
    public <T extends Serializable> void sendInformation(String type, String purpose, T content) {
        Message<T> information = new Message<T>(name, type, purpose, content, content.getClass());
        System.out.println("["+name+"] " + "Sending information. Name: " + information.getSender() + " Type: " + information.getType() + " Purpose: " + information.getPurpose());
        sendRequest(information);
    }

    // Specific methods to send requests and receive answers
    public Card drawDonjonCard() {
        Message<Card> answer = request("GAME", "DRAW_DONJON_CARD");
        System.out.println("["+name+"] " + name + " drew a donjon card: " + answer.getContent().getName());
        return answer.getContent();
    }

    public Card drawTreasureCard() {
        Message<Card> answer = request("GAME", "DRAW_TREASURE_CARD");
        System.out.println("["+name+"] " + name + " drew a treasure card: " + answer.getContent().getName());
        return answer.getContent();
    }

    public LinkedList<Card> getDonjonDiscard() {
        Message<LinkedList<Card>> answer = request("GAME", "GET_DONJON_DISCARD");
        System.out.println("["+name+"] " + name + " got the donjon discard");
        return answer.getContent();
    }

    public LinkedList<Card> getTreasureDiscard() {
        Message<LinkedList<Card>> answer = request("GAME", "GET_TREASURE_DISCARD");
        System.out.println("["+name+"] " + name + " got the treasure discard");
        return answer.getContent();
    }

    public Player getCurrentPlayer() {
        Message<Player> answer = request("GAME", "GET_CURRENT_PLAYER");
        System.out.println("["+name+"] " + name + " got the current player");
        return answer.getContent();
    }

    public ArrayList<Player> getPlayerList() {
        Message<ArrayList<Player>> answer = request("GAME", "GET_PLAYER_LIST");
        System.out.println("["+name+"] " + name + " got the player list");
        //print the player list
        for(Player player : answer.getContent()){
            System.out.println(player.getName());
        }
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
