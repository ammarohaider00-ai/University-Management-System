
import java.util.ArrayList;
class Lab extends AcademicUnit{
    private int labNo;;
    private ArrayList<LabEquipments> equipmentList=new ArrayList<>();
    public Lab() {
        super();
        labNo=0;  
    }          
        public Lab(String entityID,String name,String location,int labNo,int totalstaff,double budget) {
        super(entityID, name, location, totalstaff, budget);
        this.labNo= labNo;
    }
    public void setLabNo(int labNo) {
        this.labNo= labNo;
    }
    public int getLabNo() {
        return labNo;
    }
    public void AddEquipment(LabEquipments equipment) {
        if(equipmentList.contains(equipment)) {
            System.out.println("Equipment already exists");
            return;
        }
        equipmentList.add(equipment);
        System.out.printf("Equipment %s added to the %s%n",equipment.getName(),labNo);
    }
    public void RemoveEquipment(LabEquipments equipment) {
        if (equipmentList.contains(equipment)) {
            equipmentList.remove(equipment);
            System.out.printf("Equipment %s removed from the %s%n", equipment.getName(), labNo);
            return;
        }
        System.out.printf("Equipment %s is not in the list%n", equipment.getName());
    }
    public ArrayList<LabEquipments> getEquipmentList() {
        return equipmentList; 
    }
    public String toString() {
       return super.toString()+"\n"+String.format("Lab Number:%s%n",labNo); 
    
    }
    public void DisplayLabEquipment() {
        if(equipmentList.isEmpty()) {
            System.out.println("No equipment has been added");
            return;
        }
        System.out.printf("Lab Number:%s%n",labNo);
        System.out.println("===== Lab Equipments =====");
        for (LabEquipments equipment : equipmentList) {
            System.out.println(equipment.getName());
        
        }
    }
    public double calculateOperationalCost() {
        double totalCost = 0;
        for (LabEquipments equipment : equipmentList) {
            totalCost += equipment.getPrice()*equipment.getQuantity();
        }
        return totalCost;
    }
}