package tools;

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

    public static void automaticDiscountOnOldVehicles(){
        db.query("update vehicles set price = "+defaultDiscountedPrice+"*price, discounted = true where publishing_date < (curdate() - interval 2 month) and discounted = false;");
    }
}
