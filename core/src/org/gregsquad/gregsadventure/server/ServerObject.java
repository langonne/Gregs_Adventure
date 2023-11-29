package org.gregsquad.gregsadventure.server;


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
                Socket clientSocket = serverSocket.accept();
                System.out.println("New client connected.");

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
        ServerObject server = new ServerObject(27093);
        server.run();
    }
}

class ClientHandler implements Runnable {
    private Socket clientSocket;
    private ServerObject server;
    private PrintWriter out;
    private String clientName;

    public ClientHandler(Socket clientSocket, ServerObject server) {
        this.clientSocket = clientSocket;
        this.server = server;
    }

    public void run() {
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            out = new PrintWriter(clientSocket.getOutputStream(), true);
            
            System.out.println("Waiting for client name...");
            clientName = in.readLine();
            System.out.println(clientName + " connected.");

            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                System.out.println(clientName + " says: " + inputLine);
                Message<String> message = new Message<>(clientName, inputLine);
                server.broadcast(message, this);
            }

            out.close();
            in.close();
            clientSocket.close();
        } catch (IOException e) {
            System.err.println("Error handling client connection: " + e.getMessage());
        }
    }

    public void sendMessage(Message<String> message) {
        out.println(message.getSender() + ": " + message.getContent());
    }
}