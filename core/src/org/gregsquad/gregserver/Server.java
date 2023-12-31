package org.gregsquad.gregserver;

import java.io.*;
import java.net.*;
import java.util.*;

import org.gregsquad.gregsadventure.game.*;
import org.gregsquad.gregsadventure.card.*;

public class Server {
    private static Server instance;
    private int port;
    protected List<ClientHandler> clients;
    protected Game game;

    private Server() {
        this.clients = new ArrayList<>();
    }

    public static Server getInstance() {
        if (instance == null) {
            instance = new Server();
        }
        return instance;
    }

    public void init(int port) {
        this.port = port;
        this.game = Game.getInstance();
        // Print all the cards in the donjon stack
        System.out.println("[SERVER] Donjon stack:");
        for (Card card : Game.getInstance().getDonjonStack().getCards()) {
            System.out.println("[SERVER] " + card.getId() + " " + card.getName());
        }
    }


    public void run() {
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("[INFO] Server is listening on port " + port);

            while (true) {
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

    public void broadcast(Message<String> message, ClientHandler excludeClient) {
        for (ClientHandler client : clients) {
            if (client != excludeClient) {
                client.sendMessage(message);
            }
        }
    }

    public static void main(String[] args) {
        //Server server = new Server(27093);
        //server.run();
    }
}

class ClientHandler implements Runnable {
    private Socket clientSocket;
    private Server server;
    private ObjectOutputStream out;
    private ObjectInputStream in;
    private String clientName;

    public ClientHandler(Socket clientSocket, Server server) {
        this.clientSocket = clientSocket;
        this.server = server;
    }

    public String getClientName() {
        return clientName;
    }

    public void run() {
        try {
            out = new ObjectOutputStream(clientSocket.getOutputStream());
            in = new ObjectInputStream(clientSocket.getInputStream());
            System.out.println("[CLIENT] Waiting for client name...");

            // Read the first object from the stream and handle it based on its type

            /* 
            Object firstObject = in.readObject();
            if (firstObject instanceof Message) {
                Message<String> firstMessage = (Message<String>) firstObject;
                clientName = firstMessage.getSender();
                System.out.println("[CLIENT] " + clientName + " connected.");
                server.broadcast(firstMessage, this);
            }

            // Create a new player linked to the client .
            */

            // FAIRE LA DETECTION DES MESSAGES ICI
            Message<?> inputMessage;
            while ((inputMessage = (Message<?>) in.readObject()) != null) {

                if (inputMessage.isOfType(String.class)) {
                    Message<String> stringMessage = (Message<String>) inputMessage;
                    // Traitez le message de type String ici
                    System.out.println("[SERVER] Received message from " + stringMessage.getSender() + ": " + stringMessage.getContent());
                    //Si le message est de type CONNEXION et le propose est de type NAME
                    if(stringMessage.getType().equals("CONNEXION")) {

                        if(stringMessage.getPurpose().equals("NAME")) {
                            
                            String clientName = stringMessage.getSender();
                            System.out.println("[SERVER] " + clientName + " connected.");

                            // Vérifier si le nom est déjà pris
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

                        if(stringMessage.getPurpose().equals("GET_PLAYER_LIST")) {
    
                                System.out.println("[SERVER] " + this.getClientName() + " is getting the player list.");
                                ArrayList<Player> playerList = Game.getInstance().getPlayerList();
                                sendToClient(stringMessage.getId(), "GAME", "GET_PLAYER_LIST", playerList);
                            }
                    }

                } else if (inputMessage.isOfType(Card.class)) {
                    Message<Card> cardMessage = (Message<Card>) inputMessage;
                    // Traitez le message de type Card ici
                    
                }
            }

            out.close();
            in.close();
            clientSocket.close();
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Error handling client connection: " + e.getMessage());
        }
    }

    // Send a Message to the client
    public <T extends Serializable> void sendMessage(Message<T> message) {
        try {
            out.writeObject(message);
        } catch (IOException e) {
            System.err.println("Error sending message: " + e.getMessage());
        }
    }

    public <T extends Serializable> void sendToClient(String type, String purpose, T content) {
        System.out.println("[SERVER] Sending message to " + this.getClientName() + ": " + type + " " + purpose);
        Message<T> message = new Message<T>("SERVER", type, purpose, content, content.getClass());
        sendMessage(message);
    }
    public <T extends Serializable> void sendToClient(UUID id, String type, String purpose, T content) {
        System.out.println("[SERVER] Sending message to " + this.getClientName() + ": " + type + " " + purpose);
        Message<T> message = new Message<T>(id, "SERVER", type, purpose, content, content.getClass());
        sendMessage(message);
    }
}