import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;
import javax.swing.table.DefaultTableModel;

public class CampusManagementGUI extends JFrame {
    private CampusRepository<Student> studentRepo;
    private CampusRepository<Teacher> teacherRepo;
    private CampusRepository<Admin> adminRepo;
    private CampusRepository<Department> departmentRepo;
    private CampusRepository<Course> courseRepo;
    private CampusRepository<Library> libraryRepo;
    private CampusRepository<Cafeteria> cafeteriaRepo;
    private CampusRepository<Hostels> hostelsRepo;
    private CampusRepository<TransportService> transportRepo;
    private CampusRepository<SecurityService> securityRepo;
    private CampusRepository<HealthCenter> healthRepo;
    private CampusRepository<ClassRoom> classroomRepo;
    private CampusRepository<Bus> busRepo;
    private CampusRepository<LibraryBooks> libraryBooksRepo;
    private CampusRepository<Lab> labRepo;

    private User currentUser;
    private JTabbedPane tabbedPane;
    private CampusMapPanel campusMapPanel;
    private JButton logoutButton;
    private JTextArea consoleOutputArea;

    public CampusManagementGUI() {
        studentRepo = new CampusRepository<>();
        teacherRepo = new CampusRepository<>();
        adminRepo = new CampusRepository<>();
        departmentRepo = new CampusRepository<>();
        courseRepo = new CampusRepository<>();
        libraryRepo = new CampusRepository<>();
        cafeteriaRepo = new CampusRepository<>();
        hostelsRepo = new CampusRepository<>();
        transportRepo = new CampusRepository<>();
        securityRepo = new CampusRepository<>();
        healthRepo = new CampusRepository<>();
        classroomRepo = new CampusRepository<>();
        busRepo = new CampusRepository<>();
        libraryBooksRepo = new CampusRepository<>();
        labRepo = new CampusRepository<>();
        consoleOutputArea = new JTextArea();
        consoleOutputArea.setEditable(false);
        consoleOutputArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        redirectSystemStreams();
        loadSampleData();
        LoadSavedData();

        if (currentUser == null && !showLoginScreen()) {
            System.exit(0);
        }

        setupMainInterface();
    }

    private void loadSampleData() {
        studentRepo.AddItem(new Student("Ali", "Student", "FA25-BCS-009", "Computer Science", 50000, 3.8));
        teacherRepo.AddItem(new Teacher("Dr.Majid", "Teacher", "T001", 100000));
        departmentRepo.AddItem(new Department("DEPT001", "Computer Science", "AB 2", 25, 500000.0));
        courseRepo.AddItem(new Course("Discrete Structures", "CS101", "Dr.Majid", 3, "09:00", "10:30", 101));
        libraryRepo.AddItem(new Library("LIB001", "Junaid Zaidi Library", "Near AB-2", 15000.0, 100, 200));
        cafeteriaRepo.AddItem(new Cafeteria("CAF001", "SSC", "Sports Complex", 8000.0, 80, 300));
        hostelsRepo.AddItem(new Hostels("HOST001", " Scholar Boys Hostel", "Hostel City", 25000.0, 90, 100, 95));

        TransportService transportService = new TransportService("TRANS001", "Campus Transport", "Gate 3", 12, 50.0, false);
        transportService.addBus(new Bus("BUS001", 25000, "PWD", "Main Campus", "08:00"));
        transportService.addBus(new Bus("BUS002", 26000, "DHA", "Main Campus", "09:00"));
        transportRepo.AddItem(transportService);

        SecurityService securityService = new SecurityService("SEC001", "Campus Security", "All Areas", 10, 30000, 24, 25.0);
        securityRepo.AddItem(securityService);

        HealthCenter healthCenter = new HealthCenter("HEALTH001", "Medical Center", "Building D", 5, 100000, 12, 75, 12, 75);
        healthRepo.AddItem(healthCenter);

        classroomRepo.AddItem(new ClassRoom("CLS001", "Room A", "AB 1", 101, 40, true, 4, 120000.0));
        labRepo.AddItem(new Lab("LAB001", "Computer Lab", "Building D", 1, 8, 90000.0));
        labRepo.AddItem(new Lab("LAB002", "Physics Lab", "Building E", 2, 6, 95000.0));

        Cafeteria caf = cafeteriaRepo.getCampusRepo().get(0);
        caf.addMenuItem("Sandwich");
        caf.addMenuItem("Tea");
        System.out.println();

        Library lib = libraryRepo.getCampusRepo().get(0);
        lib.addBook(new LibraryBooks("Java", "Liang", false));
        lib.addBook(new LibraryBooks("Database Systems", "Harry", false));

        Hostels hostel = hostelsRepo.getCampusRepo().get(0);
        hostel.addResident(studentRepo.getCampusRepo().get(0));

        Department dept = departmentRepo.getCampusRepo().get(0);
        dept.AddClassroom(classroomRepo.getCampusRepo().get(0));
        dept.AddCourse(courseRepo.getCampusRepo().get(0));
        courseRepo.getCampusRepo().get(0).enrollStudent(studentRepo.getCampusRepo().get(0));

        adminRepo = new CampusRepository<>();
        adminRepo.AddItem(new Admin("Ahsan", "Admin", "A001", dept, caf, hostel, lib, transportService, securityService, healthCenter));
    }

    private void LoadSavedData() {
        BackupSystem backup = new BackupSystem();
        if (backup.loadSavedData()) {
            studentRepo = backup.getStudentRepo();
            teacherRepo = backup.getTeacherRepo();
            adminRepo = backup.getAdminRepo();
            departmentRepo = backup.getDepartmentRepo();
            courseRepo = backup.getCourseRepo();
            libraryRepo = backup.getLibraryRepo();
            cafeteriaRepo = backup.getCafeteriaRepo();
            hostelsRepo = backup.getHostelsRepo();
            transportRepo = backup.getTransportRepo();
            securityRepo = backup.getSecurityRepo();
            healthRepo = backup.getHealthRepo();
            classroomRepo = backup.getClassroomRepo();
            busRepo = backup.getBusRepo();
            libraryBooksRepo = backup.getLibraryBooksRepo();
            labRepo = backup.getLabRepo();
        }
    }

    private void autoSaveData() {
        BackupSystem backup = new BackupSystem(
                studentRepo, teacherRepo, adminRepo, departmentRepo,
                courseRepo, libraryRepo, cafeteriaRepo, hostelsRepo,
                transportRepo, securityRepo, healthRepo, classroomRepo,
                busRepo, libraryBooksRepo, labRepo);
        backup.SaveData();
    }

