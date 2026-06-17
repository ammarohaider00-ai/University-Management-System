import java.util.ArrayList;

class Hostels extends FacilityUnit {
    private  int totalRooms, occupiedRooms;
    private ArrayList<Student> residents;
    public Hostels() {
        totalRooms=0;
        occupiedRooms=0;
        residents=new ArrayList<>();
    }

    public Hostels(String entityID, String name, String location, double maintenanceCost, int usageFrequency, int totalRooms,int occupiedRooms) {
        super(entityID, name, location, maintenanceCost, usageFrequency);
        this.totalRooms = totalRooms;
        this.occupiedRooms = occupiedRooms;
        residents = new ArrayList<>();
    }

    public int getTotalRooms() {

        return totalRooms;
    }
    public int getAvailableRooms() {
        return totalRooms - occupiedRooms;
    }

    public boolean addResident(Student resident) {
            for (Student existing : residents) {
            if (existing.getRegNO().equalsIgnoreCase(resident.getRegNO())) {
                System.out.println("Resident is already staying in the hostel.");
                return false;
            }
        }
        if (occupiedRooms < totalRooms) {
            residents.add(resident);
            occupiedRooms++;
            FacilityUnit.incrementFacilityUsage();
            return true;
        } else {
            System.out.println("Hostel is full, no room available");
            return false;
        }
    }
    public boolean removeResident(Student resident) {
        if(residents.remove(resident)) {
            occupiedRooms--;
            return true;
        }
        return false;
    }
    public void viewResidents() {
        for(Student resident:residents) {
            System.out.println(resident.toString());
            System.out.println();
        }
    }
    public ArrayList<Student> getResidents() {
        return residents;
    }

    public void setTotalRooms(int totalRooms) {
        this.totalRooms = totalRooms;
    }

    public void setOccupiedRooms(int occupiedRooms) {
        this.occupiedRooms = occupiedRooms;
    }

    public int getOccupiedRooms() {
        return occupiedRooms;
    }
    @Override
    public double calculateOperationalCost() {
        return maintenanceCost + (usageFrequency * 25) + (occupiedRooms * 50);
    }

    @Override
    public String toString() {
        return (super.toString() + "\nTotal Rooms: " + totalRooms + "\nOccupied Rooms: " + occupiedRooms + "\nAvailable Rooms: " + getAvailableRooms() + "\nOperational Cost: " + calculateOperationalCost());
    }
}

