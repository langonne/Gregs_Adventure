package org.gregsquad.gregsadventure.Server;

import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.List;

public class ServerObject {
    private int port;
    private List<ClientHandler> clients;

    public ServerObject(int port) {
        this.port = port;
        this.clients = new ArrayList<>();
    }

    public void run() {
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("Server is listening on port " + port);

            while (true) {
                // Wait for a new client to connect
                Socket clientSocket = serverSocket.accept();
                System.out.println("New client connected.");

                // Create a new ClientHandler object to handle the client connection
                ClientHandler clientHandler = new ClientHandler(clientSocket, this);
                clients.add(clientHandler);

                // Start a new thread to handle the client connection
                Thread thread = new Thread(clientHandler);
                thread.start();
            }
        } catch (IOException e) {
            // Handle any exceptions that occur during server operation
            System.err.println("Server exception: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void broadcast(Object message, ClientHandler excludeClient) {
        for (ClientHandler client : clients) {
            if (client != excludeClient) {
                client.send(message);
            }
        }
    }
    public static void main(String[] args) {
        // Create a new ServerObject and run it
        ServerObject server = new ServerObject(27093); // replace 1234 with your desired port
        server.run();
    }
}

class ClientHandler implements Runnable {
    private Socket clientSocket;
    private ServerObject server;
    private ObjectOutputStream out;
    private ObjectInputStream in;

    public ClientHandler(Socket socket, ServerObject server) {
        this.clientSocket = socket;
        this.server = server;
        try {
            out = new ObjectOutputStream(clientSocket.getOutputStream());
            in = new ObjectInputStream(clientSocket.getInputStream());
        } catch (IOException e) {
            throw new RuntimeException("Error initializing streams", e);
        }
    }

    public void run() {
        try {
            Object message;
            while ((message = in.readObject()) != null) {
                System.out.println("Received: " + message);
                server.broadcast(message, this);
            }
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException("Error reading message", e);
        }
    }

    public void send(Object message) {
        try {
            out.writeObject(message);
        } catch (IOException e) {
            throw new RuntimeException("Error sending message", e);
        }
    }
}
