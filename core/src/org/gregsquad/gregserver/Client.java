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
        
            Message<String> answer = request("CONNEXION", "NAME");
                
            System.out.println("");
            System.out.println("["+name+"] " + name + " is correctly connected");
            System.out.println("");

            Thread receiveThread = new Thread(new ReceiveThread());

            Thread debugSendThread = new Thread(new DebugSendThread());
            debugSendThread.start();
            debugSendThread.join();
            //receiveThread.start();
            //receiveThread.join();

            // Close the streams and the connection
            out.close();
            in.close();
            echoSocket.close();
            
        } catch (IOException e) {
            System.err.println("IOException1: " + e.getMessage());
        } catch (InterruptedException e) {
            System.err.println("InterruptedException: " + e.getMessage());
        }
    }

    public void stop() {
        try {
            // Close the streams and the connection
            out.close();
            in.close();
            echoSocket.close();
        } catch (IOException e) {
            System.err.println("IOException1: " + e.getMessage());
        }
    }

    // Thread for receiving messages
    class ReceiveThread implements Runnable {
        public void run() {
            try {
                System.out.println("["+name+"] " + "Listening for messages");
                Message<?> inputMessage;
                while ((inputMessage = (Message<?>) in.readObject()) != null) {

                    if (inputMessage.isOfType(String.class)) {
                        Message<String> stringMessage = (Message<String>) inputMessage;
                        // Manage the message of type String here
                        System.out.println("["+name+"] " + "Received message: " + stringMessage.getType() + " " + stringMessage.getPurpose() + " " + stringMessage.getContent());
                        // Check if the message is a connexion message
                        if(stringMessage.getType().equals("CONNEXION")) {

                            if(stringMessage.getPurpose().equals("NAME")) {
                                
                            }
                        }

                    } 
                    else if (inputMessage.isOfType(Card.class)) {
                        Message<Card> cardMessage = (Message<Card>) inputMessage;
                        // Traitez le message de type Card ici    
                    }
            }
            } catch (IOException e) {
                System.err.println("IOException2: " + e.getMessage());
            } catch (ClassNotFoundException e) {
                System.err.println("ClassNotFoundException: " + e.getMessage());
            }
        }
    }

    // Thread for receiving answers
    public class AnswerReceiver<T extends Serializable> implements Runnable {
        private final UUID id;
        private final String type;
        private final String purpose;
        private final Socket echoSocket;
        private final ObjectInputStream in;
        private final String name;
        private Message<T> answer;
    
        public AnswerReceiver(UUID id, String type, String purpose, Socket echoSocket, ObjectInputStream in, String name) {
            this.id = id;
            this.type = type;
            this.purpose = purpose;
            this.echoSocket = echoSocket;
            this.in = in;
            this.name = name;
        }
    
        @Override
        public void run() {
            try {   
                echoSocket.setSoTimeout(5000);
                Object inputObject;
                while ((inputObject = in.readObject()) != null) {
                    if (inputObject instanceof Message) {
                        Message<T> inputMessage = (Message<T>) inputObject;
                        if (inputMessage.getId().equals(id) && inputMessage.getType().equals(type) && inputMessage.getPurpose().equals(purpose)) {
                            System.out.println("["+name+"] "+"Received answer: " + inputMessage.getType() + " " + inputMessage.getPurpose());
                            answer = inputMessage;
                            return;
                        }
                    }
                }
            } catch (IOException e) {
                System.err.println("IOException3: " + e.getMessage());
            } catch (ClassNotFoundException e) {
                System.err.println("ClassNotFoundException: " + e.getMessage());
            }
            System.out.println("["+name+"] "+"Error: no answer received");
        }
    
        public Message<T> getAnswer() {
            return answer;
        }
    }


    // Debug thread for sending requests every 5 seconds
    class DebugSendThread implements Runnable {
        public void run() {
            try {
                while (true) {
                    ping();
                    Thread.sleep(5000);
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
            System.err.println("IOException3: " + e.getMessage());
        } catch (ClassNotFoundException e) {
            System.err.println("ClassNotFoundException: " + e.getMessage());
        }
        System.out.println("["+name+"] "+"Error: no answer received");
        return null;
    }

    // Generic method to send a request and receive an answer
    public <T extends Serializable> Message<T> request(String type, String purpose) {
        Message<String> request_locale = new Message<String>(name, type, purpose,"",String.class);
        System.out.println("["+name+"] " + "Sending request. Name: " + request_locale.getSender() + " Type: " + request_locale.getType() + " Purpose: " + request_locale.getPurpose());

        // Create an instance of AnswerReceiver with the necessary parameters
        AnswerReceiver<T> answerReceiver = new AnswerReceiver<>(request_locale.getId(), type, purpose, echoSocket, in, name);  

        // Create a new thread with the AnswerReceiver instance
        Thread answerReceiverThread = new Thread(answerReceiver);

        answerReceiverThread.start();
        sendRequest(request_locale);

        try {
            answerReceiverThread.join();
        } catch (InterruptedException e) {
            System.err.println("InterruptedException: " + e.getMessage());
        }
        Message<T> answer = answerReceiver.getAnswer();

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
        // add the player list to the game
        return answer.getContent();
    }

    public boolean initGame() {
        Message<Boolean> answer = request("GAME", "INIT_GAME");
        System.out.println("["+name+"] " + name + " initialized the game");
        return answer.getContent();
    }

    public String ping() {
        Message<String> answer = request("PING", "");
        System.out.println("["+name+"] " + name + " pinged the server");
        return answer.getContent();
    }
}
