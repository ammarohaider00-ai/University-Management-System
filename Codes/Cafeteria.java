import java.util.ArrayList;

class Cafeteria extends FacilityUnit {
    private ArrayList<String> menuItems;
    private int dailyCustomers;
    public Cafeteria() {
        menuItems = new ArrayList<>();
        dailyCustomers = 0;
    }

    public Cafeteria(String entityID, String name, String location, double maintenanceCost, int usageFrequency, int dailyCustomers) {
        super(entityID, name, location, maintenanceCost, usageFrequency);
        this.dailyCustomers = dailyCustomers;
        menuItems = new ArrayList<>();
        totalfacilityusage+=dailyCustomers;
    }

    public int getDailyCustomers() {
        return dailyCustomers;
    }

    public void setDailyCustomers(int dailyCustomers) {
        this.dailyCustomers = dailyCustomers;
    }

    public void addMenuItem(String item) {
        if(menuItems.contains(item)) {
            System.out.printf("=== %s is Already Present in the Menu ===%n",item);
            return;
        }
        menuItems.add(item);
        System.out.printf("=== %s is Added to the Menu ===%n",item);
    }
    public void removeMenuItem(String item) {
        if (menuItems.contains(item)) {
        menuItems.remove(item);
        System.out.printf("=== %s Removed from the Menu ===%n",item);
        return;
    } System.out.printf("=== %s is not present in the Menu ===%n",item);

    }
    public ArrayList<String> getMenuItems() {
        return menuItems;
    }
    public int getTotalItems() {
        return menuItems.size();
    }

    @Override
    public double calculateOperationalCost() {
        return maintenanceCost + (usageFrequency * 15) + (dailyCustomers * 10) + (menuItems.size() * 5);
    }
    public void DisplayMenu() {
        System.out.println("=== Menu ===");
        for(String item:menuItems) {
            System.out.println(item);
            System.out.println();
        }
    }

    @Override
    public String toString() {
        return (super.toString() + "\nDaily Customers: " + dailyCustomers + "\nMenu Items: " + menuItems.size() + "\nOperational Cost: " + calculateOperationalCost());
    }

}
