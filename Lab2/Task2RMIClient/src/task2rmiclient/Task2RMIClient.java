/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package task2rmiclient;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import task2rmilib.DateTime;
import java.util.*;

/**
 *
 * @author ifgen
 */
public class Task2RMIClient {

    public static final String BINDING_NAME = "datetimer";

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        try {
            System.out.println("Client started..");

            Registry registry = LocateRegistry.getRegistry(1099);
            System.out.println("Got registry..");

            DateTime stub = (DateTime) registry.lookup(BINDING_NAME);
            System.out.println("Stub created..");

            System.out.println("\nCommands:"
                    + "\ndate - get the current date"
                    + "\ntime - get the current time"
                    + "\nstop - stop the server and shutdown app\n"
                    + "\nType command and press Enter\n");
            
            while(true){
                System.out.print(">");
                Scanner sc = new Scanner(System.in);
                String command = sc.next();
                if (command.equals("date")) {
                    System.out.println(stub.getDate());
                }
                else if (command.equals("time")) {
                    System.out.println(stub.getTime());
                }
                else if (command.equals("stop")) {
                    boolean stopServer = stub.stop();
                    System.out.println(stopServer == true?
                                    "Server was stopped":
                                    "Server was not stopped");
                    System.out.println("Program was finished");
                    break;
                }
                else{
                    System.out.println("Unknown command");
                }
            }
            

        } catch (RemoteException ex) {
            System.out.println(ex.getMessage());
        } catch (NotBoundException ex) {
            System.out.println(ex.getMessage());
        }
    }

}
