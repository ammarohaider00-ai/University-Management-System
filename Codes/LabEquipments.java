class LabEquipments {
    private String name;
    private int price;
    private int quantity;
    public LabEquipments() {
        name=null;
        price=0;
        quantity=0;
    }
    public LabEquipments(String name, int price, int quantity) {
        this.name = name;
        this.price = price;
        this.quantity = quantity;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getName() {
        return name;
    }   
    public void setPrice(int price) {
        this.price = price;
    }
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
    public int getQuantity() {
        return quantity;
    }
    public int getPrice() {
        return price;
    }
    public String toString() {   
     return String.format("Equipment Name:%s%nEquipment Price:%d%nEquipment Quantity:%d%n",name,price,quantity);
    }
}