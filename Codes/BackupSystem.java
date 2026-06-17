class BackupSystem {
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

    FileHandler<CampusRepository<Student>> studentFH = new FileHandler<>();
    FileHandler<CampusRepository<Teacher>> teacherFH = new FileHandler<>();
    FileHandler<CampusRepository<Admin>> adminFH = new FileHandler<>();
    FileHandler<CampusRepository<Department>> departmentFH = new FileHandler<>();
    FileHandler<CampusRepository<Course>> courseFH = new FileHandler<>();
    FileHandler<CampusRepository<Library>> libraryFH = new FileHandler<>();
    FileHandler<CampusRepository<Cafeteria>> cafeteriaFH = new FileHandler<>();
    FileHandler<CampusRepository<Hostels>> hostelsFH = new FileHandler<>();
    FileHandler<CampusRepository<TransportService>> transportFH = new FileHandler<>();
    FileHandler<CampusRepository<SecurityService>> securityFH = new FileHandler<>();
    FileHandler<CampusRepository<HealthCenter>> healthFH = new FileHandler<>();
    FileHandler<CampusRepository<ClassRoom>> classroomFH = new FileHandler<>();
    FileHandler<CampusRepository<Bus>> busFH = new FileHandler<>();
    FileHandler<CampusRepository<LibraryBooks>> libraryBooksFH = new FileHandler<>();
    FileHandler<CampusRepository<Lab>> labFH = new FileHandler<>();

    public BackupSystem() {
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

    }

    public BackupSystem(CampusRepository<Student> studentRepo, CampusRepository<Teacher> teacherRepo,
            CampusRepository<Admin> adminRepo, CampusRepository<Department> departmentRepo,
            CampusRepository<Course> courseRepo, CampusRepository<Library> libraryRepo,
            CampusRepository<Cafeteria> cafeteriaRepo, CampusRepository<Hostels> hostelsRepo,
            CampusRepository<TransportService> transportRepo, CampusRepository<SecurityService> securityRepo,
            CampusRepository<HealthCenter> healthRepo, CampusRepository<ClassRoom> classroomRepo,
            CampusRepository<Bus> busRepo, CampusRepository<LibraryBooks> libraryBooksRepo,
            CampusRepository<Lab> labRepo) {
        this.studentRepo = studentRepo;
        this.teacherRepo = teacherRepo;
        this.adminRepo = adminRepo;
        this.departmentRepo = departmentRepo;
        this.courseRepo = courseRepo;
        this.libraryRepo = libraryRepo;
        this.cafeteriaRepo = cafeteriaRepo;
        this.hostelsRepo = hostelsRepo;
        this.transportRepo = transportRepo;
        this.securityRepo = securityRepo;
        this.healthRepo = healthRepo;
        this.classroomRepo = classroomRepo;
        this.busRepo = busRepo;
        this.libraryBooksRepo = libraryBooksRepo;
        this.labRepo = labRepo;
    }

    public void SaveData() {
        studentFH.writeToFile(studentRepo, "students.dat");

        teacherFH.writeToFile(teacherRepo, "teachers.dat");
        adminFH.writeToFile(adminRepo, "admins.dat");
        departmentFH.writeToFile(departmentRepo, "departments.dat");
        courseFH.writeToFile(courseRepo, "courses.dat");
        libraryFH.writeToFile(libraryRepo, "libraries.dat");
        cafeteriaFH.writeToFile(cafeteriaRepo, "cafeterias.dat");
        hostelsFH.writeToFile(hostelsRepo, "hostels.dat");
        transportFH.writeToFile(transportRepo, "transport.dat");
        busFH.writeToFile(busRepo, "buses.dat");
        securityFH.writeToFile(securityRepo, "security.dat");
        healthFH.writeToFile(healthRepo, "health.dat");
        classroomFH.writeToFile(classroomRepo, "classrooms.dat");
        labFH.writeToFile(labRepo, "labs.dat");
        libraryBooksFH.writeToFile(libraryBooksRepo, "librarybooks.dat");
    }

    public boolean loadSavedData() {
        studentRepo = studentFH.readFromFile("students.dat");
        teacherRepo = teacherFH.readFromFile("teachers.dat");
        adminRepo = adminFH.readFromFile("admins.dat");
        departmentRepo = departmentFH.readFromFile("departments.dat");
        courseRepo = courseFH.readFromFile("courses.dat");
        libraryRepo = libraryFH.readFromFile("libraries.dat");
        cafeteriaRepo = cafeteriaFH.readFromFile("cafeterias.dat");
        hostelsRepo = hostelsFH.readFromFile("hostels.dat");
        transportRepo = transportFH.readFromFile("transport.dat");
        securityRepo = securityFH.readFromFile("security.dat");
        healthRepo = healthFH.readFromFile("health.dat");
        classroomRepo = classroomFH.readFromFile("classrooms.dat");
        busRepo = busFH.readFromFile("buses.dat");
        labRepo = labFH.readFromFile("labs.dat");
        libraryBooksRepo = libraryBooksFH.readFromFile("librarybooks.dat");

        return studentRepo != null && teacherRepo != null && adminRepo != null && departmentRepo != null
                && courseRepo != null && libraryRepo != null && cafeteriaRepo != null && hostelsRepo != null
                && transportRepo != null && securityRepo != null && healthRepo != null && classroomRepo != null
                && busRepo != null && labRepo != null && libraryBooksRepo != null;
    }

    public CampusRepository<Student> getStudentRepo() {
        return studentRepo;
    }

    public CampusRepository<Teacher> getTeacherRepo() {
        return teacherRepo;
    }

    public CampusRepository<Admin> getAdminRepo() {
        return adminRepo;
    }

    public CampusRepository<Department> getDepartmentRepo() {
        return departmentRepo;
    }

    public CampusRepository<Course> getCourseRepo() {
        return courseRepo;
    }

    public CampusRepository<Library> getLibraryRepo() {
        return libraryRepo;
    }

    public CampusRepository<Cafeteria> getCafeteriaRepo() {
        return cafeteriaRepo;
    }

    public CampusRepository<Hostels> getHostelsRepo() {
        return hostelsRepo;
    }

    public CampusRepository<TransportService> getTransportRepo() {
        return transportRepo;
    }

    public CampusRepository<SecurityService> getSecurityRepo() {
        return securityRepo;
    }

    public CampusRepository<HealthCenter> getHealthRepo() {
        return healthRepo;
    }

    public CampusRepository<ClassRoom> getClassroomRepo() {
        return classroomRepo;
    }

    public CampusRepository<Bus> getBusRepo() {
        return busRepo;
    }

    public CampusRepository<LibraryBooks> getLibraryBooksRepo() {
        return libraryBooksRepo;
    }

    public CampusRepository<Lab> getLabRepo() {
        return labRepo;
    }
}