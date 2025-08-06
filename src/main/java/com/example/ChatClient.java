package com.example;

import java.io.*;
import java.net.*;

public class ChatClient {

    private static final String SERVER_IP = "localhost";
    private static final int SERVER_PORT = 12345;

    public static void main(String[] args) {
        try (
                Socket socket = new Socket(SERVER_IP, SERVER_PORT);
                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
                BufferedReader console = new BufferedReader(new InputStreamReader(System.in))
        ) {
            String line;
            while ((line = in.readLine()) != null) {
                System.out.println(line);

                if (line.toLowerCase().contains("enter your username")) {
                    String user = console.readLine();
                    out.println(user);
                } else if (line.toLowerCase().startsWith("invalid")) {
                    // Server rejected the username, loop continues
                    continue;
                } else if (line.toLowerCase().startsWith("welcome")) {
                    break;
                }
            }

            // Start listener thread
            Thread listener = new Thread(() -> {
                try {
                    String msg;
                    while ((msg = in.readLine()) != null) {
                        System.out.println(msg);
                    }
                } catch (IOException e) {
                    System.out.println("Connection closed.");
                }
            });
            listener.start();

            // Send messages to server
            String userInput;
            while ((userInput = console.readLine()) != null) {
                out.println(userInput);
                if ("exit".equalsIgnoreCase(userInput)) break;
            }

            socket.close();
        } catch (IOException e) {
            System.out.println("Unable to connect to server.");
            e.printStackTrace();
        }
    }
}
