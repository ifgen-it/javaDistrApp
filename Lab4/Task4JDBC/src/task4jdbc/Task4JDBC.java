/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package task4jdbc;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Scanner;

/**
 *
 * @author evgsmi
 */
public class Task4JDBC {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {

        // CONNECTION
        String driverClass = "org.apache.derby.jdbc.ClientDriver";
        try {
            Class.forName(driverClass);
            System.out.println("Driver class connected..");
        } catch (ClassNotFoundException ex) {
            System.out.println("Driver class error: " + ex.getMessage());
        }

        String dbUrl = "jdbc:derby://localhost:1527/task4db";

        try (Connection con = DriverManager.getConnection(dbUrl)) {
            
            System.out.println("Was connected to database..");

            // CREATE TABLE
            boolean tableUsersExists = tableExists("app", "users", con);
            boolean tableRegistrationExists = tableExists("app", "registration", con);

            boolean seqUsersIdExists = sequenceExists("seq_users_id", con);
            boolean seqRegCodeExists = sequenceExists("seq_registration_code", con);

            String sqlCreateUsers = "create table users (\n"
                    + "id integer,\n"
                    + "login char(8) unique,\n"
                    + "constraint pk_users_id primary key(id)\n"
                    + ")";

            String sqlCreateRegistratioin = "create table registration (\n"
                    + "code integer primary key,\n"
                    + "id integer,\n"
                    + "role varchar(16),\n"
                    + "date timestamp,\n"
                    + "constraint fk_users_id foreign key (id) references users(id)\n"
                    + ")";

            if (!tableUsersExists) {
                Statement st = con.createStatement();
                boolean res = st.execute(sqlCreateUsers);
                if (res == false) {
                    System.out.println("Table USERS was created");
                }

                // DELETE SEQUENCE
                if (seqUsersIdExists) {
                    String sqlDelete = "drop sequence seq_users_id restrict";
                    st.execute(sqlDelete);
                    System.out.println("sequence seq_users_id was deleted");
                }

                // CREATE SEQUENCE
                String sqlCreate = "create sequence seq_users_id start with 1\n"
                        + "minvalue 1 cycle";
                st.execute(sqlCreate);
                System.out.println("sequence seq_users_id was created");

                st.close();
            } else {
                System.out.println("Table USERS already exists");
            }

            if (!tableRegistrationExists) {
                Statement st = con.createStatement();
                boolean res = st.execute(sqlCreateRegistratioin);
                if (res == false) {
                    System.out.println("Table REGISTRATION was created");
                }

                // DELETE SEQUENCE
                if (seqRegCodeExists) {
                    String sqlDelete = "drop sequence seq_registration_code restrict";
                    st.execute(sqlDelete);
                    System.out.println("sequence seq_registration_code was deleted");
                }

                // CREATE SEQUENCE
                String sqlCreate = "create sequence seq_registration_code start with 101\n"
                        + "minvalue 101 cycle";
                st.execute(sqlCreate);
                System.out.println("sequence seq_registration_code was created");

                st.close();
            } else {
                System.out.println("Table REGISTRATION already exists");
            }

            System.out.println("\n/////////////////////////////////");
            System.out.println("//    Working with database    //");
            System.out.println("/////////////////////////////////");
            // LOAD FROM FILE
            loadFromFile("data.log", con);

            // SAVE TO FILE
            saveToFile("save.log", con);

            // SELECT USERS REGISTERED AFTER SPECIFIED DATE
            String strLowBoundExclusiveDate = "16.09.2012 10:31:46";
            selectUsersRegAfterDate(strLowBoundExclusiveDate, con);

            // SELECT USERS REGISTERED WITH DIFFERENT ROLES
            selectUsersRegDiffRoles(con);

            // SELECT DATES WHEN MORE THAT 2 USERS WAS REGISTERED
            selectDatesRegDiffUsers(con);

            
            // CLOSE RESOURCES
            System.out.println("\nConnection closed..");

        } catch (SQLException ex) {
            System.out.println("SQL exception: " + ex.getMessage());
        }
       

    }

