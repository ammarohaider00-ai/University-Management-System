class Student extends User {
    ;
    private String regNO;
    private String department;
    private int fee;
    private double CGPA;

    public Student() {
        super();
        regNO = null;
        department = null;
        fee = 0;
        CGPA = 0;
    }

    public Student(String name, String userType, String regNO, String department, int fee, double CGPA) {
        super(name, userType);
        this.regNO = regNO;
        this.department = department;
        this.fee = fee;
        this.CGPA = CGPA;
    }

    public void enrollCourse(Course course) {
        course.enrollStudent(this);
    }

    public void deleteCourse(Course course) {
        course.removeStudent(this);
    }

    public void setRegNO(String regNO) {

        this.regNO = regNO;
    }

    public String getRegNO() {
        return regNO;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getDepartment() {
        return department;
    }

    public void setFee(int fee) {
        this.fee = fee;
    }

    public int getFee() {
        return fee;
    }

    public double getCGPA() {
        return CGPA;
    }

    public void setCGPA(double cGPA) {
        CGPA = cGPA;
    }

    public String toString() {
        return super.toString() + String.format("Registration Number:%s%nDepartment:%s%nFee:%d%nCGPA:%.2f", regNO,
                department, fee, CGPA);
    }

    public void viewCourseSchedule(Course course) {
        course.generateSchedule();
    }

    public void viewReport(Department department) {
        department.generateReport();
    }

}