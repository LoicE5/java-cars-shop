package pages;

import com.sun.net.httpserver.HttpExchange;
import tools.DataManager;
import tools.Database;
import tools.Utils;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static java.lang.Double.parseDouble;
import static java.lang.Integer.parseInt;

public class TrackPage extends Page {
    public TrackPage() {
        super();
    }

    private static final Database db = DataManager.getDb();

    public static String showTrackPage(String email) {
        String output = "<h1>Your orders</h1>";
        ArrayList<HashMap<String, String>> orders = db.query("select * from java_cars.orders o inner join java_cars.customers c on c.id = o.customer_id inner join java_cars.vehicles v on v.id = o.vehicle_id where c.email = '"+email+"';");

        if(orders.isEmpty()){
            return output + "<h2 class='no-orders'>There is no order associated with this email address.</h2>";
        }

        for (HashMap<String,String> order : orders) {
            String credit_status = "No";
            if(parseInt(order.get("credit")) == 1){
                credit_status = "Yes";
            }

            output += Utils.getFileAsString("./web_resources/track_order_element.html")
            .replace("{%brand%}", order.get("brand"))
            .replace("{%model%}", order.get("model"))
            .replace("{%status%}", Utils.capitalizeFirstLetter(order.get("status")))
            .replace("{%credit_bool%}",credit_status)
            .replace("{%credit_amount%}",order.get("credit_amount"))
            .replace("{%credit_currency%}",order.get("credit_currency"))
            .replace("{%credit_rate%}",parseDouble(order.get("credit_rate"))*100+"%")
            .replace("{%anonymised_card_number%}",Utils.anonymizeCardNumber(order.get("card_number")))
            .replace("{%card_expiry%}",order.get("card_expiry"))
            .replace("{%card_cvv%}","xxx")
            .replace("{%price%}",order.get("price"))
            .replace("{%country_code%}",order.get("country_code"))
            .replace("{%paid_tax%}",order.get("paid_tax"))
            .replace("{%total_amount%}",order.get("total_amount"))
            .replace("{%amount_after_credit%}",order.get("amount_after_credit"))
            .replace("{%first_name%}",order.get("first_name"))
            .replace("{%last_name%}",order.get("last_name"))
            .replace("{%address%}",order.get("address"))
            .replace("{%anonymised_phone%}",Utils.anonymizePhoneNumber(order.get("phone")))
            .replace("{%type%}",order.get("type"))
            .replace("{%meter%}",order.get("meter"))
            .replace("{%publishing_date%}",order.get("publishing_date"))
            .replace("{%location%}",order.get("location"))
            .replace("{%currency%}",order.get("currency"))
            .replace("{%quantity%}",order.get("quantity"));
        }

        return output;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {

        String response = getHtml();
        Map<String,String> params = getUrlParams(exchange);
        String email;
        try {
            email = params.get("email");
        } catch(Exception e){
            email = "";
        }

        response = insertHTML(showTrackPage(email),response);

        // Sending the response
        exchange.sendResponseHeaders(200, response.getBytes().length);
        OutputStream os = exchange.getResponseBody();
        os.write(response.getBytes());
        os.close();
    }
}
