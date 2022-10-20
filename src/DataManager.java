import java.util.*;

import static java.lang.Double.parseDouble;
import static java.lang.Integer.parseInt;

public class DataManager {
    private static final Database db = new Database("mysql","localhost",3306,"java_cars","root","");

    private static final double defaultCreditRate = 0.05;

    protected static String javascript = "<script type=\"text\\javascript\"></script>";
    protected static String css = "<style>*,::after,::before{box-sizing:border-box}html{font-family:Arial,Helvetica,sans-serif}.hidden{display:none!important}</style>";

    public static String insertCSS(String style, String body) {
        return body.replace("</head>", style + "</head>");
    }
    
    public static String insertHTML(String html, String body){
        String newBody = body.replace("</body></html>","");
        newBody += html;
        newBody += "</body></html>";
        return newBody;
    }

    public static String showAvailableVehicles(String search){
        String output = "<h1>Available vehicles</h1>";

        ArrayList<HashMap<String, String>> query;

        if(search == null || search.equals("")){
           query = db.query("select * from vehicles where ordered is false;");
        } else {
            query = db.query("select * from vehicles where ordered is false and model like '%"+search+"%' or brand like '%"+search+"%' or location like '%"+search+"%';");
        }

        for (HashMap row : query) {
            double tax_rate = parseDouble(db.query("select tax from taxes join vehicles on taxes.country_code = vehicles.country_code where vehicles.id = "+row.get("id")+";").get(0).get("tax"));

            output += "<h3>" + row.get("brand") + " " + row.get("model") + "</h3>";
            output += "<ul>" +
                    "<li>Brand : " + row.get("brand") + "</li>" +
                    "<li>Model : " + row.get("model") + "</li>" +
                    "<li>Type : " + row.get("type") + "</li>" +
                    "<li>Meter : " + row.get("meter") + "</li>" +
                    "<li>Date of publishing : " + row.get("publishing_date") + "</li>" +
                    "<li>Location : " + row.get("location") + "</li>" +
                    "<li>Country : " + row.get("country_code") + "</li>" +
                    "<li>Price (excluding taxes): " + row.get("price") + row.get("currency") + "</li>" +
                    "<li>Tax rate: " + tax_rate + "</li>" +
                    "<li>Total price: " + parseDouble((String) row.get("price"))*(1+tax_rate) + row.get("currency") + "</li>" +
                    "</ul>";
            output += "<a href='/order?id=" + row.get("id") + "'>Buy</a>";
        }

        return output;
    }

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


        output += "<form action='confirmation' method='get'> <section id=\"about-you\"> <h3>About you</h3> <label for='last_name'>Last name</label> <input type='text' name='last_name' id='last_name' required> <br><br><label for='first_name'>First name</label> <input type='text' name='first_name' id='first_name' required> <br><br><label for='birthdate'>Birthdate</label> <input type='date' name='birthdate' id='birthdate' required> <br><br><label for='address'>Address</label> <input type='text' name='address' id='address' required> <br><br><label for='email'>Email</label> <input type='email' name='email' id='email' required> <br><br><label for='phone'>Phone</label> <input type='tel' name='phone' id='phone'> <br><br></section> <section id=\"payment\"> <h3>Payment</h3> <h4>Credit</h4> <label for='credit_bool'>Do you need a credit ?</label> <br><input type='radio' name='credit_bool' id='credit_bool_yes' value='yes' onchange='hideCredit()'> <label for='credit_bool_yes'>Yes</label> <input type='radio' name='credit_bool' id='credit_bool_no' value='no' checked onchange='hideCredit()'> <label for='credit_bool_no'>No</label> <br><br><div id='credit-form-wrapper' class='hidden'> <label for='credit_amount'>Please enter the desired amount of credit for your purchase (in euros)</label> <input type='number' name='credit_amount' id='credit_amount' min='0' max='"+query.get("price")+"'> <div>Current credit rate : 5%</div></div><h4>Bank card</h4> <label for='card_number'>Card number</label> <input id='card_number' name='card_number' type='tel' inputmode='numeric' pattern='[0-9\\s]{13,19}' autocomplete='cc-number' maxlength='19' placeholder='xxxx xxxx xxxx xxxx' required> <br><br><label for='expiry'>Expiration date</label> <select name='card_expiry_month' id='card_expiry_month' required> <option value=''>Month</option> <option value='01'>January</option> <option value='02'>February</option> <option value='03'>March</option> <option value='04'>April</option> <option value='05'>May</option> <option value='06'>June</option> <option value='07'>July</option> <option value='08'>August</option> <option value='09'>September</option> <option value='10'>October</option> <option value='11'>November</option> <option value='12'>December</option> </select> <select name='card_expiry_year' id='card_expiry_year' required> <option value=''>Year</option> <option value='2022'>2022</option> <option value='2023'>2023</option> <option value='2024'>2024</option> <option value='2025'>2025</option> <option value='2026'>2026</option> <option value='2027'>2027</option> <option value='2028'>2028</option> <option value='2029'>2029</option> <option value='2030'>2030</option> <option value='2031'>2031</option> </select> <input class='inputCard' type='hidden' name='card_expiry' id='card_expiry' maxlength='4' required/> <br><br><label for='card_cvv'>CVV/CVC</label> <input type='tel' name='card_cvv' id='card_cvv' maxlength='3' pattern='[0-9\\s]{0,3}'> <input type='hidden' name='vehicle_id' value='"+query.get("id")+"'> </section> <br><br><button type='submit'>Confirm purchase</button> <script>function hideCredit(){document.querySelector('#credit-form-wrapper').classList.toggle('hidden');}</script></form>";

