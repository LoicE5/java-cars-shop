package pages;

import com.sun.net.httpserver.HttpExchange;
import tools.DataManager;
import tools.Database;
import tools.Utils;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import static java.lang.Double.parseDouble;
import static java.lang.Integer.parseInt;
import static tools.DataManager.*;

public class ConfirmationPage extends Page {
    public ConfirmationPage() {
        super();
    }

    private static final Database db = DataManager.getDb();

    private static Map<String, Object> parseOrderParams(Map<String,String> params){
        Map<String, Object> output = new HashMap<String, Object>();

        output.put("first_name",Utils.escapeSQLChars(params.get("first_name")));
        output.put("last_name",Utils.escapeSQLChars(params.get("last_name")));
        output.put("birthdate",Utils.escapeSQLChars(params.get("birthdate")));
        output.put("address",Utils.escapeSQLChars(params.get("address").replace("+"," ")));
        output.put("company",Utils.escapeSQLChars(params.get("company").replace("+"," ")));
        output.put("quantity",parseInt(params.get("quantity")));
        output.put("email",Utils.escapeSQLChars(params.get("email").replace("%40","@").replace(" ","")));
        output.put("phone",Utils.escapeSQLChars(params.get("phone").replace("+","")));

        output.put("credit_bool",(params.get("credit_bool").equals("yes")));
        output.put("credit_amount",parseDouble(params.get("credit_amount")));

        output.put("card_number",Utils.escapeSQLChars(params.get("card_number").replace("+"," ")));
        output.put("card_expiry_month",parseInt(params.get("card_expiry_month")));
        output.put("card_expiry_year",parseInt(params.get("card_expiry_year")));
        output.put("card_expiry",(output.get("card_expiry_year") + "-"+ output.get("card_expiry_month") + "-"+ "01"));
        output.put("card_cvv",parseInt(params.get("card_cvv")));

        output.put("vehicle_id",parseInt(params.get("vehicle_id")));

        output.put("current_month", Calendar.getInstance().get(Calendar.MONTH)+1);
        output.put("current_year",Calendar.getInstance().get(Calendar.YEAR));

        output.put("tax_rate", parseDouble(db.query("select tax from taxes join vehicles on taxes.country_code = vehicles.country_code where vehicles.id = "+output.get("vehicle_id")+";").get(0).get("tax")));

        output.put("vehicle_price",parseDouble(db.query("select price from vehicles where vehicles.id = "+output.get("vehicle_id")+";").get(0).get("price")));

        output.put("paid_tax",((double)output.get("vehicle_price")*(double)output.get("tax_rate"))*(int)output.get("quantity"));

        output.put("total_amount",((double)output.get("vehicle_price")*(int)output.get("quantity"))+(double)output.get("paid_tax"));

        output.put("amount_after_credit",(double)output.get("total_amount")-(double)output.get("credit_amount"));

        return output;
    }

    private static void insertOrderInDb(Map<String,String> params) throws Exception {
        Map<String, Object> p = parseOrderParams(params);

        if((int) p.get("card_expiry_year") < (int) p.get("current_year")){
            throw new Exception("card_expiry_year too high");
        } else if(p.get("card_expiry_year") == p.get("current_year") && (int) p.get("card_expiry_month") < (int) p.get("current_month")){
            throw new Exception("card_expiry_month too high for same year");
        }

        int max_stock = parseInt(db.query("select stock from vehicles where id = "+p.get("vehicle_id")+";").get(0).get("stock"));
        if((int)p.get("quantity") > max_stock){
            throw new Exception("The order quantity exceeds the available vehicle stock");
        }

        // Check if a user already exist in the db with the same email address
        ArrayList user_already_exist_check = db.query("select * from customers where email = '"+p.get("email")+"'");
        if(user_already_exist_check.size() == 0){
            // If the user doesn't already exist, we insert it in the database.
            db.query("insert into customers (last_name, first_name, birthdate, email, phone, company) values ('"+p.get("last_name")+"','"+p.get("first_name")+"','"+p.get("birthdate")+"','"+p.get("email")+"','"+p.get("phone")+"', '"+p.get("company")+"');");
        }

        int customer_id = parseInt(db.query("select id from customers where email = '"+p.get("email")+"';").get(0).get("id"));

        // We then create the order
        db.queryWithException("insert into orders (vehicle_id, customer_id, status, credit, credit_amount, credit_rate, credit_currency, card_number, card_expiry, card_cvv, paid_tax, total_amount, amount_after_credit, address, quantity, order_date) values ("+p.get("vehicle_id")+", "+customer_id+", 'validated', "+p.get("credit_bool")+", "+p.get("credit_amount")+", "+ getDefaultCreditRate() +", 'EUR', '"+p.get("card_number")+"', '"+p.get("card_expiry")+"', "+p.get("card_cvv")+", "+p.get("paid_tax")+", "+p.get("total_amount")+", "+p.get("amount_after_credit")+", '"+p.get("address")+"', "+p.get("quantity")+", curdate());");

        // We now set the vehicle stock column to stock minus the ordered quantity, so it is removed from the available list
        db.queryWithException("update vehicles set stock = stock-"+p.get("quantity")+" where id = "+p.get("vehicle_id")+";");
    }

