
abstract class ServiceUnit extends CampusEntity {
    protected int servicehours;
    protected double costperhour;
    public ServiceUnit() {
        super();
         servicehours=0;
         costperhour=0;
    }   
       public ServiceUnit(String entityID, String name, String location,int servicehours,double costperhour) {
        super(entityID, name, location);
        this.servicehours=servicehours;
        this.costperhour=costperhour;
    }

    public String toString() {
        return super.toString()+String.format("Total Service Hours:%d%nCost per Hour:%.1f",servicehours,costperhour);
    }

    public int getServicehours() {
        return servicehours;
    }

    public void setServicehours(int servicehours) {
        this.servicehours = servicehours;
    }

    public double getCostperhour() {
        return costperhour;
    }

    public void setCostperhour(double costperhour) {
        this.costperhour = costperhour;
    }
    public abstract double calculateOperationalCost();
}
        


