package task6serverdb;

import java.rmi.RemoteException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import task6serverdbrmilib.Accessable;

public class DAO implements Accessable {

    private Connection con; // need to CLOSE CONNECTION SOME WHERE

    public DAO(String driverClass, String dbUrl, String dbLogin, String dbPassword) {

        System.out.println("---> Creation DataAccessObject");
        try {
            Class.forName(driverClass);
            System.out.println("Driver class connected..");
        } catch (ClassNotFoundException ex) {
            System.err.println("DAO creation: Driver class error: " + ex.getMessage());
        }

        try {
            con = DriverManager.getConnection(dbUrl, dbLogin, dbPassword);
            System.out.println("Was connected to database..");
            System.out.println("---> DAO created");

        } catch (SQLException ex) {
            System.err.println("DAO creation: SQL exception: " + ex.getMessage());
        }

    }
    
    private String getDateTime() {
        DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        Date date = new Date(System.currentTimeMillis());
        
        return dateFormat.format(date);
    }

    public void destroyDAO() {
        try {
            con.close();
        } catch (SQLException ex) {
            System.err.println("DAO destroy: SQL exception: " + ex.getMessage());
        }
    }

    public void printAllEmployeeHistory() {

        try {
            Statement st = con.createStatement();
            String sqlEmpHist = "select * from employeehistory6";
            ResultSet rs = st.executeQuery(sqlEmpHist);

            System.out.println("\nEmployee history:");
            System.out.println("------------------------------------------");
            System.out.println("id, position, manager, hire, dismiss, code");
            System.out.println("------------------------------------------");

            while (rs.next()) {
                int id = rs.getInt("id");
                String position = rs.getString("position");
                int manager = rs.getInt("manager");
                Date hire = rs.getDate("hire");
                Date dismiss = rs.getDate("dismiss");
                int code = rs.getInt("code");

                System.out.println(id + ", " + position + ", " + manager + ", " + hire + ", " + dismiss + ", " + code);
            }
            rs.close();
            st.close();
        } catch (SQLException ex) {
            System.err.println("SQL exception: " + ex.getMessage());
        }
    }

    public void printAllEmployees() {
        try {
            Statement st = con.createStatement();
            String sqlEmp = "select * from employees6";
            ResultSet rs = st.executeQuery(sqlEmp);

            System.out.println("\nEmployees:");
            System.out.println("--------------------------------");
            System.out.println("code, name, lastName, login, psw");
            System.out.println("--------------------------------");

            while (rs.next()) {
                int code = rs.getInt("code");
                String name = rs.getString("name");
                String lastName = rs.getString("last_name");
                String login = rs.getString("login");
                String psw = rs.getString("psw");

                System.out.println(code + ", " + name + ", " + lastName + ", " + login + ", " + psw);
            }
            rs.close();
            st.close();
        } catch (SQLException ex) {
            System.err.println("SQL exception: " + ex.getMessage());
        }
    }

    @Override
    public boolean testLogin(String user, String password) throws RemoteException { 
        System.out.print(getDateTime() + " testLogin user: " + user + ", result: ");
        
        boolean result = false;
        try {
            Statement st = con.createStatement();
            String sqlPsw = "select psw\n"
                    + "from employees6\n"
                    + "where login = '" + user + "'";
            ResultSet rs = st.executeQuery(sqlPsw);

            int userCounter = 0;
            String dbPassword = null;
            while (rs.next()) {
                dbPassword = rs.getString("psw");
                userCounter++;
            }
            if (userCounter > 1) {
                System.err.println("User login is not unique");
            }
            if (dbPassword != null && dbPassword.equals(password)) {
                result = true;
            }

            rs.close();
            st.close();
        } catch (SQLException ex) {
            System.err.println("SQL exception: " + ex.getMessage());
        }
        
        System.out.println(result);
        return result;
    }

    @Override
    public ArrayList<String> getHistory(String lName) throws RemoteException { 
        System.out.println(getDateTime() + " getHistory by lastName: " + lName);
        
        ArrayList<String> result = null;
        try {
            Statement st = con.createStatement();
            String sql = "select id, position, manager, hire, dismiss, code, name, last_name\n"
                    + "from employeehistory6\n"
                    + "join employees6\n"
                    + "using (code)\n"
                    + "where last_name = '" + lName + "'\n"
                    + "order by id";
            
            ResultSet rs = st.executeQuery(sql);

            int i = 0;
            while (rs.next()) {
                if(i == 0){
                    result = new ArrayList<>();
                    i++;
                }
                
                int id = rs.getInt("id");
                String position = rs.getString("position");
                int manager = rs.getInt("manager");
                Date hire = rs.getDate("hire");
                Date dismiss = rs.getDate("dismiss");
                int code = rs.getInt("code");
                String name = rs.getString("name");
                String lastName = rs.getString("last_name");
                
                String datePattern = "dd-MM-yyyy";
                DateFormat df = new SimpleDateFormat(datePattern);
                String strHire = df.format(hire);
                String strDismiss = df.format(dismiss);
                
                String row = Integer.toString(id) + " " +
                        position + " " + Integer.toString(manager) + " " + 
                        strHire + " "  + strDismiss + " " + Integer.toString(code) + " " +
                        name + " " + lastName;
                
                result.add(row);
            }
            
            rs.close();
            st.close();
        } catch (SQLException ex) {
            System.err.println("SQL exception: " + ex.getMessage());
        }

        return result;
    }

    @Override
    public ArrayList<String> getHistory(int userCode) throws RemoteException { 
        System.out.println(getDateTime() + " getHistory by userCode: " + userCode);
        
        ArrayList<String> result = null;
        try {
            Statement st = con.createStatement();
            String sql = "select id, position, manager, hire, dismiss, code, name, last_name\n"
                    + "from employeehistory6\n"
                    + "join employees6\n"
                    + "using (code)\n"
                    + "where code = '" + userCode + "'\n"
                    + "order by id";
            
            ResultSet rs = st.executeQuery(sql);

            int i = 0;
            while (rs.next()) {
                if(i == 0){
                    result = new ArrayList<>();
                    i++;
                }
                
                int id = rs.getInt("id");
                String position = rs.getString("position");
                int manager = rs.getInt("manager");
                Date hire = rs.getDate("hire");
                Date dismiss = rs.getDate("dismiss");
                int code = rs.getInt("code");
                String name = rs.getString("name");
                String lastName = rs.getString("last_name");
                
                String datePattern = "dd-MM-yyyy";
                DateFormat df = new SimpleDateFormat(datePattern);
                String strHire = df.format(hire);
                String strDismiss = df.format(dismiss);
                
                String row = Integer.toString(id) + " " +
                        position + " " + Integer.toString(manager) + " " + 
                        strHire + " "  + strDismiss + " " + Integer.toString(code) + " " +
                        name + " " + lastName;
                
                result.add(row);
            }
            
            rs.close();
            st.close();
        } catch (SQLException ex) {
            System.err.println("SQL exception: " + ex.getMessage());
        }

        return result;
    }

}
