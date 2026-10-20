package com.shopsphere.network;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

/**
 * TCP Socket Client for Communicating with ShopSphere Inventory Server.
 * <p>
 * Academic Purpose: Demonstrates client-side socket operations, stream manipulation,
 * interactive REPL loop, and headless single-shot CLI command execution for RTU coursework.
 * </p>
 */
public class InventorySocketClient {

    private final String host;
    private final int port;

    public InventorySocketClient(String host, int port) {
        this.host = host;
        this.port = port;
    }

    /**
     * Sends a single protocol command to the server and returns the response.
     *
     * @param command Line command string (e.g. "GET_STOCK 1")
     * @return Server response line
     * @throws IOException on network communication failure
     */
    public String executeCommand(String command) throws IOException {
        try (
            Socket socket = new Socket(host, port);
            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8));
            PrintWriter writer = new PrintWriter(socket.getOutputStream(), true, StandardCharsets.UTF_8)
        ) {
            // Read welcome banner
            reader.readLine();

            // Send command
            writer.println(command);

            // Read response
            StringBuilder sb = new StringBuilder();
            String responseLine = reader.readLine();
            if (responseLine != null) {
                sb.append(responseLine);
            }
            return sb.toString();
        }
    }

    /**
     * Starts an interactive REPL terminal session with the inventory socket server.
     */
    public void startInteractiveSession() {
        System.out.println("Connecting to ShopSphere Inventory Server at " + host + ":" + port + "...");

        try (
            Socket socket = new Socket(host, port);
            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8));
            PrintWriter writer = new PrintWriter(socket.getOutputStream(), true, StandardCharsets.UTF_8);
            Scanner scanner = new Scanner(System.in)
        ) {
            System.out.println("[Connected]");
            // Print server welcome message
            System.out.println(reader.readLine());
            System.out.println("Type 'HELP' for commands, or 'QUIT' to exit.\n");

            while (true) {
                System.out.print("ShopSphere-Socket> ");
                if (!scanner.hasNextLine()) break;

                String input = scanner.nextLine().trim();
                if (input.isEmpty()) continue;

                writer.println(input);

                String response = reader.readLine();
                if (response == null) {
                    System.out.println("[Server closed connection]");
                    break;
                }

                System.out.println(response);

                if (input.equalsIgnoreCase(SocketProtocol.CMD_QUIT)) {
                    break;
                }
            }
        } catch (IOException e) {
            System.err.println("Could not connect to Inventory Server at " + host + ":" + port + ". (Is server running?)");
            System.err.println("Error: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        String host = SocketProtocol.DEFAULT_HOST;
        int port = SocketProtocol.DEFAULT_PORT;

        InventorySocketClient client = new InventorySocketClient(host, port);

        if (args.length > 0) {
            // Headless CLI execution mode: java InventorySocketClient GET_STOCK 1
            String command = String.join(" ", args);
            System.out.println("Sending command: " + command);
            try {
                String result = client.executeCommand(command);
                System.out.println("Server Reply: " + result);
            } catch (IOException e) {
                System.err.println("Execution failed: " + e.getMessage());
            }
        } else {
            // Interactive REPL Mode
            client.startInteractiveSession();
        }
    }
}
