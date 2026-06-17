import java.io.Serializable;
import java.util.ArrayList;

class CampusZone implements Serializable {
    private String zoneName;
    private String location;
    private ArrayList<FacilityUnit> facilities;
    private ArrayList<ServiceUnit> serviceUnits;
    public CampusZone() {
        zoneName=null;
        location=null;
        facilities=new ArrayList<>();
        serviceUnits=new ArrayList<>();
    }
    public CampusZone(String zoneName,String location) {
        this.zoneName=zoneName;
        this.location=location;
        facilities=new ArrayList<>();
        serviceUnits=new ArrayList<>();
    }
    public void addFacility(FacilityUnit facility) {
        if(!facilities.contains(facility)) {
            facilities.add(facility);
            System.out.printf(" Facility %s added to campus zone", facility.getName());
        }
        else {
            System.out.printf(" Facility %s already exists in campus zone", facility.getName());
        }
    }
    public void removeFacility(FacilityUnit facility) {
        if(facilities.contains(facility)) {
            facilities.remove(facility);
            System.out.printf(" Facility %s removed from campus zone", facility.getName());
        }
        else {
            System.out.printf(" Facility %s not found in campus zone", facility.getName());
        }
    }
    public void addServiceUnit(ServiceUnit serviceUnit) {
        if(!serviceUnits.contains(serviceUnit)) {
            serviceUnits.add(serviceUnit);
            System.out.printf(" Service unit %s added to campus zone", serviceUnit.getName());
        }
        else {
            System.out.printf(" Service unit %s already exists in campus zone", serviceUnit.getName());
        }
    }
    public void removeServiceUnit(ServiceUnit serviceUnit) {
        if(serviceUnits.contains(serviceUnit)) {
            serviceUnits.remove(serviceUnit);
            System.out.printf(" Service unit %s removed from campus zone", serviceUnit.getName());
        }
        else {
            System.out.printf(" Service unit %s not found in campus zone", serviceUnit.getName());
        }
    }
    public double calculateZoneCost() {
        double total=0;
        for(FacilityUnit f:facilities) {
            total+=f.calculateOperationalCost();
        }
        for(ServiceUnit s:serviceUnits) {
            total+=s.calculateOperationalCost();
        }
        return total;
    }
    public void displayZoneDetails() {
        System.out.println("Zone Name:"+zoneName);
        System.out.println("Location:"+location);
        System.out.println("===== Facilities =====");
        for(FacilityUnit f:facilities) {
            System.out.println(f.toString());
            System.out.println();
        }
        System.out.println("===== Service Units =====");
        for(ServiceUnit s:serviceUnits) {
            System.out.println(s.toString());
            System.out.println();
        }
        System.out.println("Total Zone Cost:"+calculateZoneCost());
    }
    public String toString() {
        return "Zone Name:"+zoneName+"\nLocation:"+location+"\nFacilities:"+facilities.size()+"\nService Units:"+serviceUnits.size()+"\nZone Cost:"+calculateZoneCost();
    }
}