import DAO.NamePasswordDAO;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.jtwig.JtwigModel;
import org.jtwig.JtwigTemplate;

import java.io.*;
import java.net.HttpCookie;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class Log implements HttpHandler {

    private static final String SESSION_COOKIE_NAME = "sessionId";
    CookieHelper cookieHelper = new CookieHelper();
    NamePasswordDAO namePasswordDAO = new NamePasswordDAO();
    int counter = 0;

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {

        String response = "";
        String method = httpExchange.getRequestMethod();

        // Send a form if it wasn't submitted yet.
        // get a template file
        JtwigTemplate layout = JtwigTemplate.classpathTemplate("templates/layout.twig");
        JtwigTemplate layoutMessage = JtwigTemplate.classpathTemplate("templates/layoutMessage.twig");
        JtwigTemplate layoutLogOut = JtwigTemplate.classpathTemplate("templates/layoutLogOut.twig");

        // create a model that will be passed to a template
        JtwigModel model = JtwigModel.newModel();

        if(method.equals("GET")){
            response = layout.render(model);
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
            String submitLogout = (String)inputs.get("logout");

            if( submitLogout!=null) {

                response = layout.render(model);

            } else if ( namePasswordDAO.checkNamePassword(name,password) ) {

                String helloMessage = "Hello : " + inputs.get("firstname") + " !!!" + "<br>";
                model.with("helloMessage", helloMessage);
                response = layoutLogOut.render(model);

            } else {

                String attentionMessage = " Wrong name or password !!!<br>Try again. ";
                model.with("attentionMessage", attentionMessage);
                response = layoutMessage.render(model);
            }
        }

        counter++;
        response += "Page was visited: " + counter + " times!";
        Optional<HttpCookie> cookie = getSessionIdCookie(httpExchange);

        boolean isNewSession;
        if (cookie.isPresent()) {  // Cookie already exists
            isNewSession = false;
        } else { // Create a new cookie
            isNewSession = true;
            String sessionId = String.valueOf(counter); // This isn't a good way to create sessionId. Find a better one!
            cookie = Optional.of(new HttpCookie(SESSION_COOKIE_NAME, sessionId));
            httpExchange.getResponseHeaders().add("Set-Cookie", cookie.get().toString());
        }

        response += "\n isNewSession: " + isNewSession+"<br>";
        response += "\n session id: " + cookie.get().getValue();

        sendResponse(httpExchange, response);
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

    private Optional<HttpCookie> getSessionIdCookie(HttpExchange httpExchange){
        String cookieStr = httpExchange.getRequestHeaders().getFirst("Cookie");
        List<HttpCookie> cookies = cookieHelper.parseCookies(cookieStr);
        return cookieHelper.findCookieByName(SESSION_COOKIE_NAME, cookies);
    }

    private void sendResponse(HttpExchange httpExchange, String response) throws IOException {
        httpExchange.sendResponseHeaders(200, response.length());
        OutputStream outputStream = httpExchange.getResponseBody();
        outputStream.write(response.getBytes());
        outputStream.close();
    }

}
