package pages;

import tools.DataManager;
import tools.Utils;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

class Page implements HttpHandler {
    private String html;

    public Page() {
        this.html = Utils.getFileAsString("./web_resources/base_html.html");
    }

    public Page(String ... html){
        this.html = Utils.getFileAsString("./web_resources/base_html.html");
        for(String element : html){
            this.html = insertHTML(element, this.html);
        }
    }

    protected String getHtml(){
        return this.html;
    }

    protected static Map<String,String> getUrlParams(HttpExchange exchange){
        // Source : https://stackoverflow.com/questions/11640025/how-to-obtain-the-query-string-in-a-get-with-java-httpserver-httpexchange
        String params = exchange.getRequestURI().getQuery();

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

    protected static String insertHTML(String html, String body){
        String newBody = body.replace("</body></html>","");
        newBody += html;
        newBody += "</body></html>";
        return newBody;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {

        String response = html;

        exchange.sendResponseHeaders(200, response.getBytes().length);
        OutputStream os = exchange.getResponseBody();
        os.write(response.getBytes());
        os.close();
    }

}