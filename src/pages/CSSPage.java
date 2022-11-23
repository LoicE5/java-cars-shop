package pages;

import tools.Utils;

public class CSSPage extends Page {
    public CSSPage(){
        super(Utils.getFileAsString("./web_resources/style.css"));
    }
}
