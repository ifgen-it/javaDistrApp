/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package task1udpserver;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Date;
import java.util.Random;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import libtask1.Demo;

/**
 *
 * @author ifgen
 */
public class Task1UDPServer {

    /**
     * @param args the command line arguments
     */
    private static void udpSentString(String text) {

        try {
            int port = 15679;
            int portClient = 15678;

            DatagramSocket udpSocket = new DatagramSocket(port);
            //int bufferSize = 1024;

            String message = text;
            byte[] buffer = message.getBytes();

            DatagramPacket udpPacket = new DatagramPacket(buffer, buffer.length);
            udpPacket.setAddress(InetAddress.getByName("192.168.19.255")); 
            // <- this mask for IP: 192.168.16.66  and subNet mask: 255.255.252.0 //("localhost"));
            udpPacket.setPort(portClient);
            udpSocket.send(udpPacket);

            udpSocket.close();

        } catch (SocketException ex) {
            System.out.println(ex.getMessage());
        } catch (UnknownHostException ex) {
            System.out.print(ex.getMessage());
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }

    public static void clrscr() {
        for (int i = 0; i < 100; i++) {
            System.out.println();
        }
    }

    public static void main(String[] args) {
        // TODO code application logic here
        int port = 15679;
        int portClient = 15678;

        // IT IS TEST STRING-STREAM #1
        /*
        try {

            String str = "<<< JAVA DISTRIBUTED APPLICATION <<<                   ";

            while (true) {
                String str1 = str.substring(1, str.length());
                String str2 = str.substring(0, 1);
                str = str1.concat(str2);
clrscr();
                System.out.println(str);
                udpSentString(str);
                Thread.sleep(100);
            }

        } catch (InterruptedException ex) {
            System.out.println(ex.getMessage());
        }
         */
        // IT IS TEST STRING-STREAM #2
        try {
                                        
            String sym = "JAVA";
            int max = 70;
            int min = 10;
            int cur1 = 20;
            int cur2 = 40;
            Random r = new Random();
            while (true) {
                
                // MOVE
                
              //  Random r = new Random();
                int step1 = r.nextInt(3) - 1;
                int step2 = r.nextInt(3) - 1;
                cur1 += step1;
                cur2 += step2;
                if (cur1 < min) {
                    cur1++;
                }
                if (cur1 > max) {
                    cur1--;
                }
                if (cur2 < min) {
                    cur2++;
                }
                if (cur2 > max) {
                    cur2--;
                }
                
                int curMin=0, curMax=0;
                if(cur1<cur2){
                    curMin = cur1; curMax = cur2;
                } else{
                    curMin = cur2; curMax = cur1;
                }
                int delta = curMax - curMin;
                StringBuilder sb = new StringBuilder();
                for (int i = 0; i < curMin; i++) {
                    sb.append(" ");
                }
                sb.append(sym);
                for (int i = 0; i < delta; i++) {
                    sb.append(" ");
                }
                sb.append(sym);
                String str = sb.toString();
                
                // DRAW
                    //clrscr();
              // System.out.println(str);
                udpSentString(str);
                Thread.sleep(33);
            }

        } catch (InterruptedException ex) {
            System.out.println(ex.getMessage());
        }

        // IT IS LAB
        /*
            try {
            DatagramSocket udpSocket = new DatagramSocket(port);
            int bufferSize = 1024;
            //String message = "UDP test message";
            //byte[] buffer = message.getBytes();

            Date date = new Date(System.currentTimeMillis());
            Demo d = new Demo(66, "Evgen", "Hello users, UDP works!!", date, 512);

            System.out.println(d);
            // SERIALIZATION

            ByteArrayOutputStream baos = new ByteArrayOutputStream(bufferSize);

            try {
            ObjectOutputStream oos = new ObjectOutputStream(baos);
            oos.writeObject(d);
            
            } catch (IOException ex) {
            System.out.println(ex.getMessage());
            }

            System.out.println(baos.toString());
            byte[] buffer = baos.toByteArray();

            DatagramPacket udpPacket = new DatagramPacket(buffer, buffer.length);
            udpPacket.setAddress(InetAddress.getByName("localhost"));
            udpPacket.setPort(portClient);
            udpSocket.send(udpPacket);
            System.out.println("Message sent");
            udpSocket.close();
            
            } catch (SocketException ex) {
            System.out.println(ex.getMessage());
            } catch (UnknownHostException ex) {
            System.out.print(ex.getMessage());
            } catch (IOException ex) {
            System.out.println(ex.getMessage());
            }
         */
// IT IS SERIALIZATION TEST
/*
Date date = new Date(System.currentTimeMillis());
Demo d = new Demo(66, "Evgen", "Hello users, UDP works!!", date, 512);

System.out.println(d);
// SERIALIZATION

ByteArrayOutputStream baos = new ByteArrayOutputStream(1024);

try {
ObjectOutputStream oos = new ObjectOutputStream(baos);
oos.writeObject(d);

} catch (IOException ex) {
System.out.println(ex.getMessage());
}

System.out.println(baos.toString());
byte[] bytes = baos.toByteArray();
// DESERIALIZATION
ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
try {
ObjectInputStream ois = new ObjectInputStream(bais);
Demo demoNew = (Demo)ois.readObject();
System.out.println(demoNew);

} catch (IOException ex) {
System.out.println(ex.getMessage());
} catch (ClassNotFoundException ex) {
System.out.println(ex.getMessage());
}
         */
    }

}
