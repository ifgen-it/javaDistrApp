/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package libtask1;

import java.io.Serializable;
import java.util.Date;

/**
 *
 * @author ifgen
 */
public class Demo implements Serializable {
    
    public static int last;
    
    private int id;
    private String name;
    private String message;
    private Date date;
    private transient int temp;
    
    public Demo(int id, String name, String message, Date date, int temp){
        this.id = id;
        this.name = name;
        this.message = message;
        this.date = date;
        this.temp = temp;
        
        last++;
    }
    
    @Override
    public String toString(){
        return "id: " + id + "\n" +
                "name: " + name + "\n" +
                "message: " + message + "\n" +
                "date: " + date + "\n" +
                "temp: " + temp + "\n" +
                "last: " + last + "\n";   
    }
}
