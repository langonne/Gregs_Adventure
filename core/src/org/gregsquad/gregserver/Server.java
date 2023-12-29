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

                if (inputMessage instanceof Message<String>) {
                    Message<String> stringMessage = (Message<String>) inputMessage;
                    // Traitez le message de type String ici

                    //Si le message est de type CONNEXION et le propose est de type NAME
                    if(stringMessage.getType().equals("CONNEXION")) {

                        if(stringMessage.getPurpose().equals("NAME")) {
                            
                            String clientName = stringMessage.getSender();
                            System.out.println("[CLIENT] " + clientName + " connected.");

                            // Vérifier si le nom est déjà pris
                            boolean nameTaken = Game.getInstance().getPlayerList().stream()
                                .anyMatch(player -> player.getName().equals(clientName));

                            if (nameTaken) {
                                System.out.println("[SERVER] Name " + clientName + " is already taken.");
                                sendToClient("CONNEXION", "NAME", "TAKEN");
                            } else {
                                System.out.println("[SERVER] Creating player " + this.getClientName());
                                sendToClient("CONNEXION", "NAME", "OK");
                                Player player = new Player(this.getClientName());
                                Game.getInstance().addPlayer(player);
                            }

                        }
                    }




                } else if (inputMessage instanceof Message<Card>) {
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
        Message<T> message = new Message<T>("SERVER", type, purpose, content);
        sendMessage(message);
    }
}