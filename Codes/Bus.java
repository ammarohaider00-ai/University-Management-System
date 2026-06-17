import java.io.Serializable;
class Bus implements Serializable {
    private String busID;
    private int driversalary;
    private String from;
    private String to;
    private String departureTime;
    public Bus() {
          busID=null;
          driversalary=0;
          from=null;
           to=null;
           departureTime=null;
    }
    
    public Bus(String busID,int driversalary,String from,String to,String departuretime) {
        this.busID=busID;
        this.driversalary=driversalary;
        this.from=from;
        this.to=to;
        this.departureTime=departuretime;
    }
    public String getBusID() {
        return busID;
    }
    public void setBusID(String busID) {
        this.busID = busID;
    }
    public int getDriverSalary(){
        return driversalary;
    }
    public void setDriverSalary(int driversalary) {
        this.driversalary=driversalary;
    }
    
    public String getFrom() {
        return from;
    }
    public void setFrom(String from) {
        this.from = from;
    }
    public String getTo() {
        return to;
    }
    public void setTo(String to) {
        this.to = to;
    }
    public String getDepartureTime() {
        return departureTime;
    }
    public void setDepartureTime(String departureTime) {
        this.departureTime = departureTime;
    }
    public String toString() {
        return String.format("Bus ID:%s%nDriver Monthly Salary:%d%n =====Route ===== %nFrom:%s%nTo:%s%nDeparture Time:%s%n",busID,driversalary,from,to,departureTime);
    }

}

