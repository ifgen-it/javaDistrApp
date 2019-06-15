
package task6serverdbrmilib;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;

public interface Accessable extends Remote{
    
    boolean testLogin(String user, String password) throws RemoteException;
    
    ArrayList<String> getHistory(String name) throws RemoteException;
    
    ArrayList<String> getHistory(int code) throws RemoteException;
}
