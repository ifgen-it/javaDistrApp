package task6client;

import Task6ServerUDPLib.Envelope;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Scanner;

public class Task6Client {

    // Task6Client ports:
    public static int PORT_CLIENT_OUT = 15101;
    public static int PORT_CLIENT_IN = 15102;
    public static int clientNumber;

    // Task6Server ports:
    public static final int PORT_SERVER_OUT = 16601;
    public static final int PORT_SERVER_IN = 16602;

    public static final int BUFFER_SIZE = 1024;
    
    public static String cookieUser = "";
    public static String serverIP = "";

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);
        
        // CLIENT STARTS
        System.out.println("////////////////////////////////////");
        System.out.println("//         CONSOLE CLIENT         //");
        System.out.println("////////////////////////////////////");

        System.out.println();
        System.out.println("Commands:");
        System.out.println("------------------------------------");
        System.out.println("login");
        System.out.println("logout");
        System.out.println("history -name <LAST NAME OF EMPLOYEE>");
        System.out.println("history -code <CODE OF EMPLOYEE>");
        System.out.println("exit");
        System.out.println("------------------------------------");
        System.out.print("Input server IP-address: ");
        serverIP = sc.next().trim();
        
        System.out.print("Input your client number: ");
        clientNumber = sc.nextInt();
        PORT_CLIENT_IN += clientNumber;
        PORT_CLIENT_OUT += clientNumber;
        System.out.println("Port was configurated depending on your client number");
        System.out.println("Input command and press Enter\n");
        
        while (true) {
            System.out.print(">");

            ArrayList<String> requestData = new ArrayList<>();
            String requestCommand = "";

            String input = sc.next().trim().toLowerCase();

            if (input.equals("login")) {
                System.out.print("Input login: ");
                String user = sc.next().trim();
                System.out.print("Input password: ");
                String password = sc.next().trim();

                requestCommand = "login";
                requestData.add(user);
                requestData.add(password);
                
            } else if (input.equals("logout")) {
                requestCommand = "logout";
                
            } else if (input.equals("history")) {
                String mode = sc.next().trim().toLowerCase();
                if (mode.equals("-name")) {
                    requestCommand = "getHistoryByLName";
                } else if (mode.equals("-code")) {
                    requestCommand = "getHistoryByCode";
                } else {
                    System.out.println("Unknown command");
                    continue;
                }
                String lNameOrCode = sc.next().trim();
                requestData.add(lNameOrCode);

            } else if (input.equals("exit")) {
                System.out.println("Good bye!");
                break;
            } else {
                System.out.println("Unknown command");
                continue;
            }

            // UDP-SERVER : SEND PACKET TO THE UDP-CLIENT ( = Task6Server )
            try {

                DatagramSocket udpSocket = new DatagramSocket(PORT_CLIENT_OUT);

                Envelope request = new Envelope(requestCommand, requestData, cookieUser);

                byte[] buffer = serializeEnvelope(request);

                DatagramPacket udpPacket = new DatagramPacket(buffer, buffer.length);
                udpPacket.setAddress(InetAddress.getByName(serverIP));
                udpPacket.setPort(PORT_SERVER_IN);
                udpSocket.send(udpPacket);
//                System.out.println("Message sent");
                udpSocket.close();

            } catch (SocketException ex) {
                System.out.println(ex.getMessage());
            } catch (UnknownHostException ex) {
                System.out.print(ex.getMessage());
            } catch (IOException ex) {
                System.out.println(ex.getMessage());
            }

            // WAITING FOR SYNCHRONOUS RESPONSE FROM THE SERVER
            // UDP-CLIENT : RECEIVE PACKET FROM THE UDP-SERVER ( = Task6Server )
            Envelope response = null;
            try {

                DatagramSocket udpSocket = new DatagramSocket(PORT_CLIENT_IN);

                byte[] buffer = new byte[BUFFER_SIZE];
                DatagramPacket udpPacket = new DatagramPacket(buffer, buffer.length);
                udpSocket.receive(udpPacket);

                byte[] bytes = udpPacket.getData();
                response = deserializeEnvelope(bytes);


                udpSocket.close();

            } catch (SocketException ex) {
                System.out.println(ex.getMessage());
            } catch (IOException ex) {
                System.out.println(ex.getMessage());
            }

            // WORKING WITH RESPONSE DATA
            String error = response.getError();
            if (error != null) {
                System.out.println(error);
                continue;
            }
            
            String command = response.getCommand();
            
            if (command.equals("login")) {
                String result = response.getData().get(0);
                if (result.equals("SUCCESS"))
                    cookieUser = requestData.get(0);
                System.out.println(result);
            
            } else if (command.equals("logout")) {
                String result = response.getData().get(0);
                cookieUser = "";
                System.out.println(result);
                
            } else if (command.equals("getHistoryByLName") || command.equals("getHistoryByCode")) {
                System.out.println("Employee history:");
                System.out.println("----------------------------------------------------------");
                System.out.println("id, position, manager, hire, dismiss, code, name, lastname");
                System.out.println("----------------------------------------------------------");

                if (response.getData() == null) {
                    System.out.println("history is empty");
                } else {
                    for (String s : response.getData()) {
                        String[] row = s.split(" ");
                        for (int i = 0; i < row.length; i++) {
                            System.out.print(row[i]);
                            if (i < row.length - 1) {
                                System.out.print(", ");
                            }
                        }
                        System.out.println();
                    }
                }

            }

        } // while

    } // main

    private static byte[] serializeEnvelope(Envelope env) {

        ByteArrayOutputStream baos = new ByteArrayOutputStream(BUFFER_SIZE);

        try {
            ObjectOutputStream oos = new ObjectOutputStream(baos);
            oos.writeObject(env);

        } catch (IOException ex) {
            System.err.println(ex.getMessage());
        }

        //System.out.println("Serialized env: " + baos.toString());
        byte[] buffer = baos.toByteArray();

        return buffer;
    }

    private static Envelope deserializeEnvelope(byte[] bytes) {

        Envelope env = null;
        ByteArrayInputStream bais = new ByteArrayInputStream(bytes);

        try {
            ObjectInputStream ois = new ObjectInputStream(bais);
            env = (Envelope) ois.readObject();
            //System.out.println("Deserialized envelope: " + env);

        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        } catch (ClassNotFoundException ex) {
            System.out.println(ex.getMessage());
        }

        return env;
    }

}
