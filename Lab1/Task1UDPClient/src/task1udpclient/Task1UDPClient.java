/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package task1udpclient;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import static java.lang.Runtime.getRuntime;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.logging.Level;
import java.util.logging.Logger;
import libtask1.Demo;

/**
 *
 * @author ifgen
 */
public class Task1UDPClient {

    /**
     * @param args the command line arguments
     */
    public static void clrscr() {
        for (int i = 0; i < 100; i++) {
            System.out.println();
        }
    }

    public static void main(String[] args) {
        // TODO code application logic here
        int port = 15678;

        // TEST STRING-STREAM
        try {
            while (true) {
                
                DatagramSocket udpSocket = new DatagramSocket(port);
                int bufferSize = 1024;

                byte[] buffer = new byte[bufferSize];
                DatagramPacket udpPacket = new DatagramPacket(buffer, buffer.length);
                udpSocket.receive(udpPacket);
//clrscr();
                byte[] bytes = udpPacket.getData();
                String result = new String(bytes);
                System.out.println(result);

                udpSocket.close();
                

            }

        } catch (SocketException ex) {
            System.out.println(ex.getMessage());
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }

// LAB

        /*
        try {
            DatagramSocket udpSocket = new DatagramSocket(port);
            int bufferSize = 1024;
            byte[] buffer = new byte[bufferSize];

            DatagramPacket udpPacket = new DatagramPacket(buffer, buffer.length);
            udpSocket.receive(udpPacket);

            System.out.println("Got packet from "
                    + udpPacket.getAddress() + " : "
                    + udpPacket.getPort());
            System.out.println("Packet contain:");

            byte[] bytes = udpPacket.getData();
            
            // DESERIALIZATION
            ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
            
            
            try {
                ObjectInputStream ois = new ObjectInputStream(bais);
                Demo demoNew = (Demo) ois.readObject();
                System.out.println(demoNew);

            } catch (IOException ex) {
                System.out.println(ex.getMessage());
            } catch (ClassNotFoundException ex) {
                System.out.println(ex.getMessage());
            }

            udpSocket.close();

        } catch (SocketException ex) {
            System.out.println(ex.getMessage());
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
         */
    }

}
