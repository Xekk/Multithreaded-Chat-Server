package com.example;

import java.io.*;
import java.net.*;
import java.util.*;
import java.util.concurrent.*;

public class ChatServer {

    private static final int PORT = 12345;
    private static Map<String, ClientHandler> clients = new ConcurrentHashMap<>();

    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(PORT);
        System.out.println("Chat server started on port " + PORT);

        while (true) {
            Socket socket = serverSocket.accept();
            new Thread(() -> {
                try {
                    ClientHandler handler = new ClientHandler(socket);
                    handler.start();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }).start();
        }
    }

    static class ClientHandler {
        private Socket socket;
        private PrintWriter out;
        private BufferedReader in;
        private String username;

        public ClientHandler(Socket socket) throws IOException {
            this.socket = socket;
            this.out = new PrintWriter(socket.getOutputStream(), true);
            this.in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        }

        public void start() throws IOException {
            while (true) {
                out.println("Enter your username:");
                username = in.readLine();

                if (username == null || username.isBlank()) {
                    out.println("Invalid username. Try again.");
                    continue;
                }

                if (clients.containsKey(username)) {
                    out.println("Invalid username. Username already taken. Try again.");
                    continue;
                }

                // Valid and unique username
                break;
            }

            clients.put(username, this);
            broadcast(username + " has joined the chat!", null);
            System.out.println(username + " connected from " + socket.getRemoteSocketAddress());

            out.println("Welcome, " + username + "! Type '/msg username message' for private messages. Type 'exit' to leave.");

            String msg;
            while ((msg = in.readLine()) != null) {
                if ("exit".equalsIgnoreCase(msg)) break;

                if (msg.equalsIgnoreCase("/who")) {
                    // List active users
                    StringBuilder userList = new StringBuilder("Online users: ");
                    for (String user : clients.keySet()) {
                        userList.append(user).append(", ");
                    }
                    // Remove last comma
                    if (userList.length() > 0) {
                        userList.setLength(userList.length() - 2);
                    }
                    out.println(userList.toString());
                }

                if (msg.startsWith("/msg ")) {
                    handlePrivateMessage(msg);
                } else {
                    broadcast("[" + username + "]: " + msg, this);
                }
            }

            disconnect();
        }

        private void handlePrivateMessage(String msg) {
            String[] parts = msg.split(" ", 3);
            if (parts.length < 3) {
                out.println("Usage: /msg username message");
                return;
            }

            String targetUser = parts[1];
            String privateMsg = parts[2];

            ClientHandler target = clients.get(targetUser);
            if (target != null) {
                target.out.println("[Private] " + username + ": " + privateMsg);
                out.println("[Private to " + targetUser + "]: " + privateMsg);
            } else {
                out.println("User '" + targetUser + "' not found.");
            }
        }

        private void broadcast(String message, ClientHandler sender) {
            for (ClientHandler client : clients.values()) {
                if (client != sender) {
                    client.out.println(message);
                }
            }
        }

        private void disconnect() {
            try {
                clients.remove(username);
                broadcast( username + " has left the chat.", null);
                socket.close();
                System.out.println(username + " disconnected.");
            } catch (IOException ignored) {}
        }
    }
}
