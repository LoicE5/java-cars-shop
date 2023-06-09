package pages;

import com.sun.net.httpserver.HttpExchange;

import tools.DataManager;
import tools.Utils;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Map;

public class SearchPage extends Page {

    public SearchPage() {
        super(Utils.getFileAsString("./web_resources/searchbar.html"));
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {

        String response = getHtml();
        Map<String,String> params = getUrlParams(exchange);
        String query;
        try {
            query = params.get("query");
        } catch(Exception e){
            query = null;
        }

        response = insertHTML(Homepage.showAvailableVehicles(query),response);

        // Sending the response
        exchange.sendResponseHeaders(200, response.getBytes().length);
        OutputStream os = exchange.getResponseBody();
        os.write(response.getBytes());
        os.close();
    }
}
