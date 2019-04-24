/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package task4jdbc;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *
 * @author ifgen
 */
public class Record {

    private String login;
    private String role;
    private final Timestamp date;

    public Record(String input) {

        String[] inputAr = input.split(" ", 3);
        login = inputAr[0].trim();
        role = inputAr[1].trim();
        String inputDate = inputAr[2];
        DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
        Date date1 = new Date();
        try {
            date1 = dateFormat.parse(inputDate);
        } catch (ParseException ex) {
            System.out.println("dateFormat err: " + ex.getMessage());
        }
        date = new Timestamp(date1.getTime());
    }

    String getLogin() {
        return login;
    }

    String getRole() {
        return role;
    }

    Timestamp getDate() {
        return date;
    }
}
