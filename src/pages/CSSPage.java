package pages;

import pages.Page;
import tools.Utils;

public class CSSPage extends Page {
    public CSSPage(){
        super.html = Utils.getFileAsString("./web_resources/style.css");
    }
}
