package task6serverdb;

import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import task6serverdbrmilib.Accessable;

public class Task6ServerDB {

    public static final String BINDING_NAME = "DataAccessObject";
    
    public static void main(String[] args) {

        // CONNECTION
        // ****
        // ****
        // oraclebi.avalon.ru
        // 1521
        // orcl12
        String driverClass = "oracle.jdbc.OracleDriver";
        //String driverClass = "org.apache.derby.jdbc.ClientDriver";
        String dbUrl = "jdbc:oracle:thin:@oraclebi.avalon.ru:1521:orcl12";
        //String dbUrl = "jdbc:derby://localhost:1527/task6db";
        String dbLogin = "******";
        String dbPassword = "******";

        // START RMI SERVER
        
        try {
            System.out.println("RMI Server started..");

            Accessable dao = new DAO(driverClass, dbUrl, dbLogin, dbPassword);
            Registry registry = LocateRegistry.createRegistry(1099);
            System.out.println("Registry created..");
            
            Accessable stub = (Accessable) UnicastRemoteObject.exportObject(dao, 0);
            registry.bind(BINDING_NAME, stub);
            System.out.println("Stub was bound..");
            
            
        
        } catch (RemoteException ex) {
            System.out.println(ex.getMessage());
        }catch (AlreadyBoundException ex) {
            System.out.println(ex.getMessage());
        }
        
        
        // TESTING DAO FUNCTIONALITY
//        dao.printAllEmployeeHistory();
//        dao.printAllEmployees();
        /*try {
        boolean testLogin;
        testLogin = dao.testLogin("ninok", "9999");
        System.out.println(testLogin);
        System.out.println("---------------");
        ArrayList<String> history1 = dao.getHistory("starynina");
        for (String s : history1) {
        System.out.println(s);
        }
        System.out.println("---------------");
        ArrayList<String> history2 = dao.getHistory(2);
        for (String s : history2) {
        System.out.println(s);
        }
        } catch (RemoteException ex) {
        System.out.println(ex);
        }*/
        
        // TESTING DAO FUNCTIONALITY
        
//        dao.printAllEmployeeHistory();
//        dao.printAllEmployees();
        /*try {

            boolean testLogin;
            testLogin = dao.testLogin("ninok", "9999");
            System.out.println(testLogin);

            System.out.println("---------------");
            ArrayList<String> history1 = dao.getHistory("starynina");
            for (String s : history1) {
                System.out.println(s);
            }

            System.out.println("---------------");
            ArrayList<String> history2 = dao.getHistory(2);
            for (String s : history2) {
                System.out.println(s);
            }

        } catch (RemoteException ex) {
            System.out.println(ex);
        }*/

        
        
        // CLOSE RESOURCES
        System.out.println("Main end, but registry is working now...\n");
        System.out.println("-----------------------------------------");
        System.out.println("SERVER DB LOG:");
        System.out.println("-----------------------------------------\n");
        
        //dao.destroyDAO();
    }

}
