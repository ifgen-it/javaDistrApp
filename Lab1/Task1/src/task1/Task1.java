/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package task1;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author evgsmi
 */
public class Task1 {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args){
        try {
            // TODO code application logic here
            String path = null;
            
            // path = "https://ru.wikipedia.org/wiki/Java";
            // path = "http://model.exponenta.ru/";
            // path = "https://drive.google.com/drive/u/1/folders/1rONVkFzhiAkrtRM4c1CzQDdBNPagSvO2";
            
            System.out.println("Input website address:");
            Scanner sc = new Scanner(System.in);
            path = sc.next();
            
            System.out.println("=================================================================");
            
            URL url = new URL(path);
            
            InputStream is = url.openStream();
            
            StringBuilder sb = new StringBuilder();
            String line = null;
            String chSet = "UTF-8";
            //String chSet = "windows-1251";
            
            try(BufferedReader bf = new BufferedReader(new InputStreamReader(is, chSet))){
                while((line = bf.readLine()) != null){
                    sb.append(line);
                    sb.append("\n");
                }
            }
            
           String text = sb.toString();
           
            // GET CHARSET
           // System.out.println(text);
            String regExChSet = "charset( )?=( )?\"?((\\w)|(-))+\"?(( )|(/)|(;))";
            Pattern ptChSet = Pattern.compile(regExChSet, Pattern.DOTALL);
            Matcher mChSet = ptChSet.matcher(text);
            mChSet.find();
            
            String resChSet = mChSet.group();
           // System.out.println(resChSet);
            
             // GET PURE CHARSET
           // System.out.println(text);
            regExChSet = "(^(\\w|( )|=)+?\")|(\"(.)*$)";
            ptChSet = Pattern.compile(regExChSet, Pattern.DOTALL);
            mChSet = ptChSet.matcher(resChSet);
           
            String resChSet1 = mChSet.replaceAll("");
            String siteCharSet = resChSet1;
            //System.out.println(resChSet1);
            
            // REOPEN STREAM
            is = url.openStream();
            sb = new StringBuilder();
            line = null;
            try(BufferedReader bf = new BufferedReader(new InputStreamReader(is, siteCharSet))){
                while((line = bf.readLine()) != null){
                    sb.append(line);
                    sb.append("\n");
                }
            }
           String rightText = sb.toString();
           //System.out.println(rightText);
           
           text = rightText;
            
           // =====  GET BODY
            
            String regEx = "<body.*>.*<.*body>";
            Pattern pt = Pattern.compile(regEx, Pattern.DOTALL);
            Matcher m = pt.matcher(text);
            m.find();
            
            //System.out.println("=========================");
            String res = m.group();
            //System.out.println(res);
            
            // ======== CUT OFF BODY
            regEx = "(<body.*?>)|(</body>)";
            pt = Pattern.compile(regEx, Pattern.DOTALL);
            m = pt.matcher(res);
            String res1 = m.replaceAll("");
            
           // System.out.println("=========================");
            
            // ======== CUT OFF <!--  comments -->
            regEx = "<!--.*?-->";
            pt = Pattern.compile(regEx, Pattern.DOTALL);
            m = pt.matcher(res1);
            String res2 = m.replaceAll("");
            
           // System.out.println("=========================");
            
            // ======== CUT OFF scripts
            regEx = "<script.*?>.*?<.*?script>";
            pt = Pattern.compile(regEx, Pattern.DOTALL);
            m = pt.matcher(res2);
            String res3 = m.replaceAll("");
            
           // System.out.println("=========================");

            //System.out.println(res3);
            
            // ======== CUT OFF style
            regEx = "<style.*?>.*?<.*?style>";
            pt = Pattern.compile(regEx, Pattern.DOTALL);
            m = pt.matcher(res3);
            String res4 = m.replaceAll("");
            
            //System.out.println("=========================");
            //System.out.println(res4);
            
            // ======== CUT OFF all tags
            regEx = "(<\\w+?.*?>)|(<(/)?\\w+?>)";
            pt = Pattern.compile(regEx, Pattern.DOTALL);
            m = pt.matcher(res4);
            String res5 = m.replaceAll("");
            
           // System.out.println("=========================");
           // System.out.println(res5);
            
             // ======== CUT OFF spec symbols
            regEx = "&.+?;";  
            pt = Pattern.compile(regEx, Pattern.DOTALL);
            m = pt.matcher(res5);
            String res6 = m.replaceAll("");
            
          //  System.out.println("=========================");
            //System.out.println(res6);
            
             // ======== CUT OFF spaces
            regEx = "([( )\\t\\x0B\\f])++"; 
            
            //regEx = "(\\s){2,}";  
            pt = Pattern.compile(regEx, Pattern.DOTALL);
            m = pt.matcher(res6);
            String res7 = m.replaceAll(" ");
            
          //  System.out.println("=========================");
           // System.out.println(res7);
            
             // ======== CUT OFF enters
           // regEx = "((( )\\n)++)|((\n)+)";  
            //regEx = "((\\n\\r)|(\\r\\n)|\\n|\\r)+";  
            regEx = "(( )\\n( ))";  
            
            pt = Pattern.compile(regEx, Pattern.DOTALL);
            m = pt.matcher(res7);
            String res8 = m.replaceAll("\n");
            
           // System.out.println("=========================");
           // System.out.println(res8);
            
             // ======== CUT OFF enters
           // regEx = "((( )\\n)++)|((\n)+)";  
            //regEx = "((\\n\\r)|(\\r\\n)|\\n|\\r)+";  
            regEx = "(\\n){3,}";  
            
            pt = Pattern.compile(regEx, Pattern.DOTALL);
            m = pt.matcher(res8);
            String res9 = m.replaceAll("\n\n").trim();
            
           // System.out.println("=========================");
            System.out.println(res9);
            
            
            
            
            
           // System.out.println(text);
            
            
            
            
        } catch (MalformedURLException ex) {
            System.out.println(ex.getMessage());
        }
        catch(IOException ex){
            System.out.println(ex.getMessage());
        }
        
        
    }
    
}
