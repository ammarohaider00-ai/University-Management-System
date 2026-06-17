import java.util.ArrayList;
class Department extends AcademicUnit implements Reportable {
    private ArrayList<Course> courses = new ArrayList<Course>();
    private ArrayList<ClassRoom> classrooms = new ArrayList<ClassRoom>();
    private ArrayList<Course> conflictCourses = new ArrayList<Course>();
    private int[] rooms = {101, 209, 210, 306, 110, 206, 301};

    public Department() {
        super();
    }

    public Department(String entityID, String name, String location,int totalstaff,double budget) {
        super(entityID, name, location, totalstaff, budget);
    }

    public ArrayList<Course> getCourses() {
        return courses;
    }

    public void AddCourse(Course course) {
        for (Course c : courses) {
            if (c.getCourseCode().equalsIgnoreCase(course.getCourseCode())) {
                System.out.println("Course already exists in the department.");
                return;
            }
        }
        courses.add(course);
        totalCourses++;
        System.out.printf("Course %s added to the department%n", course.getCourseName());
    }

    public void removeCourse(Course course) {
        for(Course c : courses) {
            if (c.getCourseCode().equalsIgnoreCase(course.getCourseCode())) {
                courses.remove(c);
                totalCourses--;
                System.out.printf("Course %s removed from the department%n", course.getCourseName());
                return;
            }
        }
        System.out.printf("Course %s not Exists in the department%n", course.getCourseName());
    }

    public void AddClassroom(ClassRoom classroom) {
        if (classroom == null) {
            System.out.println("Invalid classroom cannot be added.");
            return;
        }
        for (ClassRoom c : classrooms) {
            if (c.getRoomNumber() == classroom.getRoomNumber()) {
                System.out.println("Classroom already exists in the department.");
                return;
            }
        }
        classrooms.add(classroom);
        System.out.printf("ClassRoom %s Added to the Department", classroom.getRoomNumber());
    }

    public void RemoveClassRoom(ClassRoom classroom) {
        for(ClassRoom c : classrooms) {
            if (c.getRoomNumber() == classroom.getRoomNumber()) {
            classrooms.remove(classroom);
            System.out.printf("ClassRoom %s Removed from Department", classroom.getRoomNumber());
            return;
        }
    }
        System.out.printf("Classroom %d Not Exists in Department", classroom.getRoomNumber());
    }
    public ArrayList<ClassRoom> getClassrooms() {
        return classrooms;
    }

    public String toString() {
        return super.toString();
    }

    public double calculateOperationalCost() {
        double totalcost = 0;
        for (Course course : courses) {
            totalcost += course.calculateOperationalCost();
        }
        return totalcost;
    }

    public void DisplayCourses() {
        if (courses.isEmpty()) {
            System.out.println("No courses have been added");
            return;
        }
        System.out.printf("Department Name:%s%n", name);
        System.out.println("=======Courses Offered========");
        for (Course course : courses) {
            System.out.println(course.toString());
        }
    }

    public void generateReport() {
        System.out.printf("Department Name:%s%n", name);
        System.out.println("=======Courses Offered========");
        if (courses.isEmpty()) {
            System.out.println("No courses is offered");
            return;
        }
        for (Course course : courses) {
                System.out.println(course.toString());
                System.out.println();
                for (Student student : course.getStudents()) {
                    System.out.println("==== Student Details ====");
                    System.out.printf("Student Name:%s%nRegistration Number:%s%nDepartment:%s%nCourse:%s%nFee:%d%nCGPA:%.2f%n%n", student.getName(), student.getRegNO(), student.getDepartment(), course.getCourseName(), student.getFee(), student.getCGPA());
                }
            }
    }

    public void rescheduleCourses(ClassRoom classroom) {
        if (!classroom.isAvailable()) {
            System.out.printf("Classroom %d is not available%n", classroom.getRoomNumber());
            int courseaffected = 0;
            for (Course course : courses) {
                if (course.getRoomNO() == classroom.getRoomNumber()) {
                    courseaffected++;
                    System.out.printf("%s Class will be held Tomorrow According to Same Schedule%n", course.getCourseName());
                    System.out.println();
                }
            }
            if (courseaffected == 0) {
                System.out.println("No Course Affected due to Class Unavailability");
            } else {
                System.out.printf("%d Courses Affected due to Class Unavailability%n", courseaffected);
            }
        } else {
            System.out.println("ClassRoom is Available");
        }
    }

    public void  ResolveConflict() {
        conflictCourses.clear();
        for (int i = 0; i < courses.size(); i++) {
            for (int j = i + 1; j < courses.size(); j++) {
                Course t1 = courses.get(i);
                Course t2 = courses.get(j);
                if (t1.getRoomNO() == t2.getRoomNO() && t1.getStarttime().equalsIgnoreCase(t2.getStarttime()) && t1.getEndtime().equalsIgnoreCase(t2.getEndtime())) {
                    System.out.println("==== Conflict Found ====");
                    System.out.println();
                    System.out.printf("Course %s and Course %s have a scheduling conflict in Room %d%n", t1.getCourseName(), t2.getCourseName(), t1.getRoomNO());
                    System.out.println();
                    conflictCourses.add(t2);
                }
            }
        }
        for (Course course : conflictCourses) {
            boolean resolved = false;
            for (int k = 0; k < rooms.length; k++) {
                boolean roomBusy = false;
                for (Course c : courses) {
                    if (c != course && c.getRoomNO() == rooms[k] && c.getStarttime().equalsIgnoreCase(course.getStarttime()) && c.getEndtime().equalsIgnoreCase(course.getEndtime())) {
                        roomBusy = true;
                        break;
                    }
                }
                if (!roomBusy) {
                    course.setRoomNO(rooms[k]);
                    System.out.printf("%s shifted to Room %d%n", course.getCourseName(), rooms[k]);
                    resolved = true;
                    break;
                }
            }
            if (!resolved) {
                System.out.println("=== No Room Available Yet ===");
                System.out.printf("New Schedule for %s will be shared soon%n", course.getCourseName());
            }
        }
        if (conflictCourses.isEmpty()) {
            System.out.println("No scheduling conflicts were found for this department.");
        }
        conflictCourses.clear();
    }
}   
