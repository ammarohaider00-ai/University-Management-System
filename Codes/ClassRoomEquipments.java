class ClassRoomEquipments {
    private String equipmentName;
    private int price;
    private int quantity;
    public ClassRoomEquipments() {
        equipmentName=null;
        price=0;
        quantity=0;
    }

    public ClassRoomEquipments(String equipmentName, int price, int quantity) {
        this.equipmentName = equipmentName;
        this.price = price;
        this.quantity = quantity;
    }

    public String getEquipmentName() {
        return equipmentName;
    }

    public void setEquipmentName(String equipmentName) {
        this.equipmentName = equipmentName;
    }

    public int getPrice() {
        return price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public void setPrice(int price) {
        this.price = price;
    }
    public String toString() {
        return String.format("Classroom Equipment Name:%s%nEquipment Price:%d%nEquipment Quantity:%d%n", equipmentName, price, quantity);
    }
}