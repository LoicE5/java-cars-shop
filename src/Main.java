import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;

public class Main {

    public static void main(String[] args) throws IOException {

        HttpServer server = HttpServer.create(new InetSocketAddress(8001), 0);
        server.createContext("/", new Homepage());
        server.createContext("/search", new SearchPage());
        server.createContext("/order", new OrderPage());
        server.createContext("/confirmation", new ConfirmationPage());
        server.start();
        System.out.println("Server running");
    }
}