

import DAO.NamePasswordDAO;
import Model.NamePassword;
import com.sun.net.httpserver.HttpServer;
import java.net.InetSocketAddress;

public class App8001 {
    public static void main(String[] args) throws Exception {
        // create a server on port 8001
        HttpServer server = HttpServer.create(new InetSocketAddress(8001), 0);

        // set routes
//        server.createContext("/hello", new Hello());
        server.createContext("/log", new Log());
        server.createContext("/cHand", new CookieHandler());

        server.setExecutor(null); // creates a default executor

        // start listening
        server.start();

//        NamePasswordDAO namePasswordDAO = new NamePasswordDAO();
//        System.out.println( namePasswordDAO.checkNamePassword("rom","yyy")  );

    }
}

