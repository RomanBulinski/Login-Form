

import DAO.NamePasswordDAO;
import Model.NamePassword;
import com.sun.net.httpserver.HttpServer;
import java.net.InetSocketAddress;

public class App {
    public static void main(String[] args) throws Exception {
        // create a server on port 8001
        HttpServer server = HttpServer.create(new InetSocketAddress(8007), 0);

        // set routes
        server.createContext("/log", new Log());
        server.createContext("/static", new Static());

        server.setExecutor(null); // creates a default executor

        // start listening
        server.start();

//        NamePasswordDAO namePasswordDAO = new NamePasswordDAO();
//        System.out.println( namePasswordDAO.checkNamePassword("rom","yyy")  );

    }
}

