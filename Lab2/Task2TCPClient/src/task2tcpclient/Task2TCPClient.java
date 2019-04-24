/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package task2tcpclient;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 *
 * @author evgsmi
 */
public class Task2TCPClient {

    private static final int DEFAULT_SERVER_PORT = 16789;

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {

        Socket socket = null;
        try {

            InetAddress address = InetAddress.getByName("localhost");
            int port = DEFAULT_SERVER_PORT;
            System.out.println("Client started..");
            socket = new Socket(address, port);
            System.out.println("Client connected to the Server");

            InputStream in = socket.getInputStream();
            OutputStream out = socket.getOutputStream();

            // WRITE 
            Consumer<String> writer = (request) -> {
                try {
                    out.write(request.getBytes());
                } catch (IOException ex) {
                    System.out.println(ex.getMessage());
                }
            };

            // READ
            Supplier<String> reader = () -> {
                String response = null;
                try {
                    byte[] byteAnswer = new byte[1024];
                    int n = in.read(byteAnswer);
                    response = new String(byteAnswer);

                } catch (IOException ex) {
                    System.out.println(ex.getMessage());
                } finally {
                    return response.trim();
                }
            };

            // CLIENT WORK
            writer.accept("date");
            System.out.println("\nRequest: date");
            System.out.println("Response: " + reader.get());

            writer.accept("time");
            System.out.println("\nRequest: time");
            System.out.println("Response: " + reader.get());

            // CLOSE RESOURCES
            socket.shutdownInput();
            socket.shutdownOutput();
            socket.close();

        } catch (UnknownHostException ex) {
            System.out.println(ex.getMessage());
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        } finally {
            if (socket != null) {
                try {
                    socket.close();
                } catch (IOException ex) {
                    System.out.println(ex.getMessage());
                }
            }
        }

    }

}
