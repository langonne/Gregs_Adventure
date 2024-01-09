package org.gregsquad.gregserver;

import org.gregsquad.gregsadventure.card.*;
import org.gregsquad.gregsadventure.game.*;

import java.io.*;
import java.net.*;
import java.util.*;
import java.util.function.Function;

/**
 * The Client class is responsible for managing the client-side logic of the game.
 * It connects to the server, sends and receives messages, and handles reconnections.
 */
public class Client {
    private String serverIp; // Server IP address
    private int serverPort; // Server port number
    private String name; // Client name
    private int clientId; // Client ID
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

    /**
     * Tries to establish a connection to the server. If the connection attempt times out, 
     * it will retry until the maximum number of reconnect attempts is reached.
     *
     * @throws IOException If the maximum number of reconnect attempts is reached, or if an error occurs while creating the input and output streams.
     * @throws InterruptedException If the thread sleep is interrupted.
     */
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

    /**
     * The main execution method for the client. It attempts to connect to the server, sends the client's name,
     * and starts a listener thread to handle incoming messages. If an error occurs during execution, it will be caught
     * and printed to the error stream. After the listener thread finishes, it closes the streams and the connection.
     *
     * @throws IOException If an I/O error occurs during the execution, such as if the connection fails or the streams cannot be closed.
     * @throws InterruptedException If the execution is interrupted, such as if the listener thread is interrupted.
     */
    public void run() {
        try {
            connect();

            System.out.println("["+name+"] " + "Sending name: " + name);
        
            Message<String> answer = request("CONNEXION", "NAME");
                
            System.out.println("");
            System.out.println("["+name+"] " + name + " is correctly connected");
            System.out.println("");

            Thread globalListenerThread = new Thread(globalListener);
            globalListenerThread.start();
            globalListenerThread.join();


            // Close the streams and the connection
            stop();
            
        } catch (IOException e) {
            System.err.println("IOException1: " + e.getMessage());
        } catch (InterruptedException e) {
            System.err.println("InterruptedException: " + e.getMessage());
        }
    }

    /**
     * Closes the streams and the connection. If an I/O error occurs while closing the streams or the connection,
     * an error message will be printed to the error stream.
     *
     * @throws IOException If an I/O error occurs while closing the streams or the connection.
     */
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

        /**
         * Closes the streams and the connection. If an I/O error occurs while closing the streams or the connection,
         * an error message will be printed to the error stream.
         *
         * @throws IOException If an I/O error occurs while closing the streams or the connection.
         */
        public void run() {
            try {
                Object inputObject;
                while ((inputObject = in.readObject()) != null) {

                    if (inputObject instanceof Message) {
                        Message<?> inputMessage = (Message<?>) inputObject;
                        System.out.println("["+name+"] " + "Received message: " + inputMessage.getType() + " " + inputMessage.getPurpose());
                        switch (inputMessage.getType()) {
                            case "CONNEXION":
                                switch (inputMessage.getPurpose()) {
                                    case "NAME":
                                        System.out.println("["+name+"] " + "Received id: " + inputMessage.getContent());
                                        clientId = (int) inputMessage.getContent();
                                        break;
                                    default:
                                        System.err.println("Unknown message purpose: " + inputMessage.getPurpose());
                                        break;
                                }
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

                            default:
                                System.err.println("Unknown message type: " + inputMessage.getType());
                                break;
                        }

                    }
                    inputObject = null;
                }
            } catch (IOException e) {
                System.err.println("IOException in GlobalListener: " + e.getMessage());
            } catch (ClassNotFoundException e) {
                System.err.println("ClassNotFoundException in GlobalListener: " + e.getMessage());
            }
        }

        /**
         * Returns the last Donjon card message.
         *
         * @return The last Donjon card message.
         */
        public Message<Card> getLastDonjonCard() {
            return lastDonjonCard;
        }

        /**
         * Returns the last Treasure card message.
         *
         * @return The last Treasure card message.
         */
        public Message<Card> getLastTreasureCard() {
            return lastTreasureCard;
        }

        /**
         * Returns the last Donjon discard message.
         *
         * @return The last Donjon discard message.
         */
        public Message<LinkedList<Card>> getLastDonjonDiscard() {
            return lastDonjonDiscard;
        }

        /**
         * Returns the last Treasure discard message.
         *
         * @return The last Treasure discard message.
         */
        public Message<LinkedList<Card>> getLastTreasureDiscard() {
            return lastTreasureDiscard;
        }

        /**
         * Returns the last current player message.
         *
         * @return The last current player message.
         */
        public Message<Player> getLastCurrentPlayer() {
            return lastCurrentPlayer;
        }

        /**
         * Returns the last player list message.
         *
         * @return The last player list message.
         */
        public Message<ArrayList<Player>> getLastPlayerList() {
            return lastPlayerList;
        }

