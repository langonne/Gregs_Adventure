package org.gregsquad.gregserver;

import org.gregsquad.gregsadventure.card.*;
import org.gregsquad.gregsadventure.game.*;

import java.io.*;
import java.net.*;
import java.util.*;

/**
 * The Client class is responsible for managing the client-side logic of the game.
 * It connects to the server, sends and receives messages, and handles reconnections.
 */
public class Client {
    private String serverIp; // Server IP address
    private int serverPort; // Server port number
    private String name; // Client name
    private Socket echoSocket; // Socket for communication
    private ObjectOutputStream out; // Output stream
    private ObjectInputStream in; // Input stream
    private GlobalListener globalListener; // Thread for listening to the server

    private static final int MAX_RECONNECT_ATTEMPTS = 5; // Maximum number of reconnection attempts
    private static final int RECONNECT_DELAY_MS = 5000; // Delay between reconnection attempts

    /**
     * Constructs a new Client with the given server IP, server port, and client name.
     * @param serverIp the IP address of the server.
     * @param serverPort the port number of the server.
     * @param name the name of the client.
     */
    public Client(String serverIp, int serverPort, String name) {
        this.serverIp = serverIp;
        this.serverPort = serverPort;
        this.name = name;

        globalListener = new GlobalListener();
    }

    
    private void connect() throws IOException, InterruptedException {
        int attempts = 0;
        while (true) {
            try {
                // Connect to the server
                echoSocket = new Socket(serverIp, serverPort);
                System.out.println("["+name+"] " + "Connected to " + serverIp + ":" + serverPort);

                // Create input and output streams
                System.out.println("["+name+"] " + "Creating streams");
                out = new ObjectOutputStream(echoSocket.getOutputStream());
                in = new ObjectInputStream(echoSocket.getInputStream());
                System.out.println("["+name+"] " + "Streams created");

                // Connection successful, break the loop
                break;
            } catch (SocketTimeoutException e) {
                attempts++;
                if (attempts > MAX_RECONNECT_ATTEMPTS) {
                    throw new IOException("Failed to connect after " + MAX_RECONNECT_ATTEMPTS + " attempts", e);
                }
                System.err.println("Connection timed out, retrying in " + RECONNECT_DELAY_MS + "ms...");
                Thread.sleep(RECONNECT_DELAY_MS);
            }
        }
    }

