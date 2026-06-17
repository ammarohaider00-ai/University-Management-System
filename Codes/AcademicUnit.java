import java.io.Serializable;
abstract class AcademicUnit extends CampusEntity implements Serializable {
    private int totalStaff;
    private double budget;
    protected  static int totalStudents=0;
    protected static int totalCourses=0;
    public AcademicUnit() {
        super();
        totalStaff = 0;
        budget = 0;
    }
    public AcademicUnit(String entityID, String name, String location,int totalStaff, double budget) {
        super(entityID, name, location);
        this.totalStaff = totalStaff;
        this.budget = budget;
    }
    public int getTotalStaff() {
        return totalStaff;
    }
    public void setTotalStaff(int totalStaff) {
        this.totalStaff = totalStaff;
    }
    public double getBudget() {
        return budget;
    }
    public void setBudget(double budget) {
        this.budget = budget;
    }
    public static int getTotalCourses() {
        return totalCourses;
    }
   public static int getTotalStudents() {
        return totalStudents;

    }
    public String toString() {
        return super.toString()+"Total Staff: "+totalStaff+"\n"+"Budget: "+budget+"\n";
    }
    public abstract double calculateOperationalCost();

}

    

   