package pages;

import tools.DataManager;
import tools.Database;
import tools.Utils;

import java.util.ArrayList;
import java.util.HashMap;

import static java.lang.Double.parseDouble;
import static java.lang.Integer.parseInt;

public class Homepage extends Page {
    public Homepage() {
        super();
        super.html = DataManager.insertHTML(Utils.getFileAsString("./web_resources/searchbar.html"),html);
        super.html = DataManager.insertHTML(Utils.getFileAsString("./web_resources/track_order_bar.html"),html);
        super.html = DataManager.insertHTML(showAvailableVehicles(null),html);
    }

    private static final Database db = DataManager.getDb();

    protected static String showAvailableVehicles(String search){
        String output = "<h1>Available vehicles</h1>";

        ArrayList<HashMap<String, String>> query;

        if(search == null || search.equals("")){
            query = db.query("select * from vehicles where stock > 0;");
        } else {
            query = db.query("select * from vehicles where stock > 0 and (model like '%"+search+"%' or brand like '%"+search+"%' or location like '%"+search+"%');");
        }

        if(query.size() == 0){
            output += "<h2 class='no-vehicles-available'>There are currently no vehicles available.</h1>";
        }

        for (HashMap<String,String> row : query) {
            double tax_rate = parseDouble(db.query("select tax from taxes join vehicles on taxes.country_code = vehicles.country_code where vehicles.id = "+row.get("id")+";").get(0).get("tax"));

            output += "<h3>" + row.get("brand") + " " + row.get("model") + "</h3>";

            if(parseInt(row.get("discounted")) == 1){
                output += "<div class='discounted-item'>Discount!</div>";
            }

            output += "<ul>" +
                    "<li>Brand : " + row.get("brand") + "</li>" +
                    "<li>Model : " + row.get("model") + "</li>" +
                    "<li>Type : " + row.get("type") + "</li>" +
                    "<li>Meter : " + row.get("meter") + "</li>" +
                    "<li>Date of publishing : " + row.get("publishing_date") + "</li>" +
                    "<li>Location : " + row.get("location") + "</li>" +
                    "<li>Country : " + row.get("country_code") + "</li>" +
                    "<li>Remaining in stock : " + row.get("stock") + "</li>" +
                    "<li>Price (excluding taxes): " + row.get("price") + row.get("currency") + "</li>" +
                    "<li>Tax rate: " + tax_rate + "</li>" +
                    "<li>Total price: " + parseDouble(row.get("price"))*(1+tax_rate) + row.get("currency") + "</li>" +
                    "</ul>";

            output += "<a href='/order?id=" + row.get("id") + "'>Buy</a>";
        }

        return output;
    }


}