    // Method to start the client
    public void run() {
        try {
            connect();

            System.out.println("["+name+"] " + "Sending name: " + name);
        
            Message<String> answer = request("CONNEXION", "NAME");
                
            System.out.println("");
            System.out.println("["+name+"] " + name + " is correctly connected");
            System.out.println("");

            Thread debugSendThread = new Thread(new DebugSendThread());
            debugSendThread.start();
            //debugSendThread.join();

            Thread globalListenerThread = new Thread(globalListener);
            globalListenerThread.start();
            globalListenerThread.join();


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

    class GlobalListener implements Runnable {

        private Message<Card> lastDonjonCard;
        private Message<Card> lastTreasureCard;
        private Message<LinkedList<Card>> lastDonjonDiscard;
        private Message<LinkedList<Card>> lastTreasureDiscard;
        private Message<Player> lastCurrentPlayer;
        private Message<ArrayList<Player>> lastPlayerList;
        private Message<Boolean> lastInitGame;
        

        public void run() {
            try {
                Object inputObject;
                while ((inputObject = in.readObject()) != null) {

                    //Print lastInitGame
                    System.out.println("-----------------------------------["+name+"] " + "LastInitGame: " + lastInitGame);

                    if (inputObject instanceof Message) {
                        Message<?> inputMessage = (Message<?>) inputObject;
                        System.out.println("["+name+"] " + "Received message: " + inputMessage.getType() + " " + inputMessage.getPurpose());
                        switch (inputMessage.getType()) {
                            case "CONNEXION":
                                break;
                            case "GAME":
                                switch (inputMessage.getPurpose()) {
                                    case "DRAW_DONJON_CARD":
                                        lastDonjonCard = (Message<Card>) inputMessage;
                                        break;
                                    case "DRAW_TREASURE_CARD":
                                        lastTreasureCard = (Message<Card>) inputMessage;
                                        break;
                                    case "GET_DONJON_DISCARD":
                                        lastDonjonDiscard = (Message<LinkedList<Card>>) inputMessage;
                                        break;
                                    case "GET_TREASURE_DISCARD":
                                        lastTreasureDiscard = (Message<LinkedList<Card>>) inputMessage;
                                        break;
                                    case "GET_CURRENT_PLAYER":
                                        lastCurrentPlayer = (Message<Player>) inputMessage;
                                        break;
                                    case "GET_PLAYER_LIST":
                                        lastPlayerList = (Message<ArrayList<Player>>) inputMessage;
                                        break;
                                    case "INIT_GAME":
                                        lastInitGame = (Message<Boolean>) inputMessage;
                                        break;
                                    case "GET_INIT_GAME":
                                        lastInitGame = (Message<Boolean>) inputMessage;
                                        break;
                                    default:
                                        System.err.println("Unknown message purpose: " + inputMessage.getPurpose());
                                        break;
                                }
                                break;
                            // Ajoutez d'autres cas ici pour gérer d'autres types de messages
                            default:
                                System.err.println("Unknown message type: " + inputMessage.getType());
                                break;
                        }
                    }
                }
            } catch (IOException e) {
                System.err.println("IOException in GlobalListener: " + e.getMessage());
            } catch (ClassNotFoundException e) {
                System.err.println("ClassNotFoundException in GlobalListener: " + e.getMessage());
            }
        }

        public Message<Card> getLastDonjonCard() {
            return lastDonjonCard;
        }

        public Message<Card> getLastTreasureCard() {
            return lastTreasureCard;
        }

        public Message<LinkedList<Card>> getLastDonjonDiscard() {
            return lastDonjonDiscard;
        }

        public Message<LinkedList<Card>> getLastTreasureDiscard() {
            return lastTreasureDiscard;
        }

        public Message<Player> getLastCurrentPlayer() {
            return lastCurrentPlayer;
        }

        public Message<ArrayList<Player>> getLastPlayerList() {
            return lastPlayerList;
        }

        public Message<Boolean> getLastInitGame() {
            return lastInitGame;
        }

    }

    // Debug thread for sending requests every 5 seconds
    class DebugSendThread implements Runnable {
        public void run() {
            try {
                while (true) {
                    //drawDonjonCard();
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

    public Message<String> request(String type, String purpose) {
        Message<String> request_locale = new Message<String>(name, type, purpose,"",String.class);
        System.out.println("["+name+"] " + "Sending request. Name: " + request_locale.getSender() + " Type: " + request_locale.getType() + " Purpose: " + request_locale.getPurpose());
        sendRequest(request_locale);
        return request_locale;
    }

    /*
    public <T> T Answer(Message<T> request_locale, GlobalListener globalListener) {
        for (int i = 0; i < 5; i++) {
            Message<T> lastAnswer = globalListener.getLastAnswer();
            if (request_locale.getId().equals(lastAnswer.getId())) {
                System.out.println("["+name+"] " + name + " got the answer");
                return lastAnswer.getContent();
            }
        
            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                System.err.println("InterruptedException: " + e.getMessage());
            }
        }
        System.err.println("["+name+"] " + "Error: no answer received");
        return null;
    }
    */

    // Specific methods to send requests and receive answers
    public Card drawDonjonCard() {
        Message<String> request_locale = request("GAME", "DRAW_DONJON_CARD");
        System.out.println("["+name+"] " + "Sending request. Name: " + request_locale.getSender() + " Type: " + request_locale.getType() + " Purpose: " + request_locale.getPurpose());
        
        for (int i = 0; i < 5; i++) {
        
            Message<Card> lastDonjonCard = globalListener.getLastDonjonCard();
        
            if (request_locale.getId().equals(lastDonjonCard.getId())) {
                        System.out.println("["+name+"] " + name + " drew a donjon card: " + lastDonjonCard.getContent().getName());
                        return lastDonjonCard.getContent();
                    }
        
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                System.err.println("InterruptedException: " + e.getMessage());
            }
        }
        System.err.println("["+name+"] " + "Error: no answer received");
        return null;
    }
    //getPlayerList
    public ArrayList<Player> getPlayerList() {
        Message<String> request_locale = request("GAME", "GET_PLAYER_LIST");
        System.out.println("["+name+"] " + "Sending request. Name: " + request_locale.getSender() + " Type: " + request_locale.getType() + " Purpose: " + request_locale.getPurpose());
        
        for (int i = 0; i < 5; i++) {
            
            Message<ArrayList<Player>> lastPlayerList = globalListener.getLastPlayerList();
            if (lastPlayerList != null && request_locale.getId().equals(lastPlayerList.getId())) {
                System.out.println("["+name+"] " + name + " got the player list");
                return lastPlayerList.getContent();
            }
        
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                System.err.println("InterruptedException: " + e.getMessage());
            }
        }
        System.err.println("["+name+"] " + "Error: no answer received");
        return null;
    }

    //initGame
    public boolean initGame() {
        Message<String> request_locale = request("GAME", "INIT_GAME");
        
        for (int i = 0; i < 5; i++) {
            
            Message<Boolean> lastInitGame = globalListener.getLastInitGame();
            if (lastInitGame != null && request_locale.getId().equals(lastInitGame.getId())) {
                System.out.println("["+name+"] " + name + " initialized the game");
                return lastInitGame.getContent();
            }
        
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                System.err.println("InterruptedException: " + e.getMessage());
            }
        }
        System.err.println("["+name+"] " + "Error: no answer received");
        return false;
    }

    //GetInitGame
    public boolean getInitGame() {
        Message<String> request_locale = request("GAME", "GET_INIT_GAME");
        System.out.println("["+name+"] " + "Sending request. Name: " + request_locale.getSender() + " Type: " + request_locale.getType() + " Purpose: " + request_locale.getPurpose());
        
        for (int i = 0; i < 5; i++) {
            
            Message<Boolean> lastInitGame = globalListener.getLastInitGame();
            if (lastInitGame != null && request_locale.getId().equals(lastInitGame.getId())) {
                System.out.println("["+name+"] " + name + " got the init game");
                return lastInitGame.getContent();
            }
        
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                System.err.println("InterruptedException: " + e.getMessage());
            }
        }
        System.err.println("["+name+"] " + "Error: no answer received");
        return false;
    }

        
    /*
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
        System.out.println("-------------[CLIENT] " + answer.getContent().size());
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
    */
}
