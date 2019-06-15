package task6server;

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
import java.rmi.AccessException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import task6serverdbrmilib.Accessable;

public class Task6Server {

    public static final String BINDING_NAME = "DataAccessObject";

    // Task6Client ports:
    //public static final int PORT_CLIENT_OUT = 15101;
    //public static final int PORT_CLIENT_IN = 15102;
    // Task6Server ports:
    public static final int PORT_SERVER_OUT = 16601;
    public static final int PORT_SERVER_IN = 16602;

    public static final int BUFFER_SIZE = 1024;
    public static HashMap<String, String> authUsers;

    public static void main(String[] args) throws IOException {

        authUsers = new HashMap<>();

        try {
            System.out.println("RMI Client of ServerDB started..");

            Registry registry = LocateRegistry.getRegistry(1099);   // LOCAL HOST 
            System.out.println("Got registry..");

            Accessable stub = (Accessable) registry.lookup(BINDING_NAME);
            System.out.println("Stub created..");

            System.out.println("\n-----------------------------------------");
            System.out.println("WORKING WITH STUB = DATA ACCESS OBJECT:");
            System.out.println("-----------------------------------------\n");

            // WORKING WITH STUB = DATA ACCESS OBJECT
            while (true) {

                // UDP-CLIENT : RECEIVE PACKET FROM THE UDP-SERVER ( = Task6Client )
                InetAddress clientAddr = null;
                int clientPort = 1; // ClientReceivePort = ClientSendPort + 1
                String clientId = "";

                Envelope request = null;
                Envelope response = new Envelope();
                ArrayList<String> responseData = new ArrayList<>();

                try {

                    DatagramSocket udpSocket = new DatagramSocket(PORT_SERVER_IN);

                    byte[] buffer = new byte[BUFFER_SIZE];
                    DatagramPacket udpPacket = new DatagramPacket(buffer, buffer.length);
                    udpSocket.receive(udpPacket);

                    byte[] bytes = udpPacket.getData();
                    request = deserializeEnvelope(bytes);

                    clientAddr = udpPacket.getAddress();
                    clientPort += udpPacket.getPort();
                    clientId = clientAddr.getHostAddress() + ":" + String.valueOf(clientPort);

                    /*System.out.println("Got packet from "
                            + udpPacket.getAddress() + " : "
                            + udpPacket.getPort());
                    System.out.println("Packet contains:");
                    System.out.println(request);*/
                    udpSocket.close();

                } catch (SocketException ex) {
                    System.out.println(ex.getMessage());
                } catch (IOException ex) {
                    System.out.println(ex.getMessage());
                }

                // REQUEST DATA FROM SERVER DB
                String command = request.getCommand();

                if (command.equals("login")) {

                    String user = request.getData().get(0);
                    String password = request.getData().get(1);
                    boolean testLogin = stub.testLogin(user, password);
                    
                    System.out.println(clientId + " : login request from user: " + user + ", result: " + testLogin);

                    if (testLogin) {
                        responseData.add("SUCCESS");
                        authUsers.put(clientId, user);  // SAVE INFO ABOUT CLIENT: IP-ADDRESS:PORT, USER-NAME
                    } else {
                        responseData.add("FAIL");
                    }
                    
                } else if (command.equals("logout")) {  
                    
                    boolean result = logout(clientId, request.getCookieUser());
                    System.out.println(clientId + " - " + request.getCookieUser() + " : logout, result: " + result);
                    if (result) {
                        responseData.add("SUCCESS");
                    }
                    else{
                        responseData.add("FAIL");
                    }
                    
                } else if (command.equals("getHistoryByLName")) {
                    String lName = request.getData().get(0);
                    System.out.print(clientId + " - " + request.getCookieUser() + " : request history by last name: " + lName + ", result: ");

                    boolean checkAuthUser = checkAuth(clientId, request.getCookieUser());
                    if (checkAuthUser) {
                        responseData = stub.getHistory(lName);
                        System.out.println("sent");
                    } else {
                        response.setError("FAIL AUTHENTICATION");
                        System.out.println("FAIL AUTHENTICATION");
                    }

                } else if (command.equals("getHistoryByCode")) {
                    String code = request.getData().get(0);
                    System.out.print(clientId + " - " + request.getCookieUser() + " : request history by code: " + code + ", result: ");

                    boolean checkAuthUser = checkAuth(clientId, request.getCookieUser());
                    if (checkAuthUser) {
                        responseData = stub.getHistory(Integer.valueOf(code));
                        System.out.println("sent");
                    } else {
                        response.setError("FAIL AUTHENTICATION");
                        System.out.println("FAIL AUTHENTICATION");
                    }

                }

                // UDP-SERVER : SEND PACKET TO THE UDP-CLIENT ( = Task6Client )
                try {

                    DatagramSocket udpSocket = new DatagramSocket(PORT_SERVER_OUT);

                    response.setCommand(request.getCommand());
                    response.setData(responseData);

                    byte[] buffer = serializeEnvelope(response);

                    DatagramPacket udpPacket = new DatagramPacket(buffer, buffer.length);
                    udpPacket.setAddress(clientAddr);   //InetAddress.getByName("localhost"));
                    udpPacket.setPort(clientPort);
                    udpSocket.send(udpPacket);
//                    System.out.println("Message sent");
                    udpSocket.close();

                } catch (SocketException ex) {
                    System.out.println(ex.getMessage());
                } catch (UnknownHostException ex) {
                    System.out.print(ex.getMessage());
                } catch (IOException ex) {
                    System.out.println(ex.getMessage());
                }

            } // while

            // TESTING STUB
            /*
            boolean testLogin;
            testLogin = stub.testLogin("ninok", "9999");
            System.out.println(testLogin);
            System.out.println("---------------");
            ArrayList<String> history1 = stub.getHistory("starynina");
            for (String s : history1) {
            System.out.println(s);
            }
            System.out.println("---------------");
            ArrayList<String> history2 = stub.getHistory(2);
            for (String s : history2) {
            System.out.println(s);
            }
             */
        } catch (RemoteException ex) {
            System.out.println(ex.getMessage());
        } catch (NotBoundException ex) {
            System.out.println(ex.getMessage());
        }

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

    private static boolean checkAuth(String clientId, String cookieUser) {

        if (authUsers.containsKey(clientId)) {
            if (authUsers.get(clientId).equals(cookieUser)) {
                return true;
            }
        }
        return false;
    }
    
    private static boolean logout(String clientId, String cookieUser){
        
        if (authUsers.containsKey(clientId)) {
            String user = authUsers.get(clientId);
            if (user.equals(cookieUser)) {
                authUsers.remove(clientId, cookieUser);
                return true;
            }
        }
        
        
        return false;
    }

}
