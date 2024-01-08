package org.gregsquad.gregserver;

import java.io.*;
import java.net.*;
import java.util.*;

import org.gregsquad.gregsadventure.game.*;
import org.gregsquad.gregsadventure.card.*;

/**
 * The Server class is a singleton that manages the server-side logic of the game.
 * It accepts new clients, handles their requests, and manages the game state.
 */
public class Server {
    private static Server instance;
    private ServerSocket serverSocket;
    private int port;
    protected List<ClientHandler> clients;
    protected Game game;

    /**
     * Private constructor for the Server class.
     * Initializes the list of clients.
     */
    private Server() {
        this.clients = new ArrayList<>();
    }

    /**
     * Returns the singleton instance of the Server class.
     * If the instance does not exist, it is created.
     * @return the singleton instance of the Server class.
     */
    public static Server getInstance() {
        if (instance == null) {
            instance = new Server();
        }
        return instance;
    }

    /**
     * Initializes the server with the given port and the game instance.
     * Prints all the cards in the donjon stack.
     * @param port the port number on which the server will listen for connections.
     */
    public void init(int port) {
        this.port = port;
        this.game = Game.getInstance();
        // Print all the cards in the donjon stack
        System.out.println("[SERVER] Donjon stack:");
        for (Card card : Game.getInstance().getDonjonStack().getCards()) {
            System.out.println("[SERVER] " + card.getId() + " " + card.getName());
        }
    } 

    /**
     * Starts the server and accepts new clients in a new thread.
     * Continues to accept new clients until the game is started.
     */
    public void run() {
        try {
            serverSocket = new ServerSocket(port);
            System.out.println("[INFO] Server is listening on port " + port);

            while (Game.getInstance().isGameStarted() == false) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("[CLIENT] New client connected.");

                ClientHandler clientHandler = new ClientHandler(clientSocket, this);
                clients.add(clientHandler);

                Thread thread = new Thread(clientHandler);
                thread.start();
            }
        } catch (IOException e) {
            System.err.println("Server exception: " + e.getMessage());
            e.printStackTrace();
        }
    }


    /**
     * Stops the server and disconnects ,all clients.
     * If an error occurs while stopping the server, it is caught and printed.
     */
    public void stop() {
        try {
            for (ClientHandler client : clients) {
                client.stop();
            }
            if (serverSocket != null) {
                serverSocket.close();
            }
            System.out.println("Server stopped.");
        } catch (IOException e) {
            System.err.println("Error stopping server: " + e.getMessage());
        }
    }   

    /**
     * Sends a message to all connected clients, excluding a specific client.
     * @param message the message to be sent to the clients.
     * @param excludeClient the client to be excluded from the broadcast.
     */
    public void broadcast(Message<String> message, ClientHandler excludeClient) {
        for (ClientHandler client : clients) {
            if (client != excludeClient) {
                client.sendMessage(message);
            }
        }
    }
}

/**
 * The ClientHandler class implements the Runnable interface and is responsible for handling a single client connection.
 * It reads messages from the client and performs actions based on the type and content of the messages.
 */
class ClientHandler implements Runnable {
    private Socket clientSocket;
    private Server server;
    private ObjectOutputStream out;
    private ObjectInputStream in;
    private String clientName;

    /**
     * Constructs a new ClientHandler for the given client socket and server.
     * @param clientSocket the socket that is connected to the client.
     * @param server the server that the client is connected to.
     */
    public ClientHandler(Socket clientSocket, Server server) {
        this.clientSocket = clientSocket;
        this.server = server;
    }

    /**
     * Returns the name of the client.
     * @return the name of the client.
     */
    public String getClientName() {
        return clientName;
    }

