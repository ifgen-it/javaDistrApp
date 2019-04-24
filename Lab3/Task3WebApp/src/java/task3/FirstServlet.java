/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package task3;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author evgsmi
 */
public class FirstServlet extends HttpServlet {

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String clientIP = request.getRemoteAddr();
        String clientMethod = request.getMethod();
        String clientQueryString = request.getQueryString();

        String clientAddr = request.getRemoteAddr();
        String clientName = request.getRemoteHost();
        String clientLogin = request.getRemoteUser();
        int clientPort = request.getRemotePort();

        String localAddr = request.getLocalAddr();
        String localName = request.getLocalName();
        int localPort = request.getLocalPort();

        String serverName = request.getServerName();
        int serverPort = request.getServerPort();

        String protocol = request.getProtocol();

        //---------------------------------------
        // response.sendError(666, "Hello user!");
        // response.sendRedirect("https://vk.com/feed");
        //----------------------------------------------
        int servletHashCode = this.hashCode();
        String servletInfo = this.getServletInfo();
        ServletContext context = this.getServletContext();

        String serverInfo = context.getServerInfo();

        ServletConfig config = this.getServletConfig();
        Enumeration<String> initParam = config.getInitParameterNames();

        this.log(new Date(System.currentTimeMillis()) + ": doGet");
        
        
        ////////////////////////////////////////////////////////////////

        // LAB
        String analyzeResult = analyzeInput(clientQueryString);

        if (analyzeResult.equals("Empty")) {
            
            sendStartPage(response);
            return;
           
        }

        //------------------------------------------
        
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */

            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet FirstServlet</title>");
            out.println("</head>");
            out.println("<body style='background: darkslategrey; color: goldenrod;"
                    + "font-size: 18px'>");
            out.println("<h2>Response from Server:</h2>");
            out.println("<p>");

            out.println("Current time: " + new Date(System.currentTimeMillis()) + "<br>");

            out.println("Client IP-address: " + clientIP + "<br>");
            out.println("Used method: " + clientMethod + "<br>");

            out.println("<div style='font-size: 22px; color: darkred; background: lightcoral; "
                    + " border: 3px solid darkred; padding: 10px ' >");
            out.println("Query string: " + clientQueryString + "<br>");
            out.println("</div>");
            
            out.println("<br>");
            
            out.println("<div style='font-size: 22px; color: darkgreen; background: springgreen; "
                    + " border: 3px solid darkgreen; padding: 10px ' >");
            out.println("Result: " + analyzeResult + "<br>");
            out.println("</div>");

            out.println("<br>");
            
            out.println("<a href=\"index.html\" "
                    + "style='font-size: 22px; color: goldenrod; ' >");
            out.println("Home page");
            out.println("</a>");
            
            out.println("<br><br>");

            out.println("Client address: " + clientAddr + "<br>");
            out.println("Client name: " + clientName + "<br>");
            out.println("Client port: " + clientPort + "<br>");
            out.println("Protocol: " + protocol + "<br>");

            out.println("<hr>");

            out.println("Local address: " + localAddr + "<br>");
            out.println("Local name: " + localName + "<br>");
            out.println("Local port: " + localPort + "<br>");
            out.println("<br>");

            out.println("Server name: " + serverName + "<br>");
            out.println("Server port: " + serverPort + "<br>");

            out.println("<hr>");

            out.println("Servlet hash code: " + servletHashCode + "<br>");
            out.println("Serlet info: " + servletInfo + "<br>");
            out.println("Server info: " + serverInfo + "<br>");
            out.println("Servlet init params: " + initParam + "<br>");

            out.println("<br>" + "end" + "<br>");

            out.println("</p>");
            out.println("</body>");
            out.println("</html>");
        }
    }

    private void sendStartPage(HttpServletResponse response) throws IOException{
        
        response.setContentType("text/html;charset=UTF-8");
            try (PrintWriter out = response.getWriter()) {
                /* TODO output your page here. You may use following sample code. */

                out.println("<!DOCTYPE html>");
                out.println("<html>");
                
                out.println("<head>");
                out.println("<title>Practice3 Web application</title>");
                out.println("<meta charset=\"UTF-8\">");
                out.println("<meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">");
                out.println("</head>");
                
                out.println("<body style=\"background: darkslategray; color: goldenrod; font-size: 18px\">");
                out.println("<div><h2>Web application</h2></div>");

                out.println("<form name=\"form1\" method=\"get\" action=\"FirstServlet\">");
                out.println("<label id=\"label1\" for=\"input1\">Text: </label>");
                out.println("<input id=\"input1\" name=\"text\" type=\"text\" placeholder=\"Input text..\" size=\"40\">");
                out.println("<input type=\"submit\" value=\"Send\">");
                out.println("</form>");
                
                out.println("<br>");
                
                out.println("<div style=\" color: orangered; \">");
                out.println("Input text required. Try once more!");
                out.println("</div>");
                
                out.println("</body>");
                out.println("</html>");
                
            }
    }
    
    
    private String analyzeInput(String text) {

        String[] inputs = text.split("=");

        if (inputs.length == 1) {
            return "Empty";
        }

        String input = inputs[1];
        String result = input.replaceAll("\\+", " ").trim();

        if (result.equals("")) {
            return "Empty";
        }

        int inputNumber;
        try {
            inputNumber = Integer.parseInt(result);
            return Integer.toString(inputNumber + 1);

        } catch (NumberFormatException ex) {
            System.out.println("NumberFormatException: " + ex.getMessage());
        }

        result = result.replaceAll("\\s+", " ");
        int wordNumber = result.split(" ").length;
        return result + "<br>Number of words: " + Integer.toString(wordNumber);

    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

    }

   
    
    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "This servlet returns useful information!";
    }// </editor-fold>

}
