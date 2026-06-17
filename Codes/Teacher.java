class Teacher extends User {
    private String teacherID;
    private int salary;
    public Teacher() {
        super();
        teacherID=null;
        salary=0;
    }
    public Teacher(String name,String userType,String teacherID,int salary) {
        super(name,userType);
        this.teacherID=teacherID;
        this.salary=salary;
    }

    public String getID() {
        return teacherID;
    }

    public void setID(String teacherID) {
        this.teacherID = teacherID;
    }

    public int getSalary() {
        return salary;
    }

    public void setSalary(int salary) {
        this.salary = salary;
    }
    public boolean addStudentoCourse(Course c, Student s) {
        return c.enrollStudent(s);
    }
    public void viewDepartmentReport(Department d) {
        d.generateReport();
    }
    public void addAssignmentObject(Course c,AssignmentObjects a) {
        c.addAssignmentObject(a);
        System.out.printf("%s Added to Course %s",a.getObjecttype(),c.getCourseName());
    }
    public String toString() {

        return super.toString()+String.format("Teacher ID:%sTeacher Salary:%d",teacherID,salary);
    }
}
