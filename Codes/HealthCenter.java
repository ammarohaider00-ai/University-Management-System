class HealthCenter extends ServiceUnit implements Notifiable {
    private int noofdoctors;
    private int doctorsalary;
    private int noofnurses;
    private int nursesalary;
    public HealthCenter() {
        super();
        noofdoctors=0;
        doctorsalary=0;
        noofnurses=0;
        nursesalary=0;
    }
    public HealthCenter(String entityID,String name,String location,int noofdoctors,int doctorsalary,int noofnurses,int nursesalary,int servicehours,int costperhour) {
        super(entityID, name, location, servicehours, costperhour);
        this.noofdoctors = noofdoctors;
        this.doctorsalary = doctorsalary;
        this.noofnurses = noofnurses;
        this.nursesalary = nursesalary;
    }
    public int getNoofdoctors() {
        return noofdoctors;
    }

    public void setNoofdoctors(int noofdoctors) {
        this.noofdoctors = noofdoctors;
    }

    public int getDoctorsalary() {
        return doctorsalary;
    }

    public void setDoctorsalary(int doctorsalary) {
        this.doctorsalary = doctorsalary;
    }

    public int getNoofnurses() {
        return noofnurses;
    }

    public void setNoofnurses(int noofnurses) {
        this.noofnurses = noofnurses;
    }

    public int getNursesalary() {
        return nursesalary;
    }

    public void setNursesalary(int nursesalary) {
        this.nursesalary = nursesalary;
    }

    public String toString() {
        return super.toString() +"\n"+String.format("No of Doctors:%d%nDoctor Salary:%d%nNo of Nurses:%d%nNurse Salary:%d%n",noofdoctors,doctorsalary,noofnurses,nursesalary);
    }
    public double calculateOperationalCost() {
        double totalcost=0;
        totalcost+=noofdoctors * doctorsalary;
        totalcost+=noofnurses * nursesalary;
        totalcost+=servicehours * costperhour;
        return totalcost;
    }
    public void sendNotification() {
        System.out.println("===== Emergency Alert =====");
        System.out.println();
        System.out.println("=== All Doctors and Nurses must reach Health Unit ====");
        System.out.println();
    }
}
    