        /**
         * Returns the last game initialization message.
         *
         * @return The last game initialization message.
         */
        public Message<Boolean> getLastInitGame() {
            return lastInitGame;
        }
    }

    /**
     * Retrieves the client's identifier.
     *
     * @return The client's identifier.
     */
    public int getId() {
        return clientId;
    }

    // REQUESTS SECTION

    /**
     * Sends a request to the server.
     *
     * @param <T> The type of content in the request message.
     * @param request The request message to be sent.
     */
    public <T extends Serializable> void sendRequest(Message<T> request) {
        try {
            System.out.println("["+name+"] " + "Sending request: " + request.getType() + " " + request.getPurpose());
            out.reset();
            out.writeObject(request);
        } catch (IOException e) {
            System.err.println("["+name+"] " + "Error sending message: " + e.getMessage());
        }
    }

    /**
     * Creates a new request of a specific type and purpose, sends it to the server, and returns it.
     *
     * @param type The type of the request.
     * @param purpose The purpose of the request.
     * @return The request that was sent.
     */
    public Message<String> request(String type, String purpose) {
        Message<String> request_locale = new Message<String>(name, type, purpose,"",String.class);
        System.out.println("["+name+"] " + "Sending request. Name: " + request_locale.getSender() + " Type: " + request_locale.getType() + " Purpose: " + request_locale.getPurpose());
        sendRequest(request_locale);
        return request_locale;
    }

    /**
     * Sends a request of a specific category and action, and waits for a response.
     *
     * @param <T> The type of the content in the response message.
     * @param category The category of the request.
     * @param action The action of the request.
     * @param messageRetriever A function that retrieves the last message of a specific type.
     * @return The response message, or null if no response was received after 5 attempts.
     */
    public <T> Message<T> requestAndAwaitResponse(String category, String action, Function<GlobalListener, Message<T>> messageRetriever) {
        
        for (int j = 0; j < 5; j++) {
            
            Message<String> request_locale = request(category, action);

            for (int i = 0; i < 5; i++) {
                Message<T> lastMessage = messageRetriever.apply(globalListener);

                if (lastMessage != null && request_locale.getId().equals(lastMessage.getId())) {
                    System.out.println("["+name+"] " + name + " got the " + action + ": " + lastMessage.getContent());
                    return lastMessage;
                }

                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    System.err.println("InterruptedException: " + e.getMessage());
                }
            }
        }
        System.out.println("["+name+"] " + name + " did not get the " + action);
        return null;
    }

    /**
     * Sends a request to initialize the game.
     */
    public void initGame() {
        request("GAME", "INIT_GAME");
    }


    /**
     * Ends the current player's turn by sending an "END_TURN" request to the game.
     */
    public void endTurn() {
        request("GAME", "END_TURN");
    }

    /**
     * Sends a request to draw a Donjon card and waits for the response.
     *
     * @return The drawn Donjon card, or null if no response was received.
     */
    public Card drawDonjonCard() {

        Message<Card> message = requestAndAwaitResponse("GAME", "DRAW_DONJON_CARD", GlobalListener::getLastDonjonCard);
        return message != null ? message.getContent() : null;
    }

    /**
     * Sends a request to draw a Treasure card and waits for the response.
     *
     * @return The drawn Treasure card, or null if no response was received.
     */
    public Card drawTreasureCard() {

        Message<Card> message = requestAndAwaitResponse("GAME", "DRAW_TREASURE_CARD", GlobalListener::getLastTreasureCard);
        return message != null ? message.getContent() : null;

    }

    /**
     * Sends a request to get the list of players and waits for the response.
     *
     * @return The list of players, or null if no response was received.
     */
    public ArrayList<Player> getPlayerList() {

        Message<ArrayList<Player>> message = requestAndAwaitResponse("GAME", "GET_PLAYER_LIST", GlobalListener::getLastPlayerList);
        return message != null ? message.getContent() : null;
    }

    /**
     * Sends a request to get the game initialization status and waits for the response.
     *
     * @return The game initialization status, or false if no response was received.
     */
    public boolean getInitGame() {

        Message<Boolean> message = requestAndAwaitResponse("GAME", "GET_INIT_GAME", GlobalListener::getLastInitGame);
        return message != null ? message.getContent() : false;
    }

    /**
     * Sends a request to get the current player and waits for the response.
     *
     * @return The current player, or null if no response was received.
     */
    public Player getCurrentPlayer() {

        Message<Player> message = requestAndAwaitResponse("GAME", "GET_CURRENT_PLAYER", GlobalListener::getLastCurrentPlayer);
        return message != null ? message.getContent() : null;

    }

    /**
     * Sends a request to get the Donjon discard pile and waits for the response.
     *
     * @return The Donjon discard pile, or null if no response was received.
     */
    public LinkedList<Card> getDonjonDiscard() {

        Message<LinkedList<Card>> message = requestAndAwaitResponse("GAME", "GET_DONJON_DISCARD", GlobalListener::getLastDonjonDiscard);
        return message != null ? message.getContent() : null;

    }

    /**
     * Sends a request to get the Treasure discard pile and waits for the response.
     *
     * @return The Treasure discard pile, or null if no response was received.
     */
    public LinkedList<Card> getTreasureDiscard() {

        Message<LinkedList<Card>> message = requestAndAwaitResponse("GAME", "GET_TREASURE_DISCARD", GlobalListener::getLastTreasureDiscard);
        return message != null ? message.getContent() : null;

    }


}
