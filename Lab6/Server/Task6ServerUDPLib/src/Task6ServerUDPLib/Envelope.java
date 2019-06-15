
package Task6ServerUDPLib;

import java.io.Serializable;
import java.util.ArrayList;

public class Envelope implements Serializable{
    
    private String command;
    private ArrayList<String> data;
    private String cookieUser;
    private String error;
    
    public Envelope(String command, ArrayList<String> data, String cookieUser){
        this.command = command;
        this.data = data;
        this.cookieUser = cookieUser;
        this.error = null;
    }
    
    public Envelope(){
        this.command = "";
        this.data = null;
        this.cookieUser = null;
        this.error = null;
    }
    
    public void setCommand(String command){
        this.command = command;
    }
    
    public void setData(ArrayList<String> data){
        this.data = data;
    }
    
    public void setError(String error){
        this.error = error;
    }
    
    public String getError(){
        return error;
    }
    
    public String getCommand(){
        return command;
    }
    
    public ArrayList<String> getData(){
        return data;
    }
    
    public String getCookieUser(){
        return cookieUser;
    }
    @Override
    public String toString(){
        String result = command + "\n" + 
                data + "\n" + 
                cookieUser;
        return result;
    }
    
}
