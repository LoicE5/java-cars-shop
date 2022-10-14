public class Homepage extends Page {
    public Homepage() {
        super();
        super.html = DataManager.insertHTML(DataManager.showAvailableVehicles(null),html);
    }

}
