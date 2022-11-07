package pages;

import com.sun.net.httpserver.HttpExchange;
import pages.Page;
import tools.DataManager;
import tools.Database;
import tools.Utils;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Map;

import static java.lang.Integer.parseInt;
import static tools.DataManager.*;

public class ConfirmationPage extends Page {
    public ConfirmationPage() {
        super();
    }

    private static final Database db = DataManager.getDb();

    public static String showOrderConfirmation(Map<String,String> params){
        Map<String, Object> p = parseOrderParams(params);
        try {
            insertOrderInDb(params);
        } catch(Exception e) {
            return "<h1 style='color:red;'>Insertion of the order in the database have failed. Please try again later.</h1>";
        }

        String anonymised_card_number = ((String)p.get("card_number")).substring(0,4)+" xxxx xxxx "+ Utils.getLastSubstring((String)p.get("card_number"),4);

        String output = "<h1>Thanks for your purchase</h1>";
        output += "<p>Your purchase have been validated</p>";
        output += "<p>Here are your purchase information. You may also download the necessary documents below. Your vehicle will be delivered shortly. You may track your order by clicking <a href='track?email="+p.get("email")+"'>here</a>.</p>";
        output += "<ul>" +
                "<li>Name : "+p.get("first_name")+" "+p.get("last_name")+"</li>"+
                "<li>Email : "+p.get("email")+"</li>"+
                "<li>Phone : "+p.get("phone")+"</li>"+
                "<li>Birthdate : "+p.get("birthdate")+"</li>"+
                "<li>Address : "+p.get("address")+"</li>"+
                "<li>Credit : "+params.get("credit_bool")+"</li>"+
                "<li>Credit rate (if applicable) : "+getDefaultCreditRate()+"</li>"+
                "<li>Credit amount (if applicable) : "+p.get("credit_amount")+"</li>"+
                "<li>Payment card : "+anonymised_card_number+" | "+p.get("card_expiry_month")+"/"+p.get("card_expiry_year")+" | xxx</li>"+
                "<li>Price without taxes : "+p.get("vehicle_price")+"</li>"+
                "<li>Taxes : "+(double)p.get("tax_rate")*100+"%</li>"+
                "<li>Final price : "+p.get("total_amount")+"</li>"+
                "<li>Paid amount after credit (if applicable) : "+p.get("amount_after_credit")+"</li>"+
                "</ul>";
        // TODO ajouter les liens vers la carte grise et le reste (...)

        output += "<a href='track?email="+p.get("email")+"'>Track my orders</a>";

        return output;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {

        String response = super.html;
        Map<String,String> params = getUrlParams(exchange);

        response = DataManager.insertHTML(showOrderConfirmation(params),response);

        // Sending the response
        exchange.sendResponseHeaders(200, response.getBytes().length);
        OutputStream os = exchange.getResponseBody();
        os.write(response.getBytes());
        os.close();
    }
}

