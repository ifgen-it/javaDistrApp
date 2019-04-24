/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package task2tcpserver;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 *
 * @author evgsmi
 */
public class Task2TCPServer {

    private static final int DEFAULT_SERVER_PORT = 16789;
    private static final int DEFAULT_SERVER_CLIENTS_NUMBER = 125;

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        ServerSocket serverSocket = null;
        Socket clientSocket = null;
        try {

            serverSocket = new ServerSocket(DEFAULT_SERVER_PORT, DEFAULT_SERVER_CLIENTS_NUMBER);
            System.out.println("Server started.. Waiting for Client..");

            clientSocket = serverSocket.accept();
            InputStream in = clientSocket.getInputStream();
            OutputStream out = clientSocket.getOutputStream();
            System.out.println("Client connected");

            // READ
            Supplier<String> reader = () -> {
                String request = null;
                try {
                    byte[] byteRequest = new byte[1024];
                    int n = in.read(byteRequest);
                    request = new String(byteRequest);

                } catch (IOException ex) {
                    System.out.println(ex.getMessage());
                } finally {
                    return request.trim();
                }
            };

            // WRITE
            Consumer<String> writer = (response) -> {
                try {
                    out.write(response.getBytes());
                } catch (IOException ex) {
                    System.out.println(ex.getMessage());
                }
            };

            // SERVER WORK
            for (int i = 0; i < 2; i++) {
                String request = reader.get();
                String response = "404";
                System.out.println("\nRequest: " + request);

                if (request.equals("date")) {
                    DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
                    Date date = new Date(System.currentTimeMillis());
                    response = dateFormat.format(date);
                } else if (request.equals("time")) {
                    DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
                    Date date = new Date(System.currentTimeMillis());
                    response = dateFormat.format(date);
                }
                writer.accept(response);
                System.out.println("Response: " + response);
            }

            // CLOSE RESOURCES
            clientSocket.shutdownInput();
            clientSocket.shutdownOutput();
            clientSocket.close();
            serverSocket.close();
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        } finally {
            if (clientSocket != null) {
                try {
                    clientSocket.close();
                } catch (IOException ex) {
                    System.out.println(ex.getMessage());
                }
            }

            if (serverSocket != null) {
                try {
                    serverSocket.close();
                } catch (IOException ex) {
                    System.out.println(ex.getMessage());
                }
            }
        }

    }

}
