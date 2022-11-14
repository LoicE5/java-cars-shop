package tools;

import java.util.HashMap;

public class DataManager {
    private static final HashMap db_settings = (HashMap) Utils.readJsonFile("./config.json").get("database");
    private static final Database db = new Database(
        (String) db_settings.get("protocol"),
        (String) db_settings.get("host"),
        (int) db_settings.get("port"),
        (String) db_settings.get("db_name"),
        (String) db_settings.get("username"),
        (String) db_settings.get("password")
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

    public static void automaticDiscountOnOldVehicles(){
        db.query("update vehicles set price = "+defaultDiscountedPrice+"*price, discounted = true where publishing_date < (curdate() - interval 2 month) and discounted = false;");
    }
}
