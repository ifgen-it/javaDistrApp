/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package task2rmiserver;

import java.rmi.RemoteException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.function.Supplier;
import task2rmilib.DateTime;

/**
 *
 * @author ifgen
 */
public class DateTimeImpl implements DateTime {

    private static Supplier<Boolean> stopServer;
    
    @Override
    public String getDate() throws RemoteException {
        DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        Date date = new Date(System.currentTimeMillis());
        
        System.out.println(">> date was requested");
        return dateFormat.format(date);
    }

    @Override
    public String getTime() throws RemoteException {
        DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
        Date date = new Date(System.currentTimeMillis());
        
        System.out.println(">> time was requested");
        return dateFormat.format(date);
    }

    @Override
    public boolean stop() throws RemoteException {
        System.out.println(">> server stop was requested");
        
        boolean result = stopServer.get();
            if (result) {
                System.out.println("Server was stopped..");
            } else {
                System.out.println("Server was not stopped..");
            }
            
            return result;
    }
    
    public static void setMethod(Supplier<Boolean> stopServer){
        DateTimeImpl.stopServer = stopServer;
    }

}
