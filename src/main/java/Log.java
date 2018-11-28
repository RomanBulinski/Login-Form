import DAO.NamePasswordDAO;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.*;
import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

public class Log implements HttpHandler {

    NamePasswordDAO namePasswordDAO = new NamePasswordDAO();
    int counter = 0;
    String currentTime= null;

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {

        String response = "";
        String method = httpExchange.getRequestMethod();
//         Send a form if it wasn't submitted yet.
        if(method.equals("GET")){
            response = "<html><body>" +
                    "<h1>Sign in</h1>" +
                    "<form method=\"POST\">\n" +

                    "  User Name:<br>\n" +
                    "  <input type=\"text\" name=\"firstname\" value=\"\">\n" +
                    "  <br>\n" +

                    "  Password:<br>\n" +
                    "  <input type=\"text\" name=\"password\" value=\"\">\n" +
                    "  <br><br>\n" +

                    "  <input type=\"submit\" value=\"LOG IN\">\n" +
                    "</form> " +
                    "</body></html>";
        }
        // If the form was submitted, retrieve it's content.
        if (method.equals("POST")) {
            InputStreamReader inputStreamReader = new InputStreamReader(httpExchange.getRequestBody(), "utf-8");
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            String formData = bufferedReader.readLine();
            System.out.println(formData);
            Map inputs = parseFormData(formData);
            String name = (String)inputs.get("firstname");
            String password = (String)inputs.get("password");

//            SimpleDateFormat time_formatter = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss.SSS");
//            time = System.currentTimeMillis();
//            currentTime = time_formatter.format(System.currentTimeMillis());
//            counter++;

            if ( namePasswordDAO.checkNamePassword(name,password) ) {

                String message = "Hello : " + inputs.get("firstname") + " !!!" + "<br>";
                response = "<html><body>" +
                        "<h4>" + message + "</h4>" +
                        "<form method=\"POST\">\n" +
                        "  <input type=\"submit\" value=\"LOG OUT\">\n" +
                        "</form> " +
                        "</body><html>";





            }else{
                String attentionMessage = " Wrong name or password !!!<br>Try again. ";
                response = "<html><body>" +
                        "<h1>Sign in</h1>" +
                        "<h4>" + attentionMessage + "</h4>" +
                        "<form method=\"POST\">\n" +

                        "  User Name:<br>\n" +
                        "  <input type=\"text\" name=\"firstname\" value=\"...\">\n" +
                        "  <br>\n" +

                        "  Password:<br>\n" +
                        "  <input type=\"text\" name=\"password\" value=\"...\">\n" +
                        "  <br><br>\n" +

                        "  <input type=\"submit\" value=\"LOG IN\">\n" +
                        "</form> " +
                        "</body></html>";
            }
        }
        httpExchange.sendResponseHeaders(200, response.length());
        OutputStream outputStream = httpExchange.getResponseBody();
        outputStream.write(response.getBytes());
        outputStream.close();
    }

    private static Map<String, String> parseFormData(String formData) throws UnsupportedEncodingException {
        Map<String, String> map = new HashMap<>();
        String[] pairs = formData.split("&");
        for(String pair : pairs){
            String[] keyValue = pair.split("=");
            // We have to decode the value because it's urlencoded. see: https://en.wikipedia.org/wiki/POST_(HTTP)#Use_for_submitting_web_forms
            String value = new URLDecoder().decode(keyValue[1], "UTF-8");
            map.put(keyValue[0], value);
        }
        return map;
    }



}