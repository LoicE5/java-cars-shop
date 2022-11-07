package tools;

import java.util.*;

import static java.lang.Double.parseDouble;
import static java.lang.Integer.parseInt;

public class DataManager {
    private static final Database db = new Database(
        "mysql",
        "localhost",
        3306,
        "java_cars",
        "root",
        ""
    );

    public static Database getDb(){
        return db;
    }

    private static final double defaultCreditRate = 0.05;
    private static final double defaultDiscountedPrice = 0.8;

    public static double getDefaultCreditRate(){
        return defaultCreditRate;
    }

    public static String insertHTML(String html, String body){
        String newBody = body.replace("</body></html>","");
        newBody += html;
        newBody += "</body></html>";
        return newBody;
    }

    public static Map<String, Object> parseOrderParams(Map<String,String> params){
        Map<String, Object> output = new HashMap<String, Object>();

        output.put("first_name",params.get("first_name"));
        output.put("last_name",params.get("last_name"));
        output.put("birthdate",params.get("birthdate"));
        output.put("address",params.get("address").replace("+"," "));
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

        output.put("current_month",Calendar.getInstance().get(Calendar.MONTH)+1);
        output.put("current_year",Calendar.getInstance().get(Calendar.YEAR));

        output.put("tax_rate", parseDouble(db.query("select tax from taxes join vehicles on taxes.country_code = vehicles.country_code where vehicles.id = "+output.get("vehicle_id")+";").get(0).get("tax")));

        output.put("vehicle_price",parseDouble(db.query("select price from vehicles where vehicles.id = "+output.get("vehicle_id")+";").get(0).get("price")));

        output.put("paid_tax",(double)output.get("vehicle_price")*(double)output.get("tax_rate"));

        output.put("total_amount",(double)output.get("vehicle_price")*(1+(double)output.get("tax_rate")));

        output.put("amount_after_credit",(double)output.get("vehicle_price")*(1+(double)output.get("tax_rate"))-(double)output.get("credit_amount"));

        return output;
    }

    public static void insertOrderInDb(Map<String,String> params) throws Exception {
        Map<String, Object> p = parseOrderParams(params);

        if((int) p.get("card_expiry_year") < (int) p.get("current_year")){
            throw new Exception("card_expiry_year too high");
        } else if(p.get("card_expiry_year") == p.get("current_year") && (int) p.get("card_expiry_month") < (int) p.get("current_month")){
            throw new Exception("card_expiry_month too high for same year");
        }

        // Check if a user already exist in the db with the same email address
        ArrayList user_already_exist_check = db.query("select * from customers where email = '"+p.get("email")+"'");
        if(user_already_exist_check.size() == 0){
            // If the user doesn't already exist, we insert it in the database.
            db.query("insert into customers (last_name, first_name, birthdate, email, phone) values ('"+p.get("last_name")+"','"+p.get("first_name")+"','"+p.get("birthdate")+"','"+p.get("email")+"','"+p.get("phone")+"');");
        }

        int customer_id = parseInt(db.query("select id from customers where email = '"+p.get("email")+"';").get(0).get("id"));

        // We then create the order
        db.queryWithException("insert into orders ( vehicle_id, customer_id, status, credit, credit_amount, credit_rate, credit_currency, card_number, card_expiry, card_cvv, paid_tax, total_amount, amount_after_credit, address) values ("+p.get("vehicle_id")+", "+customer_id+", 'validated', "+p.get("credit_bool")+", "+p.get("credit_amount")+", "+defaultCreditRate+", 'EUR', '"+p.get("card_number")+"', '"+p.get("card_expiry")+"', "+p.get("card_cvv")+", "+p.get("paid_tax")+", "+p.get("total_amount")+", "+p.get("amount_after_credit")+", '"+p.get("address")+"');");
        // We now set the vehicle 'ordered' column to true, so it is removed from the available list
        db.queryWithException("update vehicles set ordered = true where id = "+p.get("vehicle_id")+";");
    }

    public static void automaticDiscountOnOldVehicles(){
        db.query("update vehicles set price = "+defaultDiscountedPrice+"*price, discounted = true where publishing_date < (curdate() - interval 2 month) and discounted = false;");
    }
}