    /**
     * The main loop of the ClientHandler.
     * Reads messages from the client and performs actions based on the type and content of the messages.
     * Continues to read messages until the client disconnects or an error occurs.
     */
    public void run() {
        try {
            out = new ObjectOutputStream(clientSocket.getOutputStream());
            in = new ObjectInputStream(clientSocket.getInputStream());
            System.out.println("[CLIENT] Waiting for client name...");

            // Here is the main loop of the client handler
            // It makes actions based on the messages received from the client
            Message<?> inputMessage;
            while ((inputMessage = (Message<?>) in.readObject()) != null) {

                out.reset(); // Reset the output stream to avoid sending the same object multiple times
                             // !! Very important !!

                if (inputMessage.isOfType(String.class)) {
                    Message<String> stringMessage = (Message<String>) inputMessage;
                    // Manage the message of type String here
                    System.out.println("[SERVER] Received message from " + stringMessage.getSender() + ": " + stringMessage.getContent());

                    if(stringMessage.getType().equals("PING")) {
                        sendToClient(stringMessage.getId(), "PING", "", "PONG");
                    }
                    // Check if the message is a connexion message
                    if(stringMessage.getType().equals("CONNEXION")) {

                        if(stringMessage.getPurpose().equals("NAME")) {
                            
                            String clientName = stringMessage.getSender();
                            System.out.println("[SERVER] " + clientName + " connected.");

                            // Check if the name is already taken
                            boolean nameTaken = Game.getInstance().getPlayerList().stream()
                                .anyMatch(player -> player.getName().equals(clientName));

                            if (nameTaken) {
                                System.out.println("[SERVER] Name " + clientName + " is already taken.");
                                sendToClient("CONNEXION", "NAME", "TAKEN");
                            } else {
                                this.clientName = clientName;
                                System.out.println("[SERVER] Creating player " + this.getClientName());
                                sendToClient(stringMessage.getId(), "CONNEXION", "NAME", "OK");
                                Player player = new Player(this.getClientName());
                                Game.getInstance().addPlayer(player);
                            }
                        }
                    }

                    if(stringMessage.getType().equals("GAME")) {
                        
                        if(stringMessage.getPurpose().equals("DRAW_DONJON_CARD")) {

                            System.out.println("[SERVER] " + this.getClientName() + " is drawing a donjon card.");
                            Card card = Game.getInstance().getDonjonStack().draw();
                            sendToClient(stringMessage.getId(), "GAME", "DRAW_DONJON_CARD", card);
                        }

                        if(stringMessage.getPurpose().equals("DRAW_TREASURE_CARD")) {

                            System.out.println("[SERVER] " + this.getClientName() + " is drawing a treasure card.");
                            Card card = Game.getInstance().getTreasureStack().draw();
                            sendToClient(stringMessage.getId(), "GAME", "DRAW_TREASURE_CARD", card);
                        }

                        if(stringMessage.getPurpose().equals("GET_DONJON_DISCARD")) {

                            System.out.println("[SERVER] " + this.getClientName() + " is getting the donjon discard.");
                            LinkedList<Card> cards = Game.getInstance().getDonjonDiscard().getCards();
                            sendToClient(stringMessage.getId(), "GAME", "GET_DONJON_DISCARD", cards);
                        }

                        if(stringMessage.getPurpose().equals("GET_TREASURE_DISCARD")) {
                            
                            System.out.println("[SERVER] " + this.getClientName() + " is getting the treasure discard.");
                            LinkedList<Card> cards = Game.getInstance().getTreasureDiscard().getCards();
                            sendToClient(stringMessage.getId(), "GAME", "GET_TREASURE_DISCARD", cards);
                        }

                        if(stringMessage.getPurpose().equals("GET_CURRENT_PLAYER")) {

                            System.out.println("[SERVER] " + this.getClientName() + " is getting the current player.");
                            Player player = Game.getInstance().getCurrentPlayer();
                            sendToClient(stringMessage.getId(), "GAME", "GET_CURRENT_PLAYER", player);
                        }

                        if(stringMessage.getPurpose().equals("GET_PLAYER_LIST")) {
    
                            System.out.println("[SERVER] " + this.getClientName() + " is getting the player list.");
                            ArrayList<Player> playerList = Game.getInstance().getPlayerList();   
                            System.out.println(inputMessage.getContent());                     
                            sendToClient(stringMessage.getId(), "GAME", "GET_PLAYER_LIST", playerList);
                        }

                        if(stringMessage.getPurpose().equals("INIT_GAME")) {
    
                            System.out.println("[SERVER] " + this.getClientName() + " is initializing the game.");
                            Game.getInstance().init();
                        }

                        if(stringMessage.getPurpose().equals("GET_INIT_GAME")) {
    
                            System.out.println("[SERVER] " + this.getClientName() + " is getting the game initialization status.");
                            boolean init = Game.getInstance().isGameStarted();
                            System.out.println("[SERVER] Game is initialized: " + init);
                            sendToClient(stringMessage.getId(), "GAME", "GET_INIT_GAME", init);
                        }

                    }

                } 
                else if (inputMessage.isOfType(Card.class)) {
                    Message<Card> cardMessage = (Message<Card>) inputMessage;
                    // Traitez le message de type Card ici
                    
                }
            }
            System.out.println("[CLIENT] " + clientName + " disconnected.");
            out.close();
            in.close();
            clientSocket.close();
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Error handling client connection: " + e.getMessage());
        }
    }

    /**
     * Stops the client connection by closing the client socket.
     * If an error occurs while stopping the client, it is caught and printed.
     */
    public void stop() {
        try {
            if (clientSocket != null) {
                clientSocket.close();
            }
            System.out.println("Client stopped.");
        } catch (IOException e) {
            System.err.println("Error stopping client: " + e.getMessage());
        }
    }

    /**
     * Sends a message to the client.
     * If an error occurs while sending the message, it is caught and printed.
     * @param message the message to be sent to the client.
     * @param <T> the type of the content of the message, which must implement Serializable.
     */
    public <T extends Serializable> void sendMessage(Message<T> message) {
        try {
            out.writeObject(message);
            out.flush();
        } catch (IOException e) {
            System.err.println("Error sending message: " + e.getMessage());
        }
    }

    /**
     * Sends a message to the client with a specified type, purpose, and content.
     * The message is created with the server as the sender and the specified type, purpose, and content.
     * @param type the type of the message.
     * @param purpose the purpose of the message.
     * @param content the content of the message.
     * @param <T> the type of the content of the message, which must implement Serializable.
     */
    public <T extends Serializable> void sendToClient(String type, String purpose, T content) {
        System.out.println("[SERVER] Sending message to " + this.getClientName() + ": " + type + " " + purpose);
        Message<T> message = new Message<T>("SERVER", type, purpose, content, content.getClass());
        sendMessage(message);
    }

    /**
     * Sends a message to the client with a specified ID, type, purpose, and content.
     * The message is created with the specified ID, the server as the sender, and the specified type, purpose, and content.
     * @param id the ID of the message.
     * @param type the type of the message.
     * @param purpose the purpose of the message.
     * @param content the content of the message.
     * @param <T> the type of the content of the message, which must implement Serializable.
     */
    public <T extends Serializable> void sendToClient(UUID id, String type, String purpose, T content) {
        System.out.println("[SERVER] Sending message to " + this.getClientName() + ": " + type + " " + purpose);
        Message<T> message = new Message<T>(id, "SERVER", type, purpose, content, content.getClass());
        sendMessage(message);
    }
}