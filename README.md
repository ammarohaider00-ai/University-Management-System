# Campus Management System

A Java-based desktop application that models the operations of a university campus using Object-Oriented Programming principles. The system manages students, teachers, departments, classrooms, courses, library, hostels, cafeteria, transport, security, and health services through a single, organized application with a Java Swing GUI and file-based data persistence.

## Overview

| | |
|---|---|
| **Language** | Java (JDK 11+) |
| **GUI Framework** | Java Swing |
| **Data Storage** | Java Serialization (15 `.dat` files) |
| **Total Classes** | ~30 classes + 3 interfaces |
| **Modules** | Academic · Facility · Service · Admin · Backup |

## Features

- Role-based student, teacher, and admin record management
- Academic department handling — courses, classrooms, and scheduling
- Automatic scheduling conflict detection and resolution
- Facility management for library, hostels, cafeteria, and labs
- Service modules for transport, security, and health center
- Dynamic peak-hour transport scaling
- Centralized backup system for saving and restoring all application data
- Operational cost calculation for every campus module
- Role-based Swing GUI covering every module in one interface

## OOP Concepts Applied

**Encapsulation** — All class fields are private and accessed only through getters and setters.

**Inheritance** — A class hierarchy rooted in an abstract `CampusEntity`, branching into `AcademicUnit`, `FacilityUnit`, `ServiceUnit`, and `User`, each with their own concrete subclasses.

**Polymorphism** — Each class overrides `toString()` to provide its own description, enabling uniform handling of different object types at runtime.

**Abstraction** — Five abstract classes define required behavior without specifying implementation, and three interfaces (`Scheduable`, `Reportable`, `Notifiable`) add optional contracts across unrelated classes.

## System Architecture

| Layer | Base Class | Concrete Classes |
|---|---|---|
| Academic | `AcademicUnit` | `Department`, `ClassRoom`, `Lab` |
| Facility | `FacilityUnit` | `Library`, `Hostels`, `Cafeteria` |
| Service | `ServiceUnit` | `TransportService`, `SecurityService`, `HealthCenter` |
| User | `User` (abstract) | `Student`, `Teacher`, `Admin` |
| Support | — | `Course`, `Bus`, `Book`, `LabEquipments` |
| Infrastructure | — | `CampusRepository`, `FileHandler`, `BackupSystem` |

## Data Persistence

A generic `CampusRepository<T>` and `FileHandler<T>` handle storage for any entity type without code duplication. A central `BackupSystem` class manages all 15 data stores (students, teachers, admins, departments, courses, libraries, cafeterias, hostels, transport, buses, security, health, classrooms, labs, and library books), automatically restoring all data on startup.

## Graphical User Interface

Built with Java Swing, the GUI provides role-based panels so students, teachers, and admins each see only what is relevant to them, including a login screen, admin dashboard, department panel, library panel, hostel and transport panel, student panel, teacher panel, and a costs and backup panel.

## Testing

The system was validated using 12 functional test cases covering student enrollment, duplicate enrollment handling, library borrowing, hostel capacity limits, scheduling conflict resolution, peak-hour transport adjustment, security notifications, operational cost calculation, and data save/restore — all tests passing.

## Future Improvements

- Replace `.dat` file storage with a relational database (MySQL or PostgreSQL)
- Build a web version using Java Spring Boot
- Add automated timetable generation
- Connect the `Notifiable` interface to real SMS or email alerts
- Add a fee payment and invoice module

## Academic Context

This project was developed as an Object-Oriented Programming semester project at COMSATS University Islamabad.