        return output;
    }

    public static Map<String, Object> parseOrderParams(Map<String,String> params){
        Map<String, Object> output = new HashMap<String, Object>();

        output.put("first_name",params.get("first_name"));
        output.put("last_name",params.get("last_name"));
        output.put("birthdate",params.get("birthdate"));
        output.put("address",params.get("address").replace("+"," "));
        output.put("email",params.get("email").replace("%40","@"));
        output.put("phone",params.get("phone"));

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

        output.put("customer_id", parseInt(db.query("select id from customers where email = '"+params.get("email")+"';").get(0).get("id")));

        output.put("tax_rate", parseDouble(db.query("select tax from taxes join vehicles on taxes.country_code = vehicles.country_code where vehicles.id = "+params.get("vehicle_id")+";").get(0).get("tax")));

        output.put("vehicle_price",parseDouble(db.query("select price from vehicles where vehicles.id = "+params.get("vehicle_id")+";").get(0).get("price")));

        output.put("paid_tax",(double)output.get("vehicle_price")*(double)output.get("tax_rate"));

        output.put("total_amount",(double)output.get("vehicle_price")*(1+(double)output.get("tax_rate")));

        output.put("amount_after_credit",(double)output.get("vehicle_price")*(1+(double)output.get("tax_rate"))-(int)output.get("credit_amount"));

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
            db.query("insert into customers (last_name, first_name, birthdate, address, email, phone) values ('"+p.get("last_name")+"','"+p.get("first_name")+"','"+p.get("birthdate")+"','"+p.get("address")+"','"+p.get("email")+"','"+p.get("phone")+"');");
        }

        // We then select the id of the customer
        int customer_id = parseInt(db.query("select id from customers where email = '"+p.get("email")+"';").get(0).get("id"));
        // We get the tax rate
        double tax_rate = parseDouble(db.query("select tax from taxes join vehicles on taxes.country_code = vehicles.country_code where vehicles.id = "+p.get("vehicle_id")+";").get(0).get("tax"));
        double vehicle_price = parseDouble(db.query("select price from vehicles where vehicles.id = "+p.get("vehicle_id")+";").get(0).get("price"));
        // We then create the order
        db.query("insert into orders (vehicle_id, customer_id, status, credit, credit_amount, credit_rate, credit_currency, card_number, card_expiry, card_cvv, paid_tax, total_amount, amount_after_credit) values ("+p.get("vehicle_id")+", "+customer_id+", 'validated', "+p.get("credit_bool")+", "+p.get("credit_amount")+", "+defaultCreditRate+", 'EUR', '"+p.get("card_number")+"', '"+p.get("card_expiry")+"', "+p.get("card_cvv")+", "+vehicle_price*tax_rate+", "+vehicle_price*(1+tax_rate)+", "+(vehicle_price*(1+tax_rate)-(int)p.get("credit_amount"))+");");
        // We now set the vehicle 'ordered' column to true, so it is removed from the available list
        db.query("update vehicles set ordered = true where id = "+p.get("vehicle_id")+";");
    }

    public static String showOrderConfirmation(Map<String,String> params){
        Map<String, Object> p = parseOrderParams(params);
        // TODO insertOrderInDb()
        String output = "<h1>Thanks for your purchase</h1>";
        output += "<p>Your purchase have been validated</p>";
        output += "<p>Here are your purchase information. You may also download the necessary documents below. Your vehicle will be delivered shortly. You may track your order by clicking <a href='track?id="+p.get("vehicle_id")+"' target='_blank'>here</a>.</p>";
        output += "<ul>" +
                "<li>Name :"+p.get("first_name")+" "+p.get("last_name")+"</li>"+
                "<li>Email :"+p.get("email")+"</li>"+
                "<li>Phone :"+p.get("phone")+"</li>"+
                "<li>Birthdate :"+p.get("birthdate")+"</li>"+
                "<li>Address :"+p.get("address")+"</li>"+
                "<li>Credit :"+params.get("credit_bool")+"</li>"+
                "<li>Credit rate (if applicable) :"+defaultCreditRate+"</li>"+
                "<li>Credit amount (if applicable) :"+p.get("credit_amount")+"</li>"+
                "<li>Payment card :"+((String)p.get("card_number")).substring(0,4)+" xxxx xxxx "+((String)p.get("card_number")).substring(((String) p.get("card_number")).length(),-4)+" "+p.get("card_expiry_month")+"/"+p.get("card_expiry_year")+ "xxx</li>"+
                "</ul>";
        // TODO add the vehicle_price (with and without taxes + taxes) in the confirmation page (adding them in the parseOrder())
        return output;
    }

    // TODO To delete getUrlParams
    public static Map<String,String> getUrlParams(String params){
        // Source : https://stackoverflow.com/questions/11640025/how-to-obtain-the-query-string-in-a-get-with-java-httpserver-httpexchange

        if(params == null) {
            return null;
        }

        Map<String, String> result = new HashMap<>();

        for (String param : params.split("&")) {
            String[] entry = param.split("=");
            if (entry.length > 1) {
                result.put(entry[0], entry[1]);
            }else{
                result.put(entry[0], "");
            }
        }

        return result;
    }
    public static void main(String[] args) throws Exception {
        String params_str = "last_name=Loic&first_name=Etienne&birthdate=2001-05-11&address=29+bd+henri+ruel&email=loic.e%40ik.me&phone=0613695512&credit_bool=yes&credit_amount=2000&card_number=4165+3889+8590+9938&card_expiry_month=12&card_expiry_year=2022&card_expiry=&card_cvv=123&vehicle_id=3";
        Map params = getUrlParams(params_str);
        System.out.println(params.toString());
        insertOrderInDb(params);
    }

}
