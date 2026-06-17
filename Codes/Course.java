import java.io.Serializable;
import java.util.ArrayList;
class Course implements Scheduable,Serializable {
    private String courseName;
    private String courseCode;
    private String moderator;
    private int credits;
    private String starttime;
    private String endtime;
    private int roomNO;
    private ArrayList<Student> students=new ArrayList<>();
    private ArrayList<AssignmentObjects> assignmentobjects=new ArrayList<>();
    public Course() {
        courseName=null;
        courseCode=null;
        moderator=null;
        credits=0;
        starttime=null;
        endtime=null;
        roomNO=0;
    }
    public Course(String courseName, String courseCode, String moderator, int credits,String starttime,String endtime,int roomNO) {
        this.courseName=courseName;
        this.courseCode=courseCode;
        this.moderator=moderator;
        this.credits=credits;
        this.starttime=starttime;
        this.endtime=endtime;
        this.roomNO=roomNO;
    }
    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }
    public String getCourseName() {
        return courseName;    
    }

    public void setCourseCode(String courseCode) {
        this.courseCode = courseCode;
    }
    public String getCourseCode() {
        return courseCode;
    }
    public void setModerator(String moderator) {
        this.moderator = moderator;
    }
    public String getModerator() {
        return moderator;
    }
    public void setCredits(int credits) {
        this.credits = credits;
    }
    public int getCredits() {
        return credits;
    }
     public String getStarttime() {
        return starttime;
    }
    public void setStarttime(String starttime) {
        this.starttime = starttime;
    }
    public String getEndtime() {
        return endtime;
    }
    public void setEndtime(String endtime) {
        this.endtime = endtime;
    }
    public int getRoomNO() {
        return roomNO;
    }
    public void setRoomNO(int roomNO) {
        this.roomNO = roomNO;
    }
    public ArrayList<Student> getStudents() {
        return students;
    }
    public ArrayList<AssignmentObjects> getAssignmentobjects() {
        return assignmentobjects;
    }
    public boolean enrollStudent(Student student)  {

        for (Student enrolled : students) {
            if (enrolled.getRegNO().equalsIgnoreCase(student.getRegNO())) {
                System.out.println("Student with this RegNo is already enrolled in the course.");
                return false;
            }
        }
        students.add(student);
        AcademicUnit.totalStudents++;
        System.out.printf("Student %s enrolled in the course %s%n", student.getName(), courseName);
        return true;
    }
    public boolean removeStudent(Student student) {
        if (student == null || student.getRegNO() == null) {
            System.out.println("Invalid student cannot be removed.");
            return false;
        }
        for (Student enrolled : new ArrayList<>(students)) {
            if (enrolled.getRegNO().equalsIgnoreCase(student.getRegNO())) {
                students.remove(enrolled);
                System.out.printf("Student %s removed from the course %s%n", enrolled.getName(), courseName);
                AcademicUnit.totalStudents--;
                return true;
            }
        }
        System.out.println("Student is not enrolled in the course.");
        return false;
    }
    public boolean addAssignmentObject(AssignmentObjects temp) {
        if (temp == null) {
            System.out.println("Invalid assignment object.");
            return false;
        }
        for (AssignmentObjects existing : assignmentobjects) {
            if (existing.getObjecttype().equalsIgnoreCase(temp.getObjecttype())) {
                System.out.printf("%s already exists in the assignment objects for %s%n", temp.getObjecttype(), courseName);
                return false;
            }
        }
        assignmentobjects.add(temp);
        System.out.printf("===== %s Added To Assignment Objects =====%n", temp.getObjecttype());
        return true;
    }
    public void removeAssignmentObject(AssignmentObjects temp) {
        if(temp instanceof AssignmentObjects) {
            if (assignmentobjects.contains(temp)) {
                assignmentobjects.remove(temp);
                System.out.printf("===== %s Removed From Assignment Objects =====%n", temp.getObjecttype());
                return;
            }
            System.out.printf("%s not Found in AssignmentObjects%n", temp.getObjecttype());
        }
        else {
            System.out.println("Object is not Instance of AssignmentObject");
        }
    }
    public String toString() {
        return String.format("Course Name:%s%nCourse Code:%s%nCredits:%d%nModerator:%s%nStart Time:%s%nEnd Time:%s%nClassRoom No:%d%n",courseName,courseCode,credits,moderator,starttime,endtime,roomNO);
    }
    public void DisplayStudents() {
        System.out.printf("Course Name:%s%n",courseName);
        System.out.println("=======Enrolled Students========");
        for (Student student : students) {
            System.out.println(student.toString());
        }
    }
    public void DisplayAssignmentObjects() {
        for(AssignmentObjects a:assignmentobjects) {
            System.out.println(a.toString());
            System.out.println();
        }
    }
    public double calculateOperationalCost() {
        double totalcost=0;
        for (Student student : students) {
            totalcost += student.getFee(); 
        }
        return totalcost;
    }
    public void generateSchedule() {
        toString();
    }
    
}