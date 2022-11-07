import com.sun.net.httpserver.HttpServer;
import pages.*;
import tools.DataManager;

import java.io.IOException;
import java.net.InetSocketAddress;

public class Main {

    public static void main(String[] args) throws IOException {
        // On the run of the program, we first use SQL to automatically discount the old vehicles before starting the web server
        DataManager.automaticDiscountOnOldVehicles();

        // We then create the server, set the routes, and start
        HttpServer server = HttpServer.create(new InetSocketAddress(8001), 0);

        server.createContext("/style.css", new CSSPage());
        server.createContext("/script.js", new JSPage());

        server.createContext("/", new Homepage());
        server.createContext("/search", new SearchPage());
        server.createContext("/order", new OrderPage());
        server.createContext("/confirmation", new ConfirmationPage());
        server.createContext("/track", new TrackPage());

        server.start();
        System.out.println("Server running");
    }
}