    private static boolean tableExists(String schemaName, String tableName, Connection con) {
        boolean result = false;
        ResultSet rs = null;
        try {
            schemaName = schemaName.toUpperCase();
            tableName = tableName.toUpperCase();
            DatabaseMetaData dbmd = con.getMetaData();

            rs = dbmd.getTables(null, null, null, null);
            while (rs.next()) {
                String table = rs.getString(3);
                String schema = rs.getString(2);
                //System.out.println("schema: " + schema + " table: " + table);
                if (schema.equals(schemaName) && table.equals(tableName)) {
                    //System.out.println("good");
                    result = true;
                    break;
                }
            }
        } catch (SQLException ex) {
            System.out.println("SQL err: " + ex.getMessage());
        } finally {
            try {
                rs.close();
            } catch (SQLException ex) {
                System.out.println("ResultSet err: " + ex.getMessage());
            }
        }
        return result;
    }

    private static boolean sequenceExists(String seqName, Connection con) {
        boolean result = false;
        seqName = seqName.toUpperCase();

        String sql = "select sequencename from sys.syssequences\n"
                + "where sequencename like '" + seqName + "'";
        try {
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery(sql);

            if (rs.next()) {
                String seq = rs.getString(1);
                System.out.println("Sequence found : seq: " + seq);
                result = true;
            } else {
                System.out.println("Sequence not found");
                result = false;
            }

        } catch (SQLException ex) {
            System.out.println("sequenceExist method err: " + ex.getMessage());
        }

        return result;
    }

    private static ArrayList<String> readFile(String path) {

        ArrayList<String> inputs = new ArrayList<>();
        File file = new File(path);
        try {
            Scanner sc = new Scanner(file);
            System.out.println("\nFile 'data.log':");
            System.out.println("-------------------------------------");
            while (sc.hasNextLine()) {
                String line = sc.nextLine();
                if (line.trim().equals("")) {
                    continue;
                }
                System.out.println(line);
                inputs.add(line);
            }

        } catch (FileNotFoundException ex) {
            System.out.println("err: " + ex.getMessage());
        }
        return inputs;
    }

    private static void insertIntoUsers(Record record, Connection con) {

        try {

            String login = record.getLogin();
            int idUser = getUserId(login, con);
            String id = "next value for seq_users_id";
            if (idUser != -1) {
                System.out.println("Users : user exists");
                return;
            }

            Statement st1 = con.createStatement();
            String sqlUsersAddRow = "insert into users(id, login) "
                    + "values(" + id + ", '" + login + "')";
            int x = st1.executeUpdate(sqlUsersAddRow);
            System.out.println("Users : Inserted rows: " + x);
            st1.close();

        } catch (SQLException ex) {
            System.out.println("insertIntoUsers method err: " + ex.getMessage());
        }
    }

    private static void insertIntoRegistration(Record record, Connection con) {

        try {

            String idReg = Integer.toString(getUserId(record.getLogin(), con));
            String code = "next value for seq_registration_code";
            String date = record.getDate().toString();
            String role = record.getRole();

            String sqlRegAddRow = "insert into registration(code, id, role, date)\n"
                    + "values(" + code + ", " + idReg + ", '" + role + "', '" + date + "')";

            Statement st2 = con.createStatement();
            int y = st2.executeUpdate(sqlRegAddRow);
            System.out.println("Registration : Inserted rows: " + y);
            st2.close();

        } catch (SQLException ex) {
            System.out.println("insertIntoRegistration err: " + ex.getMessage());
        }
    }

    private static int getUserId(String login, Connection con) {

        String sql = "select id, login from users\n"
                + "where login like '" + login + "%'";

        try {
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery(sql);

            while (rs.next()) {
                String userLogin = rs.getString("login").trim();
                int userId = rs.getInt("id");

                if (userLogin.equals(login)) {
                    return userId;
                }
            }

        } catch (SQLException ex) {
            System.out.println("getUserId err: " + ex.getMessage());
        }

        return -1;
    }

