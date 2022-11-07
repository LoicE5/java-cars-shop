package pages;

import pages.Page;
import tools.Utils;

public class JSPage extends Page {
    public JSPage() {
        super.html = Utils.getFileAsString("./web_resources/script.js");
    }
}
