import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Map;

import static java.lang.Integer.parseInt;

public class OrderPage extends Page {

    public OrderPage() {
        super();
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {

        String response = super.html;
        Map<String,String> params = getUrlParams(exchange);
        int id;
        try {
            id = parseInt(params.get("id"));
        } catch(Exception e){
            id = 0;
        }

        response = DataManager.insertHTML(DataManager.showPurchaseForm(id),response);

        // Sending the response
        exchange.sendResponseHeaders(200, response.getBytes().length);
        OutputStream os = exchange.getResponseBody();
        os.write(response.getBytes());
        os.close();
    }
}
