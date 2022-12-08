package pages;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;
import tools.DataManager;
import tools.Database;
import tools.Utils;

import javax.management.InvalidAttributeValueException;
import java.io.IOException;
import java.io.OutputStream;
import java.util.*;

import static java.lang.Integer.parseInt;

public class DocumentPage extends Page {

    private final String documentType;
    protected final static String[] allowedDocumentTypes = {"sale_certificate", "registration_request", "order_sheet"};

    public DocumentPage(String documentType) throws InvalidAttributeValueException {

        super();

        if(Arrays.asList(allowedDocumentTypes).contains(documentType)){

            this.documentType = documentType;

        } else {
            throw new InvalidAttributeValueException("The document type specified is not correct.");
        }
    }

    private static final Database db = DataManager.getDb();

    public static void createDocumentsPagesContexts(HttpServer server){
        try {
            server.createContext("/sale_certificate", new DocumentPage("sale_certificate"));
            server.createContext("/registration_request", new DocumentPage("registration_request"));
            server.createContext("/order_sheet", new DocumentPage("order_sheet"));
        } catch (InvalidAttributeValueException e){
            System.out.println("The parameter for one or more DocumentPage instances is wrong. Documents may not work properly.");
        }

    }

    private String showDocumentPage(int orderId){
        String output = "";

        HashMap<String, String> data = null;

        try {
             data = db.queryWithException("select * from orders o join customers c on o.customer_id = c.id join vehicles v on o.vehicle_id = v.id where o.id = "+orderId+";").get(0);
        } catch (IndexOutOfBoundsException e){
            return "<h1 style='color: orange;'>No order with the provided id exists in the database</h1>";
        } catch (Exception e){
            System.out.println(e);
            return "<h1 style='color: red;'>There have been a problem processing the request.</h1>";
        }

        int quantity = parseInt(data.get("quantity"));

        String documentPath = "./web_resources/documents/"+documentType+".html";

        Random rand = new Random();

        String currentDate = Calendar.getInstance().get(Calendar.DAY_OF_MONTH)+"-"+Calendar.getInstance().get(Calendar.MONTH)+"-"+Calendar.getInstance().get(Calendar.YEAR);

        for(int i=0; i<quantity; i++){
            output += "<fieldset><legend>Vehicle "+(i+1)+"</legend>"+
                Utils.getFileAsString(documentPath)
                .replace("{%registration_number%}",Utils.numberToString(rand.nextInt(1000000)))
                .replace("{%purchase_date%}",data.get("order_date"))
                .replace("{%order_date%}",data.get("order_date"))
                .replace("{%brand%}",data.get("brand"))
                .replace("{%model%}",data.get("model"))
                .replace("{%meter%}",data.get("meter"))
                .replace("{%type%}",Utils.capitalizeFirstLetter(data.get("type")))
                .replace("{%location%}",data.get("location"))
                .replace("{%country_code%}",data.get("country_code"))
                .replace("{%last_name%}",data.get("last_name"))
                .replace("{%first_name%}",data.get("first_name"))
                .replace("{%company%}",data.get("company"))
                .replace("{%birthdate%}",data.get("birthdate"))
                .replace("{%type%}",data.get("type"))
                .replace("{%address%}",data.get("address"))
                .replace("{%vehicle_price%}",data.get("price"))
                .replace("{%paid_tax%}",data.get("paid_tax"))
                .replace("{%total_amount%}",data.get("total_amount"))
                .replace("{%current_date%}",currentDate)
            +"</fieldset>";
        }

        return output;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {

        String response = getHtml();
        Map<String,String> params = getUrlParams(exchange);
        int orderId;
        try {
            orderId = parseInt(params.get("order_id"));
        } catch(NumberFormatException e){
            orderId = 0;
        }

        response = insertHTML(showDocumentPage(orderId),response);

        // Sending the response
        exchange.sendResponseHeaders(200, response.getBytes().length);
        OutputStream os = exchange.getResponseBody();
        os.write(response.getBytes());
        os.close();
    }
}