    private static String showOrderConfirmation(Map<String,String> params){
        Map<String, Object> p = parseOrderParams(params);
        try {
            insertOrderInDb(params);
        } catch(Exception e) {
            System.out.println(e);
            return "<h1 style='color:red;'>Insertion of the order in the database have failed. Please try again later.</h1>";
        }

        String anonymised_card_number = ((String)p.get("card_number")).substring(0,4)+" xxxx xxxx "+ Utils.getLastSubstring((String)p.get("card_number"),4);

        String output = Utils.getFileAsString("./web_resources/order_confirmation.html")
                .replace("{%email%}",(String)p.get("email"))
                .replace("{%first_name%}",(String)p.get("first_name"))
                .replace("{%last_name%}",(String)p.get("last_name"))
                .replace("{%phone%}",(String)p.get("phone"))
                .replace("{%birthdate%}",(String)p.get("birthdate"))
                .replace("{%quantity%}",Utils.numberToString((int)p.get("quantity")))
                .replace("{%credit_bool%}",params.get("credit_bool"))
                .replace("{%defaultCreditRate%}",Utils.numberToString(getDefaultCreditRate()*100)+"%")
                .replace("{%anonymised_card_number%}",anonymised_card_number)
                .replace("{%card_expiry_month%}",Utils.numberToString((int)p.get("card_expiry_month")))
                .replace("{%card_expiry_year%}",Utils.numberToString((int)p.get("card_expiry_year")))
                .replace("{%vehicle_price%}",Utils.numberToString((double)p.get("vehicle_price")))
                .replace("{%tax_rate%}",Utils.numberToString((double)p.get("tax_rate")*100))
                .replace("{%paid_tax%}",Utils.numberToString((double)p.get("paid_tax")))
                .replace("{%birthdate%}",(String)p.get("birthdate"))
                .replace("{%total_amount%}",Utils.numberToString((double)p.get("total_amount")))
                .replace("{%amount_after_credit%}",Utils.numberToString((double)p.get("amount_after_credit")))
                .replace("{%credit_amount%}",Utils.numberToString((double)p.get("credit_amount")))
                .replace("{%address%}",(String)p.get("address"));

        int orderId = parseInt(db.query("select id from orders order by id desc limit 1").get(0).get("id"));

        output += "<div class='documents-buttons-container'>" +
                "<a href='/"+DocumentPage.allowedDocumentTypes[0]+"?order_id="+orderId+"'><button>Sale Certificate</button></a>" +
                "<a href='/"+DocumentPage.allowedDocumentTypes[1]+"?order_id="+orderId+"'><button>Registration Request</button></a>" +
                "<a href='/"+DocumentPage.allowedDocumentTypes[2]+"?order_id="+orderId+"'><button>Order Sheet</button></a>" +
            "</div>";

        return output;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {

        String response = getHtml();
        Map<String,String> params = getUrlParams(exchange);

        response = insertHTML(showOrderConfirmation(params),response);

        // Sending the response
        exchange.sendResponseHeaders(200, response.getBytes().length);
        OutputStream os = exchange.getResponseBody();
        os.write(response.getBytes());
        os.close();
    }

}