    private static void saveToFile(String saveFile, Connection con) {

        File file = new File(saveFile);
        try {
            try (FileWriter fileWriter = new FileWriter(file);
                    BufferedWriter bw = new BufferedWriter(fileWriter)) {

                System.out.println("\nSave database to the file '" + saveFile + "':");
                System.out.println("--------------------------------------");
                String sqlGetAll = "select u.login, r.role, r.date\n"
                        + "from registration r\n"
                        + "join users u\n"
                        + "on (r.id = u.id)";
                Statement st = con.createStatement();
                ResultSet rs = st.executeQuery(sqlGetAll);

                boolean firstLine = true;
                while (rs.next()) {

                    if (firstLine == false) {
                        bw.newLine();
                    } else {
                        firstLine = false;
                    }
                    String login = rs.getString("login").trim();
                    String role = rs.getString("role");
                    Timestamp tsDate = rs.getTimestamp("date");

                    DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
                    Date date1 = new Date(tsDate.getTime());

                    String date = dateFormat.format(date1);

                    System.out.println(login + " " + role + " " + date);
                    bw.append(login + " " + role + " " + date);

                }
            }

        } catch (IOException ex) {
            System.out.println("SaveToFile err: " + ex.getMessage());
        } catch (SQLException ex) {
            System.out.println("Sql err: " + ex.getMessage());
        }
    }

    private static void loadFromFile(String loadFile, Connection con) {

        ArrayList<String> inputs = readFile(loadFile);

        System.out.println("\nLoad database from the file '" + loadFile + "':");
        System.out.println("--------------------------------------");

        inputs.forEach((input) -> {
            System.out.println(">" + input);
            Record record = new Record(input);
            insertIntoUsers(record, con);
            insertIntoRegistration(record, con);
            System.out.println("--------------------------------------");
        });
    }

    private static void selectUsersRegAfterDate(String strLowBoundExclusiveDate, Connection con) {

        try {

            strLowBoundExclusiveDate = strLowBoundExclusiveDate.trim();
            DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
            Date date1 = new Date();
            try {
                date1 = dateFormat.parse(strLowBoundExclusiveDate);
            } catch (ParseException ex) {
                System.out.println("lowBoundExclusiveDate format err: " + ex.getMessage());
            }

            Timestamp tsLowBoundExclusiveDate = new Timestamp(date1.getTime());

            String sqlUsersRegAfterDate = "select login\n"
                    + "from registration\n"
                    + "join users\n"
                    + "using (id)\n"
                    + "where date > ?\n"
                    + "order by login";
            PreparedStatement pst = con.prepareStatement(sqlUsersRegAfterDate);
            pst.setTimestamp(1, tsLowBoundExclusiveDate);
            ResultSet rs = pst.executeQuery();

            System.out.println("\nUsers was registered after date " + strLowBoundExclusiveDate + ":");
            System.out.println("--------------------------------------");

            while (rs.next()) {
                String user = rs.getString("login");
                System.out.println(user);
            }
        } catch (SQLException ex) {
            System.out.println("selectUsersRegAfterDate err: " + ex.getMessage());
        }
    }

    private static void selectUsersRegDiffRoles(Connection con) {

        String sqlDiffRoles = "select distinct u.login\n"
                + "from registration r1\n"
                + "join registration r2\n"
                + "on (r1.id = r2.id)\n"
                + "join users u\n"
                + "on (r1.id = u.id)\n"
                + "where r1.role <> r2.role\n"
                + "order by u.login";

        try {
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery(sqlDiffRoles);

            System.out.println("\nUsers was registered with different roles:");
            System.out.println("--------------------------------------");

            while (rs.next()) {
                String user = rs.getString(1);
                System.out.println(user);
            }

        } catch (SQLException ex) {
            System.out.println("selectUsersRegDiffRoles err: " + ex.getMessage());
        }

    }

    private static void selectDatesRegDiffUsers(Connection con) {

        String sqlDiffDates = "select distinct cast (r1.date as DATE) onlydate\n"
                + "from registration r1\n"
                + "join registration r2\n"
                + "on (cast (r1.date as DATE) = cast (r2.date as DATE))\n"
                + "where r1.id <> r2.id";

        try {
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery(sqlDiffDates);

            System.out.println("\nDates when more than 2 users was registered:");
            System.out.println("--------------------------------------");

            while (rs.next()) {

                Date date = rs.getDate(1);
                System.out.println(date);
            }

        } catch (SQLException ex) {
            System.out.println("selectDatesRegDiffUsers err: " + ex.getMessage());
        }
    }

}
