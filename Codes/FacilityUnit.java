abstract class FacilityUnit extends CampusEntity {
    protected double maintenanceCost;
    protected int usageFrequency;
    protected static int totalfacilityusage=0;
    public FacilityUnit() {
        super();
        this.usageFrequency = 0;
        this.maintenanceCost = 0;
    }

    public FacilityUnit(String entityID, String name, String location, double maintenanceCost, int usageFrequency) {
        super(entityID, name, location);
        this.maintenanceCost = maintenanceCost;
        this.usageFrequency = usageFrequency;
    }
    public void setMaintenanceCost(double maintenanceCost) {
        this.maintenanceCost = maintenanceCost;
    }
    public void setUsageFrequency(int usageFrequency) {
        this.usageFrequency = usageFrequency;
    }

    public double getMaintenanceCost() {
        return maintenanceCost;
    }
    public int getUsageFrequency() {
        return usageFrequency;
    }
    public static void incrementFacilityUsage() {
        totalfacilityusage++;
    }
    public static  int getTotalFacilityUsage() {
        return totalfacilityusage;
    }

    @Override
    public String toString() {
        return (super.toString() + "\nMaintenance Cost: " + maintenanceCost + "\nUsage Frequency: " + usageFrequency);
    }
    
    public abstract double calculateOperationalCost();

}
