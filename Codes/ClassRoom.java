import java.util.ArrayList;
class ClassRoom extends AcademicUnit {
    private int roomNumber;
    private int capacity;
    private boolean isAvailable;
    private ArrayList<ClassRoomEquipments> equipmentList = new ArrayList<>();
    public ClassRoom() {
        super();
        roomNumber=0;
        capacity=0;
        isAvailable=false;
    }
    public ClassRoom(String entityID,String name,String location,int roomNumber, int capacity, boolean isAvailable,int totalstaff,double budget) {
        super(entityID, name, location, totalstaff, budget);
        this.roomNumber = roomNumber;
        this.capacity = capacity;
        this.isAvailable = isAvailable;
    }
    public void setRoomNumber(int roomNumber) {
        this.roomNumber = roomNumber;
    }
    public int getRoomNumber() {
        return roomNumber;
    }
    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }
    public int getCapacity() {
        return capacity;
    }
    public void setAvailable(boolean isAvailable) {
        this.isAvailable = isAvailable;
    }
    public boolean isAvailable() {
        return isAvailable;
    }
    public void addEquipment(ClassRoomEquipments equipment) {
        if(equipmentList.contains(equipment)) {
            System.out.println("Equipment is Already Added in the ClassRoom");
            return;
        }
        equipmentList.add(equipment);
        System.out.printf("Equipment %s added to the classroom %d%n",equipment.getEquipmentName(),roomNumber);
    }
    public void removeEquipment(ClassRoomEquipments equipment) {
        if(equipmentList.contains(equipment)) {

            equipmentList.remove(equipment);
            System.out.printf("Equipment %s removed from the classroom %d%n", equipment.getEquipmentName(), roomNumber);
            return;
        } System.out.println("Equipment not found in the Classroom");
    }
    public ArrayList<ClassRoomEquipments> getEquipmentList() {
        return equipmentList;
    }
    public String toString() {
        return super.toString()+"\n"+String.format("Classroom Room Number:%d%nClassroom Capacity:%d%n",roomNumber,capacity);
    }
    public void DisplayClassRoomEquipment() {
        System.out.printf("Classroom Room Number:%d%n",roomNumber);
        System.out.println("===== Classroom Equipments =====");
        for (ClassRoomEquipments equipment : equipmentList) {
            System.out.println(equipment.getEquipmentName());
        }
    }
    public double calculateOperationalCost() {
        double totalcost = 0;
        for (ClassRoomEquipments equipment : equipmentList) {
            totalcost += equipment.getPrice() * equipment.getQuantity();
        }
        return totalcost;
    }
}