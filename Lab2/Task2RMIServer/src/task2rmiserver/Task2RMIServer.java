/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package task2rmiserver;

import java.rmi.AlreadyBoundException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.function.Supplier;
import task2rmilib.DateTime;

/**
 *
 * @author ifgen
 */
public class Task2RMIServer {

    public static final String BINDING_NAME = "datetimer";
    public static Supplier<Boolean> stopServer;

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {

        try {
            System.out.println("Server started..");

            DateTime dateTimer = new DateTimeImpl();
            Registry registry = LocateRegistry.createRegistry(1099);
            System.out.println("Registry created..");

            DateTime stub = (DateTime) UnicastRemoteObject.exportObject(dateTimer, 0);
            registry.bind(BINDING_NAME, stub);
            System.out.println("Stub was bound..");

            

            // STOP SERVER METHOD
            stopServer = () -> {
                boolean result = false;
                try {
                    registry.unbind(BINDING_NAME);
                    result = UnicastRemoteObject.unexportObject(dateTimer, true);

                    if (result) {
                        System.out.println("Stub was unbound..");
                    } else {
                        System.out.println("Stub was not unbound..");
                    }

                } catch (RemoteException ex) {
                    System.out.println(ex.getMessage());
                } catch (NotBoundException ex) {
                    System.out.println(ex.getMessage());
                } finally {
                    return result;
                }
            };
            DateTimeImpl.setMethod(stopServer);
      

        } catch (RemoteException ex) {
            System.out.println(ex.getMessage());
        } catch (AlreadyBoundException ex) {
            System.out.println(ex.getMessage());
        } 

    }

}
