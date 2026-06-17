import java.util.ArrayList;
class TransportService extends ServiceUnit implements Scheduable {
    private ArrayList<Bus> buses=new ArrayList<>();
    private ArrayList<Bus> extraBuses=new ArrayList<>();
    private boolean ispeakhour;
    public TransportService() {
        super();
    }
    public TransportService(String entityID,String name,String location,int servicehours,double costperhour,boolean ispeakhour) {
        super(entityID, name, location,servicehours,costperhour);
        this.ispeakhour=ispeakhour;
    }
    public ArrayList<Bus> getBuses() {
        return buses;
    }

    public boolean addBus(Bus bus) {
        if (bus == null || bus.getBusID() == null || bus.getBusID().trim().isEmpty()) {
            System.out.println("Invalid bus cannot be added.");
            return false;
        }
        for (Bus existing : buses) {
            if (existing.getBusID().equalsIgnoreCase(bus.getBusID())) {
                System.out.println("Bus with this ID is already added in the stand.");
                return false;
            }
        }
        buses.add(bus);
        System.out.printf("Bus %s Added To Transport Service%n", bus.getBusID());
        return true;
    }
    public void removeBus(Bus bus) {
        if (buses.contains(bus)) {
            buses.remove(bus);
            System.out.printf("Bus %s Removed From Transport Service%n", bus.getBusID());
            return;
        }
        System.out.printf("Bus %s is not present in the Stand%n", bus.getBusID());
    }
    public void setispeakhour(boolean ispeakhour) {
        this.ispeakhour = ispeakhour;
    }
    public boolean ispeakhour() {
        return ispeakhour;
    }
    public void peakHoursAdjustment() {
        if (ispeakhour) {
            extraBuses.clear();
            for (Bus bus : buses) {
                Bus b = new Bus(bus.getBusID(), bus.getDriverSalary(), bus.getFrom(), bus.getTo(), bus.getDepartureTime());
                extraBuses.add(b);
            }
            System.out.println("=== Peak Hours Active ===");
            System.out.println("== Extra Buses Deployed ===");
            System.out.println("=== Extra Buses Details ===");
            for (Bus bus : extraBuses) {
                System.out.println(bus.toString());
                System.out.println();

            }
            System.out.println("=== Normal Buses Details ===");
            for (Bus bus : buses) {
                System.out.println(bus.toString());
                System.out.println();
            }
        } else {
            extraBuses.clear();
            System.out.println("No Peak Hours Active ===");
            System.out.println("Only Normal Buses are Operating ===");
            System.out.println("=== Normal Buses Details ===");
            for (Bus bus : buses) {
                System.out.println(bus.toString());
                System.out.println();
            }
        }
    }

    public String toString() {
        return super.toString();
    }
    public double calculateOperationalCost() {
        double total=0;
        for(Bus bus:buses) {
            total+=bus.getDriverSalary();
        }
        total+=servicehours*costperhour;
        return total;
    }
    public void generateSchedule() {
        peakHoursAdjustment();
    }

    public String getSummary() {
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("Transport Service: %s (%s)\nLocation: %s\nService Hours: %d\nCost per Hour: $%.2f\nPeak Hours: %s\nTotal Buses: %d",
                getName(), getEntityID(), getLocation(), getServicehours(), getCostperhour(), ispeakhour ? "ACTIVE" : "INACTIVE", buses.size()));
        if (buses.isEmpty()) {
            sb.append("\nNo buses currently assigned to this service.");
        } else {
            sb.append("\n\n=== Assigned Buses ===\n");
            for (Bus bus : buses) {
                sb.append(bus.toString()).append("\n\n");
            }
        }
        return sb.toString();
    }

    public String getDetailedInfo() {
        StringBuilder sb = new StringBuilder();
        sb.append(super.toString()).append("\n");
        sb.append("Peak Hours: ").append(ispeakhour ? "ACTIVE" : "INACTIVE").append("\n");
        sb.append("Total Buses: ").append(buses.size()).append("\n");
        if (buses.isEmpty()) {
            sb.append("No buses currently assigned.\n");
        } else {
            sb.append("=== Bus Details ===\n");
            for (Bus bus : buses) {
                sb.append(bus.toString()).append("\n\n");
            }
        }
        return sb.toString();
    }

    public String getScheduleDetails() {
        StringBuilder sb = new StringBuilder();
        if (ispeakhour) {
            sb.append("=== Peak Hours Active ===\n");
            sb.append("== Extra Buses Deployed ==\n");
            for (Bus bus : buses) {
                sb.append(bus.toString()).append("\n\n");
            }
            sb.append("=== Normal Buses Details ===\n");
            for (Bus bus : buses) {
                sb.append(bus.toString()).append("\n\n");
            }
        } else {
            sb.append("No Peak Hours Active ===\n");
            sb.append("Only Normal Buses are Operating ===\n");
            sb.append("=== Normal Buses Details ===\n");
            for (Bus bus : buses) {
                sb.append(bus.toString()).append("\n\n");
            }
        }
        return sb.toString();
    }

}
