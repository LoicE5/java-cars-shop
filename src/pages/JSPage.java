package pages;

import tools.Utils;

public class JSPage extends Page {
    public JSPage() {
        super(Utils.getFileAsString("./web_resources/script.js"));
    }
}
