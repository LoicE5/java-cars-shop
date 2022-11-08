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

    public static Map<String, Object> parseOrderParams(Map<String,String> params){
        Map<String, Object> output = new HashMap<String, Object>();

        output.put("first_name",params.get("first_name"));
        output.put("last_name",params.get("last_name"));
        output.put("birthdate",params.get("birthdate"));
        output.put("address",params.get("address").replace("+"," "));
        output.put("company",params.get("company").replace("+"," "));
        output.put("quantity",parseInt(params.get("quantity")));
        output.put("email",params.get("email").replace("%40","@").replace(" ",""));
        output.put("phone",params.get("phone").replace("+",""));

        output.put("credit_bool",(params.get("credit_bool").equals("yes")));
        output.put("credit_amount",parseDouble(params.get("credit_amount")));

        output.put("card_number",params.get("card_number").replace("+"," "));
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

    public static void insertOrderInDb(Map<String,String> params) throws Exception {
        Map<String, Object> p = parseOrderParams(params);
        System.out.println(params);

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
        db.queryWithException("insert into orders (vehicle_id, customer_id, status, credit, credit_amount, credit_rate, credit_currency, card_number, card_expiry, card_cvv, paid_tax, total_amount, amount_after_credit, address, quantity) values ("+p.get("vehicle_id")+", "+customer_id+", 'validated', "+p.get("credit_bool")+", "+p.get("credit_amount")+", "+ getDefaultCreditRate() +", 'EUR', '"+p.get("card_number")+"', '"+p.get("card_expiry")+"', "+p.get("card_cvv")+", "+p.get("paid_tax")+", "+p.get("total_amount")+", "+p.get("amount_after_credit")+", '"+p.get("address")+"', "+p.get("quantity")+");");

        // We now set the vehicle stock column to stock minus the ordered quantity, so it is removed from the available list
        db.queryWithException("update vehicles set stock = stock-"+p.get("quantity")+" where id = "+p.get("vehicle_id")+";");
    }

    public static String showOrderConfirmation(Map<String,String> params){
        Map<String, Object> p = parseOrderParams(params);
        try {
            insertOrderInDb(params);
        } catch(Exception e) {
            System.out.println(e);
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
                "<li>Quantity ordered : "+p.get("quantity")+"</li>"+
                "<li>Credit : "+params.get("credit_bool")+"</li>"+
                "<li>Credit rate (if applicable) : "+getDefaultCreditRate()+"</li>"+
                "<li>Credit amount (if applicable) : "+p.get("credit_amount")+"</li>"+
                "<li>Payment card : "+anonymised_card_number+" | "+p.get("card_expiry_month")+"/"+p.get("card_expiry_year")+" | xxx</li>"+
                "<li>Unitary price without taxes : "+p.get("vehicle_price")+"</li>"+
                "<li>Tax rate : "+(double)p.get("tax_rate")*100+"%</li>"+
                "<li>Total paid tax : "+p.get("paid_tax")+"</li>"+
                "<li>Final price (for "+p.get("quantity")+" items), including taxes : "+p.get("total_amount")+"</li>"+
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