    private void setupMainInterface() {
        setTitle("Campus Management System");
        setSize(1200, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setJMenuBar(createMenuBar());

        tabbedPane = new JTabbedPane();

        if (!isStudent()) {
            JPanel dashboardPanel = createDashboardPanel();
            tabbedPane.addTab("Dashboard", dashboardPanel);
        }

        campusMapPanel = new CampusMapPanel(classroomRepo, libraryRepo, cafeteriaRepo,
                hostelsRepo, transportRepo, securityRepo, healthRepo, departmentRepo);
        tabbedPane.addTab("Campus Map", campusMapPanel);

        if (isAdmin()) {
            JPanel usersPanel = createUsersPanel();
            tabbedPane.addTab("Users", usersPanel);

            JPanel coursesPanel = createCoursesPanel();
            tabbedPane.addTab("Courses", coursesPanel);

            JPanel departmentsPanel = createDepartmentsPanel();
            tabbedPane.addTab("Departments", departmentsPanel);
        } else if (isTeacher()) {
            JPanel coursesPanel = createCoursesPanel();
            tabbedPane.addTab("Courses", coursesPanel);
        }

        if (isAdmin()) {
            JPanel facilitiesPanel = createFacilitiesPanel();
            tabbedPane.addTab("Facilities", facilitiesPanel);

            JPanel adminPanel = createAdminPanel();
            tabbedPane.addTab("Admin Tools", adminPanel);
        }
        if (isTeacher()) {
            JPanel teacherPanel = createTeacherToolPanel();
            tabbedPane.addTab("Teacher Tools", teacherPanel);
        }

        if (isStudent()) {
            JPanel studentPanel = createStudentToolPanel();
            tabbedPane.addTab("Student Tools", studentPanel);
        }

        JTabbedPane reportsPanel = createReportsPanel();
        tabbedPane.addTab("Reports", reportsPanel);
        tabbedPane.addTab("Console", createConsolePanel());

        JPanel topPanel = new JPanel(new BorderLayout());
        JLabel userInfoLabel = new JLabel(
                "Logged in as: " + currentUser.getName() + " (" + currentUser.getUserType() + ")");
        userInfoLabel.setBorder(BorderFactory.createEmptyBorder(8, 12, 8, 12));
        topPanel.add(userInfoLabel, BorderLayout.WEST);

        logoutButton = new JButton("Logout");
        logoutButton.addActionListener(e -> logout());
        topPanel.add(logoutButton, BorderLayout.EAST);

        add(topPanel, BorderLayout.NORTH);
        add(tabbedPane, BorderLayout.CENTER);
    }

    private boolean showLoginScreen() {
        JDialog loginDialog = new JDialog(this, "Campus Management Login", true);
        loginDialog.setSize(400, 320);
        loginDialog.setLocationRelativeTo(this);

        JPanel loginPanel = new JPanel(new GridLayout(4, 2, 8, 8));
        loginPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        loginPanel.add(new JLabel("Role:"));
        JComboBox<String> roleCombo = new JComboBox<>(new String[] { "Admin", "Teacher", "Student" });
        loginPanel.add(roleCombo);

        loginPanel.add(new JLabel("Username:"));
        JTextField usernameField = new JTextField();
        loginPanel.add(usernameField);

        loginPanel.add(new JLabel("ID:"));
        JTextField idField = new JTextField();
        loginPanel.add(idField);

        JButton loginButton = new JButton("Login");
        JButton cancelButton = new JButton("Cancel");

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.add(loginButton);
        buttonPanel.add(cancelButton);

        JPanel demoPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        demoPanel.setBorder(BorderFactory.createTitledBorder("Demo Admin Credentials"));
        JLabel demoLabel = new JLabel("Username: Ahsan   |   ID: A001");
        demoLabel.setFont(new Font("Monospaced", Font.PLAIN, 12));
        demoPanel.add(demoLabel);

        final boolean[] loggedIn = { false };

        loginButton.addActionListener(e -> {
            String role = (String) roleCombo.getSelectedItem();
            String username = usernameField.getText().trim();
            String id = idField.getText().trim();

            if (username.isEmpty() || id.isEmpty()) {
                JOptionPane.showMessageDialog(loginDialog, "Please enter both username and ID.", "Validation Error",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }

            User user = authenticateUser(username, id, role);
            if (user != null) {
                currentUser = user;
                loggedIn[0] = true;
                loginDialog.dispose();
            } else {
                JOptionPane.showMessageDialog(loginDialog, "Invalid credentials or role does not match.",
                        "Login Failed", JOptionPane.ERROR_MESSAGE);
            }
        });

        cancelButton.addActionListener(e -> {
            loggedIn[0] = false;
            loginDialog.dispose();
        });

        loginDialog.add(loginPanel, BorderLayout.CENTER);
        loginDialog.add(buttonPanel, BorderLayout.SOUTH);
        loginDialog.add(demoPanel, BorderLayout.NORTH);
        loginDialog.getRootPane().setDefaultButton(loginButton);
        loginDialog.setVisible(true);

        return loggedIn[0];
    }

    private User authenticateUser(String username, String id, String role) {
        String requestedName = username.trim().toLowerCase();
        String requestedId = id.trim().toLowerCase();
        switch (role) {
            case "Admin":
                for (Admin admin : adminRepo.getCampusRepo()) {
                    String storedName = admin.getName().toLowerCase();
                    String storedId = admin.getAdminID().toLowerCase();
                    if (storedId.equals(requestedId) && (storedName.equals(requestedName)
                            || storedName.contains(requestedName) || requestedName.contains(storedName))) {
                        return admin;
                    }
                }
                break;
            case "Teacher":
                for (Teacher teacher : teacherRepo.getCampusRepo()) {
                    String storedName = teacher.getName().toLowerCase();
                    String storedId = teacher.getID().toLowerCase();
                    if (storedId.equals(requestedId) && (storedName.equals(requestedName)
                            || storedName.contains(requestedName) || requestedName.contains(storedName))) {
                        return teacher;
                    }
                }
                break;
            case "Student":
                for (Student student : studentRepo.getCampusRepo()) {
                    String storedName = student.getName().toLowerCase();
                    String storedId = student.getRegNO().toLowerCase();
                    if (storedId.equals(requestedId) && (storedName.equals(requestedName)
                            || storedName.contains(requestedName) || requestedName.contains(storedName))) {
                        return student;
                    }
                }
                break;
        }
        return null;
    }

    private boolean isAdmin() { return currentUser instanceof Admin; }
    private boolean isTeacher() { return currentUser instanceof Teacher; }
    private boolean isStudent() { return currentUser instanceof Student; }
    private boolean canModifyEntity(String entityType) { return isAdmin(); }

    private void logout() {
        int choice = JOptionPane.showConfirmDialog(this, "Are you sure you want to logout?", "Logout",
                JOptionPane.YES_NO_OPTION);
        if (choice == JOptionPane.YES_OPTION) {
            currentUser = null;
            dispose();
            new CampusManagementGUI().setVisible(true);
        }
    }

    private JPanel createTeacherToolPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        JPanel controls = new JPanel(new GridLayout(0, 2, 8, 8));
        controls.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        Teacher teacher = (Teacher) currentUser;

        java.util.List<Course> moderatedCourses = new java.util.ArrayList<>();
        for (Course c : courseRepo.getCampusRepo()) {
            if (c.getModerator().equalsIgnoreCase(teacher.getName())) {
                moderatedCourses.add(c);
            }
        }
        JComboBox<Course> courseCombo = new JComboBox<>(moderatedCourses.toArray(new Course[0]));
        JComboBox<Student> studentCombo = new JComboBox<>(studentRepo.getCampusRepo().toArray(new Student[0]));
        JComboBox<Department> departmentCombo = new JComboBox<>(departmentRepo.getCampusRepo().toArray(new Department[0]));

        controls.add(new JLabel("Course:"));
        controls.add(courseCombo);
        controls.add(new JLabel("Student:"));
        controls.add(studentCombo);
        controls.add(new JLabel("Department:"));
        controls.add(departmentCombo);

        JTextArea outputArea = new JTextArea(10, 40);
        outputArea.setEditable(false);
        outputArea.setFont(new Font("Monospaced", Font.PLAIN, 12));

        JPanel buttons = new JPanel(new GridLayout(0, 1, 6, 6));

        JButton addStudentButton = new JButton("Add Student to Course");
        addStudentButton.addActionListener(e -> {
            Course course = (Course) courseCombo.getSelectedItem();
            Student student = (Student) studentCombo.getSelectedItem();
            if (teacher != null && course != null && student != null) {
                if (!course.getModerator().equalsIgnoreCase(teacher.getName())) {
                    outputArea.setText("You are not the moderator of " + course.getCourseName() + ".");
                    return;
                }
                boolean added = teacher.addStudentoCourse(course, student);
                if (added) {
                    outputArea.setText("Student " + student.getName() + " added to " + course.getCourseName() + ".\n");
                    autoSaveData();
                } else {
                    outputArea.setText("Student " + student.getName() + " could not be added to "
                            + course.getCourseName() + ". He/She is already enrolled.\n");
                }
            }
        });

        JButton viewCourseStudentsButton = new JButton("View Students in Course");
        viewCourseStudentsButton.addActionListener(e -> {
            Course course = (Course) courseCombo.getSelectedItem();
            if (course != null) {
                if (!course.getModerator().equalsIgnoreCase(teacher.getName())) {
                    outputArea.setText("You are not the moderator of " + course.getCourseName() + ".");
                    return;
                }
                StringBuilder sb = new StringBuilder();
                sb.append("Students in ").append(course.getCourseName()).append(":\n\n");
                for (Student s : course.getStudents()) {
                    sb.append(s.toString()).append("\n\n");
                }
                outputArea.setText(sb.toString());
            }
        });

        JButton addAssignmentButton = new JButton("Add Assignment Object");
        addAssignmentButton.addActionListener(e -> {
            Course course = (Course) courseCombo.getSelectedItem();
            if (course == null) {
                JOptionPane.showMessageDialog(this, "Please select a course.", "Validation Error",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }
            if (!course.getModerator().equalsIgnoreCase(teacher.getName())) {
                outputArea.setText("You are not the moderator of " + course.getCourseName() + ".");
                return;
            }
            AssignmentObjects assignmentObject = showAssignmentObjectDialog(course);
            if (assignmentObject != null) {
                teacher.addAssignmentObject(course, assignmentObject);
                outputArea.setText("Assignment object added to " + course.getCourseName() + ".\n");
                autoSaveData();
            }
        });

        JButton viewDepartmentReportButton = new JButton("View Department Report");
        viewDepartmentReportButton.addActionListener(e -> {
            Department department = (Department) departmentCombo.getSelectedItem();
            if (department != null) {
                teacher.viewDepartmentReport(department);
                outputArea.setText("Department report generated to console for " + department.getName() + ".\n");
            }
        });

        buttons.add(addStudentButton);
        buttons.add(viewCourseStudentsButton);
        buttons.add(addAssignmentButton);
        buttons.add(viewDepartmentReportButton);

        panel.add(controls, BorderLayout.NORTH);
        panel.add(buttons, BorderLayout.CENTER);
        panel.add(new JScrollPane(outputArea), BorderLayout.SOUTH);

        return panel;
    }

    private JPanel createStudentToolPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        JPanel controls = new JPanel(new GridLayout(0, 2, 8, 8));
        controls.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel studentLabel = new JLabel(currentUser != null ? currentUser.getName() : "");
        JComboBox<Course> courseCombo = new JComboBox<>(courseRepo.getCampusRepo().toArray(new Course[0]));
        JComboBox<Department> departmentCombo = new JComboBox<>(departmentRepo.getCampusRepo().toArray(new Department[0]));
        JTextField bookTitleField = new JTextField();

        controls.add(new JLabel("Student:"));
        controls.add(studentLabel);
        controls.add(new JLabel("Course:"));
        controls.add(courseCombo);
        controls.add(new JLabel("Department:"));
        controls.add(departmentCombo);
        controls.add(new JLabel("Book Title:"));
        controls.add(bookTitleField);

        JTextArea outputArea = new JTextArea(10, 40);
        outputArea.setEditable(false);
        outputArea.setFont(new Font("Monospaced", Font.PLAIN, 12));

        JPanel buttons = new JPanel(new GridLayout(0, 1, 6, 6));

        JButton viewScheduleButton = new JButton("View Course Schedule");
        viewScheduleButton.addActionListener(e -> {
            Course course = (Course) courseCombo.getSelectedItem();
            if (course == null) {
                outputArea.setText("Please select a course first.");
                return;
            }
            Student student = (Student) currentUser;
            boolean enrolled = false;
            for (Student enrolledStudent : course.getStudents()) {
                if (enrolledStudent.getRegNO().equalsIgnoreCase(student.getRegNO())) {
                    enrolled = true;
                    break;
                }
            }
            if (!enrolled) {
                outputArea.setText("You are not enrolled in " + course.getCourseName() + ".");
                return;
            }
            StringBuilder sb = new StringBuilder();
            sb.append("=== COURSE SCHEDULE FOR ").append(course.getCourseName()).append(" ===\n\n");
            sb.append("Course Code: ").append(course.getCourseCode()).append("\n");
            sb.append("Moderator: ").append(course.getModerator()).append("\n");
            sb.append("Credits: ").append(course.getCredits()).append("\n");
            sb.append("Start Time: ").append(course.getStarttime()).append("\n");
            sb.append("End Time: ").append(course.getEndtime()).append("\n");
            sb.append("Room No: ").append(course.getRoomNO()).append("\n");
            sb.append("Enrolled Students: ").append(course.getStudents().size()).append("\n");
            outputArea.setText(sb.toString());
        });

        JButton viewReportButton = new JButton("View Department Report");
        viewReportButton.addActionListener(e -> {
            Department department = (Department) departmentCombo.getSelectedItem();
            if (department != null) {
                Student student = (Student) currentUser;
                student.viewReport(department);
                outputArea.setText("Department report generated to console for " + department.getName() + ".\n");
            }
        });

        JButton searchBookButton = new JButton("Search Book");
        searchBookButton.addActionListener(e -> {
            String title = bookTitleField.getText().trim();
            if (title.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please enter a book title to search.", "Validation Error",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }
            if (libraryRepo.getCampusRepo().isEmpty()) {
                outputArea.setText("No library has been set up yet.");
                return;
            }
            Library library = libraryRepo.getCampusRepo().get(0);
            LibraryBooks book = library.searchBook(title);
            if (book == null) {
                outputArea.setText("Book not found: " + title);
            } else {
                outputArea.setText("Book found:\n" +
                        "Title: " + book.getTitle() + "\n" +
                        "Author: " + book.getAuthor() + "\n" +
                        "Status: " + (book.isBorrowed() ? "Borrowed" : "Available") + "\n");
            }
        });

        JButton borrowBookButton = new JButton("Borrow Book");
        borrowBookButton.addActionListener(e -> {
            String title = bookTitleField.getText().trim();
            if (title.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please enter the title of the book to borrow.", "Validation Error",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }
            if (libraryRepo.getCampusRepo().isEmpty()) {
                outputArea.setText("No library has been set up yet.");
                return;
            }
            Library library = libraryRepo.getCampusRepo().get(0);
            LibraryBooks book = library.searchBook(title);
            if (book == null) {
                outputArea.setText("Book not found: " + title);
                return;
            }
            if (book.isBorrowed()) {
                outputArea.setText("Book is already borrowed: " + title);
                return;
            }
            boolean success = library.borrowBook(title, currentUser.getName());
            if (success) {
                outputArea.setText("Book borrowed successfully: " + title);
                autoSaveData();
            } else {
                outputArea.setText("Failed to borrow book: " + title + " (unknown error)");
            }
        });

        JButton returnBookButton = new JButton("Return Book");
        returnBookButton.addActionListener(e -> {
            String title = bookTitleField.getText().trim();
            if (title.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please enter the title of the book to return.", "Validation Error",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }
            if (libraryRepo.getCampusRepo().isEmpty()) {
                outputArea.setText("No library has been set up yet.");
                return;
            }
            Library library = libraryRepo.getCampusRepo().get(0);
            LibraryBooks book = library.searchBook(title);
            if (book == null) {
                outputArea.setText("Book not found: " + title);
                return;
            }
            if (!book.isBorrowed()) {
                outputArea.setText("This book is not currently borrowed: " + title);
                return;
            }
            boolean success = library.returnBook(title, currentUser.getName());
            if (success) {
                outputArea.setText("Book returned successfully: " + title);
                autoSaveData();
            } else {
                outputArea.setText("Return failed: You did not borrow this book.");
            }
        });
        JButton enrollCourseButton = new JButton("Enroll in Selected Course");
        enrollCourseButton.addActionListener(e -> {
            Course selectedCourse = (Course) courseCombo.getSelectedItem();
            if (selectedCourse == null) {
                outputArea.setText("Please select a course to enroll in.");
                return;
            }
            Student student = (Student) currentUser;
            boolean alreadyEnrolled = false;
            for (Student s : selectedCourse.getStudents()) {
                if (s.getRegNO().equalsIgnoreCase(student.getRegNO())) {
                    alreadyEnrolled = true;
                    break;
                }
            }
            if (alreadyEnrolled) {
                outputArea.setText("You are already enrolled in " + selectedCourse.getCourseName() + ".");
                return;
            }
            student.enrollCourse(selectedCourse);
            outputArea.setText("Successfully enrolled in " + selectedCourse.getCourseName() + ".");
            autoSaveData();
        });

        JButton dropCourseButton = new JButton("Drop Selected Course");
        dropCourseButton.addActionListener(e -> {
            Course selectedCourse = (Course) courseCombo.getSelectedItem();
            if (selectedCourse == null) {
                outputArea.setText("Please select a course to drop.");
                return;
            }
            Student student = (Student) currentUser;
            boolean enrolled = false;
            for (Student s : selectedCourse.getStudents()) {
                if (s.getRegNO().equalsIgnoreCase(student.getRegNO())) {
                    enrolled = true;
                    break;
                }
            }
            if (!enrolled) {
                outputArea.setText("You are not enrolled in " + selectedCourse.getCourseName() + ".");
                return;
            }
            student.deleteCourse(selectedCourse);
            outputArea.setText("Successfully dropped " + selectedCourse.getCourseName() + ".");
            autoSaveData();
        });

        buttons.add(viewScheduleButton);
        buttons.add(viewReportButton);
        buttons.add(searchBookButton);
        buttons.add(borrowBookButton);
        buttons.add(returnBookButton);
        buttons.add(enrollCourseButton);
        buttons.add(dropCourseButton);

        panel.add(controls, BorderLayout.NORTH);
        panel.add(buttons, BorderLayout.CENTER);
        panel.add(new JScrollPane(outputArea), BorderLayout.SOUTH);
        return panel;
    }

    private AssignmentObjects showAssignmentObjectDialog(Course course) {
        JPanel panel = new JPanel(new GridLayout(0, 2, 8, 8));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JComboBox<String> typeCombo = new JComboBox<>(new String[] { "Book", "VideoLecture" });
        JTextField objectTypeField = new JTextField();
        JTextField titleField = new JTextField();
        JTextField numericField = new JTextField();
        JLabel numericLabel = new JLabel("Pages / Duration:");

        panel.add(new JLabel("Assignment Type:"));
        panel.add(typeCombo);
        panel.add(new JLabel("Object Type:"));
        panel.add(objectTypeField);
        panel.add(new JLabel("Title:"));
        panel.add(titleField);
        panel.add(numericLabel);
        panel.add(numericField);

        typeCombo.addActionListener(e -> {
            if ("Book".equals(typeCombo.getSelectedItem())) {
                numericLabel.setText("No. of Pages:");
            } else {
                numericLabel.setText("Duration (min):");
            }
        });

        int result = JOptionPane.showConfirmDialog(this, panel, "Create Assignment Object",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (result != JOptionPane.OK_OPTION) return null;

        String objectType = objectTypeField.getText().trim();
        String title = titleField.getText().trim();
        String numericText = numericField.getText().trim();
        if (objectType.isEmpty() || title.isEmpty() || numericText.isEmpty()) {
            JOptionPane.showMessageDialog(this, "All fields are required.", "Validation Error",
                    JOptionPane.WARNING_MESSAGE);
            return null;
        }

        try {
            int value = Integer.parseInt(numericText);
            if ("Book".equals(typeCombo.getSelectedItem())) {
                return new Book(objectType, title, value);
            } else {
                return new VideoLecture(objectType, title, value);
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Please enter a valid numeric value for pages or duration.",
                    "Validation Error", JOptionPane.WARNING_MESSAGE);
            return null;
        }
    }

    private JPanel createDashboardPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        JTextArea summaryArea = new JTextArea();
        summaryArea.setEditable(false);
        summaryArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        JButton refreshButton = new JButton("Refresh Summary");
        refreshButton.addActionListener(e -> updateDashboardSummary(summaryArea));
        panel.add(new JScrollPane(summaryArea), BorderLayout.CENTER);
        panel.add(refreshButton, BorderLayout.SOUTH);
        updateDashboardSummary(summaryArea);
        return panel;
    }

    private void updateDashboardSummary(JTextArea area) {
        StringBuilder sb = new StringBuilder();
        sb.append("=== CAMPUS MANAGEMENT SYSTEM SUMMARY ===\n\n");

        int totalStudents = studentRepo.getCampusRepo().size();
        int totalCourses = courseRepo.getCampusRepo().size();

        int totalFacilityUsage = 0;
        for (Library l : libraryRepo.getCampusRepo()) totalFacilityUsage += l.getUsageFrequency();
        for (Cafeteria c : cafeteriaRepo.getCampusRepo()) totalFacilityUsage += c.getUsageFrequency();
        for (Hostels h : hostelsRepo.getCampusRepo()) totalFacilityUsage += h.getUsageFrequency();

        sb.append("Total Students (registered): ").append(totalStudents).append("\n");
        sb.append("Total Courses (active): ").append(totalCourses).append("\n");
        sb.append("Total Facility Usage: ").append(totalFacilityUsage).append("\n");
        sb.append("Borrowed Books: ").append(Library.getBorrowedBooks()).append("\n");
        sb.append("Returned Books: ").append(Library.getReturnedBooks()).append("\n\n");

        sb.append("STUDENTS: ").append(totalStudents).append("\n");
        sb.append("TEACHERS: ").append(teacherRepo.getCampusRepo().size()).append("\n");
        sb.append("ADMINS: ").append(adminRepo.getCampusRepo().size()).append("\n");
        sb.append("DEPARTMENTS: ").append(departmentRepo.getCampusRepo().size()).append("\n");
        sb.append("COURSES: ").append(totalCourses).append("\n");
        sb.append("LIBRARIES: ").append(libraryRepo.getCampusRepo().size()).append("\n");
        sb.append("CAFETERIAS: ").append(cafeteriaRepo.getCampusRepo().size()).append("\n");
        sb.append("HOSTELS: ").append(hostelsRepo.getCampusRepo().size()).append("\n");
        sb.append("TRANSPORT SERVICES: ").append(transportRepo.getCampusRepo().size()).append("\n");
        sb.append("SECURITY SERVICES: ").append(securityRepo.getCampusRepo().size()).append("\n");
        sb.append("HEALTH CENTERS: ").append(healthRepo.getCampusRepo().size()).append("\n\n");

        double totalCost = 0;
        totalCost += departmentRepo.calculateOperationalCost();
        totalCost += libraryRepo.calculateOperationalCost();
        totalCost += cafeteriaRepo.calculateOperationalCost();
        totalCost += hostelsRepo.calculateOperationalCost();
        totalCost += transportRepo.calculateOperationalCost();
        totalCost += securityRepo.calculateOperationalCost();
        totalCost += healthRepo.calculateOperationalCost();
        sb.append("TOTAL OPERATIONAL COST: $").append(String.format("%.2f", totalCost)).append("\n");
        area.setText(sb.toString());
    }

    private JPanel createUsersPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        JTabbedPane userTabs = new JTabbedPane();
        JPanel studentsPanel = createEntityPanel("Student", studentRepo,
                new String[] { "Name", "User Type", "Reg No", "Department", "Fee", "CGPA" });
        userTabs.addTab("Students", studentsPanel);
        if (isAdmin() || isTeacher()) {
            JPanel teachersPanel = createEntityPanel("Teacher", teacherRepo,
                    new String[] { "Name", "User Type", "Teacher ID", "Salary" });
            userTabs.addTab("Teachers", teachersPanel);
        }
        if (isAdmin()) {
            JPanel adminsPanel = createEntityPanel("Admin", adminRepo,
                    new String[] { "Name", "User Type", "Admin ID" });
            userTabs.addTab("Admins", adminsPanel);
        }
        panel.add(userTabs, BorderLayout.CENTER);
        return panel;
    }

    private JPanel createDepartmentsPanel() {
        return createEntityPanel("Department", departmentRepo,
                new String[] { "Entity ID", "Name", "Location", "Total Staff", "Budget" });
    }

    private JPanel createCoursesPanel() {
        return createEntityPanel("Course", courseRepo, new String[] { "Course Name", "Course Code", "Moderator",
                "Credits", "Start Time", "End Time", "Room No" });
    }

    private JPanel createFacilitiesPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        JTabbedPane facilityTabs = new JTabbedPane();
        JPanel libraryPanel = createEntityPanel("Library", libraryRepo, new String[] { "Entity ID", "Name", "Location",
                "Maintenance Cost", "Usage Frequency", "Visitors/Day" });
        facilityTabs.addTab("Library", libraryPanel);
        JPanel cafeteriaPanel = createEntityPanel("Cafeteria", cafeteriaRepo,
                new String[] { "Entity ID", "Name", "Location", "Maintenance Cost", "Usage Frequency" });
        facilityTabs.addTab("Cafeteria", cafeteriaPanel);
        JPanel hostelsPanel = createEntityPanel("Hostels", hostelsRepo,
                new String[] { "Entity ID", "Name", "Location", "Maintenance Cost", "Usage Frequency" });
        facilityTabs.addTab("Hostels", hostelsPanel);
        JPanel classroomPanel = createEntityPanel("ClassRoom", classroomRepo, new String[] { "Entity ID", "Name",
                "Location", "Room No", "Capacity", "Available", "Total Staff", "Budget" });
        facilityTabs.addTab("Classrooms", classroomPanel);
        JPanel labPanel = createEntityPanel("Lab", labRepo,
                new String[] { "Entity ID", "Name", "Location", "Lab No", "Total Staff", "Budget" });
        facilityTabs.addTab("Labs", labPanel);
        JPanel transportPanel = createTransportPanel();
        facilityTabs.addTab("Transport", transportPanel);
        JPanel securityPanel = createEntityPanel("SecurityService", securityRepo, new String[] { "Entity ID", "Name",
                "Location", "No of Guards", "Guard Salary", "Service Hours", "Cost per Hour" });
        facilityTabs.addTab("Security", securityPanel);
        JPanel healthPanel = createEntityPanel("HealthCenter", healthRepo,
                new String[] { "Entity ID", "Name", "Location", "No of Doctors", "Doctor Salary", "No of Nurses",
                        "Nurse Salary", "Service Hours", "Cost per Hour" });
        facilityTabs.addTab("Health Centers", healthPanel);
        panel.add(facilityTabs, BorderLayout.CENTER);
        return panel;
    }

    private JPanel createTransportPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel topPanel = new JPanel(new BorderLayout(8, 8));
        JComboBox<TransportService> transportCombo = new JComboBox<>(transportRepo.getCampusRepo().toArray(new TransportService[0]));
        topPanel.add(new JLabel("Select Transport Service:"), BorderLayout.WEST);
        topPanel.add(transportCombo, BorderLayout.CENTER);
        JButton refreshButton = new JButton("Refresh");
        topPanel.add(refreshButton, BorderLayout.EAST);

        DefaultTableModel tableModel = new DefaultTableModel(
                new String[] { "Bus ID", "Driver Salary", "From", "To", "Departure Time" }, 0);
        JTable busTable = new JTable(tableModel);
        busTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane tableScrollPane = new JScrollPane(busTable);
        tableScrollPane.setBorder(BorderFactory.createTitledBorder("Assigned Buses"));

        JPanel busForm = new JPanel(new GridLayout(0, 2, 8, 8));
        busForm.setBorder(BorderFactory.createTitledBorder("Bus Details"));
        JTextField busIdField = new JTextField();
        JTextField salaryField = new JTextField();
        JTextField fromField = new JTextField();
        JTextField toField = new JTextField();
        JTextField departureField = new JTextField();

        busTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int selectedRow = busTable.getSelectedRow();
                if (selectedRow != -1) {
                    busIdField.setText((String) tableModel.getValueAt(selectedRow, 0));
                    salaryField.setText(String.valueOf(tableModel.getValueAt(selectedRow, 1)));
                    fromField.setText((String) tableModel.getValueAt(selectedRow, 2));
                    toField.setText((String) tableModel.getValueAt(selectedRow, 3));
                    departureField.setText((String) tableModel.getValueAt(selectedRow, 4));
                }
            }
        });

        busForm.add(new JLabel("Bus ID:"));
        busForm.add(busIdField);
        busForm.add(new JLabel("Driver Salary:"));
        busForm.add(salaryField);
        busForm.add(new JLabel("From:"));
        busForm.add(fromField);
        busForm.add(new JLabel("To:"));
        busForm.add(toField);
        busForm.add(new JLabel("Departure Time:"));
        busForm.add(departureField);

        JPanel actionsPanel = new JPanel(new GridLayout(0, 1, 8, 8));
        actionsPanel.setBorder(BorderFactory.createTitledBorder("Actions"));
        JButton addBusButton = new JButton("Add Bus");
        JButton editBusButton = new JButton("Edit Selected Bus");
        JButton removeBusButton = new JButton("Remove Selected Bus");
        JButton togglePeakButton = new JButton("Toggle Peak Hours");
        JButton serviceInfoButton = new JButton("Show Transport Info");

        actionsPanel.add(addBusButton);
        actionsPanel.add(editBusButton);
        actionsPanel.add(removeBusButton);
        actionsPanel.add(togglePeakButton);
        actionsPanel.add(serviceInfoButton);

        JPanel sidePanel = new JPanel(new BorderLayout(10, 10));
        sidePanel.add(busForm, BorderLayout.CENTER);
        sidePanel.add(actionsPanel, BorderLayout.SOUTH);

        JTextArea infoArea = new JTextArea();
        infoArea.setEditable(false);
        infoArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        infoArea.setBorder(BorderFactory.createTitledBorder("Transport Summary"));
        infoArea.setText("Select a transport service and refresh to view details.");

        panel.add(topPanel, BorderLayout.NORTH);
        panel.add(tableScrollPane, BorderLayout.CENTER);
        panel.add(sidePanel, BorderLayout.EAST);
        panel.add(new JScrollPane(infoArea), BorderLayout.SOUTH);

        ActionListener reloadTransport = e -> refreshTransportTable(tableModel, transportCombo, infoArea);
        refreshButton.addActionListener(reloadTransport);
        transportCombo.addActionListener(reloadTransport);

        addBusButton.addActionListener(e -> {
            TransportService transport = (TransportService) transportCombo.getSelectedItem();
            if (transport == null) {
                JOptionPane.showMessageDialog(this, "Please select a transport service first.", "Validation Error",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }
            String id = busIdField.getText().trim();
            String salaryText = salaryField.getText().trim();
            String from = fromField.getText().trim();
            String to = toField.getText().trim();
            String departure = departureField.getText().trim();
            if (id.isEmpty() || salaryText.isEmpty() || from.isEmpty() || to.isEmpty() || departure.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please fill in all bus details.", "Validation Error",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }
            try {
                int salary = Integer.parseInt(salaryText);
                Bus bus = new Bus(id, salary, from, to, departure);
                if (isAdmin()) {
                    Admin admin = (Admin) currentUser;
                    admin.addBus(bus);
                    refreshTransportTable(tableModel, transportCombo, infoArea);
                    JOptionPane.showMessageDialog(this, "Bus added to transport successfully.", "Success",
                            JOptionPane.INFORMATION_MESSAGE);
                    autoSaveData();
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Please enter a valid numeric salary.", "Validation Error",
                        JOptionPane.WARNING_MESSAGE);
            }
        });

        editBusButton.addActionListener(e -> {
            TransportService transport = (TransportService) transportCombo.getSelectedItem();
            int selectedRow = busTable.getSelectedRow();
            if (transport == null || selectedRow == -1) {
                JOptionPane.showMessageDialog(this, "Please select a bus from the table.", "Validation Error",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }
            String selectedBusId = (String) tableModel.getValueAt(selectedRow, 0);
            Bus selectedBus = null;
            for (Bus bus : transport.getBuses()) {
                if (bus.getBusID().equalsIgnoreCase(selectedBusId)) {
                    selectedBus = bus;
                    break;
                }
            }
            if (selectedBus == null) {
                JOptionPane.showMessageDialog(this, "Selected bus could not be found.", "Error",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }
            String updatedId = busIdField.getText().trim();
            String updatedFrom = fromField.getText().trim();
            String updatedTo = toField.getText().trim();
            String updatedDeparture = departureField.getText().trim();
            if (updatedId.isEmpty() || salaryField.getText().trim().isEmpty() || updatedFrom.isEmpty()
                    || updatedTo.isEmpty() || updatedDeparture.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please fill in all bus details before editing.",
                        "Validation Error", JOptionPane.WARNING_MESSAGE);
                return;
            }
            try {
                int salary = Integer.parseInt(salaryField.getText().trim());
                selectedBus.setBusID(updatedId);
                selectedBus.setDriverSalary(salary);
                selectedBus.setFrom(updatedFrom);
                selectedBus.setTo(updatedTo);
                selectedBus.setDepartureTime(updatedDeparture);
                refreshTransportTable(tableModel, transportCombo, infoArea);
                JOptionPane.showMessageDialog(this, "Bus details updated.", "Success", JOptionPane.INFORMATION_MESSAGE);
                autoSaveData();
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Please enter a valid numeric salary.", "Validation Error",
                        JOptionPane.WARNING_MESSAGE);
            }
        });

        removeBusButton.addActionListener(e -> {
            TransportService transport = (TransportService) transportCombo.getSelectedItem();
            int selectedRow = busTable.getSelectedRow();
            if (transport == null || selectedRow == -1) {
                JOptionPane.showMessageDialog(this, "Please select a bus from the table.", "Validation Error",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }
            String selectedBusId = (String) tableModel.getValueAt(selectedRow, 0);
            Bus selectedBus = null;
            for (Bus bus : transport.getBuses()) {
                if (bus.getBusID().equalsIgnoreCase(selectedBusId)) {
                    selectedBus = bus;
                    break;
                }
            }
            if (selectedBus != null && isAdmin()) {
                Admin admin = (Admin) currentUser;
                admin.removeBus(selectedBus);
                refreshTransportTable(tableModel, transportCombo, infoArea);
                JOptionPane.showMessageDialog(this, "Bus removed from transport.", "Success",
                        JOptionPane.INFORMATION_MESSAGE);
                autoSaveData();
            }
        });

        togglePeakButton.addActionListener(actionEvent-> {
            if (isAdmin()) {
                Admin admin = (Admin) currentUser;
                admin.peakHoursAdjustment();
                refreshTransportTable(tableModel, transportCombo, infoArea);
                JOptionPane.showMessageDialog(this, "Peak hours adjustment executed. Check console for details.",
                        "Transport Update", JOptionPane.INFORMATION_MESSAGE);
                autoSaveData();
                if (!transportRepo.getCampusRepo().isEmpty()) {
                    TransportService ts = transportRepo.getCampusRepo().get(0);
                    System.out.println("=== Peak Hours Adjustment ===");
                    System.out.println("Transport Service: " + ts.getName());
                    System.out.println("Peak Hours Active: " + (ts.ispeakhour() ? "YES" : "NO"));
                    System.out.println("Cost per Hour: $" + ts.getCostperhour());
                    System.out.println("Total Buses: " + ts.getBuses().size());
                    System.out.println("==============================");
                }
            }
        });

        serviceInfoButton.addActionListener(e -> {
            TransportService transport = (TransportService) transportCombo.getSelectedItem();
            if (transport != null) {
                infoArea.setText(transport.getSummary());
            } else {
                infoArea.setText("Please select a transport service first.");
            }
        });

        refreshTransportTable(tableModel, transportCombo, infoArea);
        return panel;
    }

    private void refreshTransportTable(DefaultTableModel tableModel, JComboBox<TransportService> transportCombo, JTextArea infoArea) {
        tableModel.setRowCount(0);
        TransportService transport = (TransportService) transportCombo.getSelectedItem();
        if (transport == null) {
            infoArea.setText("No transport service available.");
            return;
        }
        if (transport.getBuses().isEmpty()) {
            infoArea.setText(getTransportSummary(transport) + "\nNo buses currently assigned to this service.");
        } else {
            for (Bus bus : transport.getBuses()) {
                tableModel.addRow(new Object[] {
                        bus.getBusID(),
                        bus.getDriverSalary(),
                        bus.getFrom(),
                        bus.getTo(),
                        bus.getDepartureTime()
                });
            }
            infoArea.setText(getTransportSummary(transport));
        }
    }

    private String getTransportSummary(TransportService transport) {
        return String.format(
                "Transport Service: %s (%s)\nLocation: %s\nService Hours: %d\nCost per Hour: $%.2f\nPeak Hours: %s\nTotal Buses: %d",
                transport.getName(), transport.getEntityID(), transport.getLocation(),
                transport.getServicehours(), transport.getCostperhour(),
                transport.ispeakhour() ? "ACTIVE" : "INACTIVE", transport.getBuses().size());
    }

    private JPanel createAdminPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        JTabbedPane adminTabs = new JTabbedPane();
        JPanel entityManagementPanel = createAdminEntityManagementTab();
        adminTabs.addTab("Entity Management", entityManagementPanel);
        JPanel emergencyPanel = createAdminEmergencyTab();
        adminTabs.addTab("Emergency & Conflicts", emergencyPanel);
        panel.add(adminTabs, BorderLayout.CENTER);

        JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));

        JButton saveButton = new JButton("Save Data");
        saveButton.addActionListener(e -> {
            BackupSystem backup = new BackupSystem(
                    studentRepo, teacherRepo, adminRepo, departmentRepo,
                    courseRepo, libraryRepo, cafeteriaRepo, hostelsRepo,
                    transportRepo, securityRepo, healthRepo, classroomRepo,
                    busRepo, libraryBooksRepo, labRepo);
            backup.SaveData();
            JOptionPane.showMessageDialog(this, "Data saved successfully.", "Save Complete",
                    JOptionPane.INFORMATION_MESSAGE);
        });

        JButton loadButton = new JButton("Load Data");
        loadButton.addActionListener(e -> {
            BackupSystem backup = new BackupSystem();
            if (backup.loadSavedData()) {
                studentRepo = backup.getStudentRepo();
                teacherRepo = backup.getTeacherRepo();
                adminRepo = backup.getAdminRepo();
                departmentRepo = backup.getDepartmentRepo();
                courseRepo = backup.getCourseRepo();
                libraryRepo = backup.getLibraryRepo();
                cafeteriaRepo = backup.getCafeteriaRepo();
                hostelsRepo = backup.getHostelsRepo();
                transportRepo = backup.getTransportRepo();
                securityRepo = backup.getSecurityRepo();
                healthRepo = backup.getHealthRepo();
                classroomRepo = backup.getClassroomRepo();
                busRepo = backup.getBusRepo();
                libraryBooksRepo = backup.getLibraryBooksRepo();
                labRepo = backup.getLabRepo();
                getContentPane().removeAll();
                setJMenuBar(null);
                setupMainInterface();
                revalidate();
                repaint();
            } else {
                JOptionPane.showMessageDialog(this, "Failed to load saved data. Please save data first.", "Load Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        });

        actionPanel.add(saveButton);
        actionPanel.add(loadButton);
        panel.add(actionPanel, BorderLayout.SOUTH);
        return panel;
    }

    private JPanel createAdminEquipmentPanel() {
        JPanel panel = new JPanel(new GridLayout(3, 1, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel classEquipPanel = new JPanel(new GridLayout(4, 2, 8, 8));
        classEquipPanel.setBorder(BorderFactory.createTitledBorder("Classroom Equipment"));
        JComboBox<ClassRoom> classroomCombo = new JComboBox<>(classroomRepo.getCampusRepo().toArray(new ClassRoom[0]));
        JTextField classroomEquipmentNameField = new JTextField();
        JTextField classroomEquipmentPriceField = new JTextField();
        JTextField classroomEquipmentQtyField = new JTextField();

        classEquipPanel.add(new JLabel("Classroom:"));
        classEquipPanel.add(classroomCombo);
        classEquipPanel.add(new JLabel("Equipment Name:"));
        classEquipPanel.add(classroomEquipmentNameField);
        classEquipPanel.add(new JLabel("Price:"));
        classEquipPanel.add(classroomEquipmentPriceField);
        classEquipPanel.add(new JLabel("Quantity:"));
        classEquipPanel.add(classroomEquipmentQtyField);

        JButton addClassroomEquipmentButton = new JButton("Add Classroom Equipment");
        addClassroomEquipmentButton.addActionListener(e -> {
            ClassRoom classroom = (ClassRoom) classroomCombo.getSelectedItem();
            try {
                String name = classroomEquipmentNameField.getText().trim();
                int price = Integer.parseInt(classroomEquipmentPriceField.getText().trim());
                int qty = Integer.parseInt(classroomEquipmentQtyField.getText().trim());
                if (classroom != null && !name.isEmpty()) {
                    classroom.addEquipment(new ClassRoomEquipments(name, price, qty));
                    JOptionPane.showMessageDialog(this, "Equipment added to classroom.");
                    classroomEquipmentNameField.setText("");
                    classroomEquipmentPriceField.setText("");
                    classroomEquipmentQtyField.setText("");
                    autoSaveData();
                }
            } catch (NumberFormatException nfe) {
                JOptionPane.showMessageDialog(this,
                        "Please enter valid numeric values for classroom equipment price and quantity.",
                        "Validation Error", JOptionPane.WARNING_MESSAGE);
            }
        });

        JPanel labEquipPanel = new JPanel(new GridLayout(4, 2, 8, 8));
        labEquipPanel.setBorder(BorderFactory.createTitledBorder("Lab Equipment"));
        JComboBox<Lab> labCombo = new JComboBox<>(labRepo.getCampusRepo().toArray(new Lab[0]));
        JTextField labEquipmentNameField = new JTextField();
        JTextField labEquipmentPriceField = new JTextField();
        JTextField labEquipmentQtyField = new JTextField();

        labEquipPanel.add(new JLabel("Lab:"));
        labEquipPanel.add(labCombo);
        labEquipPanel.add(new JLabel("Equipment Name:"));
        labEquipPanel.add(labEquipmentNameField);
        labEquipPanel.add(new JLabel("Price:"));
        labEquipPanel.add(labEquipmentPriceField);
        labEquipPanel.add(new JLabel("Quantity:"));
        labEquipPanel.add(labEquipmentQtyField);

        JButton addLabEquipmentButton = new JButton("Add Lab Equipment");
        addLabEquipmentButton.addActionListener(e -> {
            Lab lab = (Lab) labCombo.getSelectedItem();
            try {
                String name = labEquipmentNameField.getText().trim();
                int price = Integer.parseInt(labEquipmentPriceField.getText().trim());
                int qty = Integer.parseInt(labEquipmentQtyField.getText().trim());
                if (lab != null && !name.isEmpty()) {
                    lab.AddEquipment(new LabEquipments(name, price, qty));
                    JOptionPane.showMessageDialog(this, "Equipment added to lab.");
                    labEquipmentNameField.setText("");
                    labEquipmentPriceField.setText("");
                    labEquipmentQtyField.setText("");
                    autoSaveData();
                }
            } catch (NumberFormatException nfe) {
                JOptionPane.showMessageDialog(this,
                        "Please enter valid numeric values for lab equipment price and quantity.", "Validation Error",
                        JOptionPane.WARNING_MESSAGE);
            }
        });

        JPanel buttonPanel = new JPanel(new GridLayout(1, 2, 10, 10));
        buttonPanel.add(addClassroomEquipmentButton);
        buttonPanel.add(addLabEquipmentButton);

        panel.add(classEquipPanel);
        panel.add(labEquipPanel);
        panel.add(buttonPanel);
        return panel;
    }

    private JPanel createAdminClassroomAvailabilityPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel formPanel = new JPanel(new GridLayout(2, 2, 8, 8));
        JComboBox<Admin> adminCombo = new JComboBox<>(adminRepo.getCampusRepo().toArray(new Admin[0]));
        JComboBox<ClassRoom> classroomCombo = new JComboBox<>(classroomRepo.getCampusRepo().toArray(new ClassRoom[0]));

        formPanel.add(new JLabel("Admin:"));
        formPanel.add(adminCombo);
        formPanel.add(new JLabel("Classroom:"));
        formPanel.add(classroomCombo);

        JPanel buttonPanel = new JPanel(new GridLayout(2, 1, 8, 8));
        JButton markClassroomUnavailable = new JButton("Mark Classroom Unavailable");
        markClassroomUnavailable.addActionListener(e -> {
            Admin admin = (Admin) adminCombo.getSelectedItem();
            ClassRoom classroom = (ClassRoom) classroomCombo.getSelectedItem();
            if (admin != null && classroom != null) {
                admin.markClassroomUnavailable(classroom);
                JOptionPane.showMessageDialog(this, "Classroom marked unavailable.");
                if (campusMapPanel != null) campusMapPanel.refreshMap();
                autoSaveData();
            }
        });

        JButton markClassroomAvailable = new JButton("Mark Classroom Available");
        markClassroomAvailable.addActionListener(e -> {
            Admin admin = (Admin) adminCombo.getSelectedItem();
            ClassRoom classroom = (ClassRoom) classroomCombo.getSelectedItem();
            if (admin != null && classroom != null) {
                admin.markClassroomAvailable(classroom);
                JOptionPane.showMessageDialog(this, "Classroom marked available.");
                if (campusMapPanel != null) campusMapPanel.refreshMap();
                autoSaveData();
            }
        });

        buttonPanel.add(markClassroomUnavailable);
        buttonPanel.add(markClassroomAvailable);

        panel.add(formPanel, BorderLayout.NORTH);
        panel.add(buttonPanel, BorderLayout.CENTER);
        return panel;
    }

    private JPanel createAdminEntityManagementTab() {
        JPanel panel = new JPanel(new BorderLayout());
        JTabbedPane adminTabs = new JTabbedPane();
        adminTabs.addTab("Enrollment", createAdminEnrollmentPanel());
        adminTabs.addTab("Facilities", createAdminFacilitiesManagementPanel());
        adminTabs.addTab("Equipment", createAdminEquipmentPanel());
        adminTabs.addTab("Classroom Availability", createAdminClassroomAvailabilityPanel());
        panel.add(adminTabs, BorderLayout.CENTER);
        return panel;
    }

    private JPanel createAdminEnrollmentPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel formPanel = new JPanel(new GridLayout(3, 2, 8, 8));
        JComboBox<Admin> adminCombo = new JComboBox<>(adminRepo.getCampusRepo().toArray(new Admin[0]));
        JComboBox<Course> courseCombo = new JComboBox<>(courseRepo.getCampusRepo().toArray(new Course[0]));
        JComboBox<Student> studentCombo = new JComboBox<>(studentRepo.getCampusRepo().toArray(new Student[0]));

        studentCombo.addPopupMenuListener(new PopupMenuListener() {
            public void popupMenuWillBecomeVisible(PopupMenuEvent e) {
                Student selected = (Student) studentCombo.getSelectedItem();
                studentCombo.setModel(new DefaultComboBoxModel<>(studentRepo.getCampusRepo().toArray(new Student[0])));
                if (selected != null) studentCombo.setSelectedItem(selected);
            }
            public void popupMenuWillBecomeInvisible(PopupMenuEvent e) {}
            public void popupMenuCanceled(PopupMenuEvent e) {}
        });
        courseCombo.addPopupMenuListener(new PopupMenuListener() {
            public void popupMenuWillBecomeVisible(PopupMenuEvent e) {
                Course selected = (Course) courseCombo.getSelectedItem();
                courseCombo.setModel(new DefaultComboBoxModel<>(courseRepo.getCampusRepo().toArray(new Course[0])));
                if (selected != null) courseCombo.setSelectedItem(selected);
            }
            public void popupMenuWillBecomeInvisible(PopupMenuEvent e) {}
            public void popupMenuCanceled(PopupMenuEvent e) {}
        });

        formPanel.add(new JLabel("Admin:"));
        formPanel.add(adminCombo);
        formPanel.add(new JLabel("Course:"));
        formPanel.add(courseCombo);
        formPanel.add(new JLabel("Student:"));
        formPanel.add(studentCombo);

        JPanel buttonPanel = new JPanel(new GridLayout(2, 1, 8, 8));
        JButton addStudentToCourse = new JButton("Add Student to Course");
        addStudentToCourse.addActionListener(e -> {
            Admin admin = (Admin) currentUser;
            Course course = (Course) courseCombo.getSelectedItem();
            Student student = (Student) studentCombo.getSelectedItem();
            if (admin != null && course != null && student != null) {
                boolean added = admin.addStudentToCourse(course, student);
                if (added) {
                    JOptionPane.showMessageDialog(this, "Student added to course.");
                    autoSaveData();
                } else {
                    JOptionPane.showMessageDialog(this, "Student could not be added. They may already be enrolled.",
                            "Duplicate Student", JOptionPane.WARNING_MESSAGE);
                }
            }
        });

        JButton removeStudentFromCourse = new JButton("Remove Student from Course");
        removeStudentFromCourse.addActionListener(e -> {
            Admin admin = (Admin) currentUser;
            Course course = (Course) courseCombo.getSelectedItem();
            Student student = (Student) studentCombo.getSelectedItem();
            if (admin != null && course != null && student != null) {
                boolean removed = admin.removeStudentFromCourse(course, student);
                if (removed) {
                    JOptionPane.showMessageDialog(this, "Student removed from course.");
                    autoSaveData();
                } else {
                    JOptionPane.showMessageDialog(this, "Student is not enrolled in this course.", "Removal Failed",
                            JOptionPane.WARNING_MESSAGE);
                }
            }
        });

        buttonPanel.add(addStudentToCourse);
        buttonPanel.add(removeStudentFromCourse);

        panel.add(formPanel, BorderLayout.NORTH);
        panel.add(buttonPanel, BorderLayout.CENTER);
        return panel;
    }

    private JPanel createAdminFacilitiesManagementPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        Library library = libraryRepo.getCampusRepo().isEmpty() ? null : libraryRepo.getCampusRepo().get(0);
        DefaultComboBoxModel<LibraryBooks> bookComboModel = new DefaultComboBoxModel<>();
        if (library != null) {
            for (LibraryBooks b : library.getBooks()) {
                bookComboModel.addElement(b);
            }
        }
        JComboBox<LibraryBooks> bookCombo = new JComboBox<>(bookComboModel);

        JPanel formPanel = new JPanel(new GridLayout(0, 2, 8, 8));
        JTextField cafeteriaItemField = new JTextField();
        JTextField removeMenuItemField = new JTextField();
        JTextField newBookTitleField = new JTextField();
        JTextField newBookAuthorField = new JTextField();
        JComboBox<Hostels> hostelCombo = new JComboBox<>(hostelsRepo.getCampusRepo().toArray(new Hostels[0]));
        JComboBox<Student> residentStudentCombo = new JComboBox<>(studentRepo.getCampusRepo().toArray(new Student[0]));

        formPanel.add(new JLabel("Cafeteria Item to Add:"));
        formPanel.add(cafeteriaItemField);
        formPanel.add(new JLabel("Cafeteria Item to Remove:"));
        formPanel.add(removeMenuItemField);
        formPanel.add(new JLabel("New Book Title:"));
        formPanel.add(newBookTitleField);
        formPanel.add(new JLabel("New Book Author:"));
        formPanel.add(newBookAuthorField);
        formPanel.add(new JLabel("Library Book to Remove:"));
        formPanel.add(bookCombo);
        formPanel.add(new JLabel("Select Hostel:"));
        formPanel.add(hostelCombo);
        formPanel.add(new JLabel("Select Student:"));
        formPanel.add(residentStudentCombo);

        JPanel buttonPanel = new JPanel(new GridLayout(0, 2, 8, 8));

        JButton addMenuItemButton = new JButton("Add Cafeteria Item");
        addMenuItemButton.addActionListener(e -> {
            Admin admin = (Admin) currentUser;
            String item = cafeteriaItemField.getText().trim();
            if (admin != null && !item.isEmpty()) {
                admin.addMenuItem(item);
                JOptionPane.showMessageDialog(this, "Menu item added.");
                cafeteriaItemField.setText("");
                autoSaveData();
            }
        });

        JButton removeMenuItemButton = new JButton("Remove Cafeteria Item");
        removeMenuItemButton.addActionListener(e -> {
            Admin admin = (Admin) currentUser;
            String item = removeMenuItemField.getText().trim();
            if (admin != null && !item.isEmpty()) {
                if (!cafeteriaRepo.getCampusRepo().isEmpty()) {
                    Cafeteria cafeteria = cafeteriaRepo.getCampusRepo().get(0);
                    if (cafeteria.getMenuItems().contains(item)) {
                      admin.removeMenuItem(item);
                    JOptionPane.showMessageDialog(this, "Menu item removed: " + item);
                    removeMenuItemField.setText("");
                    autoSaveData();
                } else {
                        JOptionPane.showMessageDialog(this, "Item '" + item + "' does not exist in the menu.",
                                "Remove Failed", JOptionPane.WARNING_MESSAGE);
                    }
                } else {
                    JOptionPane.showMessageDialog(this, "No cafeteria found.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(this, "Please enter a menu item to remove.", "Validation Error",
                        JOptionPane.WARNING_MESSAGE);
            }
        });

        JButton addBookButton = new JButton("Add New Library Book");
        addBookButton.addActionListener(e -> {
            Admin admin = (Admin) currentUser;
            String title = newBookTitleField.getText().trim();
            String author = newBookAuthorField.getText().trim();
            if (admin != null && !title.isEmpty() && !author.isEmpty() && library != null) {
                LibraryBooks book = new LibraryBooks(title, author, false);
                boolean exists = false;
                for (LibraryBooks b : library.getBooks()) {
                    if (b.getTitle().equalsIgnoreCase(title)) {
                        exists = true;
                        break;
                    }
                }
                if (!exists) {
                    library.addBook(book);
                    libraryBooksRepo.AddItem(book);
                    bookComboModel.removeAllElements();
                    for (LibraryBooks b : library.getBooks()) {
                        bookComboModel.addElement(b);
                    }
                    JOptionPane.showMessageDialog(this, "New book added to library.");
                    newBookTitleField.setText("");
                    newBookAuthorField.setText("");
                    autoSaveData();
                } else {
                    JOptionPane.showMessageDialog(this, "Book with the same title already exists.", "Add Book",
                            JOptionPane.WARNING_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(this, "Please enter both title and author for the new book.",
                        "Validation Error", JOptionPane.WARNING_MESSAGE);
            }
        });

        JButton removeBookButton = new JButton("Remove Library Book");
        removeBookButton.addActionListener(e -> {
            LibraryBooks selectedBook = (LibraryBooks) bookCombo.getSelectedItem();
            if (library != null && selectedBook != null) {
                LibraryBooks toRemove = null;
                for (LibraryBooks b : library.getBooks()) {
                    if (b.getTitle().equalsIgnoreCase(selectedBook.getTitle())) {
                        toRemove = b;
                        break;
                    }
                }
                if (toRemove != null) {
                    library.removeBook(toRemove);
                    libraryBooksRepo.RemoveItem(toRemove);
                    bookComboModel.removeAllElements();
                    for (LibraryBooks b : library.getBooks()) {
                        bookComboModel.addElement(b);
                    }
                    JOptionPane.showMessageDialog(this, "Book removed from library.");
                    autoSaveData();
                } else {
                    JOptionPane.showMessageDialog(this, "Selected book not found in the library.", "Remove Failed",
                            JOptionPane.WARNING_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(this, "No library or book selected.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        JButton addResidentButton = new JButton("Add Resident to Hostel");
        JButton addNewResidentButton = new JButton("Create & Add New Resident");
        JButton removeResidentButton = new JButton("Remove Resident from Hostel");
        JButton viewResidentsButton = new JButton("View Hostel Residents");

        addResidentButton.addActionListener(e -> {
            Hostels hostel = (Hostels) hostelCombo.getSelectedItem();
            Student student = (Student) residentStudentCombo.getSelectedItem();
            if (hostel != null && student != null) {
                boolean added = hostel.addResident(student);
                if (added) {
                    JOptionPane.showMessageDialog(this, "Resident added to hostel.");
                    autoSaveData();
                } else {
                    JOptionPane.showMessageDialog(this,
                            "Could not add resident. They may already be residing or hostel full.", "Add Resident",
                            JOptionPane.WARNING_MESSAGE);
                }
            }
        });

        addNewResidentButton.addActionListener(e -> {
            JDialog newResDialog = new JDialog(this, "Add New Hostel Resident", true);
            newResDialog.setSize(400, 360);
            newResDialog.setLocationRelativeTo(this);
            JPanel newResPanel = new JPanel(new GridLayout(0, 2, 8, 8));
            newResPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
            JTextField nameField = new JTextField();
            JTextField regNoField = new JTextField();
            JTextField departmentField = new JTextField();
            JTextField feeField = new JTextField();
            JTextField cgpaField = new JTextField();
            newResPanel.add(new JLabel("Name:"));
            newResPanel.add(nameField);
            newResPanel.add(new JLabel("Reg No:"));
            newResPanel.add(regNoField);
            newResPanel.add(new JLabel("Department:"));
            newResPanel.add(departmentField);
            newResPanel.add(new JLabel("Fee:"));
            newResPanel.add(feeField);
            newResPanel.add(new JLabel("CGPA:"));
            newResPanel.add(cgpaField);
            JButton createResidentButton = new JButton("Create and Add");
            createResidentButton.addActionListener(ev -> {
                String name = nameField.getText().trim();
                String regNo = regNoField.getText().trim();
                String department = departmentField.getText().trim();
                String feeText = feeField.getText().trim();
                String cgpaText = cgpaField.getText().trim();
                Hostels hostel = (Hostels) hostelCombo.getSelectedItem();
                if (name.isEmpty() || regNo.isEmpty() || department.isEmpty() || feeText.isEmpty() || cgpaText.isEmpty()) {
                    JOptionPane.showMessageDialog(newResDialog, "Please fill all fields.", "Validation Error",
                            JOptionPane.WARNING_MESSAGE);
                    return;
                }
                try {
                    int fee = Integer.parseInt(feeText);
                    double cgpa = Double.parseDouble(cgpaText);
                    Student student = new Student(name, "Student", regNo, department, fee, cgpa);
                    try {
                        validateDuplicateEntity("Student", student, studentRepo, null);
                        studentRepo.AddItem(student);
                        residentStudentCombo.setModel(new DefaultComboBoxModel<>(studentRepo.getCampusRepo().toArray(new Student[0])));
                        if (hostel != null) {
                            boolean added = hostel.addResident(student);
                            if (added) {
                                JOptionPane.showMessageDialog(newResDialog, "New resident created and added to hostel.");
                                newResDialog.dispose();
                                autoSaveData();
                            } else {
                                JOptionPane.showMessageDialog(newResDialog,
                                        "Resident could not be added to hostel. It may be full or the student already exists.",
                                        "Add Resident", JOptionPane.WARNING_MESSAGE);
                            }
                        }
                    } catch (Exception dupEx) {
                        JOptionPane.showMessageDialog(newResDialog, dupEx.getMessage(), "Duplicate Student",
                                JOptionPane.WARNING_MESSAGE);
                    }
                } catch (NumberFormatException nfe) {
                    JOptionPane.showMessageDialog(newResDialog, "Fee must be an integer and CGPA must be a number.",
                            "Validation Error", JOptionPane.WARNING_MESSAGE);
                }
            });
            JButton cancelResidentButton = new JButton("Cancel");
            cancelResidentButton.addActionListener(ev -> newResDialog.dispose());
            JPanel bottomPanel = new JPanel();
            bottomPanel.add(createResidentButton);
            bottomPanel.add(cancelResidentButton);
            newResDialog.add(newResPanel, BorderLayout.CENTER);
            newResDialog.add(bottomPanel, BorderLayout.SOUTH);
            newResDialog.setVisible(true);
        });

        removeResidentButton.addActionListener(e -> {
            Hostels hostel = (Hostels) hostelCombo.getSelectedItem();
            Student student = (Student) residentStudentCombo.getSelectedItem();
            if (hostel != null && student != null) {
                boolean removed = hostel.removeResident(student);
                if (removed) {
                    JOptionPane.showMessageDialog(this, "Resident removed from hostel.");
                    autoSaveData();
                } else {
                    JOptionPane.showMessageDialog(this, "Could not remove resident. They may not be in this hostel.",
                            "Remove Resident", JOptionPane.WARNING_MESSAGE);
                }
            }
        });

        viewResidentsButton.addActionListener(e -> {
            Hostels hostel = (Hostels) hostelCombo.getSelectedItem();
            if (hostel != null) {
                StringBuilder sb = new StringBuilder();
                sb.append("Residents for ").append(hostel.getName()).append(" (" + hostel.getEntityID() + ")\n\n");
                if (hostel.getResidents().isEmpty()) {
                    sb.append("No residents currently.");
                } else {
                    for (Student r : hostel.getResidents()) {
                        sb.append(r.toString()).append("\n\n");
                    }
                }
                JTextArea ta = new JTextArea(sb.toString());
                ta.setEditable(false);
                ta.setFont(new Font("Monospaced", Font.PLAIN, 11));
                JScrollPane sp = new JScrollPane(ta);
                sp.setPreferredSize(new Dimension(600, 300));
                JOptionPane.showMessageDialog(this, sp, "Hostel Residents", JOptionPane.INFORMATION_MESSAGE);
            }
        });

        JButton viewCafeteriaDetailsButton = new JButton("View Cafeteria Details");
        viewCafeteriaDetailsButton.addActionListener(e -> {
            Admin admin = (Admin) currentUser;
            if (admin != null) {
                admin.viewCafeteriaDetails();
                JOptionPane.showMessageDialog(this, "Cafeteria details sent to console.");
            }
        });

        JButton generateLibraryReportButton = new JButton("Generate Library Report");
        generateLibraryReportButton.addActionListener(e -> {
            if (library != null) {
                System.out.println("=== LIBRARY REPORT ===");
                System.out.println("Total Books: " + library.getBooks().size());
                System.out.println("Borrowed Books: " + Library.getBorrowedBooks());
                System.out.println("Returned Books: " + Library.getReturnedBooks());
                System.out.println("Visitors Per Day: " + library.getVisitorsPerDay());
                System.out.println("Total Operational Cost: " + library.calculateOperationalCost());
                System.out.println("=== Book Details ===");
                for (LibraryBooks b : library.getBooks()) {
                    System.out.println(b);
                    System.out.println();
                }
                JOptionPane.showMessageDialog(this, "Library report printed to console.");
            } else {
                JOptionPane.showMessageDialog(this, "No library found.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        buttonPanel.add(addResidentButton);
        buttonPanel.add(addNewResidentButton);
        buttonPanel.add(removeResidentButton);
        buttonPanel.add(viewResidentsButton);
        buttonPanel.add(addMenuItemButton);
        buttonPanel.add(removeMenuItemButton);
        buttonPanel.add(addBookButton);
        buttonPanel.add(removeBookButton);
        buttonPanel.add(viewCafeteriaDetailsButton);
        buttonPanel.add(generateLibraryReportButton);

        panel.add(formPanel, BorderLayout.NORTH);
        panel.add(buttonPanel, BorderLayout.CENTER);
        return panel;
    }

    private JPanel createAdminEmergencyTab() {
        JPanel panel = new JPanel(new BorderLayout());
        JPanel controls = new JPanel(new GridLayout(0, 2, 8, 8));
        controls.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JComboBox<Department> departmentCombo = new JComboBox<>(departmentRepo.getCampusRepo().toArray(new Department[0]));
        JComboBox<TransportService> transportCombo = new JComboBox<>(transportRepo.getCampusRepo().toArray(new TransportService[0]));

        controls.add(new JLabel("Select Department:"));
        controls.add(departmentCombo);
        controls.add(new JLabel("Select Transport Service:"));
        controls.add(transportCombo);

        JPanel buttons = new JPanel(new GridLayout(0, 1, 6, 6));
        JTextArea infoArea = new JTextArea(10, 40);
        infoArea.setEditable(false);
        infoArea.setFont(new Font("Monospaced", Font.PLAIN, 11));

        JButton medicalEmergencyButton = new JButton("Activate Medical Emergency");
        medicalEmergencyButton.setBackground(Color.RED);
        medicalEmergencyButton.setForeground(Color.WHITE);
        medicalEmergencyButton.addActionListener(e -> {
            if (isAdmin()) {
                Admin admin = (Admin) currentUser;
                admin.ActivateMedicalEmergency();
                infoArea.setText("MEDICAL EMERGENCY ACTIVATED!\n");
            }
        });

        JButton resolveConflictsButton = new JButton("Resolve Course Conflicts");
        resolveConflictsButton.addActionListener(e -> {
            Department dept = (Department) departmentCombo.getSelectedItem();
            if (dept != null && isAdmin()) {
                try {
                    Admin admin = (Admin) currentUser;
                    admin.resolveCourseConflicts();
                    infoArea.setText("Conflict resolution executed for department: " + dept.getName()
                            + "\nCheck the Console tab for details.");
                    JOptionPane.showMessageDialog(panel, "Conflict resolution completed for " + dept.getName(),
                            "Success", JOptionPane.INFORMATION_MESSAGE);
                } catch (Exception ex) {
                    ex.printStackTrace();
                    infoArea.setText("Error while resolving conflicts: " + ex.getMessage());
                }
            } else {
                infoArea.setText("Please select a department.");
            }
        });

        JButton transportScheduleButton = new JButton("View Transport Schedule");
        transportScheduleButton.addActionListener(e -> {
            TransportService selectedTransport = (TransportService) transportCombo.getSelectedItem();
            if (selectedTransport != null) {
                infoArea.setText(selectedTransport.getScheduleDetails());
            } else {
                infoArea.setText("Please select a transport service to view the schedule.");
            }
        });

        JButton peakHoursButton = new JButton("Peak Hours Adjustment");
        peakHoursButton.addActionListener(e -> {
            if (isAdmin()) {
                Admin admin = (Admin) currentUser;
                admin.peakHoursAdjustment();
                infoArea.setText("Peak hours adjustment executed. Check console for bus deployment details.");
                if (campusMapPanel != null) campusMapPanel.refreshMap();
            }
        });

        JButton securityAlertButton = new JButton("Activate Security Alert");
        securityAlertButton.setBackground(new Color(180, 0, 0));
        securityAlertButton.setForeground(Color.WHITE);
        securityAlertButton.addActionListener(e -> {
            if (isAdmin()) {
                Admin admin = (Admin) currentUser;
                admin.activateSecurityAlert();
                JOptionPane.showMessageDialog(this, "Security alert sent.", "SECURITY ALERT",
                        JOptionPane.WARNING_MESSAGE);
            }
        });

        JButton operationalCostsButton = new JButton("View Operational Costs");
        operationalCostsButton.addActionListener(e -> {
            if (isAdmin()) {
                Admin admin = (Admin) currentUser;
                infoArea.setText(admin.viewOperationalCostsText());
            }
        });

        buttons.add(medicalEmergencyButton);
        buttons.add(securityAlertButton);
        buttons.add(resolveConflictsButton);
        buttons.add(transportScheduleButton);
        buttons.add(peakHoursButton);
        buttons.add(operationalCostsButton);

        panel.add(controls, BorderLayout.NORTH);
        panel.add(buttons, BorderLayout.CENTER);
        panel.add(new JScrollPane(infoArea), BorderLayout.SOUTH);
        return panel;
    }

    private JTabbedPane createReportsPanel() {
        JTabbedPane reportTabs = new JTabbedPane();
        if (isAdmin()) {
            reportTabs.addTab("Resource Usage", createResourceUsageSummaryTab());
            reportTabs.addTab("Full Report", createFullReportTab());
        } else if (isTeacher()) {
            reportTabs.addTab("My Courses", createTeacherCoursesTab());
            reportTabs.addTab("Enrolled Students", createTeacherStudentsTab());
        } else if (isStudent()) {
            reportTabs.addTab("My Courses", createStudentScheduleTab());
        }
        return reportTabs;
    }

    private JPanel createConsolePanel() {
        JPanel panel = new JPanel(new BorderLayout());
        JScrollPane scrollPane = new JScrollPane(consoleOutputArea);
        JButton clearButton = new JButton("Clear Console");
        clearButton.addActionListener(e -> consoleOutputArea.setText(""));
        panel.add(scrollPane, BorderLayout.CENTER);
        panel.add(clearButton, BorderLayout.SOUTH);
        return panel;
    }

    private JPanel createTeacherCoursesTab() {
        JPanel panel = new JPanel(new BorderLayout());
        JTextArea reportArea = new JTextArea();
        reportArea.setEditable(false);
        reportArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        JButton refreshButton = new JButton("Refresh My Courses");
        refreshButton.addActionListener(e -> reportArea.setText(collectTeacherCoursesReport()));
        panel.add(new JScrollPane(reportArea), BorderLayout.CENTER);
        panel.add(refreshButton, BorderLayout.SOUTH);
        reportArea.setText(collectTeacherCoursesReport());
        return panel;
    }

    private String collectTeacherCoursesReport() {
        StringBuilder sb = new StringBuilder();
        if (!isTeacher()) return "Only teachers can view this report.";
        Teacher teacher = (Teacher) currentUser;
        sb.append("=== MY COURSES ==="+"\n");
        for (Course course : courseRepo.getCampusRepo()) {
            if (course.getModerator().equalsIgnoreCase(teacher.getName())) {
                sb.append(course.toString()).append("\n\n");
            }
        }
        if (sb.toString().trim().isEmpty()) return "No courses found for " + teacher.getName() + ".";
        return sb.toString();
    }

    private JPanel createTeacherStudentsTab() {
        JPanel panel = new JPanel(new BorderLayout());
        JTextArea reportArea = new JTextArea();
        reportArea.setEditable(false);
        reportArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        JButton refreshButton = new JButton("Refresh Enrolled Students");
        refreshButton.addActionListener(e -> reportArea.setText(collectTeacherStudentsReport()));
        panel.add(new JScrollPane(reportArea), BorderLayout.CENTER);
        panel.add(refreshButton, BorderLayout.SOUTH);
        reportArea.setText(collectTeacherStudentsReport());
        return panel;
    }

    private String collectTeacherStudentsReport() {
        StringBuilder sb = new StringBuilder();
        if (!isTeacher()) return "Only teachers can view this report.";
        Teacher teacher = (Teacher) currentUser;
        sb.append("=== STUDENTS ENROLLED IN ").append(teacher.getName()).append("'s COURSES ==="+"\n");
        for (Course course : courseRepo.getCampusRepo()) {
            if (course.getModerator().equalsIgnoreCase(teacher.getName())) {
                sb.append(course.getCourseName()).append(" (" + course.getCourseCode() + ")\n");
                for (Student student : course.getStudents()) {
                    sb.append("  - ").append(student.toString()).append("\n");
                }
                sb.append("\n");
            }
        }
        if (sb.toString().trim().isEmpty()) return "No enrolled students found for " + teacher.getName() + ".";
        return sb.toString();
    }

    private JPanel createStudentScheduleTab() {
        JPanel panel = new JPanel(new BorderLayout());
        JTextArea reportArea = new JTextArea();
        reportArea.setEditable(false);
        reportArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        JButton refreshButton = new JButton("Refresh My Courses");
        refreshButton.addActionListener(e -> reportArea.setText(collectStudentScheduleReport()));
        panel.add(new JScrollPane(reportArea), BorderLayout.CENTER);
        panel.add(refreshButton, BorderLayout.SOUTH);
        reportArea.setText(collectStudentScheduleReport());
        return panel;
    }

    private String collectStudentScheduleReport() {
        StringBuilder sb = new StringBuilder();
        if (!isStudent()) return "Only students can view this section.";
        Student student = (Student) currentUser;
        sb.append("=== MY COURSES === " +"\n");
        for (Course course : courseRepo.getCampusRepo()) {
            for (Student enrolled : course.getStudents()) {
                if (enrolled.getRegNO().equalsIgnoreCase(student.getRegNO())) {
                    sb.append(course.getCourseName()).append(" (").append(course.getCourseCode()).append(")\n");
                    sb.append("  Moderator: ").append(course.getModerator()).append("\n");
                    sb.append("  Time: ").append(course.getStarttime()).append(" - ").append(course.getEndtime()).append("\n");
                    sb.append("  Room: ").append(course.getRoomNO()).append("\n\n");
                    break;
                }
            }
        }
        if (sb.toString().trim().equals("=== MY COURSES ===")) {
            return "No enrolled courses found for " + student.getName() + ".";
        }
        return sb.toString();
    }

    private JPanel createResourceUsageSummaryTab() {
        JPanel panel = new JPanel(new BorderLayout());
        String[] columnNames = { "Resource", "Metric", "Value", "Status" };
        DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0);
        JTable table = new JTable(tableModel);
        table.setRowHeight(25);
        updateResourceUsageTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(table);
        JButton refreshButton = new JButton("Refresh Data");
        refreshButton.addActionListener(e -> updateResourceUsageTable(tableModel));
        panel.add(scrollPane, BorderLayout.CENTER);
        panel.add(refreshButton, BorderLayout.SOUTH);
        return panel;
    }

    private void updateResourceUsageTable(DefaultTableModel model) {
        model.setRowCount(0);
        if (!libraryRepo.getCampusRepo().isEmpty()) {
            Library lib = libraryRepo.getCampusRepo().get(0);
            model.addRow(new Object[] { "Library", "Visitors/Day", lib.getVisitorsPerDay(),
                    lib.getVisitorsPerDay() > 100 ? "Busy" : "Normal" });
            model.addRow(new Object[] { "Library", "Operational Cost",
                    "$" + String.format("%.2f", lib.getMaintenanceCost()), "Active" });
        }
        if (!cafeteriaRepo.getCampusRepo().isEmpty()) {
            Cafeteria caf = cafeteriaRepo.getCampusRepo().get(0);
            model.addRow(new Object[] { "Cafeteria", "Daily Customers", caf.getDailyCustomers(),
                    caf.getDailyCustomers() > 200 ? "Busy" : "Normal" });
            model.addRow(new Object[] { "Cafeteria", "Operational Cost",
                    "$" + String.format("%.2f", caf.getMaintenanceCost()), "Active" });
        }
        if (!hostelsRepo.getCampusRepo().isEmpty()) {
            Hostels h = hostelsRepo.getCampusRepo().get(0);
            int occupancy = (h.getTotalRooms() - h.getAvailableRooms()) * 100 / Math.max(1, h.getTotalRooms());
            String occupancyStatus = occupancy > 80 ? "High" : occupancy > 50 ? "Medium" : "Low";
            model.addRow(new Object[] { "Hostel", "Occupancy Rate", occupancy + "%", occupancyStatus });
            model.addRow(new Object[] { "Hostel", "Available Rooms", h.getAvailableRooms(), "Info" });
        }
        if (!transportRepo.getCampusRepo().isEmpty()) {
            TransportService ts = transportRepo.getCampusRepo().get(0);
            model.addRow(new Object[] { "Transport", "Peak Hours", ts.ispeakhour() ? "Active" : "Inactive",
                    ts.ispeakhour() ? "Busy" : "Normal" });
            model.addRow(new Object[] { "Transport", "Operational Cost",
                    "$" + String.format("%.2f", ts.calculateOperationalCost()), "Active" });
        }
        if (!securityRepo.getCampusRepo().isEmpty()) {
            SecurityService ss = securityRepo.getCampusRepo().get(0);
            model.addRow(new Object[] { "Security", "Guards On Duty", ss.getNoofguards(), "Active" });
            model.addRow(new Object[] { "Security", "Service Hours", ss.getServicehours(), "24/7" });
        }
        if (!healthRepo.getCampusRepo().isEmpty()) {
            HealthCenter hc = healthRepo.getCampusRepo().get(0);
            model.addRow(new Object[] { "Health Center", "Doctors", hc.getNoofdoctors(), "Available" });
            model.addRow(new Object[] { "Health Center", "Nurses", hc.getNoofnurses(), "Available" });
        }
        model.addRow(new Object[] { "Academic", "Total Students (enrolled)", AcademicUnit.getTotalStudents(), "Live" });
        model.addRow(new Object[] { "Academic", "Total Courses (active)", AcademicUnit.getTotalCourses(), "Live" });
        model.addRow(new Object[] { "Facilities", "Total Facility Usage", FacilityUnit.getTotalFacilityUsage(), "Live" });
        model.addRow(new Object[] { "Library", "Borrowed Books", Library.getBorrowedBooks(), "Live" });
        model.addRow(new Object[] { "Library", "Returned Books", Library.getReturnedBooks(), "Live" });
        model.addRow(new Object[] { "Summary", "Total Departments", departmentRepo.getCampusRepo().size(), "Active" });
    }

    private JPanel createFullReportTab() {
        JPanel panel = new JPanel(new BorderLayout());
        JTextArea reportArea = new JTextArea();
        reportArea.setEditable(false);
        reportArea.setFont(new Font("Monospaced", Font.PLAIN, 11));
        JButton generateButton = new JButton("Generate Report");
        generateButton.addActionListener(e -> generateReport(reportArea));
        JButton copyButton = new JButton("Copy to Clipboard");
        copyButton.addActionListener(e -> {
            reportArea.selectAll();
            reportArea.copy();
            JOptionPane.showMessageDialog(this, "Report copied to clipboard!", "Success",
                    JOptionPane.INFORMATION_MESSAGE);
        });
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(generateButton);
        buttonPanel.add(copyButton);
        panel.add(new JScrollPane(reportArea), BorderLayout.CENTER);
        panel.add(buttonPanel, BorderLayout.SOUTH);
        return panel;
    }

    private void generateReport(JTextArea area) {
        StringBuilder sb = new StringBuilder();
        if (isAdmin()) {
            Admin admin = (Admin) currentUser;
            sb.append("=== FULL CAMPUS REPORT ===\n\n");
            if (!departmentRepo.getCampusRepo().isEmpty()) {
                Department dept = departmentRepo.getCampusRepo().get(0);
                sb.append("DEPARTMENT SUMMARY:\n");
                sb.append(dept.toString()).append("\n\n");
                admin.generateDepartmentReport();
                sb.append("[Department report was also printed to console.]\n\n");
            }
            sb.append("STUDENTS:\n");
            for (Student s : studentRepo.getCampusRepo()) sb.append(s.toString()).append("\n\n");
            sb.append("TEACHERS:\n");
            for (Teacher t : teacherRepo.getCampusRepo()) sb.append(t.toString()).append("\n\n");
            sb.append("COURSES:\n");
            for (Course c : courseRepo.getCampusRepo()) {
                sb.append(c.toString()).append("Students Enrolled:\n");
                for (Student s : c.getStudents()) sb.append("  - ").append(s.getName()).append(" (" + s.getRegNO() + ")\n");
                sb.append("\n");
            }
            sb.append("LIBRARIES:\n");
            for (Library l : libraryRepo.getCampusRepo()) sb.append(l.toString()).append("\n\n");
            sb.append("CAFETERIAS:\n");
            for (Cafeteria c : cafeteriaRepo.getCampusRepo()) sb.append(c.toString()).append("\n\n");
            sb.append("HOSTELS:\n");
            for (Hostels h : hostelsRepo.getCampusRepo()) sb.append(h.toString()).append("\n\n");
            sb.append("CLASSROOMS:\n");
            for (ClassRoom c : classroomRepo.getCampusRepo()) sb.append(c.toString()).append("\n\n");
            sb.append("LABS:\n");
            for (Lab l : labRepo.getCampusRepo()) sb.append(l.toString()).append("\n\n");
            sb.append("TRANSPORT SERVICES:\n");
            for (TransportService t : transportRepo.getCampusRepo()) {
                sb.append(t.toString()).append("\nBuses:\n");
                for (Bus b : t.getBuses()) sb.append("  - ").append(b.toString()).append("\n");
                sb.append("\n");
            }
            sb.append("SECURITY SERVICES:\n");
            for (SecurityService s : securityRepo.getCampusRepo()) sb.append(s.toString()).append("\n\n");
            sb.append("HEALTH CENTERS:\n");
            for (HealthCenter h : healthRepo.getCampusRepo()) sb.append(h.toString()).append("\n\n");
        } else if (isTeacher()) {
            Teacher teacher = (Teacher) currentUser;
            sb.append("=== TEACHER REPORT for ").append(teacher.getName()).append(" ===\n\n");
            sb.append("My Courses:\n");
            for (Course course : courseRepo.getCampusRepo()) {
                if (course.getModerator().equalsIgnoreCase(teacher.getName())) {
                    sb.append(course.toString()).append("Students:\n");
                    for (Student s : course.getStudents()) sb.append("  - ").append(s.getName()).append(" (" + s.getRegNO() + ")\n");
                    sb.append("\n");
                }
            }
        } else if (isStudent()) {
            Student student = (Student) currentUser;
            sb.append("=== STUDENT REPORT for ").append(student.getName()).append(" ===\n\n");
            sb.append("Student Details:\n");
            sb.append(student.toString()).append("\n\n");
            sb.append("My Courses:\n");
            for (Course course : courseRepo.getCampusRepo()) {
                for (Student enrolled : course.getStudents()) {
                    if (enrolled.getRegNO().equalsIgnoreCase(student.getRegNO())) {
                        sb.append(course.toString()).append("\n");
                        break;
                    }
                }
            }
        }
        area.setText(sb.toString());
    }

    private JMenuBar createMenuBar() {
        JMenuBar menuBar = new JMenuBar();
        JMenu fileMenu = new JMenu("File");
        JMenuItem exitItem = new JMenuItem("Exit");
        exitItem.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(this, "Exit the application?", "Exit",
                    JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) System.exit(0);
        });
        fileMenu.addSeparator();
        fileMenu.add(exitItem);
        menuBar.add(fileMenu);
        return menuBar;
    }

    private void redirectSystemStreams() {
        PrintStream printStream = new PrintStream(new TextAreaOutputStream(consoleOutputArea), true);
        System.setOut(printStream);
        System.setErr(printStream);
    }

    private static class TextAreaOutputStream extends OutputStream {
        private final JTextArea textArea;
        TextAreaOutputStream(JTextArea textArea) { this.textArea = textArea; }
        @Override
        public void write(int b) throws IOException { write(new byte[] { (byte) b }, 0, 1); }
        @Override
        public void write(byte[] b, int off, int len) throws IOException {
            String text = new String(b, off, len);
            SwingUtilities.invokeLater(() -> {
                textArea.append(text);
                textArea.setCaretPosition(textArea.getDocument().getLength());
            });
        }
    }

    private class CampusMapPanel extends JPanel {
        private CampusRepository<ClassRoom> classroomRepo;
        private CampusRepository<Library> libraryRepo;
        private CampusRepository<Cafeteria> cafeteriaRepo;
        private CampusRepository<Hostels> hostelsRepo;
        private CampusRepository<TransportService> transportRepo;
        private CampusRepository<SecurityService> securityRepo;
        private CampusRepository<HealthCenter> healthRepo;
        private CampusRepository<Department> departmentRepo;

        private List<FacilityBlock> facilities;
        private static final int BLOCK_WIDTH = 140;
        private static final int BLOCK_HEIGHT = 80;
        private static final int PADDING = 20;
        private static final int COLS = 4;

        public CampusMapPanel(CampusRepository<ClassRoom> classroomRepo, CampusRepository<Library> libraryRepo,
                CampusRepository<Cafeteria> cafeteriaRepo, CampusRepository<Hostels> hostelsRepo,
                CampusRepository<TransportService> transportRepo, CampusRepository<SecurityService> securityRepo,
                CampusRepository<HealthCenter> healthRepo, CampusRepository<Department> departmentRepo) {
            this.classroomRepo = classroomRepo;
            this.libraryRepo = libraryRepo;
            this.cafeteriaRepo = cafeteriaRepo;
            this.hostelsRepo = hostelsRepo;
            this.transportRepo = transportRepo;
            this.securityRepo = securityRepo;
            this.healthRepo = healthRepo;
            this.departmentRepo = departmentRepo;
            facilities = new ArrayList<>();
            initializeFacilities();
            setBackground(new Color(240, 240, 240));
            addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) { handleBlockClick(e.getX(), e.getY()); }
            });
        }

        private void initializeFacilities() {
            facilities.add(new FacilityBlock("Academic Block", "Departments & Classrooms", 0, 0));
            facilities.add(new FacilityBlock("Library", "Study & Research", 1, 0));
            facilities.add(new FacilityBlock("SSC", "Dining Services", 2, 0));
            facilities.add(new FacilityBlock(" Scholar Hostel", "Student Housing", 3, 0));
            facilities.add(new FacilityBlock("Transport", "Bus Services", 0, 1));
            facilities.add(new FacilityBlock("Security", "Campus Safety", 1, 1));
            facilities.add(new FacilityBlock("Health Center", "Medical Services", 2, 1));
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g;
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            updateFacilityStatus();
            int row = 0, col = 0, x = PADDING, y = PADDING;
            for (FacilityBlock block : facilities) {
                block.x = x + col * (BLOCK_WIDTH + PADDING);
                block.y = y + row * (BLOCK_HEIGHT + PADDING + 40);
                drawFacilityBlock(g2d, block);
                col++;
                if (col >= COLS) { col = 0; row++; }
            }
            drawLegend(g2d);
        }

        private void updateFacilityStatus() {
            FacilityBlock academic = getBlockByName("Academic Block");
            FacilityBlock library = getBlockByName("Library");
            FacilityBlock cafeteria = getBlockByName("SSC");
            FacilityBlock hostel = getBlockByName(" Scholar Hostel");
            FacilityBlock transport = getBlockByName("Transport");
            FacilityBlock security = getBlockByName("Security");
            FacilityBlock health = getBlockByName("Health Center");

            boolean anyUnavailable = false;
            for (ClassRoom cr : classroomRepo.getCampusRepo()) if (!cr.isAvailable()) { anyUnavailable = true; break; }
            academic.status = anyUnavailable ? "Red" : "Green";
            academic.detail = classroomRepo.getCampusRepo().size() + " classrooms";

            if (!libraryRepo.getCampusRepo().isEmpty()) {
                Library lib = libraryRepo.getCampusRepo().get(0);
                academic.status = lib.getVisitorsPerDay() > 100 ? "Orange" : "Green";
                library.status = lib.getVisitorsPerDay() > 100 ? "Orange" : "Green";
                library.detail = lib.getVisitorsPerDay() + " visitors/day";
            }
            if (!cafeteriaRepo.getCampusRepo().isEmpty()) {
                Cafeteria caf = cafeteriaRepo.getCampusRepo().get(0);
                cafeteria.status = caf.getDailyCustomers() > 200 ? "Orange" : "Green";
                cafeteria.detail = caf.getDailyCustomers() + " customers/day";
            }
            if (!hostelsRepo.getCampusRepo().isEmpty()) {
                Hostels h = hostelsRepo.getCampusRepo().get(0);
                int occupancy = h.getOccupiedRooms() * 100 / h.getTotalRooms();
                hostel.status = h.getOccupiedRooms() == 0 ? "Red" : (occupancy > 80 ? "Orange" : "Green");
                hostel.detail = h.getOccupiedRooms() + "/" + h.getTotalRooms() + " rooms";
            }
            if (!transportRepo.getCampusRepo().isEmpty()) {
                TransportService ts = transportRepo.getCampusRepo().get(0);
                transport.status = ts.ispeakhour() ? "Orange" : "Green";
                transport.detail = ts.ispeakhour() ? "Peak hours" : "Normal hours";
            }
            security.status = "Green";
            security.detail = securityRepo.getCampusRepo().size() + " service(s)";
            health.status = "Green";
            health.detail = healthRepo.getCampusRepo().size() + " center(s)";
        }

        private FacilityBlock getBlockByName(String name) {
            for (FacilityBlock block : facilities) {
                if (block.name.equals(name)) return block;
            }
            return null;
        }

        private void drawFacilityBlock(Graphics2D g2d, FacilityBlock block) {
            Color statusColor = getColorForStatus(block.status);
            g2d.setColor(statusColor);
            g2d.fillRect(block.x, block.y, BLOCK_WIDTH, BLOCK_HEIGHT);
            g2d.setColor(Color.BLACK);
            g2d.setStroke(new BasicStroke(2));
            g2d.drawRect(block.x, block.y, BLOCK_WIDTH, BLOCK_HEIGHT);
            g2d.setFont(new Font("Arial", Font.BOLD, 11));
            drawCenteredString(g2d, block.name, block.x, block.y + 20, BLOCK_WIDTH);
            g2d.setFont(new Font("Arial", Font.PLAIN, 9));
            drawCenteredString(g2d, block.description, block.x, block.y + 40, BLOCK_WIDTH);
            drawCenteredString(g2d, block.detail, block.x, block.y + 55, BLOCK_WIDTH);
            g2d.setFont(new Font("Arial", Font.BOLD, 8));
            g2d.setColor(Color.WHITE);
            drawCenteredString(g2d, block.status, block.x, block.y + BLOCK_HEIGHT - 8, BLOCK_WIDTH);
            setToolTipText(block.name + "\n" + block.detail);
        }

        private void drawCenteredString(Graphics2D g2d, String text, int x, int y, int width) {
            FontMetrics fm = g2d.getFontMetrics();
            int textWidth = fm.stringWidth(text);
            int xPos = x + (width - textWidth) / 2;
            g2d.drawString(text, xPos, y);
        }

        private void drawLegend(Graphics2D g2d) {
            int legendY = getHeight() - 60;
            g2d.setColor(Color.DARK_GRAY);
            g2d.setFont(new Font("Arial", Font.BOLD, 12));
            g2d.drawString("Legend:", PADDING, legendY);
            g2d.setFont(new Font("Arial", Font.PLAIN, 10));
            int legendX = PADDING;
            int legendItemY = legendY + 20;
            g2d.setColor(Color.GREEN);
            g2d.fillRect(legendX, legendItemY, 15, 15);
            g2d.setColor(Color.BLACK);
            g2d.drawRect(legendX, legendItemY, 15, 15);
            g2d.drawString("Operational", legendX + 20, legendItemY + 12);
            legendX += 150;
            g2d.setColor(Color.ORANGE);
            g2d.fillRect(legendX, legendItemY, 15, 15);
            g2d.setColor(Color.BLACK);
            g2d.drawRect(legendX, legendItemY, 15, 15);
            g2d.drawString("Busy/High Usage", legendX + 20, legendItemY + 12);
            legendX += 180;
            g2d.setColor(Color.RED);
            g2d.fillRect(legendX, legendItemY, 15, 15);
            g2d.setColor(Color.BLACK);
            g2d.drawRect(legendX, legendItemY, 15, 15);
            g2d.drawString("Closed/Unavailable", legendX + 20, legendItemY + 12);
        }

        private Color getColorForStatus(String status) {
            switch (status) {
                case "Green": return Color.GREEN;
                case "Orange": return Color.ORANGE;
                case "Red": return Color.RED;
                default: return Color.LIGHT_GRAY;
            }
        }

        private void handleBlockClick(int x, int y) {
            for (FacilityBlock block : facilities) {
                if (x >= block.x && x <= block.x + BLOCK_WIDTH && y >= block.y && y <= block.y + BLOCK_HEIGHT) {
                    showFacilityDetails(block.name);
                    return;
                }
            }
        }

        private void showFacilityDetails(String facilityName) {
            StringBuilder details = new StringBuilder();
            details.append("=== ").append(facilityName).append(" ===\n\n");
            switch (facilityName) {
                case "Academic Block":
                    details.append("Total Classrooms: ").append(classroomRepo.getCampusRepo().size()).append("\n");
                    details.append("Departments: ").append(departmentRepo.getCampusRepo().size()).append("\n");
                    for (ClassRoom cr : classroomRepo.getCampusRepo()) {
                        details.append("- ").append(cr.getName()).append(" (").append(cr.getCapacity())
                                .append(" seats, ").append(cr.isAvailable() ? "Available" : "Unavailable").append(")\n");
                    }
                    break;
                case "Library":
                    if (!libraryRepo.getCampusRepo().isEmpty()) {
                        Library lib = libraryRepo.getCampusRepo().get(0);
                        details.append("Name: ").append(lib.getName()).append("\n");
                        details.append("Location: ").append(lib.getLocation()).append("\n");
                        details.append("Visitors/Day: ").append(lib.getVisitorsPerDay()).append("\n");
                        details.append("Maintenance Cost: $").append(String.format("%.2f", lib.getMaintenanceCost())).append("\n");
                        details.append("Usage Frequency: ").append(lib.getUsageFrequency()).append("\n");
                    }
                    break;
                case "SSC":
                    if (!cafeteriaRepo.getCampusRepo().isEmpty()) {
                        Cafeteria caf = cafeteriaRepo.getCampusRepo().get(0);
                        details.append("Name: ").append(caf.getName()).append("\n");
                        details.append("Daily Customers: ").append(caf.getDailyCustomers()).append("\n");
                        details.append("Maintenance Cost: $").append(String.format("%.2f", caf.getMaintenanceCost())).append("\n");
                    }
                    break;
                case "Scholar Hostel":
                    if (!hostelsRepo.getCampusRepo().isEmpty()) {
                        Hostels h = hostelsRepo.getCampusRepo().get(0);
                        details.append("Name: ").append(h.getName()).append("\n");
                        details.append("Total Rooms: ").append(h.getTotalRooms()).append("\n");
                        details.append("Occupied Rooms: ").append(h.getOccupiedRooms()).append("\n");
                        details.append("Available Rooms: ").append(h.getTotalRooms() - h.getOccupiedRooms()).append("\n");
                        details.append("Occupancy Rate: ").append(String.format("%.1f%%", (h.getOccupiedRooms() * 100.0 / h.getTotalRooms()))).append("\n");
                    }
                    break;
                case "Transport":
                    if (!transportRepo.getCampusRepo().isEmpty()) {
                        TransportService ts = transportRepo.getCampusRepo().get(0);
                        details.append("Name: ").append(ts.getName()).append("\n");
                        details.append("Service Hours: ").append(ts.getServicehours()).append("\n");
                        details.append("Peak Hours Active: ").append(ts.ispeakhour() ? "Yes" : "No").append("\n");
                        details.append("Cost per Hour: $").append(String.format("%.2f", ts.getCostperhour())).append("\n");
                    }
                    break;
                case "Security":
                    if (!securityRepo.getCampusRepo().isEmpty()) {
                        SecurityService ss = securityRepo.getCampusRepo().get(0);
                        details.append("Name: ").append(ss.getName()).append("\n");
                        details.append("Guards: ").append(ss.getNoofguards()).append("\n");
                        details.append("Guard Salary: $").append(ss.getGuardsalary()).append("\n");
                        details.append("Service Hours: ").append(ss.getServicehours()).append("\n");
                    }
                    break;
                case "Health Center":
                    if (!healthRepo.getCampusRepo().isEmpty()) {
                        HealthCenter hc = healthRepo.getCampusRepo().get(0);
                        details.append("Name: ").append(hc.getName()).append("\n");
                        details.append("Doctors: ").append(hc.getNoofdoctors()).append("\n");
                        details.append("Nurses: ").append(hc.getNoofnurses()).append("\n");
                        details.append("Service Hours: ").append(hc.getServicehours()).append("\n");
                    }
                    break;
            }
            JTextArea textArea = new JTextArea(details.toString());
            textArea.setEditable(false);
            textArea.setFont(new Font("Monospaced", Font.PLAIN, 11));
            JScrollPane scrollPane = new JScrollPane(textArea);
            scrollPane.setPreferredSize(new Dimension(400, 300));
            JOptionPane.showMessageDialog(this, scrollPane, facilityName + " Details", JOptionPane.INFORMATION_MESSAGE);
        }

        public void refreshMap() { repaint(); }

        private static class FacilityBlock {
            String name, description, detail = "", status = "Green";
            int x, y;
            FacilityBlock(String name, String description, int col, int row) {
                this.name = name;
                this.description = description;
            }
        }
    }

    private <T> JPanel createEntityPanel(String entityType, CampusRepository<T> repo, String[] columnNames) {
        JPanel panel = new JPanel(new BorderLayout());
        DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0);
        JTable table = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(table);
        JPanel formPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        formPanel.add(new JLabel("Select an entity row and use the buttons below to manage items."));
        JPanel buttonsPanel = new JPanel();
        JButton addButton = new JButton("Add " + entityType);
        JButton editButton = new JButton("Edit Selected");
        JButton deleteButton = new JButton("Delete Selected");
        JButton viewDetailsButton = new JButton("View Details");
        JButton refreshButton = new JButton("Refresh List");

        viewDetailsButton.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(this, "Please select an item to view details.", "No Selection",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }
            T entity = repo.getCampusRepo().get(selectedRow);
            if (entity != null) {
                JTextArea detailsArea = new JTextArea(entity.toString());
                detailsArea.setEditable(false);
                detailsArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
                JScrollPane detailsScrollPane = new JScrollPane(detailsArea);
                detailsScrollPane.setPreferredSize(new Dimension(450, 320));
                JOptionPane.showMessageDialog(this, detailsScrollPane, entityType + " Details",
                        JOptionPane.INFORMATION_MESSAGE);
            }
        });

        addButton.addActionListener(e -> showAddDialog(entityType, repo, tableModel));
        editButton.addActionListener(e -> editSelectedEntity(entityType, repo, table, tableModel));
        deleteButton.addActionListener(e -> deleteSelectedEntity(entityType, repo, table, tableModel));
        refreshButton.addActionListener(e -> refreshTable(entityType, repo, tableModel));

        if (canModifyEntity(entityType) && !entityType.equals("Admin")) {
            if (!entityType.equals("Library") && !entityType.equals("HealthCenter") &&
                    !entityType.equals("SecurityService") && !entityType.equals("Cafeteria")) {
                buttonsPanel.add(addButton);
            }
            buttonsPanel.add(editButton);
            buttonsPanel.add(deleteButton);
        }
        buttonsPanel.add(viewDetailsButton);
        buttonsPanel.add(refreshButton);

        panel.add(scrollPane, BorderLayout.CENTER);
        panel.add(formPanel, BorderLayout.NORTH);
        panel.add(buttonsPanel, BorderLayout.SOUTH);

        refreshTable(entityType, repo, tableModel);
        return panel;
    }

    private <T> void showAddDialog(String entityType, CampusRepository<T> repo, DefaultTableModel tableModel) {
        JDialog dialog = new JDialog(this, "Add " + entityType, true);
        dialog.setSize(400, 300);
        dialog.setLocationRelativeTo(this);
        JPanel panel = new JPanel(new GridLayout(0, 2, 5, 5));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        JTextField[] fields = createFieldsForEntity(entityType);
        String[] fieldNames = getFieldNamesForEntity(entityType);
        for (int i = 0; i < fieldNames.length; i++) {
            panel.add(new JLabel(fieldNames[i] + ":"));
            panel.add(fields[i]);
        }
        JButton saveButton = new JButton("Save");
        JButton cancelButton = new JButton("Cancel");
        saveButton.addActionListener(e -> {
            try {
                T entity = createEntityFromFields(entityType, fields);
                validateDuplicateEntity(entityType, entity, repo, null);
                repo.AddItem(entity);
                if ("LibraryBooks".equals(entityType) && !libraryRepo.getCampusRepo().isEmpty()) {
                    Library lib = libraryRepo.getCampusRepo().get(0);
                    lib.addBook((LibraryBooks) entity);
                }
                refreshTable(entityType, repo, tableModel);
                autoSaveData();
                dialog.dispose();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(dialog, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        cancelButton.addActionListener(e -> dialog.dispose());
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(saveButton);
        buttonPanel.add(cancelButton);
        dialog.add(panel, BorderLayout.CENTER);
        dialog.add(buttonPanel, BorderLayout.SOUTH);
        dialog.setVisible(true);
    }

    private <T> void editSelectedEntity(String entityType, CampusRepository<T> repo, JTable table, DefaultTableModel tableModel) {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select an item to edit.", "No Selection",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }
        T entity = repo.getCampusRepo().get(selectedRow);
        showEditDialog(entityType, repo, entity, tableModel);
    }

    private <T> void showEditDialog(String entityType, CampusRepository<T> repo, T entity, DefaultTableModel tableModel) {
        JDialog dialog = new JDialog(this, "Edit " + entityType, true);
        dialog.setSize(400, 300);
        dialog.setLocationRelativeTo(this);
        JPanel panel = new JPanel(new GridLayout(0, 2, 5, 5));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        JTextField[] fields = createFieldsForEntity(entityType);
        populateFieldsFromEntity(entityType, entity, fields);
        String[] fieldNames = getFieldNamesForEntity(entityType);
        for (int i = 0; i < fieldNames.length; i++) {
            panel.add(new JLabel(fieldNames[i] + ":"));
            panel.add(fields[i]);
        }
        JButton saveButton = new JButton("Save");
        JButton cancelButton = new JButton("Cancel");
        saveButton.addActionListener(e -> {
            try {
                updateEntityFromFields(entityType, entity, fields);
                validateDuplicateEntity(entityType, entity, repo, entity);
                refreshTable(entityType, repo, tableModel);
                autoSaveData();
                if ("LibraryBooks".equals(entityType) && !libraryRepo.getCampusRepo().isEmpty()) {
                    Library lib = libraryRepo.getCampusRepo().get(0);
                    lib.getBooks().clear();
                    for (LibraryBooks book : libraryBooksRepo.getCampusRepo()) lib.addBook(book);
                }
                dialog.dispose();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(dialog, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        cancelButton.addActionListener(e -> dialog.dispose());
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(saveButton);
        buttonPanel.add(cancelButton);
        dialog.add(panel, BorderLayout.CENTER);
        dialog.add(buttonPanel, BorderLayout.SOUTH);
        dialog.setVisible(true);
    }

    private <T> void deleteSelectedEntity(String entityType, CampusRepository<T> repo, JTable table, DefaultTableModel tableModel) {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select an item to delete.", "No Selection",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }
        int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete this item?",
                "Confirm Delete", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            T entity = repo.getCampusRepo().get(selectedRow);
            repo.RemoveItem(entity);
            if ("LibraryBooks".equals(entityType) && !libraryRepo.getCampusRepo().isEmpty()) {
                Library lib = libraryRepo.getCampusRepo().get(0);
                lib.removeBook((LibraryBooks) entity);
            }
            refreshTable(entityType, repo, tableModel);
            autoSaveData();
        }
    }

    private <T> void refreshTable(String entityType, CampusRepository<T> repo, DefaultTableModel tableModel) {
        tableModel.setRowCount(0);
        for (T entity : filterEntities(entityType, repo.getCampusRepo())) {
            Object[] row = getRowDataFromEntity(entityType, entity);
            tableModel.addRow(row);
        }
    }

    private <T> java.util.List<T> filterEntities(String entityType, java.util.List<T> entities) {
        if (currentUser == null || isAdmin()) return entities;
        java.util.List<T> filtered = new java.util.ArrayList<>();
        if (entityType == null) return entities;
        for (T entity : entities) {
            switch (entityType) {
                case "Student":
                    if (isTeacher()) {
                        Teacher teacher = (Teacher) currentUser;
                        for (Course course : courseRepo.getCampusRepo()) {
                            if (course.getModerator().equalsIgnoreCase(teacher.getName())) {
                                for (Student student : course.getStudents()) {
                                    if (student.equals(entity)) { filtered.add(entity); break; }
                                }
                            }
                            if (filtered.contains(entity)) break;
                        }
                    } else if (isStudent()) {
                        if (((Student) entity).getRegNO().equalsIgnoreCase(((Student) currentUser).getRegNO()))
                            filtered.add(entity);
                    }
                    break;
                case "Teacher":
                    if (isTeacher() && ((Teacher) entity).getID().equalsIgnoreCase(((Teacher) currentUser).getID()))
                        filtered.add(entity);
                    break;
                case "Course":
                    if (isTeacher()) {
                        if (((Course) entity).getModerator().equalsIgnoreCase(((Teacher) currentUser).getName()))
                            filtered.add(entity);
                    } else if (isStudent()) {
                        for (Student student : ((Course) entity).getStudents()) {
                            if (student.getRegNO().equalsIgnoreCase(((Student) currentUser).getRegNO())) {
                                filtered.add(entity);
                                break;
                            }
                        }
                    }
                    break;
                default:
                    filtered.add(entity);
            }
        }
        return filtered;
    }

    private JTextField[] createFieldsForEntity(String entityType) {
        switch (entityType) {
            case "Student":  return new JTextField[]{new JTextField(), new JTextField(), new JTextField(), new JTextField(), new JTextField(), new JTextField()};
            case "Teacher":  return new JTextField[]{new JTextField(), new JTextField(), new JTextField(), new JTextField(), new JTextField(), new JTextField()};
            case "Admin":  return new JTextField[]{new JTextField(), new JTextField(), new JTextField(), new JTextField(), new JTextField(), new JTextField()};
            case "Department": return new JTextField[]{new JTextField(), new JTextField(), new JTextField(), new JTextField(), new JTextField(), new JTextField()};
            case "Course": return new JTextField[]{new JTextField(), new JTextField(), new JTextField(), new JTextField(), new JTextField(), new JTextField(), new JTextField()};
            case "Library": return new JTextField[]{new JTextField(), new JTextField(), new JTextField(), new JTextField(), new JTextField(), new JTextField()};
            case "Cafeteria": return new JTextField[]{new JTextField(), new JTextField(), new JTextField(), new JTextField(), new JTextField(), new JTextField()};
            case "Hostels": return new JTextField[]{new JTextField(), new JTextField(), new JTextField(), new JTextField(), new JTextField(), new JTextField(), new JTextField()};
            case "TransportService":  return new JTextField[]{new JTextField(), new JTextField(), new JTextField(), new JTextField(), new JTextField(), new JTextField()};
            case "ClassRoom": return new JTextField[]{new JTextField(), new JTextField(), new JTextField(), new JTextField(), new JTextField(), new JTextField(), new JTextField(), new JTextField()};
            case "Lab": return new JTextField[]{new JTextField(), new JTextField(), new JTextField(), new JTextField(), new JTextField(), new JTextField()};
            case "Bus": return new JTextField[]{new JTextField(), new JTextField(), new JTextField(), new JTextField(), new JTextField()};
            case "LibraryBooks": return new JTextField[]{new JTextField(), new JTextField(), new JTextField()};
            case "HealthCenter":  return new JTextField[]{new JTextField(), new JTextField(), new JTextField(), new JTextField(), new JTextField(), new JTextField()};
            default: return new JTextField[0];     
        }
    }

    private String[] getFieldNamesForEntity(String entityType) {
        switch (entityType) {
            case "Student": return new String[] { "Name", "User Type", "Reg No", "Department", "Fee", "CGPA" };
            case "Teacher": return new String[] { "Name", "User Type", "Teacher ID", "Salary" };
            case "Admin": return new String[] { "Name", "User Type", "Admin ID" };
            case "Department": return new String[] { "Entity ID", "Name", "Location", "Total Staff", "Budget" };
            case "Course": return new String[] { "Course Name", "Course Code", "Moderator", "Credits", "Start Time", "End Time", "Room No" };
            case "Library": return new String[] { "Entity ID", "Name", "Location", "Maintenance Cost", "Usage Frequency", "Visitors/Day" };
            case "Cafeteria": return new String[] { "Entity ID", "Name", "Location", "Maintenance Cost", "Usage Frequency", "Daily Customers" };
            case "Hostels": return new String[] { "Entity ID", "Name", "Location", "Maintenance Cost", "Usage Frequency", "Total Rooms", "Occupied Rooms" };
            case "TransportService": return new String[] { "Entity ID", "Name", "Location", "Service Hours", "Cost per Hour", "Is Peak Hour" };
            case "SecurityService": return new String[] { "Entity ID", "Name", "Location", "No of Guards", "Guard Salary", "Service Hours", "Cost per Hour" };
            case "ClassRoom": return new String[] { "Entity ID", "Name", "Location", "Room No", "Capacity", "Available", "Total Staff", "Budget" };
            case "Lab": return new String[] { "Entity ID", "Name", "Location", "Lab No", "Total Staff", "Budget" };
            case "Bus": return new String[] { "Bus ID", "Driver Salary", "From", "To", "Departure Time" };
            case "LibraryBooks": return new String[] { "Title", "Author", "Borrowed" };
            case "HealthCenter": return new String[] { "Entity ID", "Name", "Location", "No of Doctors", "Doctor Salary", "No of Nurses", "Nurse Salary", "Service Hours", "Cost per Hour" };
            default: return new String[0];
        }
    }

    @SuppressWarnings("unchecked")
    private <T> T createEntityFromFields(String entityType, JTextField[] fields) throws Exception {
        switch (entityType) {
            case "Student": return (T) new Student(fields[0].getText(), fields[1].getText(), fields[2].getText(), fields[3].getText(),
                    Integer.parseInt(fields[4].getText()), Double.parseDouble(fields[5].getText()));
            case "Teacher": return (T) new Teacher(fields[0].getText(), fields[1].getText(), fields[2].getText(), Integer.parseInt(fields[3].getText()));
            case "Admin": return (T) new Admin(fields[0].getText(), fields[1].getText(), fields[2].getText(), null, null, null, null, null, null, null);
            case "Department": return (T) new Department(fields[0].getText(), fields[1].getText(), fields[2].getText(),
                    Integer.parseInt(fields[3].getText()), Double.parseDouble(fields[4].getText()));
            case "Course": return (T) new Course(fields[0].getText(), fields[1].getText(), fields[2].getText(),
                    Integer.parseInt(fields[3].getText()), fields[4].getText(), fields[5].getText(), Integer.parseInt(fields[6].getText()));
            case "Library": return (T) new Library(fields[0].getText(), fields[1].getText(), fields[2].getText(),
                    Double.parseDouble(fields[3].getText()), Integer.parseInt(fields[4].getText()), Integer.parseInt(fields[5].getText()));
            case "Cafeteria": return (T) new Cafeteria(fields[0].getText(), fields[1].getText(), fields[2].getText(),
                    Double.parseDouble(fields[3].getText()), Integer.parseInt(fields[4].getText()), Integer.parseInt(fields[5].getText()));
            case "Hostels": return (T) new Hostels(fields[0].getText(), fields[1].getText(), fields[2].getText(),
                    Double.parseDouble(fields[3].getText()), Integer.parseInt(fields[4].getText()), Integer.parseInt(fields[5].getText()), Integer.parseInt(fields[6].getText()));
            case "TransportService": return (T) new TransportService(fields[0].getText(), fields[1].getText(), fields[2].getText(),
                    Integer.parseInt(fields[3].getText()), Double.parseDouble(fields[4].getText()), Boolean.parseBoolean(fields[5].getText()));
            case "SecurityService": return (T) new SecurityService(fields[0].getText(), fields[1].getText(), fields[2].getText(),
                    Integer.parseInt(fields[3].getText()), Integer.parseInt(fields[4].getText()), Integer.parseInt(fields[5].getText()), Double.parseDouble(fields[6].getText()));
            case "ClassRoom": return (T) new ClassRoom(fields[0].getText(), fields[1].getText(), fields[2].getText(),
                    Integer.parseInt(fields[3].getText()), Integer.parseInt(fields[4].getText()), Boolean.parseBoolean(fields[5].getText()),
                    Integer.parseInt(fields[6].getText()), Double.parseDouble(fields[7].getText()));
            case "Lab": return (T) new Lab(fields[0].getText(), fields[1].getText(), fields[2].getText(),
                    Integer.parseInt(fields[3].getText()), Integer.parseInt(fields[4].getText()), Double.parseDouble(fields[5].getText()));
            case "Bus": return (T) new Bus(fields[0].getText(), Integer.parseInt(fields[1].getText()), fields[2].getText(), fields[3].getText(), fields[4].getText());
            case "LibraryBooks": return (T) new LibraryBooks(fields[0].getText(), fields[1].getText(), Boolean.parseBoolean(fields[2].getText()));
            case "HealthCenter": return (T) new HealthCenter(fields[0].getText(), fields[1].getText(), fields[2].getText(),
                    Integer.parseInt(fields[3].getText()), Integer.parseInt(fields[4].getText()), Integer.parseInt(fields[5].getText()),
                    Integer.parseInt(fields[6].getText()), Integer.parseInt(fields[7].getText()), (int) Double.parseDouble(fields[8].getText()));
            default: throw new Exception("Unknown entity type: " + entityType);
        }
    }


    private <T> void updateEntityFromFields(String entityType, T entity, JTextField[] fields) throws Exception {
        switch (entityType) {
            case "Student": Student s = (Student) entity; s.setName(fields[0].getText()); s.setUserType(fields[1].getText()); s.setRegNO(fields[2].getText());
                s.setDepartment(fields[3].getText()); s.setFee(Integer.parseInt(fields[4].getText())); s.setCGPA(Double.parseDouble(fields[5].getText())); break;
            case "Teacher": Teacher t = (Teacher) entity; t.setName(fields[0].getText()); t.setUserType(fields[1].getText()); t.setID(fields[2].getText());
                t.setSalary(Integer.parseInt(fields[3].getText())); break;
            case "Admin": Admin a = (Admin) entity; a.setName(fields[0].getText()); a.setUserType(fields[1].getText()); a.setAdminID(fields[2].getText()); break;
            case "Department": Department d = (Department) entity; d.setEntityID(fields[0].getText()); d.setName(fields[1].getText()); d.setLocation(fields[2].getText());
                d.setTotalStaff(Integer.parseInt(fields[3].getText())); d.setBudget(Double.parseDouble(fields[4].getText())); break;
            case "Course": Course c = (Course) entity; c.setCourseName(fields[0].getText()); c.setCourseCode(fields[1].getText()); c.setModerator(fields[2].getText());
                c.setCredits(Integer.parseInt(fields[3].getText())); c.setStarttime(fields[4].getText()); c.setEndtime(fields[5].getText()); c.setRoomNO(Integer.parseInt(fields[6].getText())); break;
            case "Library": Library l = (Library) entity; l.setEntityID(fields[0].getText()); l.setName(fields[1].getText()); l.setLocation(fields[2].getText());
                l.setMaintenanceCost(Double.parseDouble(fields[3].getText())); l.setUsageFrequency(Integer.parseInt(fields[4].getText())); l.setVisitorsPerDay(Integer.parseInt(fields[5].getText())); break;
            case "Cafeteria": Cafeteria caf = (Cafeteria) entity; caf.setEntityID(fields[0].getText()); caf.setName(fields[1].getText()); caf.setLocation(fields[2].getText());
                caf.setMaintenanceCost(Double.parseDouble(fields[3].getText())); caf.setUsageFrequency(Integer.parseInt(fields[4].getText())); caf.setDailyCustomers(Integer.parseInt(fields[5].getText())); break;
            case "Hostels": Hostels h = (Hostels) entity; h.setEntityID(fields[0].getText()); h.setName(fields[1].getText()); h.setLocation(fields[2].getText());
                h.setMaintenanceCost(Double.parseDouble(fields[3].getText())); h.setUsageFrequency(Integer.parseInt(fields[4].getText()));
                h.setTotalRooms(Integer.parseInt(fields[5].getText())); h.setOccupiedRooms(Integer.parseInt(fields[6].getText())); break;
            case "TransportService": TransportService trans = (TransportService) entity; trans.setEntityID(fields[0].getText()); trans.setName(fields[1].getText()); trans.setLocation(fields[2].getText());
                trans.setServicehours(Integer.parseInt(fields[3].getText())); trans.setCostperhour(Double.parseDouble(fields[4].getText())); trans.setispeakhour(Boolean.parseBoolean(fields[5].getText())); break;
            case "SecurityService": SecurityService sec = (SecurityService) entity; sec.setEntityID(fields[0].getText()); sec.setName(fields[1].getText()); sec.setLocation(fields[2].getText());
                sec.setNoofguards(Integer.parseInt(fields[3].getText())); sec.setGuardsalary(Integer.parseInt(fields[4].getText()));
                sec.setServicehours(Integer.parseInt(fields[5].getText())); sec.setCostperhour(Double.parseDouble(fields[6].getText())); break;
            case "ClassRoom": ClassRoom cr = (ClassRoom) entity; cr.setEntityID(fields[0].getText()); cr.setName(fields[1].getText()); cr.setLocation(fields[2].getText());
                cr.setRoomNumber(Integer.parseInt(fields[3].getText())); cr.setCapacity(Integer.parseInt(fields[4].getText())); cr.setAvailable(Boolean.parseBoolean(fields[5].getText()));
                cr.setTotalStaff(Integer.parseInt(fields[6].getText())); cr.setBudget(Double.parseDouble(fields[7].getText())); break;
            case "Lab": Lab lab = (Lab) entity; lab.setEntityID(fields[0].getText()); lab.setName(fields[1].getText()); lab.setLocation(fields[2].getText());
                lab.setLabNo(Integer.parseInt(fields[3].getText())); lab.setTotalStaff(Integer.parseInt(fields[4].getText())); lab.setBudget(Double.parseDouble(fields[5].getText())); break;
            case "Bus": Bus bus = (Bus) entity; bus.setBusID(fields[0].getText()); bus.setDriverSalary(Integer.parseInt(fields[1].getText()));
                bus.setFrom(fields[2].getText()); bus.setTo(fields[3].getText()); bus.setDepartureTime(fields[4].getText()); break;
            case "LibraryBooks": LibraryBooks lb = (LibraryBooks) entity; lb.setTitle(fields[0].getText()); lb.setAuthor(fields[1].getText()); lb.setBorrowed(Boolean.parseBoolean(fields[2].getText())); break;
            case "HealthCenter": HealthCenter hc = (HealthCenter) entity; hc.setEntityID(fields[0].getText()); hc.setName(fields[1].getText()); hc.setLocation(fields[2].getText());
                hc.setNoofdoctors(Integer.parseInt(fields[3].getText())); hc.setDoctorsalary(Integer.parseInt(fields[4].getText()));
                hc.setNoofnurses(Integer.parseInt(fields[5].getText())); hc.setNursesalary(Integer.parseInt(fields[6].getText()));
                hc.setServicehours(Integer.parseInt(fields[7].getText())); hc.setCostperhour(Double.parseDouble(fields[8].getText())); break;
            default: throw new Exception("Unknown entity type: " + entityType);
        }
    }

    private <T> void validateDuplicateEntity(String entityType, T entity, CampusRepository<T> repo, T ignore) throws Exception {
        String key = getEntityKeyValue(entityType, entity);
        String label = getEntityKeyLabel(entityType);
        if (key == null || key.trim().isEmpty()) throw new Exception(label + " is required.");
        for (T existing : repo.getCampusRepo()) {
            if (existing == ignore) continue;
            String existingKey = getEntityKeyValue(entityType, existing);
            if (existingKey != null && existingKey.equalsIgnoreCase(key))
                throw new Exception(entityType + " with the same " + label + " already exists.");
        }
    }

    private <T> String getEntityKeyValue(String entityType, T entity) {
        if (entity == null) return null;
        switch (entityType) {
            case "Student": return ((Student) entity).getRegNO();
            case "Teacher": return ((Teacher) entity).getID();
            case "Admin": return ((Admin) entity).getAdminID();
            case "Course": return ((Course) entity).getCourseCode();
            case "ClassRoom": case "Lab": case "Department": case "Library": case "Cafeteria": case "Hostels":
            case "TransportService": case "SecurityService": case "HealthCenter":
                return ((CampusEntity) entity).getEntityID();
            case "Bus": return ((Bus) entity).getBusID();
            case "LibraryBooks": return ((LibraryBooks) entity).getTitle();
            default: return null;
        }
    }

    private String getEntityKeyLabel(String entityType) {
        switch (entityType) {
            case "Student": return "Reg No";
            case "Teacher": return "Teacher ID";
            case "Admin": return "Admin ID";
            case "Course": return "Course Code";
            case "ClassRoom": case "Lab": case "Department": case "Library": case "Cafeteria": case "Hostels":
            case "TransportService": case "SecurityService": case "HealthCenter": return "Entity ID";
            case "Bus": return "Bus ID";
            case "LibraryBooks": return "Title";
            default: return "Identifier";
        }
    }

    private <T> void populateFieldsFromEntity(String entityType, T entity, JTextField[] fields) {
        switch (entityType) {
            case "Student": Student s = (Student) entity; fields[0].setText(s.getName()); fields[1].setText(s.getUserType()); fields[2].setText(s.getRegNO());
                fields[3].setText(s.getDepartment()); fields[4].setText(String.valueOf(s.getFee())); fields[5].setText(String.valueOf(s.getCGPA())); break;
            case "Teacher": Teacher t = (Teacher) entity; fields[0].setText(t.getName()); fields[1].setText(t.getUserType()); fields[2].setText(t.getID());
                fields[3].setText(String.valueOf(t.getSalary())); break;
            case "Admin": Admin a = (Admin) entity; fields[0].setText(a.getName()); fields[1].setText(a.getUserType()); fields[2].setText(a.getAdminID()); break;
            case "Department": Department d = (Department) entity; fields[0].setText(d.getEntityID()); fields[1].setText(d.getName()); fields[2].setText(d.getLocation());
                fields[3].setText(String.valueOf(d.getTotalStaff())); fields[4].setText(String.valueOf(d.getBudget())); break;
            case "Course": Course c = (Course) entity; fields[0].setText(c.getCourseName()); fields[1].setText(c.getCourseCode()); fields[2].setText(c.getModerator());
                fields[3].setText(String.valueOf(c.getCredits())); fields[4].setText(c.getStarttime()); fields[5].setText(c.getEndtime()); fields[6].setText(String.valueOf(c.getRoomNO())); break;
            case "Library": Library l = (Library) entity; fields[0].setText(l.getEntityID()); fields[1].setText(l.getName()); fields[2].setText(l.getLocation());
                fields[3].setText(String.valueOf(l.getMaintenanceCost())); fields[4].setText(String.valueOf(l.getUsageFrequency())); fields[5].setText(String.valueOf(l.getVisitorsPerDay())); break;
            case "Cafeteria": Cafeteria caf = (Cafeteria) entity; fields[0].setText(caf.getEntityID()); fields[1].setText(caf.getName()); fields[2].setText(caf.getLocation());
                fields[3].setText(String.valueOf(caf.getMaintenanceCost())); fields[4].setText(String.valueOf(caf.getUsageFrequency())); fields[5].setText(String.valueOf(caf.getDailyCustomers())); break;
            case "Hostels": Hostels h = (Hostels) entity; fields[0].setText(h.getEntityID()); fields[1].setText(h.getName()); fields[2].setText(h.getLocation());
                fields[3].setText(String.valueOf(h.getMaintenanceCost())); fields[4].setText(String.valueOf(h.getUsageFrequency()));
                fields[5].setText(String.valueOf(h.getTotalRooms())); fields[6].setText(String.valueOf(h.getOccupiedRooms())); break;
            case "ClassRoom": ClassRoom cr = (ClassRoom) entity; fields[0].setText(cr.getEntityID()); fields[1].setText(cr.getName()); fields[2].setText(cr.getLocation());
                fields[3].setText(String.valueOf(cr.getRoomNumber())); fields[4].setText(String.valueOf(cr.getCapacity())); fields[5].setText(String.valueOf(cr.isAvailable()));
                fields[6].setText(String.valueOf(cr.getTotalStaff())); fields[7].setText(String.valueOf(cr.getBudget())); break;
            case "Lab": Lab lab = (Lab) entity; fields[0].setText(lab.getEntityID()); fields[1].setText(lab.getName()); fields[2].setText(lab.getLocation());
                fields[3].setText(String.valueOf(lab.getLabNo())); fields[4].setText(String.valueOf(lab.getTotalStaff())); fields[5].setText(String.valueOf(lab.getBudget())); break;
            case "Bus": Bus bus = (Bus) entity; fields[0].setText(bus.getBusID()); fields[1].setText(String.valueOf(bus.getDriverSalary())); fields[2].setText(bus.getFrom());
                fields[3].setText(bus.getTo()); fields[4].setText(bus.getDepartureTime()); break;
            case "LibraryBooks": LibraryBooks book = (LibraryBooks) entity; fields[0].setText(book.getTitle()); fields[1].setText(book.getAuthor()); fields[2].setText(String.valueOf(book.isBorrowed())); break;
            case "TransportService": TransportService ts = (TransportService) entity; fields[0].setText(ts.getEntityID()); fields[1].setText(ts.getName()); fields[2].setText(ts.getLocation());
                fields[3].setText(String.valueOf(ts.getServicehours())); fields[4].setText(String.valueOf(ts.getCostperhour())); fields[5].setText(String.valueOf(ts.ispeakhour())); break;
            case "SecurityService": SecurityService ss = (SecurityService) entity; fields[0].setText(ss.getEntityID()); fields[1].setText(ss.getName()); fields[2].setText(ss.getLocation());
                fields[3].setText(String.valueOf(ss.getNoofguards())); fields[4].setText(String.valueOf(ss.getGuardsalary()));
                fields[5].setText(String.valueOf(ss.getServicehours())); fields[6].setText(String.valueOf(ss.getCostperhour())); break;
            case "HealthCenter": HealthCenter hc = (HealthCenter) entity; fields[0].setText(hc.getEntityID()); fields[1].setText(hc.getName()); fields[2].setText(hc.getLocation());
                fields[3].setText(String.valueOf(hc.getNoofdoctors())); fields[4].setText(String.valueOf(hc.getDoctorsalary()));
                fields[5].setText(String.valueOf(hc.getNoofnurses())); fields[6].setText(String.valueOf(hc.getNursesalary()));
                fields[7].setText(String.valueOf(hc.getServicehours())); fields[8].setText(String.valueOf(hc.getCostperhour())); break;
        }
    }

    private <T> Object[] getRowDataFromEntity(String entityType, T entity) {
        switch (entityType) {
            case "Student": Student s = (Student) entity; return new Object[] { s.getName(), s.getUserType(), s.getRegNO(), s.getDepartment(), s.getFee(), s.getCGPA() };
            case "Teacher": Teacher t = (Teacher) entity; return new Object[] { t.getName(), t.getUserType(), t.getID(), t.getSalary() };
            case "Admin": Admin a = (Admin) entity; return new Object[] { a.getName(), a.getUserType(), a.getAdminID() };
            case "Department": Department d = (Department) entity; return new Object[] { d.getEntityID(), d.getName(), d.getLocation(), d.getTotalStaff(), d.getBudget() };
            case "Course": Course c = (Course) entity; return new Object[] { c.getCourseName(), c.getCourseCode(), c.getModerator(), c.getCredits(), c.getStarttime(), c.getEndtime(), c.getRoomNO() };
            case "Library": Library l = (Library) entity; return new Object[] { l.getEntityID(), l.getName(), l.getLocation(), l.getMaintenanceCost(), l.getUsageFrequency(), l.getVisitorsPerDay() };
            case "Cafeteria": Cafeteria caf = (Cafeteria) entity; return new Object[] { caf.getEntityID(), caf.getName(), caf.getLocation(), caf.getMaintenanceCost(), caf.getUsageFrequency(), caf.getDailyCustomers() };
            case "Hostels": Hostels h = (Hostels) entity; return new Object[] { h.getEntityID(), h.getName(), h.getLocation(), h.getMaintenanceCost(), h.getUsageFrequency(), h.getTotalRooms(), h.getOccupiedRooms() };
            case "TransportService": TransportService ts = (TransportService) entity; return new Object[] { ts.getEntityID(), ts.getName(), ts.getLocation(), ts.getServicehours(), ts.getCostperhour(), ts.ispeakhour() };
            case "ClassRoom": ClassRoom cr = (ClassRoom) entity; return new Object[] { cr.getEntityID(), cr.getName(), cr.getLocation(), cr.getRoomNumber(), cr.getCapacity(), cr.isAvailable(), cr.getTotalStaff(), cr.getBudget() };
            case "Lab": Lab lab = (Lab) entity; return new Object[] { lab.getEntityID(), lab.getName(), lab.getLocation(), lab.getLabNo(), lab.getTotalStaff(), lab.getBudget() };
            case "Bus": Bus bus = (Bus) entity; return new Object[] { bus.getBusID(), bus.getDriverSalary(), bus.getFrom(), bus.getTo(), bus.getDepartureTime() };
            case "LibraryBooks": LibraryBooks book = (LibraryBooks) entity; return new Object[] { book.getTitle(), book.getAuthor(), book.isBorrowed() };
            case "SecurityService": SecurityService ss = (SecurityService) entity; return new Object[] { ss.getEntityID(), ss.getName(), ss.getLocation(), ss.getNoofguards(), ss.getGuardsalary(), ss.getServicehours(), ss.getCostperhour() };
            case "HealthCenter": HealthCenter hc = (HealthCenter) entity; return new Object[] { hc.getEntityID(), hc.getName(), hc.getLocation(), hc.getNoofdoctors(), hc.getDoctorsalary(), hc.getNoofnurses(), hc.getNursesalary(), hc.getServicehours(), hc.getCostperhour() };
            default: return new Object[0];
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new CampusManagementGUI().setVisible(true));
    }
}