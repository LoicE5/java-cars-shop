package pages;

import com.sun.net.httpserver.HttpExchange;
import tools.DataManager;
import tools.Database;
import tools.Utils;

import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

import static java.lang.Integer.parseInt;

public class OrderPage extends Page {

    public OrderPage() {
        super();
    }

    private static final Database db = DataManager.getDb();

    public static String showPurchaseForm(int vehicleId) {
        HashMap<String, String> query = db.query("select * from vehicles where ordered is false and id = " + vehicleId + ";").get(0);

        String output = "<h1>Purchase a vehicle</h1>";
        output += "<h2>Your purchase</h2>";

        output += "<ul>" +
                "<li>Brand : " + query.get("brand") + "</li>" +
                "<li>Model : " + query.get("model") + "</li>" +
                "<li>Type : " + query.get("type") + "</li>" +
                "<li>Meter : " + query.get("meter") + "</li>" +
                "<li>Date of publishing : " + query.get("publishing_date") + "</li>" +
                "<li>Location : " + query.get("location") + "</li>" +
                "<li>Country : " + query.get("country_code") + "</li>" +
                "<li>Price : " + query.get("price") + query.get("currency") + "</li>" +
                "</ul>";

        output += Utils.getFileAsString("./web_resources/order_form.html")
                .replace("{%price%}",query.get("price"))
                .replace("{%id%}",query.get("id"));

        return output;
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

        response = DataManager.insertHTML(showPurchaseForm(id),response);

        // Sending the response
        exchange.sendResponseHeaders(200, response.getBytes().length);
        OutputStream os = exchange.getResponseBody();
        os.write(response.getBytes());
        os.close();
    }
}
