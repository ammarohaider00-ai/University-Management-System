class Admin extends User implements Notifiable {

    private String adminID;
    private Department dept;
    private Cafeteria caf;
    private Hostels hostel;
    private Library library;
    private TransportService transport;
    private SecurityService security;
    private HealthCenter health;

    public Admin() {
        super();
        adminID = null;
        dept = null;
        caf = null;
        hostel = null;
        library = null;
        transport = null;
        security = null;
        health = null;
    }

    public Admin(String name,String userType, String adminID, Department dept, Cafeteria caf, Hostels hostel, Library library, TransportService transport, SecurityService security, HealthCenter health) {
        super(name,userType);
        this.adminID = adminID;
        this.dept = dept;
        this.caf = caf;
        this.hostel = hostel;
        this.library = library;
        this.transport = transport;
        this.security = security;
        this.health = health;
    }

    public String getAdminID() {
        return adminID;
    }

    public void setAdminID(String adminID) {
        this.adminID = adminID;
    }

    public String toString() {
        return super.toString() +String.format("AdminID:%s%n",adminID);
    }

    public void addCourseToDepartment(Course c) {
        dept.AddCourse(c);
    }

    public void removeCourseFromDepartment(Course c) {
        dept.removeCourse(c);
    }

    public void addClassroomToDepartment(ClassRoom c) {
        dept.AddClassroom(c);
    }

    public void removeClassroomFromDepartment(ClassRoom c) {
        dept.RemoveClassRoom(c);
    }

    public void viewDepartmentDetails() {
        System.out.println(dept.toString());
        System.out.println();
        dept.DisplayCourses();
    }

    public void generateDepartmentReport() {
        dept.generateReport();
    }

    public boolean addStudentToCourse(Course c, Student s) {
        return c.enrollStudent(s);
    }

    public boolean removeStudentFromCourse(Course c, Student s) {
        return c.removeStudent(s);
    }

    public void viewStudentsInCourse(Course c) {
        c.DisplayStudents();
    }

    public void addAssignmentObject(Course c, AssignmentObjects a) {
        c.addAssignmentObject(a);
    }

    public void removeAssignmentObject(Course c, AssignmentObjects a) {
        c.removeAssignmentObject(a);
    }

    public void viewAssignmentObjects(Course c) {
        c.DisplayAssignmentObjects();
    }

    public void addMenuItem(String item) {
        caf.addMenuItem(item);
    }

    public void removeMenuItem(String item) {
        caf.removeMenuItem(item);
    }

    public void viewCafeteriaDetails() {
        System.out.println(caf.toString());
        System.out.println();
        caf.DisplayMenu();
    }

    public boolean addHostelResident(Student resident) {
        return hostel.addResident(resident);
    }

    public boolean removeHostelResident(Student resident) {
        return hostel.removeResident(resident);
    }

    public void viewHostelDetails() {
        System.out.println(hostel.toString());
        System.out.println();
        hostel.viewResidents();
    }

    public boolean addBook(LibraryBooks b) {
        return library.addBook(b);
    }

    public void removeBook(LibraryBooks b) {
        library.removeBook(b);
    }


    public void viewLibraryDetails() {
        System.out.println(library.toString());
        System.out.println();
        library.displayBooks();
    }

    public void generateLibraryReport() {
        library.generateReport();
    }

    public void addBus(Bus b) {
        transport.addBus(b);
    }

    public void removeBus(Bus b) {
        transport.removeBus(b);
    }

    public void viewTransportSchedule() {
        transport.generateSchedule();
    }

    public String viewTransportScheduleText() {
        return transport.getScheduleDetails();
    }

    public void viewTransportDetails() {
        System.out.println(transport.toString());
    }

    public String viewTransportDetailsText() {
        return transport.getDetailedInfo();
    }

    public void viewSecurityDetails() {
        System.out.println(security.toString());
    }

    public void activateSecurityAlert() {
        security.sendNotification();
    }

    public void viewHealthCenterDetails() {
        System.out.println(health.toString());
    }

    public void activateMedicalEmergency() {
        health.sendNotification();
    }

    public void viewOperationalCosts() {
        System.out.println("===== Operational Costs =====");
        System.out.println();

        System.out.printf("Department Cost:%.2f%n", dept.calculateOperationalCost());
        System.out.printf("Cafeteria Cost:%.2f%n", caf.calculateOperationalCost());
        System.out.printf("Hostel Cost:%.2f%n", hostel.calculateOperationalCost());
        System.out.printf("Library Cost:%.2f%n", library.calculateOperationalCost());
        System.out.printf("Transport Cost:%.2f%n", transport.calculateOperationalCost());
        System.out.printf("Security Cost:%.2f%n", security.calculateOperationalCost());
        System.out.printf("Health Center Cost:%.2f%n", health.calculateOperationalCost());
    }

    public String viewOperationalCostsText() {
        return String.format(
            "===== Operational Costs =====\n\n" +
            "Department Cost: %.2f\n" +
            "Cafeteria Cost: %.2f\n" +
            "Hostel Cost: %.2f\n" +
            "Library Cost: %.2f\n" +
            "Transport Cost: %.2f\n" +
            "Security Cost: %.2f\n" +
            "Health Center Cost: %.2f\n",
            dept.calculateOperationalCost(), caf.calculateOperationalCost(), hostel.calculateOperationalCost(), library.calculateOperationalCost(), transport.calculateOperationalCost(), security.calculateOperationalCost(), health.calculateOperationalCost());
    }

    public void markClassroomUnavailable(ClassRoom c) {
        if (!c.isAvailable()) {
            System.out.printf("Classroom %d is Already Unavailable%n", c.getRoomNumber());
            return;
        }
        c.setAvailable(false);
        dept.rescheduleCourses(c);
    }
    public void markClassroomAvailable(ClassRoom c) {
        if (c.isAvailable()) {
            System.out.printf("Classroom %d is Already Available%n", c.getRoomNumber());
            return;
        }
        c.setAvailable(true);
        System.out.printf("Classroom %d is Now Available%n", c.getRoomNumber());
    }

    public void sendNotification() {
        System.out.println("===== Admin Notification =====");
        System.out.println();
        System.out.println(" ==== Admin Notification Sent to Relevant Authority ====");
    }
    public void peakHoursAdjustment() {
        transport.peakHoursAdjustment();
    }
    public void ActivateMedicalEmergency() {
        System.out.println("===== Medical Emergency =====");
        health.sendNotification();
        security.sendNotification();
    }
    public void resolveCourseConflicts() {
        dept.ResolveConflict();
    }
}