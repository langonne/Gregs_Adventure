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

    public void broadcast(String message, ClientHandler excludeClient) {
        for (ClientHandler client : clients) {
            if (client != excludeClient) {
                client.sendMessage(message);
            }
        }
    }

    public static void main(String[] args) {
        // Create a new Server object with the specified port number
        ServerObject server = new ServerObject(27093);

        // Start the server
        server.run();
    }
}

// This class handles communication with a single client
class ClientHandler implements Runnable {
    private Socket clientSocket; // The socket for communication with the client
    private ServerObject server; // The server that this handler is part of
    private PrintWriter out; // The output stream to the client
    private String clientName; // The name of the client

    // Constructor
    // Takes a socket and a server as parameters. The socket is used for communication with the client.
    // The server is used to interact with other parts of the server.
    public ClientHandler(Socket clientSocket, ServerObject server) {
        this.clientSocket = clientSocket;
        this.server = server;
    }

    public void run() {
        try {
            // Create input and output streams for the client socket
            BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            out = new PrintWriter(clientSocket.getOutputStream(), true);

            // Read the client's name
            clientName = in.readLine();
            System.out.println(clientName + " connected.");

            // Read messages from the client and broadcast them to all other clients
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                System.out.println(clientName + " says: " + inputLine);
                server.broadcast(clientName + ": " + inputLine, this);
            }

            // Close the input and output streams and the client socket
            out.close();
            in.close();
            clientSocket.close();
        } catch (IOException e) {
            // Handle any exceptions that occur during client connection handling
            System.err.println("Error handling client connection: " + e.getMessage());
        }
    }

    public void sendMessage(String message) {
        // Send a message to the client using the output stream
        out.println(message);
    }
}
