
class SecurityService extends ServiceUnit implements Notifiable {
   private int noofguards;
   private int guardsalary;
   public SecurityService() {
    super();
    noofguards=0;
    guardsalary=0;
   }
   public SecurityService(String entityID,String name,String location ,int noofguards,int guardsalary,int servicehours,double costperhour) {
    super(entityID,name,location,servicehours,costperhour);
    this.noofguards=noofguards;
    this.guardsalary=guardsalary;
    
   }
    public int getNoofguards() {
        return noofguards;
    }

    public void setNoofguards(int noofguards) {
        this.noofguards = noofguards;
    }

    public int getGuardsalary() {
        return guardsalary;
    }

    public void setGuardsalary(int guardsalary) {
        this.guardsalary = guardsalary;
    }
    public String toString() {
        return super.toString()+"\n"+String.format("No of Guards:%d%nGuard Salary:%d%n",noofguards,guardsalary);
    }

public double calculateOperationalCost() {
    double total=0;
    total+=noofguards*guardsalary;
    total+=servicehours*costperhour;
    return total;
}
public void sendNotification() {
    System.out.println("===== Security Alert =====");
    System.out.println();
    System.out.printf("Emergency At %s%n%nAll Staff Must be Active",location);
}
}
 




